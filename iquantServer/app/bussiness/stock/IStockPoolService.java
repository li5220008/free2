package bussiness.stock;

import models.iquantCommon.*;
import play.libs.F;
import util.Page;

import java.util.List;
import java.util.Set;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-7-3
 * Time: 下午5:36
 * 功能描述:
 */
public interface IStockPoolService {
    /**
     * @param spp    参数对象类
     * @param pageNo 当前页
     * @return_1 为结果集, _2为分页对象
     */
    public F.T2<List<StockpoolDto>, Page> stockPoolyList(StockPoolsPar spp, int pageNo, Long uid);

    /**
     * 高级搜索
     *
     * @param cnd
     * @param pageNo 当前页
     * @return _1 为结果集, _2 page分页对象
     */
    public F.T2<List<StockpoolDto>, Page> advanceSearch(StockPoolSearchCnd cnd, int pageNo, Long uid);
    /**
     * 得到股票池组合列表信息
     * @param stockpoolcode
     * @return
     */
    public  List<StockPoolCombineInfoDto> queryCombineInfo(String stockpoolcode);

    /**
     * 此方法用于根据spid和uid判断该股票池是否被收藏
     * @param spid
     * @param uid
     * @return
     */
    public  boolean iscollect(String spid, Long uid) ;
    /**
     * User: 刘建力(liujianli@gtadata.com))
     * Date: 13-7-3
     * Time: 下午5:50
     * 功能描述: 从show中迁移过来 股票池基本信息
     */
    public StockPoolBasicInfoDto strategyContrast(String stockPoolCode);
    /**
     * 从股票池列表中查询已收藏的股票池
     *
     * @param ids 股票池id列表
     * @param uid  用户id
     * @return List<Map<String,Object>>
     */
    public Set<Integer> queryStockPoolCollectMap(Long[] ids, Long uid) ;

    /**
     * @param spp    参数类对象
     * @param pageNo 当前页
     * @return_1 为结果集, _2为分页对象
     */
    public  F.T2<List<StockpoolDto>, Page> stockPoolyList(StockPoolsPar spp, int pageNo);

    /**
     * 热门收索
     * @param spp    参数类对象
     * @param pageNo 当前页
     * @return_1 为结果集, _2为分页对象
     */
    public  F.T2<List<StockpoolDto>, Page> hotList(StockPoolsPar spp, int pageNo) ;
    public  List<StockpoolDto> getStpStarLevelAndHot(String[] stockPoolIds);

    /**
     * 高级搜索
     *
     * @param cnd
     * @param pageNo 当前页
     * @return _1 为结果集, _2 page分页对象
     */
    public  F.T2<List<StockpoolDto>, Page> advanceSearch(StockPoolSearchCnd cnd, int pageNo);
}
