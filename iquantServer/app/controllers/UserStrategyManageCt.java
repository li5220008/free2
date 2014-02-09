package controllers;

import bussiness.common.ILogInfoService;
import bussiness.strategy.IBackTestService;
import bussiness.strategy.IStrategyService;
import bussiness.strategy.IUserStrategyManageService;
import com.google.common.collect.Maps;
import models.iquantCommon.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import play.Logger;
import play.data.binding.As;
import play.libs.F;
import protoc.ErrorResponseModel;
import util.CommonUtils;
import util.GsonUtil;
import util.Page;
import util.SystemLoggerMessage;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-7-4
 * Time: 下午3:11
 * 功能描述:
 */
public class UserStrategyManageCt extends BaseController {
    @Inject
    static IStrategyService strategyService;
    @Inject
    static IBackTestService backTestService;
    @Inject
    static ILogInfoService logInfoService;
    @Inject
    static IUserStrategyManageService userStrategyManageService;

    /**
     * 查询当前策略ID中哪些策略是用户已收藏的
     *
     * @param ids 当前策略id集合 字符串  用参数用逗号隔开在url上进行传递
     * @param uid 用户id
     *            返回已收藏的id集合
     */
    public static void fetchUserCollectById(@As(",") List<Long> ids, long uid) {
        Set<Integer> hasCollectIds = userStrategyManageService.queryUserCollectSet(ids, uid);
        responseJSON(hasCollectIds);

    }

    /**
     * 添加收藏策略
     *
     * @param uid 用户id
     * @param sid 被收藏的策略id
     */
    public static void addStrategyCollect(long uid, long sid) {
        try {
            userStrategyManageService.addstrategycollect(sid, uid);
            responseJSON(true);
        } catch (Exception e) {
            renderJSON(new ErrorResponseModel("策略不存在").encode());
        }
    }

    /**
     * 用户取消收藏
     *
     * @param uid
     * @param sid
     */
    public static void delStrategyCollect(long uid, long sid) {
        try {
            userStrategyManageService.deletestrategycollect(sid, uid);
            responseJSON(true);
        } catch (Exception e) {
            renderJSON(new ErrorResponseModel("策略不存在").encode());
        }
    }

    /**
     * 判断策略是否被用户收藏
     *
     * @param stid 策略id
     * @param uid  用户id
     */
    public static void hasCollected(long stid, long uid) {
        responseJSON(userStrategyManageService.iscollect(stid, uid));
    }

    /**
     * 判断策略是否被用户订阅
     *
     * @param stid 策略id
     * @param uid  用户id
     */
    public static void hasOrdered(long stid, long uid) {
        responseJSON(userStrategyManageService.isorder(uid, stid));
    }

    /**
     * 查询用户阅到期时间
     *
     * @param uid
     * @param stid
     */
    public static void fetchUserOrderEndDate(long uid, long stid) {
        StrategyOrderDto order = strategyService.findUserStrategyOrder(uid, stid);
        responseJSON(order == null ? null : order.order_etime);
    }

    /**
     * 查询用户是否评论了某条策略
     *
     * @param uid
     * @param stid
     */
    public static void hasComment(long uid, long stid) {
        responseJSON(userStrategyManageService.judge(stid, uid));
    }

    /**
     * 添加策略评论
     *
     * @param uid
     * @param stid
     */
    public static void addUserDiscuss(long uid, long stid) {
        String body = fetchBody();
        UserStrategyDiscuss userStrategyDiscuss = GsonUtil.createGson().fromJson(body, UserStrategyDiscuss.class);
        userStrategyManageService.saveDiscuss(userStrategyDiscuss, uid, stid);
        responseJSON(true);
    }

