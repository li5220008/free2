package bussiness.strategy.impl;

import bussiness.common.impl.BaseService;
import bussiness.strategy.IStrategyService;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dbutils.SqlLoader;
import models.iquantCommon.*;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.RowProcessor;
import org.apache.commons.dbutils.handlers.AbstractKeyedHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import play.Logger;
import play.libs.F;
import play.libs.XML;
import play.libs.XPath;
import play.modules.guice.InjectSupport;
import util.CommonUtils;
import util.Page;
import utils.DrawPictrueUtil;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * 策略超市基本信息展示业务方法
 * User: liangbing
 * Date: 12-11-9
 * Time: 上午9:10
 */
@InjectSupport
public class StrategyService extends BaseService implements IStrategyService {

    public StrategyBaseDto findStrategyByName(String sname) {
        String sql = SqlLoader.getSqlById("findStrategyByName");
        return qicDbUtil.querySingleBean(sql, StrategyBaseDto.class, sname);
    }


    public StrategyBaseinfo findStrategyById(long id) {
        String sql = SqlLoader.getSqlById("findStrategyById");
        return qicDbUtil.querySingleBean(sql, StrategyBaseinfo.class, id);
    }

    /**
     * 查询一批策略
     *
     * @param ids
     * @return
     */
    public List<StrategyBaseinfo> findStrategysByIds(String ids[]) {
        if (ids == null || ids.length == 0) {
            return new ArrayList<StrategyBaseinfo>();
        }

        Long[] list = new Long[ids.length];
        for (int i = 0; i < ids.length; i++) {

            list[i] = Long.valueOf(ids[i]);
        }
        return findStrategysByIds(list);
    }

    public List<StrategyBaseinfo> findStrategysByIds(Long ids[]) {
        StringBuffer sql = new StringBuffer(SqlLoader.getSqlById("findStrategysByIds"));
        sql.append(" and id IN (");
        for (int i = 0; i < ids.length; i++) {
            sql.append("?");
            if (i < ids.length - 1) {
                sql.append(",");
            }
        }
        sql.append(")");
        return qicDbUtil.queryBeanList(sql.toString(), StrategyBaseinfo.class, ids);
    }

    public StrategyDto findStrategyByUUID(String strUUID) {
        String sql = SqlLoader.getSqlById("findStrategyByUUID");
        return qicDbUtil.querySingleBean(sql, StrategyDto.class, strUUID);
    }

    public List<QiaStrategyDto> findQiaStrategyByStrategyIds(List<Long> stids) {
        if (stids == null || stids.size() == 0) {
            return Lists.newArrayList();
        }
        Joiner joiner = Joiner.on(",");
        String ids = joiner.join(stids);
        String sql = SqlLoader.getSqlById("findQiaStrategyByStrategyIds");
        return qicDbUtil.queryBeanList(sql.replace("#stids#", ids), QiaStrategyDto.class);
    }

    //策略持仓
    public List<StrategyPositionDto> getStrategyPosition(long stid, int pageNo) {
        StrategyBaseinfo strategyBaseinfo = findStrategyById(stid);

        String sql = SqlLoader.getSqlById("StrategyPosition");
        List<StrategyPositionDto> strategyPositionDtoList = qicoreDbUtil.queryBeanList(sql, StrategyPositionDto.class, strategyBaseinfo.stUuid, strategyBaseinfo.stUuid);
        if (strategyPositionDtoList != null) {
            for (StrategyPositionDto sp : strategyPositionDtoList) {
                sp.name = strategyBaseinfo.name;
                sp.trade_variety = strategyBaseinfo.tradeVariety;
            }
        } else {
            List<StrategyPositionDto> strategyPositionDtoList1 = new ArrayList<StrategyPositionDto>();
            strategyPositionDtoList = strategyPositionDtoList1;
        }
        return strategyPositionDtoList;
    }

    //QIA 策略持仓
    public List<StrategyPositionDto> getQiaPosition(long stid, int pageNo) {
        String sql = SqlLoader.getSqlById("qiaPosition");
        /*StringBuilder coutSql = new StringBuilder("select count(*) from (\n" + sql + "\n) distTable  \n");
        Long total = qicDbUtil.queryQicDbCount(coutSql.toString(),stid);
        Page page = new Page(total.intValue(), pageNo);
        if (pageNo * page.pageSize > total) {
            return null;
        }*/
        //sql += " limit " + page.beginIndex + "," + page.pageSize + "\n";
        List<StrategyPositionDto> strategyPositionDtoList = qicDbUtil.queryBeanList(sql, StrategyPositionDto.class, stid, stid);
        if (strategyPositionDtoList == null) {
            strategyPositionDtoList = new ArrayList<StrategyPositionDto>();
        }

        return strategyPositionDtoList;
    }

    //绩效指标
    public IndicatorDto getindicator(long stid, int type) {
        String sql = SqlLoader.getSqlById("Indicator");
        IndicatorDto indicator = qicDbUtil.querySingleBean(sql, IndicatorDto.class, stid, type);
        /*if(indicator == null){
            IndicatorDto indicatorDto = new IndicatorDto();
            indicator = indicatorDto;
        }*/
        return indicator;
    }

    //QIA 绩效指标
    public QiaIndicatorDto getQiaIndicatorDto(long stid, int type) {
        String sql = SqlLoader.getSqlById("qiaIndicatorSql");
        QiaIndicatorDto indicator = qicDbUtil.querySingleBean(sql, QiaIndicatorDto.class, stid, type);
        /*if(indicator == null){
            indicator = new QiaIndicatorDto();
        }*/
        return indicator;
    }

    //成交记录
    public List<ExecutionRecordDto> getExecutionRecord(Long stid, int pageNo) {
        StrategyBaseinfo strategyBaseinfo = findStrategyById(stid);

        String sql = SqlLoader.getSqlById("ExecutionRecord");
        StringBuilder coutSql = new StringBuilder("select count(*) from (\n" + sql + "\n) distTable  \n");
        Long total = qicoreDbUtil.queryCount(coutSql.toString(), strategyBaseinfo.stUuid, strategyBaseinfo.lookbackEtime);
        Page page = new Page(total.intValue(), pageNo);
        sql += " limit " + page.beginIndex + "," + page.pageSize + "\n";
        if (pageNo * page.pageSize > total) {
            return null;
        }
        List<ExecutionRecordDto> executionRecordDtoList = qicoreDbUtil.queryBeanList(sql, ExecutionRecordDto.class, strategyBaseinfo.stUuid, strategyBaseinfo.lookbackEtime);
        if (executionRecordDtoList != null) {
            for (ExecutionRecordDto er : executionRecordDtoList) {
                er.name = strategyBaseinfo.name;
                er.trade_variety = strategyBaseinfo.tradeVariety;
            }
        } else {
            List<ExecutionRecordDto> executionRecordDtoList1 = new ArrayList<ExecutionRecordDto>();
            executionRecordDtoList = executionRecordDtoList1;
        }
        return executionRecordDtoList;
    }

