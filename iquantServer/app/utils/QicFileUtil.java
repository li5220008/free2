package utils;


import bussiness.common.ISystemConfigService;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import exceptions.IquantEntityHasBeenUsedException;
import models.iquantCommon.StrategyAccountTemplateDto;
import models.iquantCommon.StrategySecurityOriginalDto;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.VisitorSupport;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import play.Logger;
import play.Play;
import play.libs.F;
import play.libs.XPath;
import play.modules.guice.InjectSupport;
import util.CommonUtils;
import util.Constants;

import javax.inject.Inject;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 12-12-11
 * Time: 下午2:52
 * 功能描述: 上传时用创建目录
 */
@InjectSupport
public class QicFileUtil {
    @Inject
    static ISystemConfigService  systemConfigService;
    public static void copyFile(File src ,File dist) throws IOException {
        FileUtils.copyFile(src,dist);
    }
    public  static void copyFileToDirectory(File src,String distDir) throws IOException{
        FileUtils.copyFileToDirectory(src,new File(distDir));
    }
    /**
     * 保存导入用户的excel文件到临时目录
     * 返回文件路径
     *
     * @param file
     * @return
     */
    public static void saveUserExcelTotmp(File file) throws Exception {

        copyFileToDirectory(file, systemConfigService.get(Constants.USER_EXCEL_UPLOAD_TEMP_DIR));
    }

    /**
     * 保存导入用户的excel文件到正式目录
     * 返回文件路径
     *
     * @return
     */
    public static String saveUserExcelToOfficai(File tmpFile, File officalFile) {
        return null;
    }

    /**
     * 给iquant api提供的策略上传保存服务
     * @param attachment
     * @return
     */
    public static Map<String, Object> saveStrategyForIquant(File attachment) throws Exception{
        Multimap<String, ZipEntry> mutiMap = ArrayListMultimap.create();
        ZipFile zip = new ZipFile(attachment, "gbk");
        for (Enumeration entries = zip.getEntries(); entries.hasMoreElements(); ) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            String zipEntryName = entry.getName();
            if (!entry.isDirectory()) {//如果
                String fileType = zipEntryName.substring(zipEntryName.lastIndexOf(".") + 1);
                if (fileType != null) {
                    mutiMap.put(fileType.toLowerCase(), entry);
                }
            } else {//如果entry是文件jia 则先写到tmp下 再遍历
                continue;
            }
        }

