package bussiness;

import models.iquantCommon.StockPoolCombineInfoDto;
import protoc.URILib;
import protoc.parser.ActionResult;

import java.util.List;

/**
 * 股票池组合列表信息查看
 * User: panzhiwei
 * Date: 12-11-21
 * Time: 上午10:48
 * To change this template use File | Settings | File Templates.
 */
public class StockPoolCombineInfoService extends BasicService {
    /**
     * 得到股票池组合列表信息
     *
     * @param stockPoolCode
     * @return
     */
    public static List<StockPoolCombineInfoDto> queryCombineInfo(String stockPoolCode) {
        ActionResult<List<StockPoolCombineInfoDto>> result = remoteRequestService.getList(URILib.FETCH_STOCKPOOLCOMBINEINFO_LIST, StockPoolCombineInfoDto.class, stockPoolCode);
        List<StockPoolCombineInfoDto> stockPoolCombineInfoDtoList = result.data;
        return stockPoolCombineInfoDtoList;
    }

    /**
     * 此方法用于根据spid和uid判断该股票池是否被收藏
     *
     * @param spid
     * @param uid
     * @return
     */
    public static boolean iscollect(String spid, Long uid) {
        ActionResult<String> result = remoteRequestService.getSingleValue(URILib.JUDGESTOCKPOOLISCOLLECT, spid,uid);

        if ("false".equals(result.data)) {
            return false;
        } else {
            return true;
        }
    }
}
