package bussiness;

import models.iquantCommon.StockPoolBasicInfoDto;
import protoc.URILib;
import protoc.parser.ActionResult;


import java.math.BigDecimal;
import java.util.Map;

/**
 * 股票池基本信息处理类
 * User: liuhongjiang
 * Date: 12-11-19
 * Time: 下午2:13
 */
public class StockPoolBasicInfoService extends BasicService{
    //股票池基本展示
    public static StockPoolBasicInfoDto strategyContrast(String stockPoolCode){
        ActionResult<StockPoolBasicInfoDto> result = remoteRequestService.getBean(URILib.FETCH_STOCKPOOLBASICINFO, StockPoolBasicInfoDto.class, stockPoolCode);
        StockPoolBasicInfoDto stockPoolBasicInfoDto = result.data;
        return stockPoolBasicInfoDto;
    }

}
