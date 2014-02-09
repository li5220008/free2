package controllers;

import annotation.QicFunction;
import bussiness.StockPoolBasicInfoService;
import bussiness.StockPoolCombineInfoService;
import bussiness.StockPoolDiscussService;

import models.iquantCommon.StockPoolBasicInfoDto;
import models.iquantCommon.StockPoolCombineInfoDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: liuhongjiang
 * Date: 12-11-19
 * Time: 下午2:30
 */

public class StockPoolBasic extends BasePlayControllerSupport {


    //出入股票池编码 得到股票池基本信息对象
    @QicFunction(id=9)
    public static void basicInfo(String stockPoolCode, Long uid) {
        StockPoolBasicInfoDto stockPoolBasicInfo = StockPoolBasicInfoService.strategyContrast(stockPoolCode);
        int is_comment = StockPoolDiscussService.judge(stockPoolCode, uid);

        List<StockPoolCombineInfoDto> stockPoolCombineInfoDtoList = StockPoolCombineInfoService.queryCombineInfo(stockPoolCode);
        //
        Boolean iscollect = StockPoolCombineInfoService.iscollect(stockPoolCode, uid);
        render(stockPoolBasicInfo, is_comment, stockPoolCombineInfoDtoList, iscollect, stockPoolCode);
    }

    /**
     * 保存评论
     *
     * @param uid
     * @param spid
     * @param star
     */
    @QicFunction(id=9)
    public static void comment(Long uid, Long spid, int star) {
        boolean  flag =false;
        Map<String, Object> json = new HashMap<String, Object>();
        flag =   StockPoolDiscussService.saveDiscuss(star, uid, spid);
        json.put("commented", flag);
        renderJSON(json);
    }
    @QicFunction(id=9)
    public static void digest(String stockPoolCode) {
        // stockPoolCode ="1" ;
        StockPoolBasicInfoDto stockPoolBasicInfo = StockPoolBasicInfoService.strategyContrast(stockPoolCode);
        render(stockPoolBasicInfo);
    }
}
