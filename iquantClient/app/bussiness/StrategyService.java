package bussiness;

import com.google.common.base.Joiner;
import com.google.gson.Gson;
import models.iquantCommon.*;
import play.libs.F;
import protoc.HttpBody;
import protoc.ResponseHeader;
import protoc.URILib;
import protoc.parser.ActionResult;
import util.CommonUtils;
import util.GsonUtil;
import util.Page;
import util.QicFileUtil;

import java.io.File;
import java.util.*;

import static protoc.URILib.*;

/**
 * 策略业务操作
 * User: liangbing
 * Date: 13-7-1
 * Time: 下午1:46
 */
public class StrategyService extends BasicService {

    /**
     *
     * @param orderType
     * @param keyword
     * @param pageNo
     * @return
     */
    public  F.T2<List<StrategyBaseDto>, Page> strategyList(int orderType, String keyword, int pageNo) {
        List<StrategyBaseDto> strategyBaseDtoList = new ArrayList<StrategyBaseDto>();
        Page page = null;

        String url = URILib.FETCH_STRATEGY_LIST;//todo
        ActionResult<List<StrategyBaseDto>> strategyBaseDtoActionResult = remoteRequestService.getList(url,StrategyBaseDto.class,orderType,keyword,pageNo);
        if (strategyBaseDtoActionResult != null) {
            ResponseHeader responseHeader = strategyBaseDtoActionResult.header;
            strategyBaseDtoList = strategyBaseDtoActionResult.data;
            page = new Page(responseHeader.total,pageNo);
        }
        return F.T2(strategyBaseDtoList, page);
    }


    /**
     * 得到已收藏的策略,放入set中
     * @param ids
     * @param uid
     * @return
     */
    public  Set<Integer> queryUserCollectSet(String ids, Long uid){

        List<String> stringList = new ArrayList<String>();
        Set<Integer> result = new HashSet<Integer>();
        String url = URILib.QUERY_USER_COLLECT_LIST;//todo 需要定的协议
        ActionResult<List<String>> actionResult = remoteRequestService.getListWithoutField(url,ids,uid);
        if(actionResult!=null){
            stringList = actionResult.data;
        }
        if(stringList.size()>0){
            for(String id:stringList){
                result.add(Integer.parseInt(id));
            }
        }
        return result;
    }


    /**
     * 高级搜索
     * @param cnd
     * @param pageNo 当前页
     * @return _1 为结果集, _2为总条数, _3 总共页数
     */
    public  F.T2<List<StrategyBaseDto>, Page> advanceSearch(StrategySearchCnd cnd,int orderType, int pageNo) {
        List<StrategyBaseDto> strategyBaseDtoList = new ArrayList<StrategyBaseDto>();
        Page page = null;
        String json = GsonUtil.createWithoutNulls().toJson(cnd);
        String url = URILib.FETCH_ADVANCE_SEARCH_LIST;
        HttpBody httpBody = new HttpBody();
        httpBody.body = json;
        ActionResult<List<StrategyBaseDto>> strategyBaseDtoActionResult = remoteRequestService.getList(url,httpBody,StrategyBaseDto.class,orderType,pageNo);
        if (strategyBaseDtoActionResult != null) {
            ResponseHeader responseHeader = strategyBaseDtoActionResult.header;
            strategyBaseDtoList = strategyBaseDtoActionResult.data;
            page = new Page(responseHeader.total,pageNo);
        }

        return F.T2(strategyBaseDtoList, page);
    }


    //region 是原来对策略的收藏的操作
    //加入收藏
    public boolean addstrategycollect(long sid,Long uid){
        String url = URILib.ADD_STRATEGY_COLLECT;
        boolean b = false;
        b= Boolean.valueOf(remoteRequestService.getSingleValue(url,sid,uid).data);
        return b;
    }
    //取消收藏
    public boolean deletestrategycollect(long sid,Long uid){
        String url = URILib.DELECT_STRATEGY_COLLECT;
        boolean b = false;
        b= Boolean.valueOf(remoteRequestService.getSingleValue(url,sid,uid).data);
        return b;
    }