    //委托记录
    public List<AuthorizeRecordDto> getAuthorizeRecord(long stid, int pageNo) {
        StrategyBaseinfo strategyBaseinfo = findStrategyById(stid);

        String sql = SqlLoader.getSqlById("AuthorizeRecord");
        StringBuilder coutSql = new StringBuilder("select count(*) from (\n" + sql + "\n) distTable  \n");
        Long total = qicoreDbUtil.queryCount(coutSql.toString(), strategyBaseinfo.stUuid, strategyBaseinfo.lookbackEtime);
        Page page = new Page(total.intValue(), pageNo);
        if (pageNo * page.pageSize > total) {
            return null;
        }
        sql += " limit " + page.beginIndex + "," + page.pageSize + "\n";
        List<AuthorizeRecordDto> authorizeRecordDtoList = qicoreDbUtil.queryBeanList(sql, AuthorizeRecordDto.class, strategyBaseinfo.stUuid, strategyBaseinfo.lookbackEtime);
        if (authorizeRecordDtoList != null) {
            for (AuthorizeRecordDto ar : authorizeRecordDtoList) {
                ar.name = strategyBaseinfo.name;
                ar.trade_variety = strategyBaseinfo.tradeVariety;
            }
        } else {
            List<AuthorizeRecordDto> authorizeRecordDtoList1 = new ArrayList<AuthorizeRecordDto>();
            authorizeRecordDtoList = authorizeRecordDtoList1;
        }
        return authorizeRecordDtoList;
    }


    /**
     * @param myselect 分类搜索
     * @param content  关键字
     * @param pageNo   当前页数
     * @return _1 为结果集, _2为 分页page信息,
     */
    public F.T2<List<StrategyBaseDto>, Page> strategyList(int myselect, String content, int pageNo) {
        String sqlList = SqlLoader.getSqlById("strategyListSql");

        List<StrategyBaseDto> sbdList = null;
        String condition = "";

        //content 关键字查询内容
        if (StringUtils.isNotBlank(content) && !"null".equals(content)) {
            condition = " AND  (sb.`name` like ? OR sb.`provider` like ?) ";
        }
        if (myselect == 0) {//分类查询,默认按收益率排行 排序
            if (content != null && content != "") {
                sqlList += condition;
            }
            sqlList += " AND sb.up_time < DATE_ADD(NOW(), INTERVAL - 3 DAY)  ORDER BY yield DESC ";

        } else if (myselect == 1) {//myselect = 1按收益率排行 排序
            if (content != null && content != "") {
                sqlList += condition;
            }
            sqlList += " AND sb.up_time < DATE_ADD(NOW(), INTERVAL - 3 DAY)  ORDER BY yield DESC ";

        } else if (myselect == 2) {//myselect =2 按人气排行 排序
            if (content != null && content != "") {
                sqlList += condition;
            }
            sqlList += " ORDER BY collectCount DESC ";

        } else {//按最新 排序
            if (content != null && content != "") {
                sqlList += condition;
            }
            sqlList += " ORDER BY upTime DESC ";

        }
        Long total;
        StringBuilder coutSql = new StringBuilder("select count(*) from (\n" + sqlList + "\n) distTable where 1=1 \n");

        if (StringUtils.isNotBlank(content) && !"null".equals(content)) {
            total = qicDbUtil.queryCount(coutSql.toString(), "%" + content + "%", "%" + content + "%");
        } else {
            total = qicDbUtil.queryCount(coutSql.toString());
        }
        Page page = new Page(total.intValue(), pageNo);
        sqlList += " limit " + page.beginIndex + "," + page.pageSize + "\n";
        if (StringUtils.isNotBlank(content) && !"null".equals(content)) {
            sbdList = qicDbUtil.queryBeanList(sqlList, StrategyBaseDto.class, "%" + content + "%", "%" + content + "%");
        } else {
            sbdList = qicDbUtil.queryBeanList(sqlList, StrategyBaseDto.class);
        }
        return F.T2(sbdList, page);

    }


    /**
     * 高级搜索
     *
     * @param cnd
     * @param pageNo 当前页
     * @return _1 为结果集, _2为总条数, _3 总共页数
     */
    public F.T2<List<StrategyBaseDto>, Page> advanceSearch(StrategySearchCnd cnd, int myselect, int pageNo) {
        String sql = SqlLoader.getSqlById("strategyListSql");
        StringBuilder listSelectSql = new StringBuilder("select * from (\n" + sql + "\n) distTable where 1=1 \n");
        StringBuilder coutSql = new StringBuilder("select count(*) from (\n" + sql + "\n) distTable where 1=1 \n");
        StringBuilder where = new StringBuilder();
        if (cnd.tradeType != null && cnd.tradeType != CommonUtils.SELECT_ALL_OPTION_VALUE) {
            where.append(" and stype = " + cnd.tradeType.intValue());
        }

        if (cnd.tradeVariety != null && cnd.tradeVariety != CommonUtils.SELECT_ALL_OPTION_VALUE) {
            where.append(" and tradeVariety = " + cnd.tradeVariety.intValue()).append('\n');
        }

        if (cnd.yieldDown != null) {
            where.append(" and yield >= " + cnd.yieldDown / 100).append('\n');
        }

        if (cnd.yieldUp != null) {
            where.append(" and yield <= " + cnd.yieldUp / 100).append('\n');
        }

        if (cnd.profitRatioDown != null) {
            where.append(" and profitRatio >= " + cnd.profitRatioDown / 100).append('\n');
        }

        if (cnd.profitRatioUp != null) {
            where.append(" and profitRatio <= " + cnd.profitRatioUp / 100).append('\n');
        }

        if (cnd.starDown != null) {
            where.append(" and starLevel >= " + cnd.starDown).append('\n');
        }

        if (cnd.starUp != null) {
            where.append(" and starLevel <= " + cnd.starUp).append('\n');
        }

        if (myselect == 3) {
            where.append(" ORDER BY upTime DESC ");
        } else if (myselect == 2) {
            where.append(" ORDER BY collectCount DESC ");
        } else {
            where.append(" AND upTime < DATE_ADD(NOW(), INTERVAL - 3 DAY)  ORDER BY yield DESC ");
        }

        if (Logger.isDebugEnabled()) {
            Logger.debug("sql where ==" + where.toString());
        }

        listSelectSql.append(where);
        coutSql.append(where);

        Long total = qicDbUtil.queryWithHandler(coutSql.toString(), new ScalarHandler<Long>());
        Page page = new Page(total.intValue(), pageNo);
        listSelectSql.append("\n limit " + page.beginIndex + "," + page.pageSize + "\n");
        List<StrategyBaseDto> dtoList = qicDbUtil.queryBeanList(listSelectSql.toString(), StrategyBaseDto.class);

        return F.T2(dtoList, page);
    }

