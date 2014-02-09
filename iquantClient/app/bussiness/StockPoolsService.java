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

import java.util.ArrayList;
import java.util.List;

import static protoc.URILib.FETCH_STOCKPOOLHOT_LIST;
import static protoc.URILib.FETCH_STOCKPOOL_LIST;

/**
 * 股票池组合浏览基本业务类
 * User: liangbing
 * Date: 12-11-19
 * Time: 下午1:24
 * To change this template use File | Settings | File Templates.
 */
public class StockPoolsService extends BasicService{
    /**
     * @param spp    参数类对象
     * @param pageNo 当前页
     * @return_1 为结果集, _2为分页对象
     */
    public static F.T2<List<StockpoolDto>, Page> stockPoolyList(StockPoolsPar spp, int pageNo) {
        String keyword = spp.content;
        int flag = spp.flag;
        String strategyName = spp.strategyName;
        String orderType = spp.orderSort;
        ActionResult<List<StockpoolDto>> result = remoteRequestService.getList(FETCH_STOCKPOOL_LIST,StockpoolDto.class, keyword,strategyName,orderType, pageNo,flag);
        ResponseHeader responseHeader = result.header;
        int status = responseHeader.status;
        List<StockpoolDto> sbdList = null;
        if (status == Protocol.STATU_SSUCCESS) {
            sbdList = result.data;
        }
        Page page = new Page(responseHeader.total, pageNo);
        return F.T2(sbdList, page);

    }

    /**
     * 热门收索
     * @param spp    参数类对象
     * @param pageNo 当前页
     * @return_1 为结果集, _2为分页对象
     */
    public static F.T2<List<StockpoolDto>, Page> hotList(StockPoolsPar spp, int pageNo) {

        String keyword = spp.content;
        int flag = spp.flag;
        String strategyName = spp.strategyName;
        String orderType = spp.orderSort;

        ActionResult<List<StockpoolDto>> result = remoteRequestService.getList(FETCH_STOCKPOOLHOT_LIST,StockpoolDto.class, keyword,strategyName, orderType, pageNo);
        ResponseHeader responseHeader = result.header;
        int status = responseHeader.status;
        List<StockpoolDto> sbdList = new ArrayList<StockpoolDto>();
        if (status == Protocol.STATU_SSUCCESS) {
            sbdList = result.data;
        }
        Page page = new Page(20, pageNo);
        return F.T2(sbdList, page);
    }

   /* public static List<StockpoolDto> getStpStarLevelAndHot(String[] stockPoolIds){
        String sql = SqlLoader.getSqlById("getStpStarLevelAndHot");
        sql +=" (";
        for(int i = 0 ; i< stockPoolIds.length;i++){
            sql += ( (i< stockPoolIds.length - 1 ) ?  "?," : "?");
        }
        sql +=")";
        List<StockpoolDto> list = QicDbUtil.queryQicDBBeanList(sql,StockpoolDto.class,stockPoolIds);
        return list;
    }
*/
    /**
     * 高级搜索
     *
     * @param cnd
     * @param pageNo 当前页
     * @return _1 为结果集, _2 page分页对象
     */
    public static F.T2<List<StockpoolDto>, Page> advanceSearch(StockPoolSearchCnd cnd, int pageNo) {
        Gson gson = new Gson();
        String json = gson.toJson(cnd);
        ActionResult<List<StockpoolDto>> result = remoteRequestService.getList(URILib.FETCH_STOCKPOOLADVANCESEARCH_LIST, new HttpBody(json), StockpoolDto.class,pageNo);
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
