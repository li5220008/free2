package controllers;

import bussiness.stock.*;
import models.iquantCommon.StockPoolSearchCnd;
import models.iquantCommon.StockPoolsPar;
import models.iquantCommon.StockpoolDto;
import play.data.binding.As;
import play.libs.F;
import util.GsonUtil;
import util.Page;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-7-11
 * Time: 上午9:56
 * 功能描述: 用户股票池相关的API
 */
public class UserStockPoolManageCt extends BaseController {
    @Inject
    static IUserStockPoolManageService userStockPoolManageService;
    @Inject
    static IStockPoolService stockPoolService;

    /**
     * 查询用户收藏股票池列表
     *
     * @param keyword      关键字
     * @param strategyName 搜索策略
     * @param orderType    排序
     * @param pageNo       页码
     * @param uid          用户id
     */
    public static void fetchCollectedStockPoolList(String keyword, String strategyName, String orderType, int pageNo, long uid,int flag) {

        StockPoolsPar stockPoolsPar = new StockPoolsPar();
        stockPoolsPar.content = keyword;
        stockPoolsPar.strategyName = strategyName;
        stockPoolsPar.orderSort = orderType;
        stockPoolsPar.flag = flag;
        F.T2<List<StockpoolDto>, Page> result = stockPoolService.stockPoolyList(stockPoolsPar, pageNo, uid);
        responseJSON(result._1, result._2);

    }

    /**
     * 用户已收藏股票池高级搜索
     *
     * @param pageNo 页码
     * @param uid    用户id
     */
    public static void advanceSearch(int pageNo, long uid) {
        String body = fetchBody();
        StockPoolSearchCnd stockPoolSearchCnd = GsonUtil.createWithoutNulls().fromJson(body, StockPoolSearchCnd.class);
        F.T2<List<StockpoolDto>, Page> result = stockPoolService.advanceSearch(stockPoolSearchCnd, pageNo, uid);
        responseJSON(result._1, result._2);

    }

    /**
     * 查看当前页中用户已收藏的股票池id
     *
     * @param ids
     * @param uid
     */
    public static void fetchCollectIdList(@As(",") Long[] ids, Long uid) {
        Set<Integer> hasCollectids = stockPoolService.queryStockPoolCollectMap(ids, uid);
        responseJSON(hasCollectids);
    }

    /**
     * 查询当前页中用户已评论的股票池id
     *
     * @param uid
     */
    public static void fetchCommentList(Long uid) {

        Set<Integer> hasDiscussList = userStockPoolManageService.queryStockPoolDiscussMap(uid);
        responseJSON(hasDiscussList);

    }

    /**
     * @param star 星级
     * @param uid  用户id
     * @param spid 股票池id
     */
    public static void addComment(int star, Long uid, Long spid) {
        boolean success = userStockPoolManageService.saveDiscuss(star, uid, spid);
        responseJSON(success);

    }

    /**
     * @param uid  用户id
     * @param spid 股票池id
     */
    public static void hasComment(long uid, String spid) {
        responseJSON(userStockPoolManageService.judge(spid, uid) > 0);
    }

    /**
     * 判断用户是否已收藏
     *
     * @param uid
     * @param spid
     */
    public static void hasCollected(long uid, String spid) {
        responseJSON(stockPoolService.iscollect(spid, uid));
    }

    /**
     * 添加收藏
     *
     * @param spid 股票池id
     * @param uid  用户id
     */
    public static void addCollections(long spid, long uid) {

        try {
            userStockPoolManageService.stockpooladdcollect(uid, spid);
            responseJSON(true);
        } catch (Exception e) {
            responseError("添加收藏出错!");
        }
    }

    /**
     * 取消收藏
     *
     * @param spid 股票池id
     * @param uid  用户id
     */
    public static void delCollections(long spid, long uid) {
        try {
            userStockPoolManageService.stockpooldeletecollect(uid, spid);
            responseJSON(true);
        } catch (Exception e) {
            responseError("取消收藏出错!");
        }
    }
}
