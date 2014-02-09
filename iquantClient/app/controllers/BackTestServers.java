package controllers;


import bussiness.BackTestService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import models.iquantCommon.BackTestServerDto;
import models.iquantCommon.QiaBackTestResultDto;
import models.iquantCommon.StrageServerDto;
import play.data.binding.As;
import play.jobs.Job;
import play.mvc.With;

import java.util.List;
import java.util.Map;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 12-12-20
 * Time: 下午4:34
 * 功能描述: 回测服务器管理
 */
@With({AuthorBaseIntercept.class})
public class BackTestServers extends BasePlayControllerSupport {
    public static void getServers(int type,int eId){
        BackTestServerDto.ServerTypeEnum  serverTypeEnum= null;
        for(BackTestServerDto.ServerTypeEnum typeEnums : BackTestServerDto.ServerTypeEnum.values()){
           if(typeEnums.getValue() == type){
               serverTypeEnum = typeEnums;
               break;
           }
        }
        List<StrageServerDto> list = BackTestService.findServerByTypeAndEngineeId(BackTestServerDto.ServerStatusEnum.VALID, serverTypeEnum, eId);
        renderJSON(list);
    }
    public static void startBackTest(int serverId){
       // F.Promise<QiaBackTestResultDto> result =   BackTestService.startBackTestQiaStrategy(serverId);
        final int sid = serverId;
        //QiaBackTestResultDto result = BackTestService.startBackTestQiaStrategy(serverId);
        QiaBackTestResultDto result = await(new Job<QiaBackTestResultDto>(){
            public QiaBackTestResultDto doJobWithResult(){
                return BackTestService.startBackTestQiaStrategy(sid);
            }
        }.now());
        renderJSON(result);
    }
    public static void getQiaBackTestStatus(final @As(",")Integer[] serverIds){
     //  final  List<QiaBackTestResultDto> list = Lists.newArrayList();
        List<QiaBackTestResultDto>  result = await(new Job<List<QiaBackTestResultDto>>(){
            public List<QiaBackTestResultDto> doJobWithResult(){
                List<QiaBackTestResultDto> list = Lists.newArrayList();
                for(int serverId : serverIds){
                    QiaBackTestResultDto resultDto = BackTestService.getQiaStrategyBackTestStatus(serverId);
                    list.add(resultDto);
                }
                return list;
            }
        }.now());
     /*   for(int serverId : serverIds){
        QiaBackTestResultDto resultDto = BackTestService.getQiaStrategyBackTestStatus(serverId);
        list.add(resultDto);
        }*/
        renderJSON(result);
    }

    /**
     * 获得当前  serverId 服务器状态和对应的策略数
     * @param serverId   回测服务器ID
     */
     public static void getServiceStatus(final int serverId){
         Map<String,Object> map = Maps.newHashMap();
         List<QiaBackTestResultDto>  result = await(new Job<List<QiaBackTestResultDto>>(){
             public List<QiaBackTestResultDto> doJobWithResult(){
                 List<QiaBackTestResultDto> list = Lists.newArrayList();
                     QiaBackTestResultDto resultDto = BackTestService.getQiaStrategyBackTestStatus(serverId);
                     list.add(resultDto);
                 return list;
             }
         }.now());

         int count = BackTestService.getRunningStrategyNum(serverId);
         if(result!=null && result.size()>0){
             map.put("result",result.get(0).result);
         }
         map.put("count",count);
         renderJSON(map);
     }
}
