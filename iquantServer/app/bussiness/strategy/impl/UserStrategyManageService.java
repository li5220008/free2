package bussiness.strategy.impl;

import bussiness.common.ILogInfoService;
import bussiness.common.ISystemConfigService;
import bussiness.common.impl.BaseService;
import bussiness.strategy.IBackTestService;
import bussiness.strategy.IStrategyService;
import bussiness.strategy.IUserStrategyManageService;
import bussiness.user.IUserInfoService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dbutils.SqlLoader;
import exceptions.IquantEntityHasBeenUsedException;
import models.iquantCommon.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import play.Logger;
import play.Play;
import play.db.DB;
import play.db.jpa.JPA;
import play.libs.XPath;
import play.modules.guice.InjectSupport;
import util.CommonUtils;
import util.Constants;
import util.MessageBuilder;
import util.SystemLoggerMessage;

import javax.inject.Inject;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: liuhongjiang
 * Date: 13-7-26
 * Time: 下午2:31
 */
@InjectSupport
public class UserStrategyManageService extends BaseService implements IUserStrategyManageService{
    @Inject
    static IStrategyService strategyService;
    @Inject
    static ILogInfoService logInfoService;
    @Inject
    static IUserInfoService userInfoService;
    @Inject
    static ISystemConfigService systemConfigService;
    @Inject
    static IBackTestService backTestService;

    //得到已收藏的策略,放入set中
    public Set<Integer> queryUserCollectSet(List<Long> ids, Long uid) {
        String sql = "SELECT uid AS uid,stid AS stid FROM qic_db.user_strategy_collect WHERE uid=" + uid + " AND stid in (";
        Set<Integer> result = new HashSet<Integer>();
        if (ids != null && ids.size() > 0) {
            for (long item : ids) {
                sql += item + ",";
            }
            sql = sql.substring(0, sql.length() - 1);
            sql += ")";
            Logger.debug(sql);
            List<Map<String, Object>> queryList = qicDbUtil.queryMapList(sql);

            for (Map<String, Object> item : queryList) {
                result.add(Integer.parseInt(item.get("stid").toString()));
            }
        }

        return result;
    }

    //加入收藏
    public void addstrategycollect(long stid, Long uid) throws Exception {
        String sql = "SELECT \n" +
                "  * \n" +
                "FROM\n" +
                "  qic_db.`user_strategy_collect` a \n" +
                "WHERE a.`stid` = ? \n" +
                "  AND a.`uid` = ?";
        UserStrategyCollect userStrategyCollect = qicDbUtil.querySingleBean(sql, UserStrategyCollect.class, stid, uid);
        if (userStrategyCollect == null) {
            userStrategyCollect = new UserStrategyCollect();
            StrategyBaseinfo strategy = strategyService.findStrategyById(stid);
            userStrategyCollect.strategy = strategy;
            if (userStrategyCollect.strategy == null) {
                throw new Exception("不存在的策略.");
            }
            String sql2 = "INSERT INTO qic_db.`user_strategy_collect`(uid,stid) VALUES(?,?)";
            qicDbUtil.update(sql2, uid, stid);
            String sql3 = "UPDATE qic_db.`strategy_baseinfo` a SET a.`collect_count` = a.`collect_count` + 1 WHERE a.`id` = ?";
            qicDbUtil.update(sql3, stid);
        } else {
            throw new Exception("已经收藏策略.");
        }
    }

    //取消收藏
    public void deletestrategycollect(long stid, Long uid) throws Exception {
        //查询策略收藏列表
        String sql = "SELECT \n" +
                "  * \n" +
                "FROM\n" +
                "  qic_db.`user_strategy_collect` a \n" +
                "WHERE a.`stid` = ? \n" +
                "  AND a.`uid` = ?";
        UserStrategyCollect userStrategyCollect = qicDbUtil.querySingleBean(sql, UserStrategyCollect.class, stid, uid);
        if (userStrategyCollect != null) {
            StrategyBaseinfo strategy = strategyService.findStrategyById(stid);
            String sql2 = "DELETE FROM qic_db.`user_strategy_collect` WHERE qic_db.`user_strategy_collect`.`stid`=? AND qic_db.`user_strategy_collect`.`uid`=?";
            qicDbUtil.update(sql2, stid, uid);
            String sql3 = "UPDATE qic_db.`strategy_baseinfo` a SET a.`collect_count` = a.`collect_count` - 1 WHERE a.`id` = ?";
            qicDbUtil.update(sql3, stid);

        } else {
            throw new Exception("已经取消收藏.");
        }
    }

