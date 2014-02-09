package bussiness.stock.impl;

import bussiness.common.impl.BaseService;
import bussiness.stock.IStockPoolService;
import dbutils.SqlLoader;
import models.iquantCommon.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import play.libs.F;
import util.CommonUtils;
import util.Page;

import java.math.BigDecimal;
import java.util.*;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-7-3
 * Time: 下午5:34
 * 功能描述:
 */
public class StockPoolService extends BaseService implements IStockPoolService {

    /**
     * @param spp    参数对象类
     * @param pageNo 当前页
     * @return_1 为结果集, _2为分页对象
     */
    public F.T2<List<StockpoolDto>, Page> stockPoolyList(StockPoolsPar spp, int pageNo, Long uid) {

        String sqlList = SqlLoader.getSqlById("stockpoolFavoriteSql");
        sqlList += " WHERE uspc.`uid`=" + uid;
        List<StockpoolDto> sbdList = null;
        String condition = "";
        Long total;
        StringBuilder coutSql = new StringBuilder();
        //content 关键字查询内容
        if (StringUtils.isNotBlank(spp.content) && StringUtils.isNotBlank(spp.strategyName)) {
            if (spp.strategyName.equals("I05")) {
                condition = " AND  (a.`StockPoolName` like ? OR source like ?) and ss.`StrategyCode`in (?,?,?)";
            } else {
                condition = " AND  (a.`StockPoolName` like ? OR source like ?) and ss.`StrategyCode`in (?)";
            }
            sqlList += condition;
            coutSql.append("select count(*) from (\n" + sqlList + "\n) distTable where 1=1 \n");
            if (spp.strategyName.equals("I05")) {
                total = qicDbUtil.queryCount(coutSql.toString(), "%" + spp.content + "%", "%" + spp.content + "%", spp.strategyName, "I0501", "I0502");
            } else {
                total = qicDbUtil.queryCount(coutSql.toString(), "%" + spp.content + "%", "%" + spp.content + "%", spp.strategyName);
            }
        } else if (StringUtils.isNotBlank(spp.content) && !StringUtils.isNotBlank(spp.strategyName)) {
            condition = " AND  a.`StockPoolName` like ? OR source like ? ";
            sqlList += condition;
            coutSql.append("select count(*) from (\n" + sqlList + "\n) distTable where 1=1 \n");
            total = qicDbUtil.queryCount(coutSql.toString(), "%" + spp.content + "%", "%" + spp.content + "%");
        } else if (!StringUtils.isNotBlank(spp.content) && StringUtils.isNotBlank(spp.strategyName)) {
            if (spp.strategyName.equals("I05")) {
                condition = " AND  ss.`StrategyCode`  in(?,?,?) ";
            } else {
                condition = " AND  ss.`StrategyCode`  in(?) ";
            }
            sqlList += condition;
            coutSql.append("select count(*) from (\n" + sqlList + "\n) distTable where 1=1 \n");
            if (spp.strategyName.equals("I05")) {
                total = qicDbUtil.queryCount(coutSql.toString(), spp.strategyName, "I0501", "I0502");
            } else {
                total = qicDbUtil.queryCount(coutSql.toString(), spp.strategyName);
            }
        } else {
            coutSql.append("select count(*) from (\n" + sqlList + "\n) distTable where 1=1 \n");
            total = qicDbUtil.queryCount(coutSql.toString());
        }

        if (StringUtils.isNotBlank(spp.orderSort)) {
            if (spp.flag == 0)
                sqlList += " ORDER BY " + spp.orderSort + " asc ";
            else
                sqlList += " ORDER BY " + spp.orderSort + " desc ";

        }

        Page page = new Page(total.intValue(), pageNo);
        sqlList += " limit " + page.beginIndex + "," + page.pageSize + "\n";


        if (StringUtils.isNotBlank(spp.content) && StringUtils.isNotBlank(spp.strategyName)) {
            if (spp.strategyName.equals("I05")) {
                sbdList = qicDbUtil.queryBeanList(sqlList, StockpoolDto.class, "%" + spp.content + "%", "%" + spp.content + "%", spp.strategyName, "I0501", "I0502");
            } else {
                sbdList = qicDbUtil.queryBeanList(sqlList, StockpoolDto.class, "%" + spp.content + "%", "%" + spp.content + "%", spp.strategyName);
            }
        } else if (StringUtils.isNotBlank(spp.content) && !StringUtils.isNotBlank(spp.strategyName)) {
            sbdList = qicDbUtil.queryBeanList(sqlList, StockpoolDto.class, "%" + spp.content + "%", "%" + spp.content + "%");
        } else if (!StringUtils.isNotBlank(spp.content) && StringUtils.isNotBlank(spp.strategyName)) {
            if (spp.strategyName.equals("I05")) {
                sbdList = qicDbUtil.queryBeanList(sqlList, StockpoolDto.class, spp.strategyName, "I0501", "I0502");
            } else {
                sbdList = qicDbUtil.queryBeanList(sqlList, StockpoolDto.class, spp.strategyName);
            }
        } else {
            sbdList = qicDbUtil.queryBeanList(sqlList, StockpoolDto.class);
        }

        return F.T2(sbdList, page);

    }

