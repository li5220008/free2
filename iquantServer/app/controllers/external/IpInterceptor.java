package controllers.external;

import bussiness.strategy.IBackTestService;
import models.iquantCommon.BackTestServerDto;
import models.iquantCommon.StrategyDto;
import play.Logger;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Util;
import util.StrategyServer;
import util.SystemResponseMessage;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 12-12-24
 * Time: 上午8:53
 * 功能描述: ip 分配和过虑
 */
public class IpInterceptor extends Controller {
    @Inject
    static IBackTestService backTestService;
    private static ThreadLocal<Long>  current = new ThreadLocal<Long>();

    @Before(priority = 1)
    public static void doFilter(){
        String ip =  getRemoteIp();
        Logger.info("client ip:%s",ip);
        Integer engineType =  params.get("etype",Integer.class);
        StrategyServer engine= request.invokedMethod.getAnnotation(StrategyServer.class);
        int engineId = 0;
        if(engineType != null){
            engineId = engineType;
        }else{
            engineId = engine == null? StrategyDto.QICORE_ENGINEE_ID :engine.id();
        }
        int serverType = engine == null ?0:engine.serverType();
        // ip鉴权 如果ip有权限  则返回服务器id 否则返回-1 表示无权限
        long sid = backTestService.findServerIdByAddr(ip, engineId, serverType);
        Map<String, Object> jsonMap = new HashMap<String, Object>();

        if (sid < 0 ) {
            jsonMap.put("status", BackTestServerDto.ServerStatusEnum.DISABLED.getValue());
            jsonMap.put("message", SystemResponseMessage.SERVER_DISABLED_RSP);
           // jsonMap.put("message", "来自过滤器的响应");
            renderJSON(jsonMap);
        }else{
            current.set(sid);
        }
    }

    @Util
    public static String getRemoteIp(){
        String ip = null;
        Http.Header header = request.headers.get("x-forwarded-for");
        if(header != null){
            ip = header.value();
            Logger.info("client remote ip:%s",ip);
        }else{
            ip = request.remoteAddress;
            Logger.info("client local ip:%s",ip);
        }
        return ip;
    }

    @Util
    public static String getBody() {
     String body = params.get("body");
        Logger.info("收到请求数据:\n%s", body);
        return body;
    }

    @Util
    public static long getServerId(){
        return current.get() == null ? -1 :  current.get();
    }
}