    //根据策略id查询是否被收藏
    public boolean iscollect(long stid, long uid) {
        UserStrategyCollect userStrategyCollect = findUserStrategyCollect(uid, stid);
        if (userStrategyCollect == null)
            return false;
        else
            return true;

    }
    public UserStrategyCollect  findUserStrategyCollect(long uid,long stid){
        Map<String,Long> paramMap = Maps.newHashMap();
        paramMap.put("uid",uid);
        paramMap.put("stid",stid);
        return qicDbUtil.querySingleBeanWithNameParam("findUserCollectStrategy",UserStrategyCollect.class,paramMap);
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
     * 加入订阅
     *
     * @param month
     * @param uid
     * @param stid
     */
    public Date addstrategyorder(int month, Long uid, Long stid) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String getdowntime_sql = SqlLoader.getSqlById("getdowntime_sql");//得到下架时间
        Map<String, Object> map = qicDbUtil.querySingleMap(getdowntime_sql, stid);
        String downtime = "";
        if (map.get("down_time") == null) {
            downtime = "2099-12-12";
        } else {
            downtime = map.get("down_time").toString();
        }

        Date downtime2 = new Date();
        try {
            downtime2 = sdf.parse(downtime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //订阅结束时间
        Date date = new Date();
        Date newDate = DateUtils.addMonths(date, month);

        long l = newDate.getTime() - downtime2.getTime();
        long day = l / (24 * 60 * 60 * 1000);
        long hour = (l / (60 * 60 * 1000) - 24 * day);
        long min = (l / (60 * 1000) - day * 24 * 60 - hour * 60);

        //插入订阅时间
        if (min <= 0) {
            String insert_orderetime_sql = SqlLoader.getSqlById("insert_downtime");
            qicDbUtil.update(insert_orderetime_sql, uid, stid, newDate);
            String add_sumOrderCount_sql = SqlLoader.getSqlById("add_sumOrderCount_sql");
            qicDbUtil.update(add_sumOrderCount_sql, stid);
            return newDate;
        } else {
            return null;
        }
    }

    /**
     * 续订
     *
     * @param month
     * @param uid
     * @param stid
     * @return
     */
    public Date delaystrategyorder(int month, Long uid, Long stid) {
        //得到下架时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String getdowntime_sql = SqlLoader.getSqlById("getdowntime_sql");
        Map<String, Object> map = qicDbUtil.querySingleMap(getdowntime_sql, stid);
        String downtime = "";
        if (map.get("down_time") == null) {
            downtime = "2099-12-12";
        } else {
            downtime = map.get("down_time").toString();
        }

        Date downtime2 = new Date();
        try {
            downtime2 = sdf.parse(downtime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String get_orderetime_sql = SqlLoader.getSqlById("get_orderetime_sql");
        StrategyOrderDto strategyorder = qicDbUtil.querySingleBean(get_orderetime_sql, StrategyOrderDto.class, uid, stid);
        //订阅结束时间
        Date newDate = DateUtils.addMonths(strategyorder.order_etime, month);

        long l = newDate.getTime() - downtime2.getTime();
        long day = l / (24 * 60 * 60 * 1000);
        long hour = (l / (60 * 60 * 1000) - 24 * day);
        long min = (l / (60 * 1000) - day * 24 * 60 - hour * 60);
        //更新订阅时间
        if (min <= 0) {
            String update_orderetime_sql = SqlLoader.getSqlById("update_downtime");
            qicDbUtil.update(update_orderetime_sql, newDate, uid, stid);
            return newDate;
        } else {
            return null;
        }

    }

    public StrategyOrderDto getStrategyOrderById(Long uid, Long stid) {
        String get_orderetime_sql = SqlLoader.getSqlById("get_orderetime_sql");
        StrategyOrderDto strategyorder = qicDbUtil.querySingleBean(get_orderetime_sql, StrategyOrderDto.class, uid, stid);
         return strategyorder;
    }

    /**
     * author 潘志威
     * 加入回测
     *
     * @param ids
     */
    @Deprecated
    public  void addLookback(String ids[], int status) {

        String sql = "UPDATE qic_db.`strategy_baseinfo` a SET a.`status` = ?, a.`pass_time` = NOW() WHERE a.`id` IN (";
        for (int i = 0; i < ids.length; i++) {
            if (i == ids.length - 1) {
                sql += ids[i];
            } else {
                sql += ids[i] + ",";
            }
        }
        sql += ")";
        qicDbUtil.update(sql, status);
    }

    /**
     * author 潘志威
     * 删除策略
     *
     * @param ids
     */
    public  boolean delstrategy(String ids[]) {
        List<StrategyBaseinfo> strategyBaseinfos = findStrategysByIds(ids);
        for (StrategyBaseinfo strategyBaseinfo : strategyBaseinfos) {
            if (strategyBaseinfo.status == StrategyDto.StrategyStatus.BACKTESTING.value || strategyBaseinfo.status == StrategyDto.StrategyStatus.UPSHELF.value) {
                return false;
            }
        }
        String sql = SqlLoader.getSqlById("delStrategy");
        Object[][] params = new Object[ids.length][2];
        for (int i = 0; i < ids.length; i++) {
            params[i][0] = StrategyDto.StrategyStatus.DELETED.value;
            params[i][1] = ids[i];
        }
        qicDbUtil.batchQicDB(sql, params);
        return true;
    }

    /**
     * author 潘志威
     * 策略清空
     */
    public  void emptystrategy() {
        String sql = SqlLoader.getSqlById("emptyStrategy");
        qicDbUtil.update(sql);
    }

    /**
     * 处理 立即下架/延时下架
     * <p/>
     * 立即下架包括两个步骤
     * 1.如果当前时间大于最大收藏时间--》策略下架
     * 2.把每条下架信息保存到user_message表
     * <p/>
     * 延时下架 把相关信息存入任务调度表（scheduling_task）由定时任务类处理
     *
     * @param stIds    策略id数组
     * @param setTime 用户设置的 延时下架时间
     * @param flag    客户端传过来的标示 1 ：立即下架 2 ：延时下架
     * @return 7.策略在运行中,不允许下架 6.设置时间小于当前时间 5.策略为待下架状态不允许修改，4.模板内容非法 3.非法操作 2.策略当前有用户订阅，不能下架 1.下架成功
     * @author 刘泓江
     */
    public  int strategyDown(String[] stIds, Date setTime, String message, long uid, int flag) {
        Date currentTime = new Date();
        StrategyDownTaskContextDto tastContext = new StrategyDownTaskContextDto();
        if (stIds == null) {
            return 3;//3.非法操作
        }
        tastContext.strategyIdArray = stIds;
        tastContext.messageTemplate = message;
        String StrategyDownSql = SqlLoader.getSqlById("StrDown");
        String maxOrderTimeSql = SqlLoader.getSqlById("maxOrderTime");
        String getOrderedUserSql = SqlLoader.getSqlById("getOrderedUser");
        String sendUserMessageSql = SqlLoader.getSqlById("sendUserMassage");
        String queryStraStatusSql = SqlLoader.getSqlById("queryStraStatus");
        String isRunSql = SqlLoader.getSqlById("checkRunStatus");
        Gson gson = new Gson();
        String strIdGroup = gson.toJson(stIds).replace("[", "").replace("]", "");
        String sql = maxOrderTimeSql.replaceAll("#strIdGroup#", strIdGroup);
        isRunSql = isRunSql.replaceAll("#strIdGroup#", strIdGroup);
        UserStrategyOrderDto userStrategyOrder = qicDbUtil.querySingleBean(sql, UserStrategyOrderDto.class);
        Object obj = qicDbUtil.queryCount(isRunSql);
        Long count = obj==null? 0L:(Long)obj;
        if(count!=0){
            return 7;//策略在运行中，不允许下架
        }
        //5.策略为待下架状态不允许修改
        for (String id : stIds) {
            StrategyDto strategyDto = qicDbUtil.querySingleBean(queryStraStatusSql, StrategyDto.class, id);
            if (strategyDto == null || strategyDto.status == StrategyDto.StrategyStatus.WAITINGUPSHELF.value) {
                return 5;//5.策略为待下架状态不允许修改
            }
        }
        if (flag == 2) {//处理延时下架
            if (setTime == null || setTime.before(currentTime)) {
                return 6;//6.设置时间小于当前时间
            }
            if (setTime.before(userStrategyOrder.orderTime)) {//设定时间 比 所有已订阅用户最大收藏时间小。
                return 2;//2.策略当前有用户订阅，不能下架
            }
            StraDownDelayed(stIds, tastContext, StrategyDownSql, setTime, currentTime, getOrderedUserSql, sendUserMessageSql, message);
            logInfoService.writeSystemLog(uid, SystemLoggerMessage.DO_STR_DOWN, SystemLoggerMessage.STR_DOWN_DELAY, SystemLoggerMessage.TYPE);//写入系统日志
        } else if (flag == 1) { //处理立即下架
            if (currentTime.before(userStrategyOrder.orderTime)) {//设定时间 比 所有已订阅用户最大收藏时间小。
                return 2;//2.策略当前有用户订阅，不能下架
            }
            for (int i = 0; i < stIds.length; i++) {
                //设置策略状态为已下架
                qicDbUtil.update(StrategyDownSql, setTime, StrategyDto.StrategyStatus.DOWNSHELF.value, Long.parseLong(stIds[i]));
            }
            logInfoService.writeSystemLog(uid, SystemLoggerMessage.DO_STR_DOWN, SystemLoggerMessage.STR_DOWN_PROMPTLY, SystemLoggerMessage.TYPE);//写入系统日志

        } else {
            return 3;//3.非法请求
        }
        return 1;//1.下架成功
    }

    // 策略延时下架
    public  int StraDownDelayed(String[] stIds, StrategyDownTaskContextDto tastContext, String StrategyDownSql,
                                Date setTime, Date currentTime, String getOrderedUserSql, String sendUserMessageSql,
                                String message) {
        currentTime.setHours(24);
        currentTime.setMinutes(0);
        currentTime.setSeconds(0);
        final Long SPACING_INTERVAL = 7 * 24 * 60 * 60 * 1000L;//间隔时间
        String setScdulingTaskSql = SqlLoader.getSqlById("setScdulingTaskInfo");
        for (int i = 0; i < stIds.length; i++) {
            //设置策略状态为待下架
            qicDbUtil.update(StrategyDownSql, setTime, StrategyDto.StrategyStatus.WAITINGUPSHELF.value, Long.parseLong(stIds[i]));
        }
        Gson gson = new Gson();
        String contextJsonString = gson.toJson(tastContext);
        if (setTime.getTime() - currentTime.getTime() <= SPACING_INTERVAL) {//设置时间-当前时间小于7天 马上发下架通知给用户表
            Date orderDate = new Date(setTime.getTime() - SPACING_INTERVAL);
            for (int i = 0; i < stIds.length; i++) {
                try {
                    List<UserStrategyOrderDto> list = qicDbUtil.queryBeanList(getOrderedUserSql, UserStrategyOrderDto.class, Long.parseLong(stIds[i]), orderDate);
                    StrategyBaseinfo strategy = findStrategyById(Long.parseLong(stIds[i]));//组装用户消息
                    MessageBuilder messageBuilder = new MessageBuilder(message);
                    messageBuilder.addParameter("strategy", strategy);//下架通知模板 【$strategy.name】
                    String inputVal = messageBuilder.execute();
                    for (UserStrategyOrderDto userStrategyOrderDto : list) {
                        qicDbUtil.update(sendUserMessageSql, userStrategyOrderDto.uid, inputVal);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            qicDbUtil.update(setScdulingTaskSql, contextJsonString, setTime, "StrategyDownTaskRunner");
        } else {//设置时间-当前时间大于7天
            Date beforeDate = new Date(setTime.getTime() - SPACING_INTERVAL);
            qicDbUtil.update(setScdulingTaskSql, contextJsonString, beforeDate, "StrategyDownTaskRunner");//提前7天通知用户
            qicDbUtil.update(setScdulingTaskSql, contextJsonString, setTime, "StrategyDownTaskRunner");//在设定日期再次通知用户
        }
        return 1;
    }

    /**
     * author 潘志威
     * 策略上架
     *
     * @param ids
     * @param serverId qsm中的agentIP
     */
    public  boolean upstrategy(String ids[], Map<String, Integer> serverId) {
        List<StrategyBaseinfo> strategyList = findStrategysByIds(ids);
        List<StrategyBaseinfo> qicoreStrategy = Lists.newArrayList();
        List<StrategyBaseinfo> qiaStrategy = Lists.newArrayList();
        for (StrategyBaseinfo strategyBaseinfo : strategyList) {
            if (strategyBaseinfo.status != StrategyDto.StrategyStatus.FINISHTEST.value) {
                return false;
            }
            if (strategyBaseinfo.enginetypeId == StrategyDto.QICORE_ENGINEE_ID) {
                qicoreStrategy.add(strategyBaseinfo);
            }
            if (strategyBaseinfo.enginetypeId == StrategyDto.QIA_ENGINEE_ID) {
                qiaStrategy.add(strategyBaseinfo);
            }
        }
        String sql = SqlLoader.getSqlById("upShelfStrategy");
        Object[][] params = new Object[ids.length][3];
        int index = 0;
        for (StrategyBaseinfo strategyBaseinfo : strategyList) {
            params[index][0] = StrategyDto.StrategyStatus.UPSHELF.value;
            params[index][1] = strategyBaseinfo.enginetypeId == StrategyBaseinfo.QICORE_ENGINEE_ID ? serverId.get("qicore") : serverId.get("qiaSimulate");
            params[index][2] = strategyBaseinfo.id;
            ++index;
        }
        qicDbUtil.batchQicDB(sql, params);
        //同步数据到qsm个
        //写到qsm库中
        if (qicoreStrategy.size() > 0) {
            updateAgentIpWhenUpShelf(qicoreStrategy, serverId.get("qicore"));
        }
        if (qiaStrategy.size() > 0) {
            updateAgentIpWhenUpShelf(qiaStrategy, serverId.get("qiaAgentId")); //qia
        }
        return true;
    }

    /**
     * 查询一批策略
     *
     * @param ids
     * @return
     */
    public  List<StrategyBaseinfo> findStrategysByIds(String ids[]) {
        if (ids == null || ids.length == 0) {
            return new ArrayList<StrategyBaseinfo>();
        }

        Long[] list = new Long[ids.length];
        for (int i = 0; i < ids.length; i++) {

            list[i] = Long.valueOf(ids[i]);
        }
        return findStrategysByIds(list);
    }

    public  List<StrategyBaseinfo> findStrategysByIds(Long ids[]) {
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

    public  StrategyBaseinfo findStrategyById(long id) {
        String sql = SqlLoader.getSqlById("findStrategyById");
        return qicDbUtil.querySingleBean(sql, StrategyBaseinfo.class, id);
    }

    private  void updateAgentIpWhenUpShelf(List<StrategyBaseinfo> list, int serverId) {
        String sql = SqlLoader.getSqlById("updateServerIpWhenUpShelf");
        Object[][] params = new Object[list.size()][2];
        for (int row = 0; row < list.size(); row++) {
            params[row][0] = String.valueOf(serverId);
            params[row][1] = list.get(row).stUuid;
        }
        qicDbUtil.batch(sql, params);
    }

    /**
     * 判断该用户是否已经评论的该策略
     *
     * @param stid
     * @param uid
     */
    public Integer judge(Long stid, Long uid) {
        int result = 0;
        UserStrategyDiscuss userStrategyDiscuss = new UserStrategyDiscuss();
        userStrategyDiscuss.uid = uid;
        userStrategyDiscuss.stid = stid;
        List<UserStrategyDiscuss> usdList = qicDbUtil.queryBeanListWithNameParam("findUserStrategyCommentList", UserStrategyDiscuss.class, userStrategyDiscuss);
        if (usdList != null && usdList.size() > 0) {
            result = 1;
        }
        return result;
    }

    /**
     * 保存该用户对该策略的评论
     *
     * @param usd
     * @param uid
     * @param stid
     */
    public void saveDiscuss(UserStrategyDiscuss usd, Long uid, Long stid) {
        if (uid != null && stid != null) {
            UserInfo u = userInfoService.findUserById(uid);
            usd.uid = uid;
            usd.stid = stid;
            qicDbUtil.updateWithNameParam("updateStrategyDiscussData", usd);
            qicDbUtil.insertWithNameParam("addStrategyUserComment", usd);
        }

    }

    public  int syncBackTestResult(StrategyDto strategyDto) {
        String sql = SqlLoader.getSqlById("syncBackTestResult");
        return qicDbUtil.updateDB(sql,
                strategyDto.id,
                strategyDto.retainedProfits,
                strategyDto.grossProfit,
                strategyDto.overallProfitability,
                strategyDto.overallDeficit,
                strategyDto.canhsiedRatio,
                strategyDto.tradeCount,
                strategyDto.longPositionTradeCount,
                strategyDto.shortPositionTradeCount,
                strategyDto.profitRatio,
                strategyDto.profitCount,
                strategyDto.deficitCount,
                strategyDto.positionCloseCount,
                strategyDto.maxSingleProfit,
                strategyDto.maxSingleDeficit,
                strategyDto.maxSingleProfitRatio,
                strategyDto.maxSingleDeficitRatio,
                strategyDto.profitLossRatio,
                strategyDto.sumOfCommission,
                strategyDto.yield,
                strategyDto.avgProfitOfMonth,
                strategyDto.floatingProfitAndLoss,
                strategyDto.totalAsset,
                strategyDto.yieldOfMonth,
                strategyDto.yieldOfYear,
                strategyDto.maxSequentialDeficitCapital,
                strategyDto.lastSequentialDeficitCapital,
                strategyDto.maxSequentialProfitCount,
                strategyDto.lastSequentialProfitCount,
                strategyDto.maxSequentialDeficitCount,
                strategyDto.lastSequentialDeficitCount,
                strategyDto.tradeDays,
                strategyDto.maxShortPositionTime,
                strategyDto.yieldOfMonthStandardDeviation,
                strategyDto.sharpeIndex,
                strategyDto.movingCost);
    }

    public  int syncQiaBackTestResult(JsonObject jo) {
        String sql = SqlLoader.getSqlById("syncQiaBackTestResult");
        return qicDbUtil.update(sql,
                jo.get("id").getAsInt(),
                1,//回验期数据
                jo.get("sharpRatio").getAsDouble(),
                jo.get("volatility").getAsDouble(),
                jo.get("beta").getAsDouble(),
                jo.get("averageSimpleRateOfReturn,").getAsDouble(),
                jo.get("calmarRatio").getAsDouble(),
                jo.get("conditionalSharpRatio").getAsDouble(),
                jo.get("excessReturnOnVar").getAsDouble(),
                jo.get("highterPartialMoments").getAsDouble(),
                jo.get("jensenRatio").getAsDouble(),
                jo.get("kappa3").getAsDouble(),
                jo.get("conditionalVar").getAsDouble(),
                jo.get("lowerPartialMoments").getAsDouble(),
                jo.get("maximumDrawdown").getAsDouble(),
                jo.get("modifiedSharpRatio").getAsDouble(),
                jo.get("maxSingleDeficitRatio").getAsDouble(),
                jo.get("mvar").getAsDouble(),
                jo.get("omega").getAsDouble(),
                jo.get("sortinoRatio").getAsDouble(),
                jo.get("treynorRatio").getAsDouble(),
                jo.get("upsidePotentialRatio").getAsDouble(),
                jo.get("var").getAsDouble(),
                jo.get("skewness").getAsDouble(),
                jo.get("kurtosis").getAsDouble(),
                jo.get("corrWithMarket").getAsDouble(),
                jo.get("hitRate").getAsDouble(),
                jo.get("cumsumSimpleReturn").getAsDouble());
    }

    public  int updateStategyStatus(StrategyDto.StrategyStatus status, String suuid) {
        String sql = SqlLoader.getSqlById("updateStategyStatus");
        return qicDbUtil.update(sql, status.value, suuid);
    }

    public  int updateStategyStatusbyId(StrategyDto.StrategyStatus status, String id) {
        String sql = SqlLoader.getSqlById("updateStategyStatusbyId");
        return qicDbUtil.update(sql, status.value, id);
    }


    public  int deleteStrategyFromPerformance(String strUUID, StrategyDto.StrategyType strategyType) {
        String sql = SqlLoader.getSqlById("deleteStrategyFromPerformance");
        String tableName = "";
        switch (strategyType) {
            case QICORE:
                tableName = "strategy_performance_qicore";
                break;
            case QIA:
                tableName = "strategy_performance_qia";
                break;
        }
        return qicDbUtil.update(sql.replace("#tableName#", tableName), strUUID);
    }

    /**
     * 上传策略
     */
    public  boolean insertStrategy(StrategyDto strategyDto, long uid,String filePath) throws SQLException {
        Connection conn = DB.getDBConfig().getConnection();
        String sql = SqlLoader.getSqlById("insertStrategy");
        //conn.setTransactionIsolation(TransactionIsolation.);设置事务的隔离级别
        String strategyFileBasePath = Play.configuration.getProperty("iquant.strategy.upload.dir")+"/";
        try {
            Long  id = qicDbUtil.insert(sql, strategyDto.strategyId,
                                              strategyDto.sname,
                                              strategyDto.stype,
                                              strategyDto.tradeVariety,
                                              strategyDto.provider,
                                              strategyDto.providerDesp,
                                              strategyDto.desp,
                                              StrategyBaseDto.StrategyStatus.CHECKING.value,
                                              uid,
                                              strategyDto.customerLookbackStartTime,
                                              strategyDto.customerLookbackEndTime,
                                              strategyDto.enginetypeId,
                                              strategyDto.customerLookbackStartTime,
                                              strategyDto.customerLookbackEndTime);
           parseStrategyAccountTemplate(strategyFileBasePath+filePath+"/StrategyCfg.xml",id,uid);
           parseStrategyOrignSercurities(strategyFileBasePath + filePath, id);
            String strategyName = strategyDto.sname;
            File directory = new File(strategyFileBasePath+filePath);
            File[] allStrategyFiles = directory.listFiles();
            boolean  hasStrategyName = false;
            for(File file :allStrategyFiles){
                String fileName = file.getName();
                Logger.info("文件名为:" + fileName);
                if(fileName.startsWith("BackTestCfg_")){
                    strategyName = fileName.substring(fileName.indexOf("_")+1,fileName.indexOf("."));
                    hasStrategyName = true;
                    break;
                }
            }
            Logger.info("策略名字查找结果:"+ hasStrategyName+"策略名为:" + strategyName);
            String qsmSql = SqlLoader.getSqlById("syncStrategyToQicQsm");
            qicDbUtil.updateDB(qsmSql,  strategyDto.strategyId, strategyName, filePath, "127.0.0.1");//2012-12-24改的
            return true;
        } catch (Exception e) {
            Logger.error(e, e.getMessage());
            //出现异常,删除临时文件
            JPA.closeTx(true);//只要一方面出异常则删除文件 ，数据库回滚
            return false;
            //throw new DatabaseException(e.getMessage(),e);
        }

    }
    //解析策略账号模板
    private static boolean  parseStrategyAccountTemplate(String filePath,long strategyId,long uid) throws Exception {
        File file = new File(filePath);
        if(!file.exists()){
            Logger.warn("找不到策略账号模板文件:%s",filePath);
           return false;
        }
        FileInputStream xmlInput = new FileInputStream(new File(filePath));
        org.w3c.dom.Document root = null;
        try{
           root = CommonUtils.getDocument(xmlInput);
        }catch (Exception e){
          throw new IquantEntityHasBeenUsedException("StrategyCfg.xml文件解析出错，请检查该文件结构");
        }
        List<org.w3c.dom.Node> list = XPath.selectNodes("/Strategy/Accounts/Account", root);
        List<StrategyAccountTemplateDto> accounts = Lists.newArrayList();
        for(org.w3c.dom.Node accountNode:list){
            StrategyAccountTemplateDto strategyAccountTemplateDto = new StrategyAccountTemplateDto();
            Integer accountOrder = CommonUtils.parseNumber(XPath.selectText("@serialID", accountNode), Integer.class, null);
            String accountType = XPath.selectText("@type",accountNode);
            strategyAccountTemplateDto.accountOrder=accountOrder;
            strategyAccountTemplateDto.strategyId = strategyId;
            strategyAccountTemplateDto.createUid =uid;
            strategyAccountTemplateDto.status=1;
            strategyAccountTemplateDto.accountType=StrategyAccountTemplateDto.accountType2Int(accountType);
            strategyService.addStrategyAccountTemplate(strategyAccountTemplateDto);
        }
        Logger.info("通过后台上传时解析出策略账号模板信息成功,策略id:"+ strategyId);
        return true;
    }
    //解析策略原始标的信息
    private static boolean  parseStrategyOrignSercurities(String filePath,long strategyId) throws Exception {
        File strategyConfigFile = new File(filePath+"/StrategyCfg.xml");
        FileInputStream strategyConfigFileXmlInput = new FileInputStream(strategyConfigFile);
        org.w3c.dom.Document xmlRoot = null;
        try{
            xmlRoot =CommonUtils.getDocument(strategyConfigFileXmlInput);
        }catch (Exception e){
            throw new IquantEntityHasBeenUsedException(strategyConfigFileXmlInput +"文件解析出错，请检查该文件结构");
        }
        String sercurityConfigFileName = null;
        sercurityConfigFileName  = XPath.selectText("/Strategy/FactorDataCfg/@tickerList", xmlRoot);
        Logger.info("通过后台上传时解析出策略原始配制文件名称为[securityConfigFileName]:"+ sercurityConfigFileName);
        File file = new File(filePath + "/" +sercurityConfigFileName );
        if(!file.exists()){
            Logger.warn("找不到策略标的文件[securityConfigFileName]:%s",filePath + "/" +sercurityConfigFileName );
            return false;
        }
        FileInputStream xmlInput = new FileInputStream(file);
        org.w3c.dom.Document root = CommonUtils.getDocument(xmlInput);
        List<org.w3c.dom.Node> list = XPath.selectNodes("/Strategy/code", root);
        List<StrategySecurityOriginalDto> original = Lists.newArrayList();
        for(org.w3c.dom.Node accountNode:list){
            Integer maxPosition = CommonUtils.parseNumber(XPath.selectText("@MaxShare", accountNode), Integer.class, null);
            String symbol = XPath.selectText("@id",accountNode);
            String market = XPath.selectText("@exchangeType",accountNode);
            StrategySecurityOriginalDto strategySecurityOriginalDto = new StrategySecurityOriginalDto(symbol,maxPosition,market);
            strategySecurityOriginalDto.strategyId = strategyId;
            strategyService.addStrategySecurityOrigunalInfo(strategySecurityOriginalDto);
        }
        Logger.info("通过后台上传时解析出策略原始配制的交易标的成功,策略id:"+ strategyId);
        return true;
    }
    public  boolean synStrateToQsm(List<StrategyBaseinfo> list, int serverId) {
        //List<StrategyBaseinfo> list =findStrategysByIds(ids);
        String ip = backTestService.findServerIpById(serverId);
        if (list == null || list.size() == 0) {
            return false;
        } else {
            //String basePath = SystemConfigService.get(Constants.OTHERS_LOAD_STRATEGY_BASE_DIR);
            String qsmSql = SqlLoader.getSqlById("syncStrategyToQsm");
            String queryFilePathSql = SqlLoader.getSqlById("queryFilePath");
            Object[][] params = new Object[list.size()][5];
            String runBasePath = systemConfigService.get(Constants.OTHERS_LOAD_STRATEGY_BASE_DIR);
            for (int row = 0; row < list.size(); row++) {
                QsmStrategyDto qsmStrategyDto = qicDbUtil.querySingleBean(queryFilePathSql, QsmStrategyDto.class, list.get(row).stUuid);
                params[row][0] = list.get(row).stUuid;
                params[row][1] = qsmStrategyDto.strategyName;
                //params[row][2] = basePath + list.get(row).stUuid;//是否要拼要基地址? 改为查询数据库得到地址
                params[row][2] = runBasePath + qsmStrategyDto.filePath.replace("/", "\\");//是否要拼要基地址? 改为查询数据库得到地址
                params[row][3] = ip;//是否要拼要基地址?
                params[row][4] = list.get(row).name;
            }
            qsmDbUtil.batch(qsmSql, params);
            return true;
        }

    }

    public  boolean syncProductToQsm(long strategyVirtualProductRelatedId, long strategyId) {
        //List<StrategyBaseinfo> list =findStrategysByIds(ids);
        try {
            String qsmSql = SqlLoader.getSqlById("syncStrategyToQsm");
            String queryFilePathSql = SqlLoader.getSqlById("queryFilePath");
            StrategyBaseinfo strategyBaseinfo = findStrategyById(strategyId);
            String strategyUuid = strategyBaseinfo.stUuid;
            QsmStrategyDto qsmStrategyDto = qicDbUtil.querySingleBean(queryFilePathSql, QsmStrategyDto.class, strategyUuid);

            String ip = backTestService.findServerIpById(Integer.valueOf(qsmStrategyDto.agentIp));//其实之前存的是服务器id
            // String runBasePath = SystemConfigService.get(Constants.OTHERS_LOAD_STRATEGY_BASE_DIR);
            String realFilePath = qsmStrategyDto.filePath.replace("/", "\\");

            if (StringUtils.isBlank(ip)) {
                Logger.warn("qsmStrategyDto.agentIp = [%s],查不到agent的ip, 是数据上的错误, 这里先附值为 没有找到ip", qsmStrategyDto.agentIp);
                ip = "没有找到ip";
            }
            //同步到qsm库的时候在策略名前加上lib( 2013-10-12又去掉 qse自已拼上lib)
            qsmDbUtil.updateDB(qsmSql, String.valueOf(strategyVirtualProductRelatedId), qsmStrategyDto.strategyName, realFilePath, ip,strategyBaseinfo.name);
            Logger.info("添加运行策略到qsm库中 关联id=%d,策略id=%d,finalPath=%s", strategyVirtualProductRelatedId, strategyId, realFilePath);
            return true;
        } catch (Exception e) {
            Logger.error(e, "添加运行策略到qsm失败，没有配置相应的服务器ip 关联id=%d,策略id=%d", strategyVirtualProductRelatedId, strategyId);
            return false;
        }

    }

    public  boolean deleteProductFromQsm(long strategyVirtualProductRelatedId) {
        //List<StrategyBaseinfo> list =findStrategysByIds(ids);
        try {
            String qsmSql = SqlLoader.getSqlById("deleteProductFromQsm");
            qsmDbUtil.update(qsmSql, String.valueOf(strategyVirtualProductRelatedId));
            Logger.info("从qsm库中 删除id=%d", strategyVirtualProductRelatedId);
            return true;
        } catch (Exception e) {
            Logger.error(e, e.getMessage());
            return false;
        }

    }
}