    //endregion


    public  F.T2<List<StrategyBaseDto>, Page> findStrategysByUser(Map<String,String> queryParams){

        List<StrategyBaseDto> strategyBaseDtoList = new ArrayList<StrategyBaseDto>();
        Page page = null;
        int pageNo = queryParams.get("pageNo")!=null?Integer.parseInt(queryParams.get("pageNo")):1;

        String json = GsonUtil.createWithoutNulls().toJson(queryParams);

        String url_find_strategy = URILib.FIND_STRATEGYS_BY_USER;

        ActionResult<List<StrategyBaseDto>> actionResult = remoteRequestService.getList(url_find_strategy,new HttpBody(json),StrategyBaseDto.class);
        if(actionResult!=null){
            ResponseHeader responseHeader = actionResult.header;
            strategyBaseDtoList = actionResult.data;
            page = new Page(responseHeader.total,pageNo);
        }


        return F.T2(strategyBaseDtoList,page);
    }




    public static boolean insertStrategy(StrategyDto strategyDto, String path, long uid)throws Exception{
        boolean bool = false;
        HttpBody httpBody = new HttpBody();
        httpBody.body = strategyDto;
        String url_insert_strategy = URILib.INSERT_STRATEGY;
        String[] files = path.split(",");
        String dynamicDir = "";
        //uuid
        String strategyId = UUID.randomUUID().toString().replaceAll("-", "");
        strategyDto.strategyId = strategyId;
        for (String tmpFile : files) {
            dynamicDir = String.format("%1$ty" + File.separator + "%1$tm" + File.separator + "%1$td" + File.separator + "%1$tH" + File.separator + "%1$tM" + File.separator, System.currentTimeMillis());
            File file = new File(tmpFile);
            File newFile = null;
            String fileSuffix = file.getName().substring(file.getName().lastIndexOf("."));
            if (strategyDto.enginetypeId == StrategyBaseDto.QICORE_ENGINEE_ID) {//QICORE
                newFile = new File(file.getParentFile().getAbsolutePath(), strategyId + fileSuffix);
                //如果回测时间有修改 则修改文件
                    if(file.getName().endsWith(".xml") && (!strategyDto.lookbackEtime.equals(strategyDto.customerLookbackEndTime)||!strategyDto.lookbackStime.equals(strategyDto.customerLookbackStartTime))){//修改回测时间
                        QicFileUtil.updateQicoreXml(file, strategyDto.customerLookbackStartTime.replace("-", ""), strategyDto.customerLookbackEndTime.replace("-", ""));
                    }
                } else {//QIA
                    newFile = new File(file.getParentFile().getAbsolutePath(), file.getName().substring(0, file.getName().lastIndexOf(".")) + fileSuffix);
                    if(file.getName().endsWith("BackTestCfg.xml")){
                        QicFileUtil.turnOffExportExcelAndSaveResult(file);
                    }
                }
                file.renameTo(newFile);
                QicFileUtil.saveStrategyToOfficai(newFile, strategyId, dynamicDir);//移动正式文件库中,用uuid作文件 防止文件名重复
                QicFileUtil.deleteFile(newFile);//将临时文件库中文件删除
            }
          String filePath = dynamicDir+strategyId;
          ActionResult<String> actionResult = remoteRequestService.getSingleValue(url_insert_strategy,httpBody,uid,filePath);
        if(actionResult!=null){
            bool = Boolean.valueOf(actionResult.data);
        }
        return bool;
    }