        if (mutiMap.containsKey("p") || mutiMap.containsKey("m")) {//Qia策略包
            Logger.info("处理qiA策略上传");
            return unZipQiaStrategy4Iquant(mutiMap, zip);
        } else if (mutiMap.containsKey("dll")) {//qicore策略包
            Logger.info("处理qicore策略上传");
            return unZipQicoreStrategy4Iquant(mutiMap, zip);
        } else {
            Logger.error("不处理策略文件的类型,不处理,请检查");
            //没有处理其它类型
            return null;
        }
    }

    /**
    * 返回的key有:
    * distDir       string   解压的目标地址
    * stkcdContent  string   标的的内容
    * fundsProportion  double 资金使用比例
    * enginetypeId   int     类型类型
    * uuid          string   uuid
    */
    private static Map<String, Object> unZipQiaStrategy4Iquant(Multimap<String, ZipEntry> mutiMap, ZipFile zip) throws IOException {
        Map<String, Object> resultMap = Maps.newHashMap();

        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        //开始复制文件
        F.T2 t2 = composeDir(Constants.iquantUploadDir, uuid);
        String folderName = t2._1 + uuid;
        folderName = StringUtils.replaceChars(folderName, '\\', '/');
        Logger.debug("策略上传后的目录%s", folderName);
        boolean  hasBackTestConfigFile = false;
        boolean  hasStrategyConfigFile = false;
        //找出策略标的配置文件
        String securityConfigFileName = null;
        for(ZipEntry zipEntry : mutiMap.values()){
            String entryName = zipEntry.getName();
            entryName = StringUtils.replaceChars(entryName, '\\', '/');
            int index2 = entryName.lastIndexOf("/");
            String fileName = index2 == -1 ? entryName : entryName.substring(index2 + 1);
            if("StrategyCfg.xml".equalsIgnoreCase(fileName)){
                InputStream xmlInput = zip.getInputStream(zipEntry);
                org.w3c.dom.Document root = null;
                try{
                    root =CommonUtils.getDocument(xmlInput);
                }catch (Exception e){
                    throw new IquantEntityHasBeenUsedException("StrategyCfg.xml文件解析出错，请检查该文件结构");
                }
                securityConfigFileName  = XPath.selectText("/Strategy/FactorDataCfg/@tickerList", root);
                Logger.info("通过后台上传时解析出策略原始配制文件名称为[securityConfigFileName]:"+ securityConfigFileName);
                break;
            }
        }
        for (ZipEntry zipEntry : mutiMap.values()) {
            InputStream in = zip.getInputStream(zipEntry);
            String entryName = zipEntry.getName();
            entryName = StringUtils.replaceChars(entryName, '\\', '/');
            int index2 = entryName.lastIndexOf("/");
            String suffixDir = index2 == -1 ? "" : entryName.substring(0, index2);
            String fileName = index2 == -1 ? entryName : entryName.substring(index2 + 1);
            String newFile=copyFileToDirectoty(in, folderName + suffixDir, fileName);

            if (securityConfigFileName!=null && securityConfigFileName.equalsIgnoreCase(fileName)) { //提取标的的内容. 也就是股票代码
                InputStream xmlInput = zip.getInputStream(zipEntry);
                org.w3c.dom.Document root = null;
                try{
                    root =CommonUtils.getDocument(xmlInput);
                }catch (Exception e){
                    throw new IquantEntityHasBeenUsedException(fileName +"文件解析出错，请检查该文件结构");
                }
                List<org.w3c.dom.Node> list = XPath.selectNodes("/Strategy/code", root);
                List<StrategySecurityOriginalDto> original = Lists.newArrayList();
                for(org.w3c.dom.Node accountNode:list){
                    Integer maxPosition = CommonUtils.parseNumber(XPath.selectText("@MaxShare", accountNode), Integer.class, null);
                    String symbol = XPath.selectText("@id",accountNode);
                    String market = XPath.selectText("@exchangeType",accountNode);
                    StrategySecurityOriginalDto strategySecurityOriginalDto = new StrategySecurityOriginalDto(symbol,maxPosition,market);
                    original.add(strategySecurityOriginalDto);
                }
                resultMap.put("securityOriginal",original);
                /*InputStream xmlInput = zip.getInputStream(zipEntry);
                String content = IO.readContentAsString(xmlInput);//要改写
                resultMap.put("stkcdContent", content);*/
            }if("BackTestCfg.xml".equalsIgnoreCase(fileName)){
                hasBackTestConfigFile = turnOffExportExcelAndSaveResult(newFile);
            }if(fileName.startsWith("BackTestCfg_")){//刘建力修改 修改qsm中的策略名不是上传时用户填的策略名
                String strategyName = fileName.substring(fileName.indexOf("_")+1,fileName.indexOf("."));
                resultMap.put("immutableStrategyName",strategyName);
            }if("StrategyCfg.xml".equalsIgnoreCase(fileName)){
                hasStrategyConfigFile = true;
                //解析账号模板
                InputStream xmlInput = zip.getInputStream(zipEntry);
                org.w3c.dom.Document root = CommonUtils.getDocument(xmlInput);
                List<org.w3c.dom.Node> list = XPath.selectNodes("/Strategy/Accounts/Account", root);
                List<StrategyAccountTemplateDto> accounts = Lists.newArrayList();
                for(org.w3c.dom.Node accountNode:list){
                    StrategyAccountTemplateDto strategyAccountTemplateDto = new StrategyAccountTemplateDto();
                    Integer accountOrder = CommonUtils.parseNumber(XPath.selectText("@serialID", accountNode), Integer.class, null);
                    String accountType = XPath.selectText("@type",accountNode);
                    strategyAccountTemplateDto.accountOrder=accountOrder;
                    strategyAccountTemplateDto.accountType=StrategyAccountTemplateDto.accountType2Int(accountType);
                    accounts.add(strategyAccountTemplateDto);
                }
                resultMap.put("accounts",accounts);

            }else if ("TradeCfg.xml".equalsIgnoreCase(fileName)) { //提取策略的资金比例数值
                InputStream xmlInput = zip.getInputStream(zipEntry);
                org.w3c.dom.Document document = CommonUtils.getDocument(xmlInput);
                if (document != null) {
                    Double fundsProportion = CommonUtils.parseNumber(XPath.selectText("/TradeInfor/TradingArguments/@FundsProportion", document), Double.class, null); //资金使用比例
                    resultMap.put("fundsProportion", fundsProportion);
                }
            }


            if (newFile != null) {
                try {
                    //给exe文件增加执行权限, windows文件要用
                    String newFileFullPath = newFile.toLowerCase();
                    if (newFileFullPath.endsWith(".dll") ||newFileFullPath.endsWith(".exe") || newFileFullPath.endsWith(".a") || newFileFullPath.endsWith(".p") || newFileFullPath.endsWith(".m")) {
                        new File(newFile).setExecutable(true, false); //给所有人都有执行权限
                    }
                } catch (Exception e) {
                    //出现异常不处理
                    Logger.warn("给文件[%s]增加执行权限出现问题", newFile);
                }
            }

        }

        if(!hasBackTestConfigFile || !hasStrategyConfigFile){
            throw  new IOException(String.format("找不到回测配置文件:%s和策略配置文件:%s","BackTestCfg.xml","StrategyCfg.xml"));
        }
        resultMap.put("distDir", t2._2); //目标目录,相对目录
        resultMap.put("enginetypeId", 2);
        resultMap.put("uuid", uuid);

        return resultMap;
    }

    /**
     * 返回的key有:
     * fileNotFound  boolean  文件没有找到
     * fileErr       boolean  文件错误
     * distDir       string   解压的目标地址
     * enginetypeId   int     类型类型
     * uuid          string   uuid
     */
    private static Map<String, Object> unZipQicoreStrategy4Iquant(Multimap<String, ZipEntry> mutiMap, ZipFile zip) throws Exception {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        if (mutiMap.size() < 2) {
            returnMap.put("fileNotFound", true);
            return returnMap;
        } else {//判断两个文件名是否相同

        }

        //先解析校验xml文件
        InputStream is = zip.getInputStream(mutiMap.get("xml").iterator().next());
        SAXReader xmlReader = new SAXReader();
        parseXml(xmlReader.read(is), returnMap);//解析后的数据会存储到map中
        if (!validXml(returnMap)) {
            returnMap.put("fileErr", true);
            return returnMap;
        } else {
            //解析时间
            try {
                String[] playBackDate = ((String) returnMap.get("PlayBackDate")).split("_");
                String[] playBackTime = ((String) returnMap.get("PlayBackTime")).split("_");
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
                SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
                returnMap.put("BackTestStartDate", format2.format(format.parse(playBackDate[0])));
                returnMap.put("BackTestEndtDate", format2.format(format.parse(playBackDate[1])));
            } catch (Exception e) {
                e.printStackTrace();
                returnMap.put("fileErr", true);
                return returnMap;
            }

        }

        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        F.T2 t2 = composeDir(Constants.iquantUploadDir, uuid);
        String folderName = t2._1 + uuid;
        folderName = StringUtils.replaceChars(folderName, '\\', '/');
        Logger.debug("策略上传后的目录%s", folderName);
        for (ZipEntry zipEntry : mutiMap.values()) {
            InputStream in = zip.getInputStream(zipEntry);
            int index2 = zipEntry.getName().lastIndexOf("/");
            String suffixDir = index2 == 0 ? "" : zipEntry.getName().substring(0, index2);
            String fileName = index2 == 0 ? zipEntry.getName() : zipEntry.getName().substring(index2 + 1);
            copyFileToDirectoty(in, folderName + suffixDir, fileName);
        }

        returnMap.put("distDir", t2._2); //相对目录
        returnMap.put("enginetypeId", 1); //策略引擎类型
        returnMap.put("uuid", uuid);

        return returnMap;
    }

    //构成策略上传的目录. T2._1全目录, T2._2 相对目录
    private static F.T2<String, String> composeDir(String key, String uuid){
        String baseDir = Play.configuration.getProperty(key);
        baseDir = StringUtils.replaceChars(baseDir, '\\', '/');
        String dynamicDir = String.format("%1$ty" + File.separator + "%1$tm" + File.separator + "%1$td" + File.separator +  "%1$tH" + File.separator +  "%1$tM"  + File.separator, System.currentTimeMillis());
        dynamicDir = StringUtils.replaceChars(dynamicDir, '\\', '/');
        if (baseDir.charAt(baseDir.length() -1 ) == '/') {
            return F.T2(baseDir + dynamicDir, dynamicDir + uuid);
        }else {
            return F.T2(baseDir + "/" + dynamicDir, dynamicDir + uuid);
        }
    }

    /**
     * 保存策略文件到临时目录
     * 返回文件路径
     *
     * @param file
     * @return Map
     * key说明:
     *
     *
     */
    public static Map<String,Object> saveStrategyToTmp(File file) throws Exception {
        return unZipStrategy(file);
    }

    /**
     * 保存策略文件到正式目录
     * @return
     */
    public static void saveStrategyToOfficai(File tmpFile,String strageName,String dynamicDir) throws Exception {
         FileUtils.copyFileToDirectory(tmpFile,new File(pinDir(Constants.STRATEGY_UPLOAD_OFFICIAL_DIR,dynamicDir) + strageName));


    }

    /**
     * 保存策略文件到正式目录
     * @param tmpFilePath
     * @throws Exception
     */
    public static void saveStrategyToOfficai(String tmpFilePath,String strageName,String dynamicDir) throws Exception {
        saveStrategyToOfficai(new File(tmpFilePath),strageName,dynamicDir);
    }

    /**
     * 删除文件
     *
     * @param file
     * @return
     */
    public static boolean deleteFile(File file) {
        if(file.exists()){
          return  file.delete();
        }
        return false;
    }
    /**
     * 删除文件
     *
     * @return
     */
    public static boolean deleteFile(String path) {
        return deleteFile(new File(path));
    }

    /**
     * 拼接形成文件上传目录
     * 基本目录+年/月/日组成文件最终存放的路径
     * linux下inodes限制
     *年/月/日/时/分 如13/03/19/17/46
     * @param key
     * @return
     */
    private static String pinDir(String key) {
        String baseDir = QicConfigProperties.getString(key);
        String dynamicDir = String.format("%1$ty" + File.separator + "%1$tm" + File.separator + "%1$td" + File.separator +  "%1$tH" + File.separator +  "%1$tM"  + File.separator, System.currentTimeMillis());
        return baseDir + dynamicDir;
    }

    /**
     * 拼接形成文件上传目录
     * 基本目录+年/月/日组成文件最终存放的路径
     * linux下inodes限制
     *年/月/日/时/分 如13/03/19/17/46
     * @param key
     * @return
     */
    private static String pinDir(String key,String dynamicDir) {
        String baseDir = QicConfigProperties.getString(key);
        return baseDir + dynamicDir;
    }

    /**
     * 解压文件
     *
     * @param file
     * @return
     * @throws Exception
     */
    private static Map<String,Object> unZipStrategy(File file) throws Exception {
        Map<String,ZipEntry> map = new HashMap<String,ZipEntry>();
        ZipFile zip = new ZipFile(file,"gbk");
        for (Enumeration entries = zip.getEntries(); entries.hasMoreElements(); ) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            String zipEntryName = entry.getName();
            if(!entry.isDirectory()){//如果
                String fileType = zipEntryName.substring(zipEntryName.lastIndexOf(".")+1);
                if(map.containsKey(fileType)){//暂时为了兼容QIA而设置的
                    fileType = zipEntryName.substring(0,zipEntryName.lastIndexOf("."));
                }
                map.put(fileType,entry);
            }else{//如果entry是文件jia 则先写到tmp下 再遍历
                continue;
            }
        }
        if(map.containsKey("dll")){//qicore策略包
            return unZipQicoreStrategy(map,zip);
        }else if(map.containsKey("p") || map.containsKey("m")){//Qia策略包
            return unZipQiaStrategy(map,zip);
        }else{
            return null;
        }

    }
    //解析qia策略
    private static Map<String,Object> unZipQiaStrategy(Map<String,ZipEntry> map,ZipFile zip) throws Exception{
        List<String> files = new ArrayList<String>(2);
        Map<String,Object> returnMap = new HashMap<String,Object>();
        if(map.size()<2){
            returnMap.put("fileNotFound",true);
            return returnMap;
        }else {//判断两个文件名是否相同

        }
        //解析时间
        try{
          //  String[] playBackDate =((String)returnMap.get("PlayBackDate")).split("_");
           // String[] playBackTime =((String)returnMap.get("PlayBackTime")).split("_");
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            returnMap.put("BackTestStartDate","2013-02-27");
            returnMap.put("BackTestEndtDate","2013-04-27");
        }catch (Exception e){
            e.printStackTrace();
            returnMap.put("fileErr",true);
            return returnMap;
        }
        //开始复制文件
        boolean  hasBackTestConfigFile = false;
        boolean  hasStrategyConfigFile = false;
        String  folderName = UUID.randomUUID().toString().replaceAll("-", "");
        for(ZipEntry zipEntry:map.values()){
            InputStream in = zip.getInputStream(zipEntry);
            int index1 = zipEntry.getName().lastIndexOf("\\");//用File.sperator 不生效
            int index2= zipEntry.getName().lastIndexOf("/");
            String suffixDir  = (index1 - index2) != 0 ? zipEntry.getName().substring(0,index1>index2?index1:index2):"";
            String fileName = (index1 - index2) != 0? zipEntry.getName().substring(index1>index2?index1+1:index2+1):zipEntry.getName();
            if("BackTestCfg.xml".equalsIgnoreCase(fileName)){
                hasBackTestConfigFile =true;
            }
            if("StrategyCfg.xml".equalsIgnoreCase(fileName)){
                hasStrategyConfigFile = true;
            }
            //策略文件最终保存的路目录为基目录加策略各
            String filePath = copyFileToDirectoty(in,pinDir(Constants.STRATEGY_UPLOAD_TEMP_DIR) + folderName,fileName);
            if(filePath != null){
                files.add(filePath);
            }else{
                returnMap.put("fileExist",true);
                return returnMap;
            }
        }
        if(!hasBackTestConfigFile ){
            returnMap.put("fileErr",true);
            returnMap.put("errMessage","缺少回验配置文件");
        }
        if(!hasStrategyConfigFile){
            returnMap.put("fileErr",true);
            returnMap.put("errMessage","缺少策略配置文件");
        }
        returnMap.put("files",files);
        returnMap.put("enginetypeId",2);
        return returnMap;

    }

    /**
     * 解析qicore策略
     * @param map
     * @param zip
     * @return
     * @throws Exception
     */
    private static Map<String,Object>  unZipQicoreStrategy(Map<String,ZipEntry> map,ZipFile zip) throws Exception{
        List<String> files = new ArrayList<String>(2);
        Map<String,Object> returnMap = new HashMap<String,Object>();
        if(map.size()<2){
            returnMap.put("fileNotFound",true);
            return returnMap;
        }else {//判断两个文件名是否相同

        }
        //写文件
        //先解析校验xml文件
        InputStream is = zip.getInputStream(map.get("xml"));
        SAXReader xmlReader = new SAXReader();
        parseXml(xmlReader.read(is),returnMap);//解析后的数据会存储到map中
        if(!validXml(returnMap)){
            returnMap.put("fileErr",true);
            return returnMap;
        }else{
            //解析时间
            try{
                String[] playBackDate =((String)returnMap.get("PlayBackDate")).split("_");
                String[] playBackTime =((String)returnMap.get("PlayBackTime")).split("_");
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
                SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
                returnMap.put("BackTestStartDate",format2.format(format.parse(playBackDate[0])));
                returnMap.put("BackTestEndtDate",format2.format(format.parse(playBackDate[1])));
            }catch (Exception e){
                e.printStackTrace();
                returnMap.put("fileErr",true);
                return returnMap;
            }

        }
        //开始复制文件
        String  folderName = UUID.randomUUID().toString().replaceAll("-", "");
        for(ZipEntry zipEntry:map.values()){
            InputStream in = zip.getInputStream(zipEntry);
            int index1 = zipEntry.getName().lastIndexOf("\\");//用File.sperator 不生效
            int index2= zipEntry.getName().lastIndexOf("/");
            String suffixDir  = (index1 - index2) != 0 ? zipEntry.getName().substring(0,index1>index2?index1:index2):"";
            String fileName = (index1 - index2) != 0? zipEntry.getName().substring(index1>index2?index1+1:index2+1):zipEntry.getName();
            String filePath = copyFileToDirectoty(in,pinDir(Constants.STRATEGY_UPLOAD_TEMP_DIR) + folderName,fileName);
            if(filePath != null){
                files.add(filePath);
            }else{
                returnMap.put("fileExist",true);
                return returnMap;
            }
        }
        returnMap.put("files",files);
        returnMap.put("enginetypeId",1);
        return returnMap;
    }
    private static String copyFileToDirectoty(InputStream in,String directory,String fileName) throws IOException {

        File newFileDir = new File(directory);
        if(!newFileDir.isDirectory()){
            newFileDir.mkdirs();
        }

        File newFile = new File(newFileDir,fileName);
        FileOutputStream fos = new FileOutputStream(newFile);
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            fos.write(buf, 0, len);
        }
        in.close();
        fos.close();
       return newFile.getAbsolutePath();
    }

    private static boolean isDll(String fileName) {
        return fileName == null ? false : fileName.endsWith(".dll");
    }

    private static boolean isXml(String fileName) {
        return fileName == null ? false : fileName.endsWith(".xml");
    }

    /**
     * 解析xml
     * @param document
     * @param map
     * @return
     */
    private static Map<String,Object> parseXml(Document document,final Map<String,Object> map) {
        /**
         * 以访问者模式遍历xml，这里只需重写属性访问的方法
         */
        final List<String> attubutes = Arrays.asList(new String[]{"Author","CreateTime","StrategyName","Instruction"});
        //needAttr.addAll(new String[]{});
        document.getRootElement().accept(new VisitorSupport() {

            @Override
            public void visit(Attribute attribute){

             /* if("Description".equals(attribute.getParent().getParent().getName())){
                  map.put(attribute.getName(),attribute.getValue());
              }*/
              if(attubutes.contains(attribute.getName().trim())){
                  map.put(attribute.getName(),attribute.getValue());
              }
                if(attribute.getName().equals("key")&&attribute.getValue().equals("InitFund") ){
                    map.put("InitFund",attribute.getParent().attribute("value").getValue());
                }
                if(attribute.getName().equals("key")&&attribute.getValue().equals("PlayBackDate") ){
                    map.put("PlayBackDate",attribute.getParent().attribute("value").getValue());
                }
                if(attribute.getName().equals("key")&&attribute.getValue().equals("PlayBackTime") ){
                    map.put("PlayBackTime",attribute.getParent().attribute("value").getValue());
                }

            }
        });


        return map;
    }

    /**
     * 校验初始是否有初始资金和回验时间
     * @param map
     * @return
     */
    private static boolean  validXml(Map<String,Object> map){
        if(map == null){
            return false;
        }else{
            return map.containsKey("PlayBackDate") && map.containsKey("InitFund");
        }
    }
    public static boolean turnOffExportExcelAndSaveResult(String file){
      return  turnOffExportExcelAndSaveResult(new File(file));
    }
    public static boolean turnOffExportExcelAndSaveResult(File file){
        try{
            SAXReader xmlReader = new SAXReader();
            Document document =  xmlReader.read(file);
            Node reportSwitchNode = document.selectSingleNode("/BackTest/backtestArguments/@reportDisplay");
            Node resultSaveSwitchNode = document.selectSingleNode("/BackTest/backtestArguments/@reportDisplay");
            if("on".equalsIgnoreCase(reportSwitchNode.getStringValue()) || "on".equalsIgnoreCase(resultSaveSwitchNode.getStringValue())){
                document.selectSingleNode("/BackTest/backtestArguments/@reportDisplay").setText("off");
                document.selectSingleNode("/BackTest/backtestArguments/@resultSave").setText("off");
                XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(file));
                xmlWriter.write(document);
                xmlWriter.close();
            }
            return true;
        }catch (Exception e){
            Logger.error("修改qia策略文件出错:%s",e.getMessage());
            return false;
        }
    }
    public static boolean updateQicoreXml(String filePath ,final String startDate,final String endDate) throws Exception {
        //InputStream is = zip.getInputStream(map.get("xml"));
        File xmlFile = new File(filePath);
        return updateQicoreXml(xmlFile,startDate,endDate);
    }
    public static boolean updateQicoreXml(File file ,final String startDate,final String endDate) throws Exception {
        //InputStream is = zip.getInputStream(map.get("xml"));
        if(!file.isFile()){
            throw new FileNotFoundException("找不到文件" + file);
        }
        SAXReader xmlReader = new SAXReader();
        Document document =  xmlReader.read(file);
        document.getRootElement().accept(new VisitorSupport() {
            @Override
            public void visit(Attribute attribute){


                if(attribute.getName().equals("key")&&attribute.getValue().equals("PlayBackDate") ){
                    Attribute curattribute = attribute.getParent().attribute("value");
                    curattribute.setValue(startDate+ "_" + endDate);

                }


            }
        });
        XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(file));
        xmlWriter.write(document);

        xmlWriter.close();

        return true;
    }
}
