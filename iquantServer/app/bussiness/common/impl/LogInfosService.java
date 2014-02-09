package bussiness.common.impl;

import bussiness.common.ILogInfoService;
import dbutils.SqlLoader;
import models.iquantCommon.LogInfoDto;
import play.libs.F;
import util.Page;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 操作日志业务方法
 * User: panzhiwei
 * Date: 12-12-12
 * Time: 下午2:51
 * To change this template use File | Settings | File Templates.
 */
public class LogInfosService extends BaseService implements ILogInfoService{
    /**
     * 操作日志列表
     * @param begindate     起始日期
     * @param enddate       截止日期
     * @param pageNo        当前页数
     * @return             _1. 操作日志对象集合, _2 Page 分页对象
     */
    public  F.T2<List<LogInfoDto>, Page> logList(Date begindate,Date enddate,int pageNo){
        String sql = SqlLoader.getSqlById("loglistsql");
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        SimpleDateFormat sfend = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        if(begindate != null && enddate != null){
            sql += " WHERE a.`cdate` >="+ "'" + sf.format(begindate)+ "'" + "AND a.`cdate` <=" + "'" + sfend.format(enddate)+ "'";
        }
        else if(begindate != null && enddate == null){
            sql += " WHERE a.`cdate` >=" + "'" + sf.format(begindate)+ "'";
        }
        else if(begindate == null && enddate != null){
            sql += " WHERE a.`cdate` <=" + "'" + sfend.format(enddate)+ "'";
        }

        StringBuilder coutSql = new StringBuilder("select count(*) from (\n" + sql + "\n) distTable  \n");

        Long total = qicDbUtil.queryCount(coutSql.toString());
        Page page = new Page(total.intValue(), pageNo);
        sql += "order by cdate desc limit " + page.beginIndex + "," + page.pageSize + "\n";
        List<LogInfoDto> logInfoDtoList = null;
        if (total > 0){
            logInfoDtoList = qicDbUtil.queryBeanList(sql, LogInfoDto.class);
        }
        return F.T2(logInfoDtoList,page);
    }


    /**
     * @Author 刘泓江
     * @param uid 用户ID
     * @param function 操作功能
     * @param content 操作内容
     * @param type 操作类型
     */
    public  void writeSystemLog(long uid,String function,String content,int type){
            String sql = SqlLoader.getSqlById("writeSystemLog");
            qicDbUtil.updateDB(sql, uid, function, content, type);
    }
}