    public  StrategyBaseDto findStrategyByName(String sname) {
        StrategyBaseDto strategyBaseDto = new StrategyBaseDto();
        ActionResult<StrategyBaseDto> actionResult = remoteRequestService.getBean(URILib.FETCH_STRATEGY_BY_NAME,StrategyBaseDto.class,sname);
        if(actionResult!=null){
           strategyBaseDto = actionResult.data;
        }
        return strategyBaseDto;
    }

    //将传入的list集合 取出它们的ID组装成String
    public String listToString(List<StrategyBaseDto> sbdList){
        StringBuffer stringBuffer = new StringBuffer();
        if (sbdList != null && sbdList.size() > 0) {
            for (StrategyBaseDto strategyBaseDto:sbdList){
                stringBuffer.append(strategyBaseDto.id);
                stringBuffer.append(",");
            }
            stringBuffer.delete(stringBuffer.length()-1,stringBuffer.length());
        }
        return stringBuffer.toString();
    }



    //region manage service  begin

    /**
     * 查询待上架策略
     *
     * @param sp     参数类
     * @param pageNo 当前页
     * @return _1. 待上架策略对象集合, _2 Page 分页对象
     */
    public static F.T2<List<StrategyDto>, Page> waitList(StrategyPar sp, int pageNo,long uid) {
        Gson gson = new Gson();
        String json = gson.toJson(sp);

        ActionResult<List<StrategyDto>> result = remoteRequestService.getList(FETCH_WAIT_LIST, new HttpBody(json), StrategyDto.class, pageNo, uid);
        ResponseHeader responseHeader = result.header;
        Page page = new Page(responseHeader.total, pageNo);
        List<StrategyDto> list = result.data;
        return F.T2(list, page);
    }

    /**
     * 策略回收站列表
     *
     * @param sp     参数类
     * @param pageNo 当前页
     * @return _1. 策略回收站对象集合, _2 Page 分页对象
     */
    public static F.T2<List<StrategyDto>, Page> retrieveList(StrategyPar sp, int pageNo,long uid) {
        String json =  GsonUtil.createWithoutNulls().toJson(sp);
        ActionResult<List<StrategyDto>> result = remoteRequestService.getList(FETCH_RETRIEVE_STRATEGY_LIST, new HttpBody(json),StrategyDto.class,pageNo, uid);
        ResponseHeader responseHeader = result.header;
        Page page = new Page(responseHeader.total, pageNo);
        List<StrategyDto> list = result.data;
        return F.T2(list, page);
    }

    /**
     * 上架策略列表
     *
     * @param sp     参数类
     * @param pageNo 当前页
     * @return _1. 策略列表对象集合, _2 Page 分页对象
     */
    public static F.T2<List<StrategyDto>, Page> groundingList(StrategyPar sp, int pageNo,long uid) {
        String json = GsonUtil.createWithoutNulls().toJson(sp);
        ActionResult<List<StrategyDto>> result = remoteRequestService.getList(FETCH_UP_STRATEGY_LIST,new HttpBody(json), StrategyDto.class,pageNo, uid);
        ResponseHeader responseHeader = result.header;
        Page page = new Page(responseHeader.total, pageNo);
        List<StrategyDto> list = result.data;
        return F.T2(list, page);
    }

    /**
     * author 潘志威
     * 加入回测
     * @param ids
     */
    @Deprecated
    public static void addLookback(String ids[],int status){
        Joiner joiner = Joiner.on(",");
        String stids = joiner.join(ids);
        ActionResult result = remoteRequestService.getJustHeader(ADD_LOOKBACK, stids, status);
    }

    /**
     * author 潘志威
     * 删除策略
     * @param ids
     */
    public static void delstrategy(String ids[]){
        Joiner joiner = Joiner.on(",");
        String stids = joiner.join(ids);
        ActionResult result = remoteRequestService.getJustHeader(DELETE_STRATEGY, stids);
    }