    /**
     * 添加订阅
     *
     * @param month
     * @param uid   用户id
     * @param stid  策略id
     */
    public static void orderStrategy(int month, long uid, long stid) {
        Map<String, Object> json = new HashMap<String, Object>();
        Date edate = DateUtils.addMonths(new Date(), month);
        if (edate.getYear() + 1900 > 2030) {
            json.put("message", "订阅到期日期不能超过2030年");
            json.put("success", false);
        } else {
            Date newDate = userStrategyManageService.addstrategyorder(month, uid, stid);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if (newDate != null) {
                json.put("message", "订阅成功");
                json.put("success", true);
                json.put("date", sdf.format(newDate));
            } else {
                StrategyBaseinfo strategyBaseinfo = strategyService.findStrategyById(stid);
                String downtime = "";
                if (strategyBaseinfo.downTime == null) {
                    downtime = "2099-12-12";
                } else {
                    downtime = CommonUtils.getFormatDate(CommonUtils.DATE_FORMAT_STR_ARR[1], strategyBaseinfo.downTime);
                }
                downtime = downtime.substring(0, downtime.length() - 2);
                StringBuffer sb = new StringBuffer("该策略将于");
                sb.append(downtime);
                sb.append("下架,请重新选择订阅时间");

                json.put("message", sb);
                json.put("success", false);

            }
        }
        responseJSON(json);
    }

    /**
     * 续订
     * @param month
     * @param uid
     * @param stid
     */
    public static void delayStrategyOrder(int month,Long uid,Long stid){
        Map<String, Object> json = new HashMap<String, Object>();
        StrategyOrderDto strategyOrderDto = userStrategyManageService.getStrategyOrderById(uid, stid);
        Date edate = DateUtils.addMonths(strategyOrderDto.order_etime, month);
        if(edate.getYear() + 1900 > 2030){
            json.put("message","订阅到期日期不能超过2030年");
            json.put("success",false);
        }
        else{
            Date newDate = userStrategyManageService.delaystrategyorder(month, uid, stid);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if(newDate != null){
                json.put("message","续订成功");
                json.put("success",true);
                json.put("date",sdf.format(newDate));
            }
            else {
                StrategyBaseinfo strategyBaseinfo = strategyService.findStrategyById(stid);
                String downtime = "";
                if(strategyBaseinfo.downTime == null){
                    downtime = "2099-12-12";
                }
                else {
                    downtime = CommonUtils.getFormatDate(CommonUtils.DATE_FORMAT_STR_ARR[1], strategyBaseinfo.downTime);
                }
                downtime = downtime.substring(0,downtime.length()-2);
                StringBuffer sb = new StringBuffer("该策略将于");
                sb.append(downtime);
                sb.append("下架,请重新选择订阅时间");

                json.put("message",sb);
                json.put("success",false);
            }
        }

        responseJSON(json);
    }

    /**
     * 获取用户收藏列表
     *
     * @param uid       用户id
     * @param sortType， 0,1 收益率排行  2 按人气排序 3.按最新上架排序
     * @param keyword   搜索关键字
     * @param pageNo    页码
     */
    public static void fetchUserCollectList(long uid, int sortType, String keyword, int pageNo) {
        F.T2<List<StrategyBaseDto>, Page> strategyList = strategyService.favoriteStrategyList(sortType, keyword, pageNo, uid);
        responseJSON(strategyList._1, strategyList._2);
    }

    /**
     * 用户收藏  高级搜索
     *
     * @param sortType 0,1 收益率排行  2 按人气排序 3.按最新上架排序
     * @param uid      用户id
     * @param pageNo   页码
     */
    public static void fetchUserCollectionListAdvance(int sortType, long uid, int pageNo) {
        String body = fetchBody();
        StrategySearchCnd cnd = GsonUtil.createWithoutNulls().fromJson(body, StrategySearchCnd.class);
        F.T2<List<StrategyBaseDto>, Page> result = strategyService.favoriteStrategyAdvanceSearch(cnd, sortType, pageNo, uid);
        responseJSON(result._1, result._2);
    }

    /**
     * 查询用户的策略订阅列表
     *
     * @param uid      用户id
     * @param sortType 0,1 收益率排行  2 按人气排序 3.按最新上架排序
     * @param keyword  搜索关键字
     * @param pageNo   页码
     */
    public static void fetchUserOrderList(long uid, int sortType, String keyword, int pageNo) {
        F.T2<List<StrategyBaseDto>, Page> result = strategyService.subscriptionStrategyList(sortType,keyword, pageNo, uid);
        responseJSON(result._1, result._2);
    }

    /**
     * 高级搜索我的订阅
     *
     * @param sortType 0,1 收益率排行  2 按人气排序 3.按最新上架排序
     * @param uid      用户id
     * @param pageNo   页码
     */
    public static void fetchUserOrderListAdvance(int sortType, long uid, int pageNo) {
        String body = fetchBody();
        StrategySearchCnd cnd = GsonUtil.createWithoutNulls().fromJson(body, StrategySearchCnd.class);
        F.T2<List<StrategyBaseDto>, Page> result = strategyService.subscriptionStrategyAdvanceSearch(cnd, sortType, pageNo, uid);
        responseJSON(result._1, result._2);
    }

