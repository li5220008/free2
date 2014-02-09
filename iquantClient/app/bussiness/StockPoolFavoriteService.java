package bussiness;

import com.google.gson.Gson;
import models.iquantCommon.StockPoolSearchCnd;
import models.iquantCommon.StockPoolsPar;
import models.iquantCommon.StockpoolDto;
import play.libs.F;
import protoc.HttpBody;
import protoc.Protocol;
import protoc.ResponseHeader;
import protoc.URILib;
import protoc.parser.ActionResult;
import util.Page;

import java.util.List;

/**
 * 股票池组合浏览基本业务类 我的收藏
 * User: liangbing
 * Date: 12-11-23
 * Time: 上午11:06
 * To change this template use File | Settings | File Templates.
 */
public class StockPoolFavoriteService extends BasicService {


    /**
     * @param spp    参数对象类
     * @param pageNo 当前页
     * @return_1 为结果集, _2为分页对象
     */
    public static F.T2<List<StockpoolDto>, Page> stockPoolyList(StockPoolsPar spp, int pageNo, Long uid) {
        String keyword = spp.content;
        int flag = spp.flag;
        String strategyName = spp.strategyName;
        String orderType = spp.orderSort;

        ActionResult<List<StockpoolDto>> result = remoteRequestService.getList(URILib.FETCH_COLLECTED_STOCKPOOL_LIST, StockpoolDto.class,keyword,strategyName,orderType,pageNo,uid,flag);
        ResponseHeader responseHeader = result.header;
        List<StockpoolDto> sbdList = null;
        Page page = new Page(responseHeader.total, pageNo);
        if (responseHeader.status == Protocol.STATU_SSUCCESS) {
            sbdList = result.data;
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
    public static F.T2<List<StockpoolDto>, Page> advanceSearch(StockPoolSearchCnd cnd, int pageNo, Long uid) {
        Gson gson = new Gson();
        String json = gson.toJson(cnd);
        ActionResult<List<StockpoolDto>> result = remoteRequestService.getList(URILib.FETCH_STOCKPOOL_COLLECTION_ADVANCESEARCH_LIST
                , new HttpBody(json), StockpoolDto.class,  pageNo,uid);
        ResponseHeader responseHeader = result.header;
        int status = responseHeader.status;
        List<StockpoolDto> StockpoolList = null;
        if (status == Protocol.STATU_SSUCCESS) {
            StockpoolList = result.data;
        }
        Page page = new Page(responseHeader.total, pageNo);
        return F.T2(StockpoolList, page);
    }

}