    /**
     * author 潘志威
     * 策略清空
     */
    public static void emptystrategy(){
        ActionResult result = remoteRequestService.getJustHeader(EMPTY_STRATEGY);
    }
    /**
     *
     * 处理 立即下架/延时下架
     *
     * 立即下架包括两个步骤
     * 1.如果当前时间大于最大收藏时间--》策略下架
     * 2.把每条下架信息保存到user_message表
     *
     * 延时下架 把相关信息存入任务调度表（scheduling_task）由定时任务类处理
     * @author 刘泓江
     * @param stids  策略id数组
     * @param setTime 用户设置的 延时下架时间
     * @param flag 客户端传过来的标示 1 ：立即下架 2 ：延时下架
     * @return 6.设置时间小于当前时间 5.策略为待下架状态不允许修改，4.模板内容非法 3.非法操作 2.策略当前有用户订阅，不能下架 1.下架成功
     */
    public static int strategyDown(String stids,Date setTime,String message,long uid,int flag){
        String downTime =  CommonUtils.getFormatDate("yyyy-MM-dd HH:mm:ss", setTime) ;
        ActionResult<String> result = remoteRequestService.getSingleValue(STRATEGY_DOWN, stids, downTime, message, uid, flag);
        int a = 0;
        if (result != null) {
           a = Integer.parseInt(result.data);
        }
        return a;
    }

    /**
     * author 潘志威
     * 策略上架
     * @param ids
     * @param serverId qsm中的agentIP
     */
    public static boolean upstrategy(String ids[],Map<String,Integer> serverId){
       Joiner joiner = Joiner.on(",");
        String stids = joiner.join(ids);
        String qicoreServerId = serverId.get("qicore").toString();
        String qiaServerId = serverId.get("qiaSimulate").toString();
        String qiaAgentId = serverId.get("qia").toString();
        ActionResult<String> result = remoteRequestService.getSingleValue(UP_STRATEGY, stids, qicoreServerId, qiaServerId,qiaAgentId);
        boolean b = Boolean.parseBoolean(result.data);
        return b;
    }
    public static int updateStategyStatus(StrategyDto.StrategyStatus status,String suuid){
        ActionResult<String> result = remoteRequestService.getSingleValue(UPDATE_STATEGY_STATUS,status, suuid);
        int a = 0;
        if (result != null) {
            a = Integer.parseInt(result.data);
        }
        return a;
    }

    public static int updateStategyStatusbyId(StrategyDto.StrategyStatus status,String id){
        ActionResult<String> result = remoteRequestService.getSingleValue(UPDATE_STATEGY_STATUS_BY_ID, status, id);
        int a = 0;
        if (result != null) {
            a = Integer.parseInt(result.data);
        }
        return a;
    }
    public static Integer judgeStrategyIsOut30(long uid){
        ActionResult<String> result = remoteRequestService.getSingleValue(JUDGE_STRATEGY_IS_OUT_30, uid);
        Integer a = 0;
        if (result != null) {
            a = Integer.parseInt(result.data);
        }
        return a;
    }

    //endregion  manage service end



    public static List<StrategyBaseinfo> findStrategysByIds(String idsArr[]) {
        Joiner joiner = Joiner.on(",");
        String ids = joiner.join(idsArr);
        ActionResult<List<StrategyBaseinfo>> result = remoteRequestService.getList(FIND_STRATEGYS_BY_IDS, StrategyBaseinfo.class, ids);
        List<StrategyBaseinfo> list = new ArrayList<StrategyBaseinfo>();
        if (result != null) {
            list = result.data;
        }
        return list;
    }

    public static StrategyBaseinfo findStrategyById(long id) {
        ActionResult<StrategyBaseinfo> result = remoteRequestService.getBean(FIND_STRATEGY_BY_ID, StrategyBaseinfo.class, id);
        StrategyBaseinfo strategyBaseinfo = new StrategyBaseinfo();
        if (result != null) {
            strategyBaseinfo = result.data;
        }
        return strategyBaseinfo;
    }

}