    /**
     * 策略加入回测
     *
     * @param qiaServerId    所选qia器id
     * @param qicoreServerId 所选qicore服务器id
     * @param stids
     */
    public static void addBackTest(String qiaServerId, String qicoreServerId, @As(",") String[] stids) {

        Map<String, Integer> serverIds = Maps.newHashMap();
        if (!StringUtils.isEmpty(qiaServerId)) {
            serverIds.put("qia", Integer.valueOf(qiaServerId));
        }
        if (!StringUtils.isEmpty(qicoreServerId)) {
            serverIds.put("qicore", Integer.valueOf(qicoreServerId));
        }
        int[] effects = backTestService.updateStratedyServerIdByIntId(serverIds, stids);
        responseJSON(effects);

    }

    /**
     * 批量删除策略
     *
     * @param stids 非uuid
     */
    public static void deleteStrategy(@As(",") String[] stids) {
        responseJSON(userStrategyManageService.delstrategy(stids));
        logInfoService.writeSystemLog(1, SystemLoggerMessage.DO_DELETE_STRATEGY, SystemLoggerMessage.DELETE_STRATEGY, SystemLoggerMessage.TYPE);
    }

    /**
     * 清空策略回收站
     */
    public static void emptyStrategy() {
        userStrategyManageService.emptystrategy();
        responseJSON(true);
    }

    /**
     * 策略下架
     *
     * @param downTime 下架时间
     * @param stids    策略id
     * @param message  下架通知消息
     * @param uid      用户id
     * @param flag     下架方式 1 ：立即下架 2 ：延时下架
     */
    public static void downStrategy(@As("yyyy-MM-dd HH:mm:ss") Date downTime, @As(",") String[] stids, String message, long uid, int flag) {
        responseJSON(userStrategyManageService.strategyDown(stids, downTime, message, uid, flag));
    }

    public static void upStrategy(@As(",")String[] stids, String qiaServerId, String qicoreServerId,String qiaAgentId) {

        Map<String, Integer> serverIds = Maps.newHashMap();
        if (!StringUtils.isEmpty(qiaServerId)) {
            serverIds.put("qiaSimulate", Integer.valueOf(qiaServerId));
        }
        if (!StringUtils.isEmpty(qicoreServerId)) {
            serverIds.put("qicore", Integer.valueOf(qicoreServerId));
        }
        if (!StringUtils.isEmpty(qiaAgentId)) {
            serverIds.put("qiaAgentId", Integer.valueOf(qiaAgentId));
        }
        responseJSON(userStrategyManageService.upstrategy(stids, serverIds));
    }

    /**
     * 查询用户运行中的策略数  含运行中的   待下架的
     * @param uid
     */
    public static void countUserRunntimeStrategy(long uid) {
        int totalRuntimeStrategy = strategyService.countUserRunntimeStrategy(uid);
        responseJSON(totalRuntimeStrategy);
    }
    /**
     * 添加策略
     * @param uid 策略上传者id
     */
    public static void addStrategy( long uid,String filePath) {
        try {
            String body = fetchBody();
            StrategyDto strategyDto = GsonUtil.createWithoutNulls().fromJson(body,StrategyDto.class);
            boolean success = userStrategyManageService.insertStrategy(strategyDto, uid,filePath);
            responseJSON(success);
        } catch (Exception e) {
            responseError();
            Logger.error(e, e.getMessage());
        }
    }

    /**
     * 用户策略上传列表
     */
    public static void fetchUserStrategyList(){
        String body = fetchBody();
        Map<String,String> paramMap = GsonUtil.createWithoutNulls().fromJson(body,Map.class);
        F.T2<List<StrategyBaseDto>, Page> result = strategyService.findStrategysByUser(paramMap);
        responseJSON(result._1,result._2);
    }

    /**
     * 获得该回测服务器对应的策略数
     * @param serviceId  回测服务器ID
     */
    public static void fetchStartegyNumByServiceId(int serviceId) {
        long totalRuntimeStrategy = strategyService.fetchStartegyNumByServiceId(serviceId);
        responseJSON(totalRuntimeStrategy);
    }

}