    /**
     * 高级搜索
     *
     * @param cnd
     * @param pageNo 当前页
     * @return _1 为结果集, _2 page分页对象
     */
    public F.T2<List<StockpoolDto>, Page> advanceSearch(StockPoolSearchCnd cnd, int pageNo, Long uid) {
        String sql = SqlLoader.getSqlById("stockpoolFavoriteSql");
        StringBuilder listSelectSql = new StringBuilder("select * from (\n" + sql + "\n) distTable where 1=1 and distTable.uid =" + uid + " \n");
        StringBuilder coutSql = new StringBuilder("select count(*) from (\n" + sql + "\n) distTable where 1=1 and distTable.uid =" + uid + " \n");
        StringBuilder where = new StringBuilder();
        if (cnd.recommendOrgs != null && cnd.recommendOrgs.length > 0) { //机构代码
            StringBuilder orgwhere = new StringBuilder();
            orgwhere.append(" and orgId in (");
            boolean hasOrg = false;
            for (String org : cnd.recommendOrgs) {
                String[] item = org.split("\\|\\|"); //多个代码, 用 ||分开
                for (String s : item) {
                    if (StringUtils.isNotBlank(s)) {
                        orgwhere.append(s.trim() + ",");
                        hasOrg = true;
                    }

                }
            }
            if (hasOrg) {
                orgwhere.deleteCharAt(orgwhere.length() - 1);
            }
            orgwhere.append(") \n");

            if (hasOrg) {
                where.append(orgwhere.toString());
            }
        }

        if (cnd.reportUpdatePeriod != null && cnd.reportUpdatePeriod != CommonUtils.SELECT_ALL_OPTION_VALUE) {
            Date curDate = new Date();
            Date startDate;
            switch (cnd.reportUpdatePeriod.intValue()) {
                case 1:
                    startDate = DateUtils.addDays(curDate, -1);
                    break;

                case 2:
                    startDate = DateUtils.addDays(curDate, -7);
                    break;

                case 3:
                    startDate = DateUtils.addMonths(curDate, -1);
                    break;

                case 4:
                    startDate = DateUtils.addMonths(curDate, -3);
                    break;

                case 5:
                    startDate = DateUtils.addMonths(curDate, -6);
                    break;

                case 6:
                    startDate = DateUtils.addYears(curDate, -1);
                    break;
                default:
                    startDate = DateUtils.addDays(curDate, -1); //默认为1天前
                    break;
            }
            where.append(" and updateDate >= '" + DateFormatUtils.format(startDate, "yyyy-MM-dd") + "'").append('\n');
        }

        if (cnd.starDown != null) {
            where.append(" and starLevel >= " + cnd.starDown).append('\n');
        }

        if (cnd.starUp != null) {
            where.append(" and starLevel <= " + cnd.starUp).append('\n');
        }

        if (cnd.yearYieldDown != null) {
            where.append(" and annualizedYield >= " + cnd.yearYieldDown).append('\n');
        }

        if (cnd.yearYieldUp != null) {
            where.append(" and annualizedYield <= " + cnd.yearYieldUp).append('\n');
        }

        if (cnd.sharpRateDown != null) {
            where.append(" and yearJensenRatio >= " + cnd.sharpRateDown).append('\n');
        }

        if (cnd.sharpRateUp != null) {
            where.append(" and yearJensenRatio <= " + cnd.sharpRateUp).append('\n');
        }

        listSelectSql.append(where);
        coutSql.append(where);

        Long total = qicDbUtil.queryCount(coutSql.toString());

        Page page = new Page(total.intValue(), pageNo);
        listSelectSql.append("\n limit " + page.beginIndex + "," + page.pageSize + "\n");
        List<StockpoolDto> dtoList = qicDbUtil.queryBeanList(listSelectSql.toString(), StockpoolDto.class);

        return F.T2(dtoList, page);
    }
    /**
     * 得到股票池组合列表信息
     *
     * @param stockpoolcode
     * @return
     */
    public List<StockPoolCombineInfoDto> queryCombineInfo(String stockpoolcode) {
        String sql = SqlLoader.getSqlById("stockPoolList1");
        List<Map<String, Object>> inlist = qicDbUtil.queryMapList(sql, stockpoolcode); //股票池所有的股票list
        String sql2 = SqlLoader.getSqlById("stockPoolList2");
        List<Map<String, Object>> outlist = qicDbUtil.queryMapList(sql2, stockpoolcode); //选出最后一次调整状态是调出的list
        for (int i = 0; i < outlist.size(); i++) {
            String a = (String) outlist.get(i).get("scode");
            for (int j = 0; j < inlist.size(); j++) {
                String b = (String) inlist.get(j).get("scode");
                if (a.equals(b)) {
                    inlist.remove(j);
                }
            }
        }
        List<StockPoolCombineInfoDto> stockPoolCombineInfoDtoList = new ArrayList<StockPoolCombineInfoDto>();

        for (int k = 0; k < inlist.size(); k++) {
            StockPoolCombineInfoDto s = new StockPoolCombineInfoDto();
            s.scode = (String) inlist.get(k).get("scode");
            s.exchangeCode = (String) inlist.get(k).get("exchangeCode");
            s.shortName = (String) inlist.get(k).get("shortName");
            stockPoolCombineInfoDtoList.add(s);
        }
        return stockPoolCombineInfoDtoList;
    }

