package controllers.external;

import bussiness.strategy.IBackTestService;
import bussiness.strategy.IStrategyService;
import bussiness.strategy.IUserStrategyManageService;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import models.iquantCommon.BackTestServerDto;
import models.iquantCommon.BackTestStrategyDto;
import models.iquantCommon.StrategyDto;
import play.Logger;
import util.StrategyServer;
import util.SystemResponseMessage;

import javax.inject.Inject;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 12-12-20
 * Time: 下午1:32
 * 功能描述:  提供外部调用接口 1. 查询回测策略
 */
public class GetStrategy extends IpInterceptor {
    @Inject
    static IBackTestService backTestService;
    @Inject
    static IStrategyService strategyService;
    @Inject
    static IUserStrategyManageService userStrategyManageService;
    public static void getBackTestStrategy() {

        renderJSON(getBackTestStrategysUtil(getServerId(), StrategyDto.QICORE_ENGINEE_ID));
        //  记录此次拿到的最大id(待分析)
    }
    @StrategyServer(id=StrategyDto.QIA_ENGINEE_ID)
    public static void getQiaBackTestStrategy() {

        renderJSON(getBackTestStrategysUtil(getServerId(),StrategyDto.QIA_ENGINEE_ID));
        //  记录此次拿到的最大id(待分析)
    }
    private static Map<String,Object> getBackTestStrategysUtil(long serverId,int engineId){
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        //  取策略，返回json数据

        List<BackTestStrategyDto> list = backTestService.findBackTestStrategyByServerId(getServerId(),engineId);
        jsonMap.put("data", list);
        jsonMap.put("status", BackTestServerDto.ServerStatusEnum.VALID.getValue());
        jsonMap.put("message", SystemResponseMessage.SYSTEM_DEFAULT_MSG_RSP);
        return jsonMap;
    }
    @StrategyServer(id=StrategyDto.QIA_ENGINEE_ID,serverType = 2)
    public static void getInRunningStrategy(){
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        //  取策略，返回json数据

        List<BackTestStrategyDto> list = backTestService.findInRuningStrategy(StrategyDto.QIA_ENGINEE_ID,getServerId());
        jsonMap.put("data", list);
        jsonMap.put("status", BackTestServerDto.ServerStatusEnum.VALID.getValue());
        jsonMap.put("message", SystemResponseMessage.SYSTEM_DEFAULT_MSG_RSP);
        renderJSON(jsonMap);


    }

    /**
     * 检查是否仍是“我” 回测
     */
    public static void checkBackTestStatus() {
        String struuid = params.get("sid");
        Integer engineType =  params.get("etype",Integer.class);
        engineType = engineType == null?StrategyDto.QICORE_ENGINEE_ID :engineType;
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        if (struuid == null) {
            jsonMap.put("status", BackTestServerDto.ServerStatusEnum.DISABLED.getValue());
            jsonMap.put("message", "策略ID不能为空");
        } else {
            boolean ret = backTestService.checkBackTestStatus( backTestService.findServerIdByAddr(getRemoteIp(),engineType,0), struuid);
            //改状态
            backTestService.updateStrategyStatus(struuid, StrategyDto.StrategyStatus.BACKTESTING);
            jsonMap.put("status", ret ? BackTestServerDto.ServerStatusEnum.VALID.getValue() : BackTestServerDto.ServerStatusEnum.DISABLED.getValue());
            jsonMap.put("message", ret ? SystemResponseMessage.SYSTEM_DEFAULT_MSG_RSP : SystemResponseMessage.SERVER_DISABLED_RSP + ",可能已被其它服务器回测完毕!");
        }
        renderJSON(jsonMap);


    }