    public F.T2<List<StrategyBaseDto>, Page> findStrategysByUser(Map<String, String> queryParams) {
        //查询sql
        StringBuffer querySql = new StringBuffer(SqlLoader.getSqlById("findStrategysByUser"));
        //计算总数sql
        StringBuffer countSql = new StringBuffer();
        countSql.append("SELECT \n").append("COUNT(1)\n").append("FROM (\n");
        countSql.append(SqlLoader.getSqlById("countOfStrategysByUser"));
        String keyWord = queryParams.get("keyword");
        String orderCol = queryParams.get("orderCol") == null ? "1" : queryParams.get("orderCol");
        int pageNo = Integer.valueOf(queryParams.get("pageNo"));
        int status = Integer.valueOf(queryParams.get("status"));
        int uid = Integer.valueOf(queryParams.get("uid"));
        int orderByType = queryParams.get("orderType") == null ? 0 : Integer.valueOf(queryParams.get("orderType"));
        List<Object> queryList = new ArrayList<Object>(4);
        List<Object> countList = new ArrayList<Object>(2);
        queryList.add(uid);
        countList.add(uid);
        Long totalSize = 0L;

        if (status == StrategyBaseDto.StrategyStatus.DOWNSHELF.value || status == StrategyBaseDto.StrategyStatus.DELETED.value) {//状态查询
            querySql.append(" and status=?\n");
            countSql.append(" and status=?\n");
            queryList.add(status);
            countList.add(status);
        } else if (status == StrategyBaseDto.StrategyStatus.UPSHELF.value) {
            querySql.append(" and status=?  or status=?\n");
            countSql.append(" and status=?  or status=?\n");
            queryList.add(StrategyBaseDto.StrategyStatus.UPSHELF.value);
            countList.add(StrategyBaseDto.StrategyStatus.UPSHELF.value);
            queryList.add(StrategyBaseDto.StrategyStatus.WAITINGUPSHELF.value);
            countList.add(StrategyBaseDto.StrategyStatus.WAITINGUPSHELF.value);
        } else if (status == -2) {//查询审核中的 此处的审核中包含 沙箱测试(2) 回测中(3) 回测试失败(8) 审核中(1) 四个状态
            querySql.append(" and (status >0 and status<? or status=? or status=?)\n");
            countSql.append(" and (status >0 and status<? or status=? or status=?)\n");
            queryList.add(StrategyBaseDto.StrategyStatus.UPSHELF.value);
            countList.add(StrategyBaseDto.StrategyStatus.UPSHELF.value);
            queryList.add(StrategyBaseDto.StrategyStatus.FINISHTEST.value);
            countList.add(StrategyBaseDto.StrategyStatus.FINISHTEST.value);
            queryList.add(StrategyBaseDto.StrategyStatus.BACKTESTINGFAILER.value);
            countList.add(StrategyBaseDto.StrategyStatus.BACKTESTINGFAILER.value);
        }
        if (keyWord != null && !"".equals(keyWord)) {//关键查询
            //这里注意 like需要使用预编译的时候 不用写成:querySql.append(" and (provider like ? or name like '%?%' )\n");
            //%号必需在set的时候进行拼接
            querySql.append(" and (provider like ? or name like ? )\n");
            countSql.append(" and (provider like ?  or name like  ? )\n");
            queryList.add("%" + keyWord + "%");
            queryList.add("%" + keyWord + "%");
            countList.add("%" + keyWord + "%");
            countList.add("%" + keyWord + "%");
        }

        /**
         * 这里加入三个查询条件
         */

        int tradeType = Integer.valueOf(queryParams.get("tradeType"));//交易类型
        if (tradeType > 0) {
            querySql.append(" AND sb.trade_type=?\n");
            countSql.append(" AND sb.trade_type=?\n");
            queryList.add(tradeType);
            countList.add(tradeType);
        }
        int tradeVariety = Integer.valueOf(queryParams.get("tradeVariety"));//交易品种
        if (tradeVariety > 0) {
            querySql.append(" AND sb.trade_variety=?\n");
            countSql.append(" AND sb.trade_variety=?\n");
            queryList.add(tradeVariety);
            countList.add(tradeVariety);
        }
        int strategyLanguage = Integer.valueOf(queryParams.get("strategyLanguage"));//策略语言
        if (strategyLanguage > 0) {
            querySql.append(" AND sb.enginetype_id=?\n");
            countSql.append(" AND sb.enginetype_id=?\n");
            queryList.add(strategyLanguage);
            countList.add(strategyLanguage);
        }


        //排序,把order by 的字段出来
        querySql.append(" order by " + getColNameByIndex(orderCol) + (orderByType == 0 ? " ASC" : " DESC") + " \n");
        countSql.append("\n) tmp");
        totalSize = qicDbUtil.queryCount(countSql.toString(), countList.toArray());
        Page page = new Page(totalSize.intValue(), pageNo);
        querySql.append(" limit ?,? ");
        queryList.add(page.beginIndex);
        queryList.add(page.pageSize);

        List<StrategyBaseDto> strategyBaseDtoList = qicDbUtil.queryBeanList(querySql.toString(), StrategyBaseDto.class, queryList.toArray());
        return F.T2(strategyBaseDtoList, page);
    }

    private String getColNameByIndex(String index) {
        switch (Integer.valueOf(index)) {
            case 1:
                return "sb.name";
            case 2:
                return "sb.provider";
            case 3:
                return "sb.status";
            case 4:
                return "sb.upload_time";
            case 5:
                return "sb.pass_time";
            case 6:
                return "sb.up_time";
            case 7:
                return "sb.trade_type";
            case 8:
                return "sb.order_count";
            case 9:
                return "uso.validCount";
            case 10:
                return "sb.collect_count";
            case 11:
                return "sb.trade_variety";
            case 12:
                return "sb.enginetype_id";
            default:
                return "sb.status";


        }

    }

    public List<F.T2<StrategyBaseDto, StrategyOrderDto>> userOrderStrateList(long uid) {
        String sql = SqlLoader.getSqlById("strategyUserListSql");
        sql = sql.replaceAll("#user_type_table#", "user_strategy_order");
        sql = sql.replace("#fieldList#", "ust.id AS orderId, ust.id AS orderId, ust.order_etime AS order_etime, ust.order_stime AS order_stime, ust.stid AS stid");

        return qicDbUtil.queryWithHandler(sql, new ResultSetHandler<List<F.T2<StrategyBaseDto, StrategyOrderDto>>>() {
            @Override
            public List<F.T2<StrategyBaseDto, StrategyOrderDto>> handle(ResultSet rs) throws SQLException {
                RowProcessor convert = qicDbUtil.ROW_PROCESSOR;
                List<F.T2<StrategyBaseDto, StrategyOrderDto>> list = Lists.newLinkedList();
                while (rs.next()) {
                    StrategyBaseDto strategyBaseDto = convert.toBean(rs, StrategyBaseDto.class);
                    StrategyOrderDto strategyOrderDto = qicDbUtil.PLAY_BEAN_PROCESSOR.toBeanWithField(rs, StrategyOrderDto.class, "order_etime", "order_stime", "stid");
                    list.add(F.T2(strategyBaseDto, strategyOrderDto));
                }
                return list;
            }
        }, uid);
    }

    //==================manage