    /**
     * 此方法用于根据spid和uid判断该股票池是否被收藏
     *
     * @param spid
     * @param uid
     * @return
     */
    public boolean iscollect(String spid, Long uid) {
        String sql = "SELECT * FROM qic_db.`user_stock_pool_collect` WHERE uid=? AND spid=?";
        UserStockPoolCollect userStockPoolCollect = qicDbUtil.querySingleBean(sql, UserStockPoolCollect.class, uid, spid);
        if (userStockPoolCollect == null)
            return false;
        else
            return true;
    }
    /**
     * 从股票池列表中查询已收藏的股票池
     *
     * @param ids 股票池id列表
     * @param uid  用户id
     * @return List<Map<String,Object>>
     */
    public Set<Integer> queryStockPoolCollectMap(Long[] ids, Long uid) {
        String sql = "SELECT spid as spid FROM qic_db.`user_stock_pool_collect`  WHERE uid =" + uid + " AND spid in ( ";
        Set<Integer> result = new HashSet<Integer>();
        if (ids != null && ids.length > 0) {
            for (long id  : ids) {
                sql += id + ",";
            }
            sql = sql.substring(0, sql.length() - 1);
            sql += ")";
            List<Map<String, Object>> stockmapList = qicDbUtil.queryMapList(sql);

            for (Map<String, Object> map : stockmapList) {
                result.add(Integer.parseInt(map.get("spid").toString()));
            }
        }
        return result;
    }