    /**
     * 同步回测结果数据,只接收当前状态为回测中的策略
     * {status:0成功 1失败  data:{key1:value1,key2:value2}}
     */
    public static void syncBackTestResult() {

        InputStream inputStream = request.body;
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("status", -1);

        if (inputStream == null) {
            json.put("message", "请求内容为空");
        } else {
            try {

                JsonParser jsonParser = new JsonParser();
                JsonElement jsonElement = jsonParser.parse(getBody());
                JsonObject jo = jsonElement.getAsJsonObject();

                JsonElement status = jo.get("status");
                JsonElement strategyData = jo.get("data");
                Gson deSerializer = new Gson();
                StrategyDto result = deSerializer.fromJson(strategyData, StrategyDto.class);
                //插入高频低频中间表
                if (result != null && result.strategyId != null) {
                    //检查 如果当前同步来的strategyId状态是否处于回测中，并且策略存在
                    StrategyDto historyDto = strategyService.findStrategyByUUID(result.strategyId);
                    if (historyDto == null || historyDto.status != StrategyDto.StrategyStatus.BACKTESTING.value) {
                        json.put("status", -1);
                        json.put("message", "同步失败，该策略不存在或已被其它服务器回测");
                    } else {
                        //出现重复的先删除
                        result.id = historyDto.id;
                        userStrategyManageService.deleteStrategyFromPerformance(String.valueOf(result.id), StrategyDto.StrategyType.QICORE);
                        userStrategyManageService.syncBackTestResult(result);
                        //将strategy_baseinfo中的状态改为已回测
                        StrategyDto.StrategyStatus status2 = status.getAsInt() == 0 ? StrategyDto.StrategyStatus.FINISHTEST :
                                StrategyDto.StrategyStatus.BACKTESTINGFAILER;
                        userStrategyManageService.updateStategyStatus(status2, result.strategyId);
                        //插入数据库
                        json.put("status", 0);
                        json.put("message", "同步成功");
                    }
                }

            } catch (Exception e) {
                Logger.error(e, e.getMessage());
                json.put("message", "同步失败,读取出错");
            }
        }
        renderJSON(json);

    }
    /**
     * 同步回测结果数据,只接收当前状态为回测中的策略
     * {status:0成功 1失败  data:{key1:value1,key2:value2}}
     */
    @StrategyServer(id=StrategyDto.QIA_ENGINEE_ID,serverType = 0)
    public static void syncQiaBackTestResult() {

        InputStream inputStream = request.body;
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("status", -1);

        if (inputStream == null) {
            json.put("message", "请求内容为空");
        } else {
            try {

                JsonParser jsonParser = new JsonParser();
                JsonElement jsonElement = jsonParser.parse(getBody());
                JsonObject jo = jsonElement.getAsJsonObject();

                JsonElement status = jo.get("status");
               // JsonElement strategyData = jo.get("data");
                JsonObject dataJson = jo.get("data").getAsJsonObject();
                Gson deSerializer = new Gson();
                String strategyId = dataJson.get("strategyId").getAsString();
              //  StrategyDto result = deSerializer.fromJson(strategyData, StrategyDto.class);
                //插入高频低频中间表
                if (strategyId != null) {
                    //检查 如果当前同步来的strategyId状态是否处于回测中，并且策略存在
                    StrategyDto historyDto = strategyService.findStrategyByUUID(strategyId);
                    if (historyDto == null || historyDto.status != StrategyDto.StrategyStatus.BACKTESTING.value) {
                        json.put("status", -1);
                        json.put("message", "同步失败，该策略不存在或已被其它服务器回测");
                    } else {
                        //将strategy_baseinfo中的状态改为已回测
                        StrategyDto.StrategyStatus status2 = status.getAsInt() == 0 ? StrategyDto.StrategyStatus.FINISHTEST :
                                StrategyDto.StrategyStatus.BACKTESTINGFAILER;
                        userStrategyManageService.updateStategyStatus(status2, strategyId);
                        //插入数据库
                        json.put("status", 0);
                        json.put("message", "同步成功");
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                json.put("message", "同步失败,读取出错");
            }
        }
        renderJSON(json);

    }

    /**
     * 回测失败通知接口
     */
    public static void noticeTestFailure() {
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("status", -1);
        json.put("message","通知失败");
        try {
            int effectRow = backTestService.rollBackAfterTestFailure(getServerId());
            json.put("status",0);
            if (effectRow > 0) {
                json.put("message","通知成功");
            }else{
                json.put("message","通知失败，没有符合条件的策略");
            }
        } catch (Exception e) {
          e.printStackTrace();
        }
        renderJSON(json);
    }
}
