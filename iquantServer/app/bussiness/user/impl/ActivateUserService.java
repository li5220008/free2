package bussiness.user.impl;

import bussiness.common.impl.BaseService;
import bussiness.user.IActivateUserService;
import dbutils.SqlLoader;
import models.iquantCommon.ActivateUserDto;
import models.iquantCommon.AvtivatePar;
import org.apache.commons.lang.StringUtils;
import play.libs.F;
import util.Page;

import java.util.List;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-7-16
 * Time: 下午1:56
 * 功能描述:
 */
public class ActivateUserService extends BaseService implements IActivateUserService {

    /**
     * 待激活用户
     *
     * @param ap     参数对象
     * @param pageNo 页码
     * @return _1 列表list _2 page对象
     */
    public  F.T2<List<ActivateUserDto>, Page> userList(AvtivatePar ap, int pageNo) {
        String sqlList = SqlLoader.getSqlById("activateSql");
        List<ActivateUserDto> audList = null;
        if (StringUtils.isNotBlank(ap.name)) {
            sqlList += " AND ui.`name` like ? ";
        }
        if (StringUtils.isNotBlank(ap.account)) {
            sqlList += " AND ui.`account` like ? ";
        }
        if (ap.saleId != null && ap.saleId != 0l) {
            sqlList += " AND sd.`id` = '" + ap.saleId + "' ";
        }
        StringBuilder coutSql = new StringBuilder("select count(*) from (\n" + sqlList + "\n) distTable  \n");
        Long total;
        if(StringUtils.isNotBlank(ap.name)&&StringUtils.isNotBlank(ap.account)){
            total = qicDbUtil.queryCount(coutSql.toString(), "%" + ap.name + "%", "%" + ap.account + "%");
        }else if(StringUtils.isNotBlank(ap.name)&&StringUtils.isBlank(ap.account)){
            total = qicDbUtil.queryCount(coutSql.toString(), "%" + ap.name + "%");
        }else if(StringUtils.isBlank(ap.name)&&StringUtils.isNotBlank(ap.account)){
            total = qicDbUtil.queryCount(coutSql.toString(), "%" + ap.account + "%");
        }else{
            total = qicDbUtil.queryCount(coutSql.toString());
        }

        Page page = new Page(total.intValue(), pageNo);
        if(StringUtils.isBlank(ap.orderSort)){
            sqlList += " ORDER BY applyDate desc ";
        }else{
            if (ap.orderFlag == 0)
                sqlList += " ORDER BY " + ap.orderSort + " asc ";
            else
                sqlList += " ORDER BY " + ap.orderSort + " desc ";
        }
        sqlList += " limit " + page.beginIndex + "," + page.pageSize + "\n";
        if (total > 0) {
            if(StringUtils.isNotBlank(ap.name)&&StringUtils.isNotBlank(ap.account)){
                audList = qicDbUtil.queryBeanList(sqlList, ActivateUserDto.class, "%" + ap.name + "%", "%" + ap.account + "%");
            }else if(StringUtils.isNotBlank(ap.name)&&StringUtils.isBlank(ap.account)){
                audList = qicDbUtil.queryBeanList(sqlList, ActivateUserDto.class, "%" + ap.name + "%");
            }else if(StringUtils.isBlank(ap.name)&&StringUtils.isNotBlank(ap.account)){
                audList = qicDbUtil.queryBeanList(sqlList, ActivateUserDto.class, "%" + ap.account + "%");
            }else{
                audList = qicDbUtil.queryBeanList(sqlList, ActivateUserDto.class);
            }
        }
        return F.T2(audList, page);
    }

