package bussiness;


import models.iquantCommon.StockpoolDto;
import protoc.URILib;
import protoc.parser.ActionResult;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 股票池收藏
 * User: panzhiwei
 * Date: 12-11-19
 * Time: 上午11:43
 * To change this template use File | Settings | File Templates.
 */
public class StockPoolCollectService extends BasicService {

    /**
     * 从股票池列表中查询已收藏的股票池
     *
     * @param list 股票池列表
     * @param uid  用户id
     * @return List<Map<String,Object>>
     */
    public static Set<Integer> queryStockPoolCollectMap(List<StockpoolDto> list, Long uid) {
        String ids = "";
        for (StockpoolDto stockpoolDto : list) {
            ids += stockpoolDto.id + ",";
        }
        if(ids.length()>0){
            ids = ids.substring(0, ids.length() - 1);
        }
        Set<Integer> setResult = new HashSet<Integer>();
        ActionResult<List<String>> result = remoteRequestService.getListWithoutField(URILib.FETCH_STOCKPOOLCOLLECT_LIST, ids, uid);
        List<String> list2 = result.data;
        for (String s : list2) {
            setResult.add(Integer.parseInt(s));
        }
        return setResult;
    }

    /**
     * 股票池收藏
     *
     * @param uid  用户id
     * @param spid 股票池id
     */
    public static void stockpooladdcollect(long uid, long spid) throws Exception {
        ActionResult result = remoteRequestService.getJustHeader(URILib.ADD_STOCKPOOLCOLLECT, uid, spid);
    }

    /**
     * 股票池取消收藏
     *
     * @param uid  用户id
     * @param spid 股票池id
     */
    public static void stockpooldeletecollect(long uid, long spid) throws Exception {
        ActionResult result = remoteRequestService.getJustHeader(URILib.DELETE_STOCKPOOLCOLLECT, uid, spid);
    }

}