    /**
     * User: 刘建力(liujianli@gtadata.com))
     * Date: 13-7-3
     * Time: 下午5:50
     * 功能描述: 从show中迁移过来 股票池基本信息
     */
    public StockPoolBasicInfoDto strategyContrast(String stockPoolCode) {
        //根据股票池编码 查询组合股票数，更新频率，组合股票池，组合收益
        String stock_pool_basic_info_sql = SqlLoader.getSqlById("stock_pool_basic_info");

        //根据股票池编码 最近一期的附件下载地址，研报摘要，组合来源
        String stock_pool_download_summary_sql = SqlLoader.getSqlById("stock_pool_download_summary");

        //根据spid 查询股票池组合评级
        String stock_pool_starNum_sql = SqlLoader.getSqlById("stock_pool_starNum");

        //因为股票池基本信息涉及的字段较多且关联复杂，这里分三次查询
        Map<String, Object> basicInfoMap = qicDbUtil.querySingleMap(stock_pool_basic_info_sql, stockPoolCode);
        Map<String, Object> downloadSummaryMap = qicDbUtil.querySingleMap(stock_pool_download_summary_sql, stockPoolCode);
        Map<String, Object> starNumMap = qicDbUtil.querySingleMap(stock_pool_starNum_sql, stockPoolCode);

        StockPoolBasicInfoDto sbi = new StockPoolBasicInfoDto();
        if (basicInfoMap != null && basicInfoMap.size() != 0) {
            sbi.stockPoolName = (String) basicInfoMap.get("StockPoolName");
            sbi.stockPoolCode = (Long) basicInfoMap.get("stockPoolCode") == null ? 0l : (Long) basicInfoMap.get("stockPoolCode");
            sbi.stockNum = (Long) basicInfoMap.get("stockNum") == null ? 0l : (Long) basicInfoMap.get("stockNum");
            sbi.updateFrequency = (String) basicInfoMap.get("updateFrequency") == null ? "" : (String) basicInfoMap.get("updateFrequency");
            sbi.strategy = (String) basicInfoMap.get("strategy") == null ? "" : (String) basicInfoMap.get("strategy");
            sbi.annualizedYield = (BigDecimal) basicInfoMap.get("AnnualizedYield");
        }

        if (downloadSummaryMap != null && downloadSummaryMap.size() != 0) {
            sbi.filestoragePath = (String) downloadSummaryMap.get("filestoragePath") == null ? "" : (String) downloadSummaryMap.get("filestoragePath");
            sbi.summary = (String) downloadSummaryMap.get("summary") == null ? "" : (String) downloadSummaryMap.get("summary");
            sbi.institutionName = (String) downloadSummaryMap.get("institutionName") == null ? "" : (String) downloadSummaryMap.get("institutionName");
        }

        if (starNumMap != null && starNumMap.size() != 0) {
            sbi.starNum = (BigDecimal) starNumMap.get("starNum");
        }
        return sbi;
    }