    /**
     * 查询待上架策略
     *
     * @param sp     参数类
     * @param pageNo 当前页
     * @return _1. 待上架策略对象集合, _2 Page 分页对象
     */
    public F.T2<List<StrategyDto>, Page> waitList(StrategyPar sp, int pageNo, long uid) {
        String sqlList = SqlLoader.getSqlById("StrategyGroundingRetrieve");
        /* if (FunctionAuthManager.auth(FunctionAuthManager.ALLOW_MANAGE_ALL_STRATEGY_FUN, uid)) {
          sqlList += " and status in (1,2,3,6,8) ";
      } else {*/
        sqlList += " and status in (1,2,3,6,8) and sb.stup_uid=" + uid + " ";
        // }
        if (sp.tradeType != 0) {
            sqlList += " AND sb.`trade_type` =" + sp.tradeType + " ";
        }
        if (sp.tradeVariety != 0) {
            sqlList += " AND sb.`trade_variety` =" + sp.tradeVariety + " ";
        }
        if (sp.status != 0) {
            sqlList += " AND sb.`status`=" + sp.status + " ";
        }
        if (sp.strategyLanguage != 0) {
            sqlList += " AND sb.enginetype_id=" + sp.strategyLanguage + " ";
        }
        if (StringUtils.isNotBlank(sp.keyWords)) {
            sqlList += " and (sb.`name` LIKE '%" + sp.keyWords + "%' OR sb.`provider` LIKE '%" + sp.keyWords + "%')  ";
        }
        List<StrategyDto> audList = null;

        StringBuilder coutSql = new StringBuilder("select count(*) from (\n" + sqlList + "\n) distTable  \n");
        Long total = qicDbUtil.queryCount(coutSql.toString());
        if (StringUtils.isNotBlank(sp.orderSort)) {
            if (sp.orderFlag == 0)
                sqlList += " ORDER BY " + sp.orderSort + " asc ";
            else
                sqlList += " ORDER BY " + sp.orderSort + " desc ";

        }
        Page page = new Page(total.intValue(), pageNo);
        sqlList += " limit " + page.beginIndex + "," + page.pageSize + "\n";
        if (total > 0) {
            audList = qicDbUtil.queryBeanList(sqlList, StrategyDto.class);
        }
        return F.T2(audList, page);
    }

    private List<StrategyDto> wrapQiaStrategyData(List<StrategyDto> list) {
        if (list != null && list.size() > 0) {
            List<Long> stids = Lists.newArrayList();
            Map<String, StrategyDto> map = Maps.newHashMap();
            for (StrategyDto strategyDto : list) {
                map.put(String.valueOf(strategyDto.id), strategyDto);
                if (strategyDto.enginetypeId == StrategyDto.QIA_ENGINEE_ID)
                    stids.add(strategyDto.id);
            }
            List<QiaStrategyDto> qiaStrategyDtoList = findQiaStrategyByStrategyIds(stids);
            for (QiaStrategyDto qiaStrategyDto : qiaStrategyDtoList) {
                StrategyDto dto = map.get(qiaStrategyDto.strategyId);
                map.get(qiaStrategyDto.strategyId).qiaStrategyDto = qiaStrategyDto;
            }
        }
        return list;
    }

    /**
     * 策略回收站列表
     *
     * @param sp     参数类
     * @param pageNo 当前页
     * @return _1. 策略回收站对象集合, _2 Page 分页对象
     */
    public F.T2<List<StrategyDto>, Page> retrieveList(StrategyPar sp, int pageNo, long uid) {
        String sqlList = SqlLoader.getSqlById("StrategyGroundingRetrieve");
        /*  if (FunctionAuthManager.auth(FunctionAuthManager.ALLOW_MANAGE_ALL_STRATEGY_FUN, uid)) {
          sqlList += " and status in(?,?)  ";
      } else {*/
        sqlList += " and status in(?,?) and sb.stup_uid=" + uid + " ";
        // }
        if (sp.tradeType != 0) {
            sqlList += " AND sb.`trade_type` =" + sp.tradeType + " ";
        }
        if (sp.tradeVariety != 0) {
            sqlList += " AND sb.`trade_variety` =" + sp.tradeVariety + " ";
        }
        if (sp.status != 0) {
            sqlList += " AND sb.`status`=" + sp.status + " ";
        }
        if (sp.strategyLanguage != 0) {
            sqlList += " AND sb.`enginetype_id` =" + sp.strategyLanguage + " ";
        }

        if (StringUtils.isNotBlank(sp.keyWords)) {
            sqlList += " and (sb.`name` LIKE '%" + sp.keyWords + "%' OR sb.`provider` LIKE '%" + sp.keyWords + "%')  ";
        }
        List<StrategyDto> audList = null;
        StringBuilder coutSql = new StringBuilder("select count(*) from (\n" + sqlList + "\n) distTable  \n");
        Long total = qicDbUtil.queryCount(coutSql.toString(), StrategyDto.StrategyStatus.DOWNSHELF.value, StrategyDto.StrategyStatus.DELETED.value);
        if (StringUtils.isNotBlank(sp.orderSort)) {
            if (sp.orderFlag == 0)
                sqlList += " ORDER BY " + sp.orderSort + " asc ";
            else
                sqlList += " ORDER BY " + sp.orderSort + " desc ";

        }
        Page page = new Page(total.intValue(), pageNo);
        sqlList += " limit " + page.beginIndex + "," + page.pageSize + "\n";
        if (total > 0) {
            audList = qicDbUtil.queryBeanList(sqlList, StrategyDto.class, StrategyDto.StrategyStatus.DOWNSHELF.value, StrategyDto.StrategyStatus.DELETED.value);
        }
        return F.T2(audList, page);
    }

    /**
     * 上架策略列表
     *
     * @param sp     参数类
     * @param pageNo 当前页
     * @return _1. 策略列表对象集合, _2 Page 分页对象
     */
    public F.T2<List<StrategyDto>, Page> groundingList(StrategyPar sp, int pageNo, long uid) {
        Map<String, Date> getOrderTimeById = fetchAllMaxOrder();
        String sqlList = SqlLoader.getSqlById("StrategySql");
        sqlList += " and status in (?,?) and sb.stup_uid in(" + uid + ",-1000) ";
        if (StringUtils.isNotBlank(sp.keyWords)) {
            sqlList += " and (sb.`name` LIKE '%" + sp.keyWords + "%' OR sb.`provider` LIKE '%" + sp.keyWords + "%')  ";
        }
        if (sp.tradeType != 0) {
            sqlList += " AND sb.`trade_type` =" + sp.tradeType + " ";
        }
        if (sp.tradeVariety != 0) {
            sqlList += " AND sb.`trade_variety` =" + sp.tradeVariety + " ";
        }
        if (sp.strategyLanguage != 0) {
            sqlList += " AND sb.`enginetype_id` =" + sp.strategyLanguage + " ";
        }
        List<StrategyDto> audList = new ArrayList<StrategyDto>();
        StringBuilder coutSql = new StringBuilder("select count(*) from (\n" + sqlList + "\n) distTable  \n");
        Long total = qicDbUtil.queryCount(coutSql.toString(), StrategyDto.StrategyStatus.UPSHELF.value, StrategyDto.StrategyStatus.WAITINGUPSHELF.value);
        if (StringUtils.isNotBlank(sp.orderSort)) {
            if (sp.orderFlag == 0)
                sqlList += " ORDER BY " + sp.orderSort + " asc ";
            else
                sqlList += " ORDER BY " + sp.orderSort + " desc ";

        }
        Page page = new Page(total.intValue(), pageNo);
        sqlList += " limit " + page.beginIndex + "," + page.pageSize + "\n";
        if (total > 0) {
            List<StrategyDto>  strategyList = qicDbUtil.queryBeanList(sqlList, StrategyDto.class, StrategyDto.StrategyStatus.UPSHELF.value, StrategyDto.StrategyStatus.WAITINGUPSHELF.value);
            for(StrategyDto sd:strategyList){
                sd.endDate=  getOrderTimeById.get(sd.id.toString());
                audList.add(sd);
            }

        }
        return F.T2(audList, page);
    }

