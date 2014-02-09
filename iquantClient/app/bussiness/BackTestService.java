package bussiness;

import com.google.common.base.Joiner;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import models.iquantCommon.BackTestServerDto;
import models.iquantCommon.QiaBackTestResultDto;
import models.iquantCommon.StrageServerDto;
import models.iquantCommon.StrategyDto;
import play.Logger;
import play.libs.WS;
import protoc.URILib;
import protoc.parser.ActionResult;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import static protoc.URILib.ADD_STRATEGY_BACKTEST;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 12-12-20
 * Time: 下午1:49
 * 功能描述:  策略回测同外部api交互相关处理方法
 */
public class BackTestService extends BasicService {
    private static String  START_BACKTEST_URL_TEMPLATE = "http://%s:9501/backtest/start";
    private static String  GET_BACKTEST_STATUS_URL_TEMPLATE = "http://%s:9501/backtest/status";
    private static List<StrageServerDto> serverList_cacehe = new CopyOnWriteArrayList<StrageServerDto>();

    public static String findServerIpById(int serverId){
        List<StrageServerDto> list = findAllServer();
        for(BackTestServerDto backTestServerDto:list){
            if(backTestServerDto.id == serverId)
            return backTestServerDto.ip;
        }
        return null;
    }

    /**
     * 批量修改策略所对应的回测服务器ID
     * @param serverId
     * @param sid 自增长主键id
     * @return
     */
    public static boolean updateStratedyServerIdByIntId(Map<String,Integer> serverId,String[] sid){
        Joiner joiner =  Joiner.on(",");
        String stids = joiner.join(sid);
        String qicoreServerId = serverId.get("qicore").toString();
        String qiaServerId = serverId.get("qia").toString();
        ActionResult<List<String>> result = remoteRequestService.getListWithoutField(ADD_STRATEGY_BACKTEST, qicoreServerId, qiaServerId, stids);

        return result.header.status ==0 &&result.data !=null && result.data.size()>0;
    }




    /**
     * 查询所有的回测服务器列表
     * @return
     */
    public synchronized static List<StrageServerDto> findAllServer(){
        if (serverList_cacehe == null || serverList_cacehe.size() == 0) {
           ActionResult<List<StrageServerDto>> result = remoteRequestService.getList(URILib.FETCH_ALL_SERVER, StrageServerDto.class);
           serverList_cacehe = result.data;
        }
        return serverList_cacehe;
    }


    /**
     * 查询回测服务器列表
     * @param status
     * @return
     */
    public static List<StrageServerDto> findBackTestServerByStatus(BackTestServerDto.ServerStatusEnum status){
        return findServerByStatusAndType(status, BackTestServerDto.ServerTypeEnum.BACKTEST);
    }
    /**
     * 查询代理服务器列表
     * @param status
     * @return
     */
    public static List<StrageServerDto> findAgentServerByStatus(BackTestServerDto.ServerStatusEnum status){
        return findServerByStatusAndType(status, BackTestServerDto.ServerTypeEnum.AGENT);
    }

    /**
     * 按服务器状态和类型查找服务器
     * @param status
     * @return
     */
    public static List<StrageServerDto> findServerByStatusAndType(BackTestServerDto.ServerStatusEnum status,BackTestServerDto.ServerTypeEnum type){
        //默认查qicore的
        return  findServerByTypeAndEngineeId(status,type, StrategyDto.QICORE_ENGINEE_ID);
    }
    public static List<StrageServerDto> findServerByTypeAndEngineeId(BackTestServerDto.ServerStatusEnum status,BackTestServerDto.ServerTypeEnum type,int enginetypeId){
       int serverType = type.getValue();
       int  egineType = enginetypeId;
        ActionResult<List<StrageServerDto>> result = remoteRequestService.getList(URILib.FETCH_STRATEGY_SERVER, StrageServerDto.class, serverType, egineType);
        List<StrageServerDto> list = result.data;
        return list;
    }





   //清空缓存列表
    public static void clearCache(){

        if( serverList_cacehe.size()>0){
            serverList_cacehe.clear();
        }
    }



    /**
     * 获取qia服务器回测结果
     * @param serverId
     * @return
     */
    public  static QiaBackTestResultDto getQiaStrategyBackTestStatus(int serverId){
        String ip = findServerIpById(serverId);
        QiaBackTestResultDto resultDto = new QiaBackTestResultDto();
        resultDto.result = false;
        resultDto.serverId = serverId;
        try{
            WS.HttpResponse response = WS.url(GET_BACKTEST_STATUS_URL_TEMPLATE, ip).timeout("5s").get();

            if(response.success()){
                JsonElement je= response.getJson();
                JsonObject jo = je.getAsJsonObject();
                resultDto.result = jo.get("Result").getAsBoolean();
                Gson deSerializer = new Gson();
                QiaBackTestResultDto.ResultData  data = deSerializer.fromJson( jo.get("Data")
                        , QiaBackTestResultDto.ResultData.class);
                resultDto.data = data;
            }
        }catch (Exception ex){
            Logger.debug("服务器:id=[%s],ip=[%s]:状态：离线中", serverId, ip);
        }
        return resultDto;
    }

    /**
     * 启动qia回测
     * @param serverId
     * @return
     */
    public static QiaBackTestResultDto startBackTestQiaStrategy(int serverId){
        String ip = findServerIpById(serverId);

        QiaBackTestResultDto resultDto = new QiaBackTestResultDto();
        resultDto.result = false;
        resultDto.serverId = serverId;
        try{
            WS.HttpResponse response = WS.url(START_BACKTEST_URL_TEMPLATE,ip).timeout("5s").get();
            if(response.success()){
                JsonElement je= response.getJson();
                JsonObject jo = je.getAsJsonObject();
                resultDto.result = jo.get("Result").getAsBoolean();
            }
        }catch (Exception ex){
            Logger.debug("服务器:id=[%s],ip=[%s]:状态：离线中",serverId,ip);
        }
        return resultDto;
    }

    /**
     *
     * @param serviceId
     * @return
     */
    public static int getRunningStrategyNum(int serviceId) {
        int num = 0;
        try {
            String strNum = remoteRequestService.getSingleValue(URILib.FETCH_STRATEGY_BY_SERVICEID, serviceId).data;
            num = Integer.parseInt(strNum==null?"0":strNum);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return num;

    }
}
