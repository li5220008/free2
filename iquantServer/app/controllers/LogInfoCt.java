package controllers;

import bussiness.common.ILogInfoService;
import models.iquantCommon.LogInfoDto;
import play.Logger;
import play.data.binding.As;
import play.libs.F;
import util.Page;
import util.SystemResponseMessage;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-7-16
 * Time: 下午2:30
 * 功能描述: 操作日志查询
 */
public class LogInfoCt extends BaseController {

    @Inject
    static ILogInfoService logInfoService;

    /**
     * 查询系统操作日志
     *
     * @param begindate 起始日期
     * @param enddate   结束日期
     * @param pageNo    页码
     */
    public static void fetchLogList(@As(format = "yyyy-MM-dd:hh:mm:ss") Date begindate, @As(format = "yyyy-MM-dd:hh:mm:ss") Date enddate, int pageNo) {

        F.T2<List<LogInfoDto>, Page> result = logInfoService.logList(begindate, enddate, pageNo);
        responseJSON(result._1, result._2);

    }

    /**
     * @Author 刘泓江
     * @param uid 用户ID
     * @param function 操作功能
     * @param content 操作内容
     * @param type 操作类型
     */
    public static void writeSystemLog(long uid,String function,String content,int type){

        try {
            logInfoService.writeSystemLog(uid, function, content, type);
            responseJSON(true);
        } catch (Exception e) {
            Logger.warn("fail to write system log");
            responseError(SystemResponseMessage.SYSTEM_DEFAULT_ERR_RSP);
        }
    }

}