    public Map<String, Date> fetchAllMaxOrder(){
        Map<String, Date> allMaxOrderTime = Maps.newHashMap();
        String sql= SqlLoader.getSqlById("StrategyMaxOrderTimeSql");
        allMaxOrderTime = qicDbUtil.queryWithHandler(sql, new AbstractKeyedHandler<String, Date>() {
            @Override
            protected String createKey(ResultSet rs) throws SQLException {
                return rs.getString("id");
            }
            @Override
            protected Date createRow(ResultSet rs) throws SQLException {
                return rs.getDate("endDate");
            }
        });
        return allMaxOrderTime;
    }


    @Deprecated //没用了
    public boolean deleteStrategyFromQsm(String[] ids) {
        /*    List<StrategyBaseinfo> strategyBaseinfoList = findStrategysByIds(ids);
        if (strategyBaseinfoList == null || strategyBaseinfoList.size() == 0) {
            return false;
        } else {
            String qsmSql = SqlLoader.getSqlById("deleteStrategyFromQsm");
            Object[][] params = new Object[strategyBaseinfoList.size()][1];
            for (int row = 0; row < strategyBaseinfoList.size(); row++) {
                params[row][0] = strategyBaseinfoList.get(row).stUuid;
            }
            qsmDbUtil.batch(qsmSql, params);
        }*/
        return true;
    }


    public F.T4<IndicatorDto, IndicatorDto, QiaIndicatorDto, QiaIndicatorDto> getIndicator(long stid, int enginetypeId) {
        //绩效指标
        IndicatorDto indicator = new IndicatorDto();
        IndicatorDto indicatorhis = new IndicatorDto();
        //ＱＩＡ的绩效指标
        QiaIndicatorDto qiaIndicatorDto = new QiaIndicatorDto();
        QiaIndicatorDto qiaIndicatorDtohis = new QiaIndicatorDto();
        switch (enginetypeId) {
            case StrategyDto.QICORE_ENGINEE_ID://QICore的绩效指标
                indicator = getindicator(stid, 2);
                indicatorhis = getindicator(stid, 1);
                break;
            case StrategyDto.QIA_ENGINEE_ID://QIA的绩效指标
                qiaIndicatorDto = getQiaIndicatorDto(stid, 2);
                qiaIndicatorDtohis = getQiaIndicatorDto(stid, 1);
                break;
            default:
        }
        return F.T4(indicator, indicatorhis, qiaIndicatorDto, qiaIndicatorDtohis);
    }


    public Integer countUserRunntimeStrategy(long uid) {
        String sqlList = SqlLoader.getSqlById("StrategySql");
        sqlList += " and status in (?,?) and sb.stup_uid=" + uid + " ";
        StringBuilder coutSql = new StringBuilder("select count(*) from (\n" + sqlList + "\n) distTable  \n");
        Long total = qicDbUtil.queryCount(coutSql.toString(), StrategyDto.StrategyStatus.UPSHELF.value, StrategyDto.StrategyStatus.WAITINGUPSHELF.value);
        int num = total.intValue();
        return num;
    }

    @Override
    public List<Map<String, Object>> findStrategyStockInfo(List<Long> ids, long uid) {
        String sql = SqlLoader.getSqlById("fetchStrategyStockInfo");
        List<Map<String, Object>> jsonList = Lists.newLinkedList();
        if (ids != null) {
            for (Long id : ids) {
                List<StrategySecDto> secDtoList = Lists.newArrayList();
                String param = qicDbUtil.queryWithHandler(sql, new ScalarHandler<String>(), id, uid);
                //解析xml
                if (StringUtils.isNotBlank(param)) {
                    Document document = XML.getDocument(param);
                    if (document != null) {
                        List<Node> nodeList = XPath.selectNodes("/Strategy/code", document);
                        Map<String, Object> itemMap = Maps.newHashMap();
                        for (Node node : nodeList) {
                            StrategySecDto secDto = new StrategySecDto();
                            secDto.contractMultiplier = CommonUtils.parseNumber(XPath.selectText("@ContractMultiplier", node), Double.class, null);
                            secDto.marginLevel = CommonUtils.parseNumber(XPath.selectText("@MarginLevel", node), Double.class, null);
                            secDto.maxShare = CommonUtils.parseNumber(XPath.selectText("@MaxShare", node), Double.class, null);
                            secDto.currency = XPath.selectText("@Currency", node);
                            secDto.exchangeType = XPath.selectText("@exchangeType", node);
                            secDto.secId = XPath.selectText("@id", node);
                            secDto.secName = XPath.selectText("@name", node);

                            secDtoList.add(secDto);
                        }

                        itemMap.put("strategyId", id);
                        itemMap.put("strategySecInfos", secDtoList);
                        jsonList.add(itemMap);
                    }
                }
            }
        }
        return jsonList;
    }


    @Override
    public Map<String, Object> fetchStrategySecurityOriginal(long id) {
        Map<String, Object> item = Maps.newHashMap();
        List<Map<String, Object>> securityOnlyList = qicDbUtil.queryMapList(SqlLoader.getSqlById("fetchStrategySecurityOnly"), id);
        List<Map<String, Object>> plateOnlyList = qicDbUtil.queryMapList(SqlLoader.getSqlById("fetchStrategyPlateOnly"), id);
        for (Map<String, Object> map : plateOnlyList) {
            String plateId = String.valueOf(map.get("name"));
            map.put("name", StrategySecurityOriginalDto.PLATEID_PLATE_CN_NAME_MAPPING.get(map.get("name")));
            if (map.get("name") == null) {
                map.put("name", plateId);
            }
        }
        item.put("strategyId", id);
        item.put("plates", plateOnlyList);
        item.put("securities", securityOnlyList);

        return item;
    }

    @Override
    public List<Map<String, Object>> findStrategyByStatus(int status, long uid) {
        String sql = SqlLoader.getSqlById("fetchStrategyWithState");
        List<Map<String, Object>> mapList = qicDbUtil.queryMapList(sql, uid, status);
        return mapList;
    }

