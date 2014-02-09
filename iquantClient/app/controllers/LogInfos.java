package controllers;

import annotation.QicFunction;
import bussiness.LogInfosService;
import models.iquantCommon.LogInfoDto;
import play.libs.F;
import util.Page;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

/**
 * 操作日志
 * User: panzhiwei
 * Date: 12-12-12
 * Time: 下午1:25
 * To change this template use File | Settings | File Templates.
 */
public class LogInfos extends BasePlayControllerSupport {
    @Inject
    static LogInfosService logInfosService;

    /**
     * 操作日志
     * @param begindate   起始日期
     * @param enddate     截止日期
     * @param pageNo      当前页
     */
    @QicFunction(id=25)
      public static void loglist(Date begindate,Date enddate,int pageNo){
          F.T2<List<LogInfoDto>, Page> t2 = logInfosService.logList(begindate, enddate, pageNo);
          List<LogInfoDto> logInfoDtoList = t2._1;
          Page page = t2._2;
          render(logInfoDtoList,page,begindate,enddate);
      }
}
