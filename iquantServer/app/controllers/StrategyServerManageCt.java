package controllers;

import bussiness.strategy.IBackTestService;
import models.iquantCommon.BackTestServerDto;
import models.iquantCommon.StrageServerDto;

import javax.inject.Inject;
import java.util.List;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-7-16
 * Time: 下午2:30
 * 功能描述:
 */
public class StrategyServerManageCt extends BaseController  {

    @Inject
    static IBackTestService backTestService;
    /**
     * 查询策略服务器
     * @param serverType 0 回测服务器   1运行服务器
     * @param egineType 1 qicore  qia
     */
    public static void fetchStategyServer(int serverType,int egineType){
        List<StrageServerDto> list = backTestService.findServerByTypeAndEngineeId(BackTestServerDto.ServerStatusEnum.VALID, BackTestServerDto.ServerTypeEnum.BACKTEST.getByIntValue(serverType), egineType);
        responseJSON(list);
    }

    /**
     * 查询所有服务器列表
     */
    public static void fetchAllServer(){
        List<StrageServerDto> serverDtoList  =   backTestService.findAllServer();
        responseJSON(serverDtoList);
    }
}