    /**
     * 处理 策略对比表格展示
     * User: liuhongjiang
     * Date: 12-11-9
     * Time: 下午4:54
     */
    public List<StrategyContrastDto> strategyContrast(String idArray[]) {
        if (idArray == null || idArray.length == 0) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        //策略ID
        List<StrategyContrastDto> strategyContrastlist = new ArrayList<StrategyContrastDto>();
        //根据策略ID查 qic策略的相关信息
        String StrategyContrastForQICSql = SqlLoader.getSqlById("StrategyContrastForQIC");
        //根据策略ID查 qia策略的相关信息
        String StrategyContrastForQIASql = SqlLoader.getSqlById("StrategyContrastForQIA");
        //查询策略(qic_db.strategy_baseinfo)
        String StrategyContrastInfoSql = SqlLoader.getSqlById("StrategyContrastInfo");
        //查询该策略当前的订阅人数
        String getCurrentOrderNumSql = SqlLoader.getSqlById("getCurrentOrderNum");
        //为页面策略名称添加颜色样式
        String[] colors = new String[]{"#059af8", "#a67ec7", "#2bb431", "#fb3118", "#ef9c00"};

        for (int i = 0; i < idArray.length; i++) {
            StrategyContrastDto sci = qicDbUtil.querySingleBean(StrategyContrastInfoSql, StrategyContrastDto.class, idArray[i]);
            if (sci == null) {
                sci = new StrategyContrastDto();
            }
            sci.currentOrder = qicDbUtil.queryCount(getCurrentOrderNumSql, idArray[i]);
            if (sci.enginetypeId == StrategyBaseinfo.QICORE_ENGINEE_ID) {
                map = qicDbUtil.querySingleMap(StrategyContrastForQICSql, idArray[i]);
            } else if (sci.enginetypeId == StrategyBaseinfo.QIA_ENGINEE_ID) {
                map = qicDbUtil.querySingleMap(StrategyContrastForQIASql, idArray[i]);
            }
            sci.color = colors[i];
            if (map != null && map.size() != 0) {
                sci.profitRatio = Double.parseDouble(map.get("profitratio").toString());
                sci.yieldOfYear = Double.parseDouble(map.get("yieldofyear").toString());
                sci.sharpeIndex = Double.parseDouble(map.get("sharpeindex").toString());
            }
            //将组合的数据放入list
            strategyContrastlist.add(sci);
        }
        return strategyContrastlist;
    }


    /**
     * 策略对比 图形展示
     * 这个方法的主要作用是组装数据
     * 需要的数据：
     * 1.格式化近三个月的起始时间 maxDate minDate
     * 2.列出最近三个月收益率的最大值 最小值  取绝对值最大的modYield
     * 3.把数据组装成[{name=“策略名1” date:[ Date.UTC(2010, 0, 1), 9.05],...},
     * {name=“策略名2” date:[ Date.UTC(2010, 1, 1), 9.05],...}]类型
     *
     * @param idArray 策略ID数组
     * @retrun arr_strategys[]数组  放入的数据依次为： 最大日期（对应X轴最大值），
     * 最小日期（对应X轴最小值），
     * 最大收益率（对应Y轴最大值），
     * 按highcharts格式组装好的一个或多个策略的单天收益率和时间，
     * Y轴最小间距，X轴最小间距
     */

    public String[] strategyContrastForPictrue(String idArray[]) {
        String[] arr_strategys = new String[5];
        String sname = "--";//默认策略名
        String strategys = "";//组装多个策略数据
        Date minDate = null;
        Date maxDate = new Date();//取当前日期作为最大时间
        float maxYield = 0f;
        String pictrue_sql = SqlLoader.getSqlById("strategy_contrast_for_pictrue_sql");
        String getName_sql = SqlLoader.getSqlById("StrategyContrastInfo");

        if (idArray == null) {
            return null;
        }

        for (int i = 0; i < idArray.length; i++) {  //根据策略ID查询 单位时间内的收益率
            String strategy = "";
            //取当前策略数据库最大（最近）时间
            //maxDate  = getlatelyDate(idArray[i]);
            minDate = DrawPictrueUtil.getTime(maxDate, 5, -89); //取89天以前的时间
            //取最近三个月的收益率
            List<StrategyDailyYieldDto> yield_list = qicDbUtil.queryBeanList(pictrue_sql, StrategyDailyYieldDto.class, idArray[i], minDate);
            StrategyContrastDto strategyContrast = qicDbUtil.querySingleBean(getName_sql, StrategyContrastDto.class, idArray[i]);
            if (strategyContrast != null) {
                sname = strategyContrast.name;
            }
            Iterator<StrategyDailyYieldDto> it = yield_list.iterator();
            while (it.hasNext()) {
                StrategyDailyYieldDto sdy = it.next();
                String s = DrawPictrueUtil.combinationData(sdy);
                strategy += (s + "\n");
            }
            int flag = strategy.lastIndexOf(","); //剔除字符串中最后一个“,”
            if (flag == -1) {//数据库没有数据 给一个初始化值 页面图形就能正确展示
                strategy = "{name:'" + sname + "', data:[[Date.UTC(2012,01,01),1.00]]}";
            } else {
                strategy = "{name:'" + sname + "', data:[" + strategy.substring(0, flag) + "]}";//一个策略数据拼接完成
            }
            strategys += (strategy + ",\n");
            float Yield = DrawPictrueUtil.getmaxModYield(idArray[i], minDate);//取当前策略绝对值最大的yield
            //取所有对比的策略中最大的  yield
            if (maxYield < Yield) {
                maxYield = Yield;
            }

        }
        int flag1 = strategys.lastIndexOf(",");//剔除字符串中最后一个“,”
        if (flag1 == -1) {
            return arr_strategys;
        }
        strategys = "[" + strategys.substring(0, flag1) + "]";//参与策略对比的所有策略数据拼接完成
        //取最大时间
        String str_maxDate = "";
        if (maxDate != null) {
            str_maxDate = DrawPictrueUtil.getFormatMaxorMinDate(maxDate);
        }
        //取最小时间
        String str_minDate = "";
        if (minDate != null) {
            str_minDate = DrawPictrueUtil.getFormatMaxorMinDate(minDate);

        }
        String str_maxYield = String.format("%.2f", maxYield * 100);
        //（最大收益率/3）来表示图中Y轴坐标间隔 保留小数点后两位 无论小数点后第三位是否大于5都入位
        String str_averYield = new BigDecimal((maxYield / 3) * 100).setScale(2, BigDecimal.ROUND_CEILING).toString();
        //最大时间，最小时间，最大收益率，组装的series，收益率均值
        arr_strategys = new String[]{str_maxDate, str_minDate, str_maxYield, strategys, str_averYield};
        if (Logger.isDebugEnabled()) {
            Logger.debug("strategys============" + strategys);
            Logger.debug("maxYield============" + maxYield);
            Logger.debug("str_maxDate============" + str_maxDate);
            Logger.debug("str_minDate============" + str_minDate);
            Logger.debug("str_averYield=====================================" + str_averYield);
        }
        return arr_strategys;
    }

    //策略基本信息
    public StrategyBaseDto getStrategyDetail(long stid) {
        String sql = SqlLoader.getSqlById("getStrategyBaseInfo");
        StrategyBaseDto strategyBaseDto = qicDbUtil.querySingleBean(sql, StrategyBaseDto.class, stid);
        if (strategyBaseDto.discussCount == 0)
            strategyBaseDto.starLevel = 0;
        else
            strategyBaseDto.starLevel = (float) strategyBaseDto.discussTotal / strategyBaseDto.discussCount;
        return strategyBaseDto;
    }

    //策略交易简单信息展示
    public StrategyBaseDto getstratebasicinfo(long stid) {
        StrategyBaseinfo strategy = findStrategyById(stid);
        if (strategy == null) {
            StrategyBaseinfo strategy1 = new StrategyBaseinfo();
            strategy = strategy1;
        }
        StrategyBaseDto strategyBaseDto = new StrategyBaseDto();
        strategyBaseDto.sname = strategy.name;
        strategyBaseDto.stype = strategy.tradeType;
        strategyBaseDto.upTime = strategy.upTime;
        strategyBaseDto.collectCount = strategy.collectCount;
        if (strategy.discussCount == 0)
            strategyBaseDto.starLevel = 0;
        else
            strategyBaseDto.starLevel = (float) strategy.discussTotal / strategy.discussCount;
        return strategyBaseDto;
    }