    /**
     * @param spp    参数类对象
     * @param pageNo 当前页
     * @return_1 为结果集, _2为分页对象
     */
    public  F.T2<List<StockpoolDto>, Page> stockPoolyList(StockPoolsPar spp, int pageNo) {

        String sqlList = SqlLoader.getSqlById("stockpoolSql");
        List<StockpoolDto> sbdList = null;
        List<StockpoolDto> listSize = null;
        String condition = "";
        Long total;
        StringBuilder coutSql = new StringBuilder();
        //content 关键字查询内容
        if (StringUtils.isNotBlank(spp.content) && StringUtils.isNotBlank(spp.strategyName)) {
            if(spp.strategyName.equals("I05")){
                condition = " WHERE  (a.`StockPoolName` like ? OR source like ?) and ss.`StrategyCode`in (?,?,?)";
            }else{
                condition = " WHERE  (a.`StockPoolName` like ? OR source like ?) and ss.`StrategyCode`in (?)";
            }
            sqlList += condition;
            //sqlList += " group by id  ";
            coutSql.append("select count(*) from (\n" + sqlList + "\n) distTable where 1=1 \n");
            if(spp.strategyName.equals("I05")){
                total = qicDbUtil.queryCount(coutSql.toString(), "%" + spp.content + "%", "%" + spp.content + "%", spp.strategyName, "I0501", "I0502");
            }else{
                total = qicDbUtil.queryCount(coutSql.toString(), "%" + spp.content + "%", "%" + spp.content + "%", spp.strategyName);
            }
        } else if (StringUtils.isNotBlank(spp.content) && !StringUtils.isNotBlank(spp.strategyName)) {
            condition = " WHERE  a.`StockPoolName` like ? OR source like ? ";
            sqlList += condition;
            //sqlList += " group by id  ";
            coutSql.append("select count(*) from (\n" + sqlList + "\n) distTable where 1=1 \n");
            total = qicDbUtil.queryCount(coutSql.toString(), "%" + spp.content + "%", "%" + spp.content + "%");
        } else if (!StringUtils.isNotBlank(spp.content) && StringUtils.isNotBlank(spp.strategyName)) {
            if(spp.strategyName.equals("I05")){
                condition = " WHERE  ss.`StrategyCode`  in(?,?,?) ";
            }else{
                condition = " WHERE  ss.`StrategyCode`  in(?) ";
            }
            sqlList += condition;
            // sqlList += " group by id  ";
            coutSql.append("select count(*) from (\n" + sqlList + "\n) distTable where 1=1 \n");
            if(spp.strategyName.equals("I05")){
                total = qicDbUtil.queryCount(coutSql.toString(), spp.strategyName, "I0501", "I0502");
            }else{
                total = qicDbUtil.queryCount(coutSql.toString(), spp.strategyName);
            }
        } else {
            // sqlList += " group by id  ";
            coutSql.append("select count(*) from (\n" + sqlList + "\n) distTable where 1=1 \n");
            total = qicDbUtil.queryCount(coutSql.toString());
        }

        if (StringUtils.isNotBlank(spp.orderSort)) {
            if (spp.flag == 0)
                sqlList += " ORDER BY " + spp.orderSort + " asc ";
            else
                sqlList += " ORDER BY " + spp.orderSort + " desc ";

        }

        Page page = new Page(total.intValue(), pageNo);
        sqlList += " limit " + page.beginIndex + "," + page.pageSize + "\n";


        if (StringUtils.isNotBlank(spp.content) && StringUtils.isNotBlank(spp.strategyName)) {
            if(spp.strategyName.equals("I05")){
                sbdList = qicDbUtil.queryBeanList(sqlList, StockpoolDto.class, "%" + spp.content + "%", "%" + spp.content + "%", spp.strategyName, "I0501", "I0502");
            }else{
                sbdList = qicDbUtil.queryBeanList(sqlList, StockpoolDto.class, "%" + spp.content + "%", "%" + spp.content + "%", spp.strategyName);
            }
        } else if (StringUtils.isNotBlank(spp.content) && !StringUtils.isNotBlank(spp.strategyName)) {
            sbdList = qicDbUtil.queryBeanList(sqlList, StockpoolDto.class, "%" + spp.content + "%", "%" + spp.content + "%");
        } else if (!StringUtils.isNotBlank(spp.content) && StringUtils.isNotBlank(spp.strategyName)) {
            if(spp.strategyName.equals("I05")){
                sbdList = qicDbUtil.queryBeanList(sqlList, StockpoolDto.class, spp.strategyName, "I0501", "I0502");
            }else{
                sbdList = qicDbUtil.queryBeanList(sqlList, StockpoolDto.class, spp.strategyName);
            }
        } else {
            sbdList = qicDbUtil.queryBeanList(sqlList, StockpoolDto.class);
        }

        return F.T2(sbdList, page);

    }

    /**
     * 热门收索
     * @param spp    参数类对象
     * @param pageNo 当前页
     * @return_1 为结果集, _2为分页对象
     */
    public  F.T2<List<StockpoolDto>, Page> hotList(StockPoolsPar spp, int pageNo) {

        String sqlList = SqlLoader.getSqlById("hotStockpoolSql");
        List<StockpoolDto> sbdList = null;
        String condition = "";

        //content 关键字查询内容
        if (StringUtils.isNotBlank(spp.content)) {
            condition = " WHERE  tab1.poolName like ? OR tab1.source like ? ";
            sqlList += condition;
        }
        if (StringUtils.isNotBlank(spp.orderSort)) {
            if (spp.flag == 0)
                sqlList += " ORDER BY " + spp.orderSort + " asc ";
            else
                sqlList += " ORDER BY " + spp.orderSort + " desc ";
        }
        Page page = new Page(20, pageNo);
        if (StringUtils.isNotBlank(spp.content)) {
            sbdList = qicDbUtil.queryBeanList(sqlList, StockpoolDto.class, "%" + spp.content + "%", "%" + spp.content + "%");
        } else {
            sbdList = qicDbUtil.queryBeanList(sqlList, StockpoolDto.class);
        }
        return F.T2(sbdList, page);
    }
    public  List<StockpoolDto> getStpStarLevelAndHot(String[] stockPoolIds){
        String sql = SqlLoader.getSqlById("getStpStarLevelAndHot");
        sql +=" (";
        for(int i = 0 ; i< stockPoolIds.length;i++){
            sql += ( (i< stockPoolIds.length - 1 ) ?  "?," : "?");
        }
        sql +=")";
        List<StockpoolDto> list = qicDbUtil.queryBeanList(sql, StockpoolDto.class, stockPoolIds);
        return list;
    }