    /**
     * 用户列表
     *
     * @param ap     参数对象
     * @param pageNo 页码
     * @return _1 列表list _2 page对象
     */
    public  F.T2<List<ActivateUserDto>, Page> users(AvtivatePar ap, int pageNo) {

        String sqlList = SqlLoader.getSqlById("users");
        List<ActivateUserDto> audList = null;
        if (StringUtils.isNotBlank(ap.name)) {
            sqlList += " AND ui.`name` like ? ";
        }
        if (StringUtils.isNotBlank(ap.account)) {
            sqlList += " AND ui.`account` like ? ";
        }
        if (ap.saleId != null && ap.saleId != 0l) {
            sqlList += " AND sd.`id` = '" + ap.saleId + "' ";
        }
        if (ap.roleId != null && ap.roleId != 0l) {
            sqlList += " AND ri.`id` ='" + ap.roleId + "'";
        }
        if (ap.status != 0) {
            if (ap.status == 1 || ap.status == -100 || ap.status == 2) {//status :1:禁用,-100:删除,2:未激活
                sqlList += " AND ui.`status` = " + ap.status;
            } else {
                if (ap.status == 10) {//status =10 正常,且授权截至日期比当前日期大7天以上
                    sqlList += " AND ui.`status` =10 AND ui.`edate`>NOW() AND DATEDIFF(ui.`edate`,NOW())>7 ";
                } else if (ap.status == 70) {//status标识,查询一周到期用户.
                    sqlList += " AND ui.`status` =10 AND ui.`edate`>NOW() AND DATEDIFF(ui.`edate`,NOW())<=7 ";
                } else {//已到期用户
                    sqlList += " AND ui.`status` =10 AND ui.`edate`<NOW()";
                }
            }
        }
        sqlList += "  GROUP BY id ";
        StringBuilder coutSql = new StringBuilder("select count(*) from (\n" + sqlList + "\n) distTable  \n");
        Long total;
        if(StringUtils.isNotBlank(ap.name)&&StringUtils.isNotBlank(ap.account)){
            total = qicDbUtil.queryCount(coutSql.toString(), "%" + ap.name + "%", "%" + ap.account + "%");
        }else if(StringUtils.isNotBlank(ap.name)&&StringUtils.isBlank(ap.account)){
            total = qicDbUtil.queryCount(coutSql.toString(), "%" + ap.name + "%");
        }else if(StringUtils.isBlank(ap.name)&&StringUtils.isNotBlank(ap.account)){
            total = qicDbUtil.queryCount(coutSql.toString(), "%" + ap.account + "%");
        }else{
            total = qicDbUtil.queryCount(coutSql.toString());
        }
        Page page = new Page(total.intValue(), pageNo);
        if (StringUtils.isNotBlank(ap.orderSort)) {
            if (ap.orderFlag == 0)
                sqlList += " ORDER BY " + ap.orderSort + " asc ";
            else
                sqlList += " ORDER BY " + ap.orderSort + " desc ";

        }
        sqlList += " limit " + page.beginIndex + "," + page.pageSize + "\n";
        if (total > 0) {
            if(StringUtils.isNotBlank(ap.name)&&StringUtils.isNotBlank(ap.account)){
                audList = qicDbUtil.queryBeanList(sqlList, ActivateUserDto.class, "%" + ap.name + "%", "%" + ap.account + "%");
            }else if(StringUtils.isNotBlank(ap.name)&&StringUtils.isBlank(ap.account)){
                audList = qicDbUtil.queryBeanList(sqlList, ActivateUserDto.class, "%" + ap.name + "%");
            }else if(StringUtils.isBlank(ap.name)&&StringUtils.isNotBlank(ap.account)){
                audList = qicDbUtil.queryBeanList(sqlList, ActivateUserDto.class, "%" + ap.account + "%");
            }else{
                audList = qicDbUtil.queryBeanList(sqlList, ActivateUserDto.class);
            }
        }

        return F.T2(audList, page);
    }


    /**
     * 到期用户
     *
     * @param ap     参数对象
     * @param pageNo 页码
     * @return _1 列表list _2 page对象
     */
    public  F.T2<List<ActivateUserDto>, Page> dueUsers(AvtivatePar ap, int pageNo) {

        String sqlList = SqlLoader.getSqlById("dueUsersSql");
        List<ActivateUserDto> audList = null;
        if (StringUtils.isNotBlank(ap.name)) {
            sqlList += " AND ui.`name` like ? ";
        }
        if (StringUtils.isNotBlank(ap.account)) {
            sqlList += " AND ui.`account` like ? ";
        }
        if (ap.saleId != null && ap.saleId != 0l) {
            sqlList += " AND sd.`id` = '" + ap.saleId + "' ";
        }
        if (ap.roleId != null && ap.roleId != 0l) {
            sqlList += " AND ri.`id` ='" + ap.roleId + "'";
        }

        sqlList += "  GROUP BY id ";
        StringBuilder coutSql = new StringBuilder("select count(*) from (\n" + sqlList + "\n) distTable  \n");

        Long total;
        if(StringUtils.isNotBlank(ap.name)&&StringUtils.isNotBlank(ap.account)){
            total = qicDbUtil.queryCount(coutSql.toString(), "%" + ap.name + "%", "%" + ap.account + "%");
        }else if(StringUtils.isNotBlank(ap.name)&&StringUtils.isBlank(ap.account)){
            total = qicDbUtil.queryCount(coutSql.toString(), "%" + ap.name + "%");
        }else if(StringUtils.isBlank(ap.name)&&StringUtils.isNotBlank(ap.account)){
            total = qicDbUtil.queryCount(coutSql.toString(), "%" + ap.account + "%");
        }else{
            total = qicDbUtil.queryCount(coutSql.toString());
        }

        Page page = new Page(total.intValue(), pageNo);
        if (StringUtils.isNotBlank(ap.orderSort)) {
            if (ap.orderFlag == 0)
                sqlList += " ORDER BY " + ap.orderSort + " asc ";
            else
                sqlList += " ORDER BY " + ap.orderSort + " desc ";

        }
        sqlList += " limit " + page.beginIndex + "," + page.pageSize + "\n";
        if (total > 0) {
            if(StringUtils.isNotBlank(ap.name)&&StringUtils.isNotBlank(ap.account)){
                audList = qicDbUtil.queryBeanList(sqlList, ActivateUserDto.class, "%" + ap.name + "%", "%" + ap.account + "%");
            }else if(StringUtils.isNotBlank(ap.name)&&StringUtils.isBlank(ap.account)){
                audList = qicDbUtil.queryBeanList(sqlList, ActivateUserDto.class, "%" + ap.name + "%");
            }else if(StringUtils.isBlank(ap.name)&&StringUtils.isNotBlank(ap.account)){
                audList = qicDbUtil.queryBeanList(sqlList, ActivateUserDto.class, "%" + ap.account + "%");
            }else{
                audList = qicDbUtil.queryBeanList(sqlList, ActivateUserDto.class);
            }
        }
        return F.T2(audList, page);
    }

}
