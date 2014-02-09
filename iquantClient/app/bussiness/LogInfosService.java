package bussiness;

import models.iquantCommon.LogInfoDto;
import play.libs.F;
import protoc.URILib;
import protoc.parser.ActionResult;
import util.CommonUtils;
import util.Page;

import java.util.Date;
import java.util.List;

/**
 * desc: 操作日志业务方法
 * User: weiguili(li5220008@163.com)
 * Date: 13-7-9
 * Time: 上午11:27
 */
public class LogInfosService extends BasicService {
    /**
     * 操作日志列表
     * @param begindateTime     起始日期
     * @param enddateTime       截止日期
     * @param pageNo        当前页数
     * @return             _1. 操作日志对象集合, _2 Page 分页对象
     */
    public static F.T2<List<LogInfoDto>, Page> logList(Date begindateTime,Date enddateTime,int pageNo){
        String begindate = CommonUtils.getFormatDate("yyyy-MM-dd HH:mm:ss", begindateTime);
        String enddate = CommonUtils.getFormatDate("yyyy-MM-dd HH:mm:ss", enddateTime);
        ActionResult<List<LogInfoDto>> result = remoteRequestService.getList(URILib.FETCH_LOG_LIST, LogInfoDto.class,begindate, enddate, pageNo);
        Page page = new Page(result.header.total.intValue(), pageNo);
        return F.T2(result.data, page);
    }

    /**
     * @Author 刘泓江
     * @param uid 用户ID
     * @param function 操作功能
     * @param content 操作内容
     * @param type 操作类型
     */
    public static void writeSystemLog(long uid,String function,String content,int type){
        remoteRequestService.getJson(URILib.WRITE_SYSTEM_LOG, uid, function, content, type);
    }
}
