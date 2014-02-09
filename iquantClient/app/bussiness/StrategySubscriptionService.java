package bussiness;

import models.iquantCommon.StrategyBaseDto;
import models.iquantCommon.StrategySearchCnd;
import play.libs.F;
import protoc.HttpBody;
import protoc.ResponseHeader;
import protoc.URILib;
import protoc.parser.ActionResult;
import util.GsonUtil;
import util.Page;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的订阅策略
 * User: liangbing
 * Date: 13-7-8
 * Time: 下午3:18
 */
public class StrategySubscriptionService extends BasicService{


    /**
     * @param sortType 分类搜索
     * @param keyword  关键字
     * @param pageNo   当前页数
     * @return _1 为结果集, _2为 分页page信息,
     */
    public static F.T2<List<StrategyBaseDto>, Page> strategyList(int sortType, String keyword, int pageNo, Long uid) {

        List<StrategyBaseDto> strategyBaseDtoList = new ArrayList<StrategyBaseDto>();
        Page page = null;
        String url = URILib.FETCH_SUBSCRIPTION_STRATEGY_BASEINFO;
        ActionResult<List<StrategyBaseDto>> strategyBaseDtoActionResult = remoteRequestService.getList(url, StrategyBaseDto.class, sortType, keyword, pageNo, uid);
        if (strategyBaseDtoActionResult != null) {
            ResponseHeader responseHeader = strategyBaseDtoActionResult.header;
            strategyBaseDtoList = strategyBaseDtoActionResult.data;
            page = new Page(responseHeader.total, pageNo);
        }
        return F.T2(strategyBaseDtoList, page);
    }


    /**
     * 高级搜索
     *
     * @param cnd
     * @param pageNo 当前页
     * @return _1 为结果集, _2为总条数, _3 总共页数
     */
    public static F.T2<List<StrategyBaseDto>, Page> advanceSearch(StrategySearchCnd cnd, int sortType, int pageNo, Long uid) {
        List<StrategyBaseDto> strategyBaseDtoList = new ArrayList<StrategyBaseDto>();
        Page page = null;
        String json = GsonUtil.createWithoutNulls().toJson(cnd);
        String url = URILib.FETCH_SUBSCRIPTION_ADVANCE_SEARCH_LIST;
        HttpBody httpBody = new HttpBody();
        httpBody.body = json;
        ActionResult<List<StrategyBaseDto>> strategyBaseDtoActionResult = remoteRequestService.getList(url, httpBody, StrategyBaseDto.class,sortType, pageNo, uid);
        if (strategyBaseDtoActionResult != null) {
            ResponseHeader responseHeader = strategyBaseDtoActionResult.header;
            strategyBaseDtoList = strategyBaseDtoActionResult.data;
            page = new Page(responseHeader.total, pageNo);
        }

        return F.T2(strategyBaseDtoList, page);
    }
}