    //策略委托信号
    public List<AuthorizeRecordDto> getorder_signal(long stid) {
        String strategy_baseinfo_sql = SqlLoader.getSqlById("strategy_baseinfo");
        StrategyBaseinfo strategyBaseinfo = qicDbUtil.querySingleBean(strategy_baseinfo_sql, StrategyBaseinfo.class, stid);
        String sql = SqlLoader.getSqlById("order_signal");
        List<AuthorizeRecordDto> ordersignallist = qicoreDbUtil.queryBeanList(sql, AuthorizeRecordDto.class, strategyBaseinfo.stUuid, strategyBaseinfo.upTime, CommonUtils.getFormatDate("yyyy-MM-dd", new Date()));
        if (ordersignallist != null) {
            for (AuthorizeRecordDto ar : ordersignallist) {
                ar.name = strategyBaseinfo.name;
                ar.trade_variety = strategyBaseinfo.tradeVariety;
            }
        } else {
            List<AuthorizeRecordDto> authorizeRecordDtoList1 = new ArrayList<AuthorizeRecordDto>();
            ordersignallist = authorizeRecordDtoList1;
        }
        return ordersignallist;
    }


    /**
     * 判断是否订阅
     *
     * @param uid
     * @param stid
     * @return
     */
    public boolean isorder(Long uid, Long stid) {
        String isorder_sql = SqlLoader.getSqlById("isorder_sql");
        StrategyOrderDto strategy = qicDbUtil.querySingleBean(isorder_sql, StrategyOrderDto.class, uid, stid);
        if (strategy != null) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * @param myselect 分类搜索
     * @param content  关键字
     * @param pageNo   当前页数
     * @return _1 为结果集, _2为 分页page信息,
     */
    public F.T2<List<StrategyBaseDto>, Page> favoriteStrategyList(int myselect, String content, int pageNo, Long uid) {
        String sqlList = SqlLoader.getSqlById("strategyUserListSql");
        sqlList = sqlList.replaceAll("#user_type_table#", "user_strategy_collect");
        sqlList = sqlList.replace("#fieldList#", "1");

        List<StrategyBaseDto> sbdList = null;
        String condition = "";

        //content 关键字查询内容
        if (StringUtils.isNotBlank(content) && !"null".equals(content)) {
            condition = "  and  (sname like ? OR provider like ? )";
        }
        if (myselect == 0) {//分类查询,默认按收益率排行 排序
            if (content != null && content != "") {
                sqlList += condition;
            }
            sqlList += " ORDER BY yield DESC ";

        } else if (myselect == 1) {//myselect = 1按收益率排行 排序
            if (content != null && content != "") {
                sqlList += condition;
            }
            sqlList += " ORDER BY yield DESC";

        } else if (myselect == 2) {//myselect =2 按人气排行 排序
            if (content != null && content != "") {
                sqlList += condition;
            }
            sqlList += " ORDER BY collectCount DESC";

        } else {//按最新 排序
            if (content != null && content != "") {
                sqlList += condition;
            }
            sqlList += " ORDER BY upTime DESC";

        }
        Long total;
        StringBuilder coutSql = new StringBuilder("select count(*) from (\n" + sqlList + "\n) distTable ");
        if (StringUtils.isNotBlank(content) && !"null".equals(content)) {
            total = qicDbUtil.queryCount(coutSql.toString(), uid, "%" + content + "%", "%" + content + "%");
        } else {
            total = qicDbUtil.queryCount(coutSql.toString(), uid);
        }
        Page page = new Page(total.intValue(), pageNo);
        if (total > 0) {
            sqlList += " limit " + page.beginIndex + "," + page.pageSize + "\n";
            if (StringUtils.isNotBlank(content)) {
                sbdList = qicDbUtil.queryBeanList(sqlList, StrategyBaseDto.class, uid, "%" + content + "%", "%" + content + "%");
            } else {
                sbdList = qicDbUtil.queryBeanList(sqlList, StrategyBaseDto.class, uid);
            }
        }
        if (sbdList == null) {
            sbdList = new ArrayList<StrategyBaseDto>();
        }
        return F.T2(sbdList, page);

    }


    /**
     * 用戶收藏列表 高级搜索
     *
     * @param cnd
     * @param pageNo 当前页
     * @return _1 为结果集, _2为总条数, _3 总共页数
     */
    public F.T2<List<StrategyBaseDto>, Page> favoriteStrategyAdvanceSearch(StrategySearchCnd cnd, int myselect, int pageNo, Long uid) {
        String sql = SqlLoader.getSqlById("strategyUserListSql");
        sql = sql.replaceAll("#user_type_table#", "user_strategy_collect");
        sql = sql.replace("#fieldList#", "1");

        StringBuilder listSelectSql = new StringBuilder("select * from (\n" + sql + "\n) distTable where 1=1 ");
        StringBuilder coutSql = new StringBuilder("select count(*) from (\n" + sql + "\n) distTable where 1=1 ");
        StringBuilder where = new StringBuilder();
        if (cnd.tradeType != null && cnd.tradeType != CommonUtils.SELECT_ALL_OPTION_VALUE) {
            where.append(" and stype = " + cnd.tradeType.intValue());
        }

        if (cnd.tradeVariety != null && cnd.tradeVariety != CommonUtils.SELECT_ALL_OPTION_VALUE) {
            where.append(" and tradeVariety = " + cnd.tradeVariety.intValue()).append('\n');
        }

        if (cnd.yieldDown != null) {
            where.append(" and yield >= " + cnd.yieldDown / 100).append('\n');
        }

        if (cnd.yieldUp != null) {
            where.append(" and yield <= " + cnd.yieldUp / 100).append('\n');
        }

        if (cnd.profitRatioDown != null) {
            where.append(" and profitRatio >= " + cnd.profitRatioDown).append('\n');
        }

        if (cnd.profitRatioUp != null) {
            where.append(" and profitRatio <= " + cnd.profitRatioUp).append('\n');
        }

        if (cnd.starDown != null) {
            where.append(" and starLevel >= " + cnd.starDown).append('\n');
        }

        if (cnd.starUp != null) {
            where.append(" and starLevel <= " + cnd.starUp).append('\n');
        }

        if (myselect == 3) {
            where.append(" ORDER BY upTime DESC ");
        } else if (myselect == 2) {
            where.append(" ORDER BY collectCount DESC ");
        } else {
            where.append(" ORDER BY yield DESC ");
        }

        if (Logger.isDebugEnabled()) {
            Logger.debug("sql where ==" + where.toString());
        }

        listSelectSql.append(where);
        coutSql.append(where);

        Long total = qicDbUtil.queryWithHandler(coutSql.toString(), new ScalarHandler<Long>(), uid);

        Page page = new Page(total.intValue(), pageNo);
        listSelectSql.append("\n limit " + page.beginIndex + "," + page.pageSize + "\n");
        List<StrategyBaseDto> dtoList = qicDbUtil.queryBeanList(listSelectSql.toString(), StrategyBaseDto.class, uid);

        return F.T2(dtoList, page);
    }

    /**
     * 订阅策略列表
     *
     * @param myselect 分类
     * @param content  关键字
     * @param pageNo   页码
     * @param uid      用户ID
     * @return
     */
    public F.T2<List<StrategyBaseDto>, Page> subscriptionStrategyList(int myselect, String content, int pageNo, Long uid) {
        String sql = SqlLoader.getSqlById("strategyUserListSql");
        sql = sql.replaceAll("#user_type_table#", "user_strategy_order");
        sql = sql.replace("#fieldList#", "1");
        StringBuilder querySql = new StringBuilder(sql);
        List<Object> queryList = new ArrayList<Object>();
        queryList.add(uid);
        //关键字过滤
        if (StringUtils.isNotBlank(content)) {
            querySql.append(" AND (sname like ? OR provider like ? )\n");
            queryList.add("%" + content + "%");
            queryList.add("%" + content + "%");
        }

        //排序
        if (myselect == 2) {
            querySql.append(" ORDER BY collectCount DESC\n");
        } else if (myselect == 3) {
            querySql.append(" ORDER BY upTime DESC\n");
        } else {
            querySql.append(" ORDER BY yield DESC\n");//默认排序
        }

        StringBuilder countSql = new StringBuilder("select count(*) from (\n" + querySql + "\n) distTable ");
        //获得总记录
        Long total = qicDbUtil.queryCount(countSql.toString(), queryList.toArray());
        //实例分页类
        Page page = new Page(total.intValue(), pageNo);
        querySql.append(" limit ?,?\n");
        queryList.add(page.beginIndex);
        queryList.add(page.pageSize);
        List<StrategyBaseDto> sbList = qicDbUtil.queryBeanList(querySql.toString(), StrategyBaseDto.class, queryList.toArray());

        return F.T2(sbList, page);
    }

    /**
     * 订阅策略列表 高级搜索
     *
     * @param cnd    搜索DTO
     * @param pageNo 当前页
     * @return _1 为结果集, _2为总条数, _3 总共页数
     */
    public F.T2<List<StrategyBaseDto>, Page> subscriptionStrategyAdvanceSearch(StrategySearchCnd cnd, int myselect, int pageNo, Long uid) {
        String sql = SqlLoader.getSqlById("strategyUserListSql");
        sql = sql.replaceAll("#user_type_table#", "user_strategy_order");
        sql = sql.replace("#fieldList#", "1");

        StringBuilder listSelectSql = new StringBuilder("select * from (\n" + sql + "\n) distTable where 1=1 ");
        StringBuilder coutSql = new StringBuilder("select count(*) from (\n" + sql + "\n) distTable where 1=1 ");
        StringBuilder where = new StringBuilder();
        if (cnd.tradeType != null && cnd.tradeType != CommonUtils.SELECT_ALL_OPTION_VALUE) {
            where.append(" and stype = " + cnd.tradeType.intValue());
        }

        if (cnd.tradeVariety != null && cnd.tradeVariety != CommonUtils.SELECT_ALL_OPTION_VALUE) {
            where.append(" and tradeVariety = " + cnd.tradeVariety.intValue()).append('\n');
        }

        if (cnd.yieldDown != null) {
            where.append(" and yield >= " + cnd.yieldDown / 100).append('\n');
        }

        if (cnd.yieldUp != null) {
            where.append(" and yield <= " + cnd.yieldUp / 100).append('\n');
        }

        if (cnd.profitRatioDown != null) {
            where.append(" and profitRatio >= " + cnd.profitRatioDown).append('\n');
        }

        if (cnd.profitRatioUp != null) {
            where.append(" and profitRatio <= " + cnd.profitRatioUp).append('\n');
        }

        if (cnd.starDown != null) {
            where.append(" and starLevel >= " + cnd.starDown).append('\n');
        }

        if (cnd.starUp != null) {
            where.append(" and starLevel <= " + cnd.starUp).append('\n');
        }

        if (myselect == 3) {
            where.append(" ORDER BY upTime DESC ");
        } else if (myselect == 2) {
            where.append(" ORDER BY collectCount DESC ");
        } else {
            where.append(" ORDER BY yield DESC ");
        }

        if (Logger.isDebugEnabled()) {
            Logger.debug("sql where ==" + where.toString());
        }

        listSelectSql.append(where);
        coutSql.append(where);

        Long total = qicDbUtil.queryWithHandler(coutSql.toString(), new ScalarHandler<Long>(), uid);

        Page page = new Page(total.intValue(), pageNo);
        listSelectSql.append("\n limit " + page.beginIndex + "," + page.pageSize + "\n");
        List<StrategyBaseDto> dtoList = qicDbUtil.queryBeanList(listSelectSql.toString(), StrategyBaseDto.class, uid);
        return F.T2(dtoList, page);
    }

    public StrategyOrderDto findUserStrategyOrder(long uid, long stid) {
        String isorder_sql = SqlLoader.getSqlById("findUserStrategyOrder");
        StrategyOrderDto strategy = qicDbUtil.querySingleBean(isorder_sql, StrategyOrderDto.class, uid, stid);
        return strategy;
    }

    /**
     * 查询该策略所有的评论
     *
     * @param id
     * @return
     */
    public List<UserStrategyDiscuss> userDiscussList(Long id) {
        Map<String, Long> map = Maps.newHashMap();
        map.put("strategyId", id);
        List<UserStrategyDiscuss> usdList = qicDbUtil.queryBeanListWithNameParam("findStrategyCommentList", UserStrategyDiscuss.class, map);
        return usdList;
    }

    /**
     * 查询用户的已上架策略
     *
     * @param uid 用户ID
     * @return
     */
    public List<Map<String, Object>> fetchStrategyByUserId(long uid) {
        String sql = SqlLoader.getSqlById("fetchStrategyByUserId"); //根据用户ID查询策略名称、交易品种
        String accountTemplateSql = SqlLoader.getSqlById("fetchAccountTemplateByStrategyId"); //根据策略ID查询交易帐号序号、帐号类型
        List<Map<String, Object>> list = qicDbUtil.queryMapList(sql, uid);
        if (list.size() > 0) {
            for (Map<String, Object> strategyMap : list) {
                long strategyId = (Long) strategyMap.get("id");
                List<Map<String, Object>> accountTemplateList = qicDbUtil.queryMapList(accountTemplateSql, strategyId);

                strategyMap.put("accountTemplate", accountTemplateList);

            }
        }
        return list;
    }

    @Override
    public long addStrategyAccountTemplate(StrategyAccountTemplateDto strategyAccountTemplateDto) {
        return qicDbUtil.insertWithNameParam("addStrategyAccountTemplate", strategyAccountTemplateDto);
    }

    @Override
    public long addStrategySecurityOrigunalInfo(StrategySecurityOriginalDto securityOriginalDto) {
        return qicDbUtil.insertWithNameParam("addStrategySecurityOrigunalInfo", securityOriginalDto);
    }

    @Override
    public long fetchStartegyNumByServiceId(int serviceId) {
        return qicDbUtil.queryCount(SqlLoader.getSqlById("fetchCountStrategyByServiceId"), serviceId);
    }
}
