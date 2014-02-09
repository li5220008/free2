package controllers;

import bussiness.stock.IStockPoolService;
import models.iquantCommon.*;
import play.libs.F;
import util.GsonUtil;
import util.Page;

import javax.inject.Inject;
import java.util.List;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-7-11
 * Time: 上午9:55
 * 功能描述: 股票池相关的api
 */
public class StockPoolCt extends BaseController {

    @Inject
    static IStockPoolService stockPoolService;

    /**
     * 股票池列表
     *
     * @param keyword      关键字
     * @param strategyName 名称
     * @param orderType    排序字段
     * @param pageNo       页码
     */
    public static void fetchStockPoolList(String keyword, String strategyName, String orderType, int pageNo, int flag) {


        StockPoolsPar stockPoolsPar = new StockPoolsPar();
        stockPoolsPar.content =  keyword;
        stockPoolsPar.strategyName = strategyName;
        stockPoolsPar.orderSort = orderType;
        stockPoolsPar.flag = flag;
        F.T2<List<StockpoolDto>, Page> t2 = null;

        if (strategyName!=null&&strategyName.equals("hot")) {
            t2 = stockPoolService.hotList(stockPoolsPar, pageNo);
        } else {
            t2 = stockPoolService.stockPoolyList(stockPoolsPar, pageNo);
        }
        responseJSON(t2._1, t2._2);

    }

    /**
     * 股票池列表 热门搜索  不知道 和上面有啥区别...先这样
     *
     * @param keyword
     * @param strategyName
     * @param orderType
     * @param pageNo
     */
    public static void fetchHotStockPoolList(String keyword, String strategyName, String orderType, int pageNo) {
        StockPoolsPar stockPoolsPar = new StockPoolsPar();
        stockPoolsPar.content =  keyword;
        stockPoolsPar.strategyName = strategyName;
        stockPoolsPar.orderSort = orderType;
        F.T2<List<StockpoolDto>, Page> t2 = null;
        if ("hot".equals(stockPoolsPar.strategyName)) {
            t2 = stockPoolService.hotList(stockPoolsPar, pageNo);
        } else {
            t2 = stockPoolService.stockPoolyList(stockPoolsPar, pageNo);
        }
        responseJSON(t2._1, t2._2);
    }

    /**
     * 股票池列表高级搜索
     *
     * @param pageNo 页码
     */
    public static void advanceSearch(int pageNo) {
        String body = fetchBody();
        StockPoolSearchCnd stockPoolSearchCnd = GsonUtil.createWithoutNulls().fromJson(body, StockPoolSearchCnd.class);
        F.T2<List<StockpoolDto>, Page> t2 = stockPoolService.advanceSearch(stockPoolSearchCnd, pageNo);
        responseJSON(t2._1, t2._2);

    }

    /**
     * 获取股票池基本信息
     *
     * @param stockPoolCode 股票池代码
     */
    public static void fetchStockpoolInfo(String stockPoolCode) {
        StockPoolBasicInfoDto stockPoolBasicInfoDto = stockPoolService.strategyContrast(stockPoolCode);
        responseJSON(stockPoolBasicInfoDto);
    }

    /**
     * 查询股票池组合列表信息
     *
     * @param stockPoolCode 股票池代码
     */
    public static void fetchCombinInfo(String stockPoolCode) {
        List<StockPoolCombineInfoDto> result = stockPoolService.queryCombineInfo(stockPoolCode);
        responseJSON(result);
    }

}
