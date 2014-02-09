package bussiness.common;

import models.iquantCommon.LogInfoDto;
import play.libs.F;
import util.Page;

import java.util.Date;
import java.util.List;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-7-4
 * Time: 上午10:21
 * 功能描述:
 */
public interface ILogInfoService {


    /**
     * 操作日志列表
     *
     * @param begindate 起始日期
     * @param enddate   截止日期
     * @param pageNo    当前页数
     * @return _1. 操作日志对象集合, _2 Page 分页对象
     */
    public F.T2<List<LogInfoDto>, Page> logList(Date begindate, Date enddate, int pageNo);


    /**
     * @param uid      用户ID
     * @param function 操作功能
     * @param content  操作内容
     * @param type     操作类型
     * @Author 刘泓江
     */
    public void writeSystemLog(long uid, String function, String content, int type);
}