    /**
     * 高级搜索
     *
     * @param cnd
     * @param pageNo 当前页
     * @return _1 为结果集, _2 page分页对象
     */
    public  F.T2<List<StockpoolDto>, Page> advanceSearch(StockPoolSearchCnd cnd, int pageNo) {
        String sql = SqlLoader.getSqlById("stockpoolSql");
        StringBuilder listSelectSql = new StringBuilder("select * from (\n" + sql + "\n) distTable where 1=1 \n");
        StringBuilder coutSql = new StringBuilder("select count(*) from (\n" + sql + "\n) distTable where 1=1 \n");
        StringBuilder where = new StringBuilder();
        if (cnd.recommendOrgs != null && cnd.recommendOrgs.length > 0) { //机构代码
            StringBuilder orgwhere = new StringBuilder();
            orgwhere.append(" and orgId in (");
            boolean hasOrg = false;
            for (String org : cnd.recommendOrgs) {
                String[] item = org.split("\\|\\|"); //多个代码, 用 ||分开
                for (String s : item) {
                    if (StringUtils.isNotBlank(s)) {
                        orgwhere.append(s.trim() + ",");
                        hasOrg = true;
                    }

                }
            }
            if (hasOrg) {
                orgwhere.deleteCharAt(orgwhere.length() - 1);
            }
            orgwhere.append(") \n");

            if (hasOrg) {
                where.append(orgwhere.toString());
            }
        }

        if (cnd.reportUpdatePeriod != null && cnd.reportUpdatePeriod != CommonUtils.SELECT_ALL_OPTION_VALUE) {
            Date curDate = new Date();
            Date startDate;
            switch (cnd.reportUpdatePeriod.intValue()) {
                case 1:
                    startDate = DateUtils.addDays(curDate, -1);
                    break;

                case 2:
                    startDate = DateUtils.addDays(curDate, -7);
                    break;

                case 3:
                    startDate = DateUtils.addMonths(curDate, -1);
                    break;

                case 4:
                    startDate = DateUtils.addMonths(curDate, -3);
                    break;

                case 5:
                    startDate = DateUtils.addMonths(curDate, -6);
                    break;

                case 6:
                    startDate = DateUtils.addYears(curDate, -1);
                    break;
                default:
                    startDate = DateUtils.addDays(curDate, -1); //默认为1天前
                    break;
            }
            where.append(" and updateDate >= '" + DateFormatUtils.format(startDate, "yyyy-MM-dd") + "'").append('\n');
        }

        if (cnd.starDown != null) {
            where.append(" and starLevel >= " + cnd.starDown).append('\n');
        }

        if (cnd.starUp != null) {
            where.append(" and starLevel <= " + cnd.starUp).append('\n');
        }

        if (cnd.yearYieldDown != null) {
            where.append(" and annualizedYield >= " + cnd.yearYieldDown/100).append('\n');
        }

        if (cnd.yearYieldUp != null) {
            where.append(" and annualizedYield <= " + cnd.yearYieldUp/100).append('\n');
        }

        if (cnd.sharpRateDown != null) {
            where.append(" and yearJensenRatio >= " + cnd.sharpRateDown).append('\n');
        }

        if (cnd.sharpRateUp != null) {
            where.append(" and yearJensenRatio <= " + cnd.sharpRateUp).append('\n');
        }

        listSelectSql.append(where);
        coutSql.append(where);

        Long total = qicDbUtil.queryCount(coutSql.toString());

        Page page = new Page(total.intValue(), pageNo);
        listSelectSql.append("\n limit " + page.beginIndex + "," + page.pageSize + "\n");
        List<StockpoolDto> dtoList = qicDbUtil.queryBeanList(listSelectSql.toString(), StockpoolDto.class);

        return F.T2(dtoList, page);
    }
}
