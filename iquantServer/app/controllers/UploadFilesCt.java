package controllers;

import bussiness.strategy.IStrategyService;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dbutils.MyDbUtil;
import dbutils.SqlLoader;
import models.iquantCommon.StrategyAccountTemplateDto;
import models.iquantCommon.StrategyBaseDto;
import models.iquantCommon.StrategySecurityOriginalDto;
import play.Logger;
import play.mvc.Util;
import play.mvc.With;
import protoc.ResponseHeader;
import util.Constants;
import util.GsonUtil;
import util.LoginTokenCompose;
import utils.QicFileUtil;

import javax.inject.Inject;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * 上传文件. 从mange工程的 controllers.UploadFiles 拷贝一份过来
 * User: wenzhihong
 * Date: 13-5-7
 * Time: 下午1:43
 */
@With(value = {LoginTokenCheckIntercept.class})
public class UploadFilesCt extends BaseController {
    protected static MyDbUtil qicDbUtil = new MyDbUtil();
    @Inject
    static IStrategyService strategyService;
    /**
     * 检查策略名称是否唯一. 唯一返回 true, 否则返回 false
     */
    public static void checkStategyOnlyName(String name) {
        boolean onlyName = checkStategyName(name);
        if (onlyName) {
            renderText("true");
        }else {
            renderText("false");
        }
    }

    @Util
    static boolean checkStategyName(String name){
        String sql = SqlLoader.getSqlById("checkStategyName");
        long count = qicDbUtil.queryCount(sql, name);
        return count == 0;
    }

    /**
     * 上传策略. 给iquant使用
     * @param file 策略附件
     * @param otherParam 策略的其它参数. json格式
     * json参数格式为:
     <pre>
     {
     	"sname" : "策略名称",
     	"tradeType: 1, // 策略类型
     	"tradeVariety" : 1, //交易品种
     	"provider" : "策略师姓名",
     	"providerDesp" : "策略师简介",
     	"desp" : "策略简介",
     	"lookBackStime" : '2012-03-12', //回测开始时间
     	"lookBackEtime" : '2013-03-13' //回测结束时间
     }
    </pre>
    交易类型: 1. 选股型 2. 择时型 3. 交易型 4. 其他
    交易品种: 1. 股票 2. 期货 3. 混合
     */
    public static void uploadStrage(File file, String otherParam) {
        LoginTokenCompose compose = LoginTokenCompose.current();
        Map<String, Object> paramMap = null;
        Gson gson = GsonUtil.createGson();
        if (file != null) {
            Logger.info("上传的文件" + file.getName());
            if (!file.getName().endsWith(".zip")) {
                ResponseHeader.ErrorEntry errorEntry = new ResponseHeader.ErrorEntry("file", "文件类型错误，只能上传zip压缩文件");
                responseError(errorEntry);
                return;
            }

            if (file.length() > Constants.MAX_SIZE_OF_ZIP_STRATEGY_FILE) {
                ResponseHeader.ErrorEntry errorEntry = new ResponseHeader.ErrorEntry("file", String.format("文件过大，只能上传最大为%dM的文件", Constants.MAX_SIZE_OF_ZIP_STRATEGY_FILE >> 20));
                responseError(errorEntry);
                return;
            }
        } else {
            Logger.warn("没有收到上传的文件");
            ResponseHeader.ErrorEntry errorEntry = new ResponseHeader.ErrorEntry("file", "上传文件不能为空");
            responseError(errorEntry);
            return;
        }

        if (otherParam != null) {
            Logger.info("json格式的otherParam=%d\n", otherParam);
            try{
                paramMap = gson.fromJson(otherParam, new TypeToken<Map<String, Object>>() {}.getType());
            }catch (Exception e){
                ResponseHeader.ErrorEntry errorEntry = new ResponseHeader.ErrorEntry("jsonerror", "json格式不符合协议要求的格式");
                responseError(errorEntry);
                return;
            }
        }else {
            Logger.info("没有收到 otherParam");
            ResponseHeader.ErrorEntry errorEntry = new ResponseHeader.ErrorEntry("otherParam", "其它参数不能为空");
            responseError(errorEntry);
            return;
        }

        //检查重名
        if (paramMap.containsKey("sname")) {
            boolean onlyName = checkStategyName(paramMap.get("sname").toString());
            if (!onlyName) {
                ResponseHeader.ErrorEntry errorEntry = new ResponseHeader.ErrorEntry("name", String.format("策略名:%s已被占用", paramMap.get("sname")));
                responseError(errorEntry);
                return;
            }
        }

        try {
            //解压保存文件
            Map<String, Object> map = QicFileUtil.saveStrategyForIquant(file);

            //上传用户id
            paramMap.put("stupUid", compose.uid);
            //状态
            paramMap.put("status", StrategyBaseDto.StrategyStatus.CHECKING.value); //刚上传

            paramMap.putAll(map);

            if (!paramMap.containsKey("stkcdContent")) { //标的内容
                Logger.warn("策略里没有标的内容,设置为空");
                paramMap.put("stkcdContent", "");
            }

            if (!paramMap.containsKey("fundsProportion")) {
                Logger.warn("策略里没有资金使用比例,设置为null");
                paramMap.put("fundsProportion", null);
            }

            paramMap.put("agentIp", "127.0.0.1"); //先写死??


            //存放帐号模板
            long strategyId = qicDbUtil.insertWithNameParam("strateSave4Iquant", paramMap);
            if(paramMap.get("accounts")!=null){
                List<StrategyAccountTemplateDto> list = ( List<StrategyAccountTemplateDto>)paramMap.get("accounts");
                for(StrategyAccountTemplateDto accountTemplateDto : list){
                    accountTemplateDto.status=1;
                    accountTemplateDto.strategyId = strategyId;
                    accountTemplateDto.createUid = compose.uid;
                    strategyService.addStrategyAccountTemplate(accountTemplateDto);
                    Logger.info("解析出策略文件账号信息");
                }
            }

            //存放从stkcm.xml文件里解析出来的内容到 strategy_security_original 表
            if(paramMap.get("securityOriginal")!=null){
                List<StrategySecurityOriginalDto> list = ( List<StrategySecurityOriginalDto>)paramMap.get("securityOriginal");
                for(StrategySecurityOriginalDto securityOriginalDto : list){
                    securityOriginalDto.strategyId = strategyId;
                    strategyService.addStrategySecurityOrigunalInfo(securityOriginalDto);
                    Logger.info("解析出策略原始配制的交易标的");
                }
            }
            Map<String,Object> qsmMap = Maps.newHashMap();
            qsmMap.putAll(paramMap);
            if(paramMap.containsKey("immutableStrategyName")){//存到qstrategy表中StrategyName用BackTestCfg_xxxx.xml中的xxx作为策略名，而不是上传时用户填的策略名，10.09修改(刘建力)
                qsmMap.put("sname",paramMap.get("immutableStrategyName"));
                Logger.info("从文件名中解析中策略名存到qstrategy表中:"+paramMap.get("immutableStrategyName"));
            }
            qicDbUtil.insertWithNameParam("syncStrategyToQsm4Iquant", qsmMap);
        } catch (Exception e) {
            Logger.error(e, "上传策略出错:%s", e.getMessage());
            ResponseHeader.ErrorEntry errorEntry = new ResponseHeader.ErrorEntry("message", e.getMessage());
            responseError(errorEntry);
            return;
        }

        responseJSON(Maps.newHashMap());
    }

}
