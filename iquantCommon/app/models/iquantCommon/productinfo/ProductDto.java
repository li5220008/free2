package models.iquantCommon.productinfo;

import com.google.gson.annotations.Expose;
import models.iquantCommon.BaseUserResourceDtoSupport;

import java.util.Date;
import java.util.List;

/**
 * 交易设置里的产品
 * User: wenzhihong
 * Date: 13-9-20
 * Time: 下午3:48
 */
public class ProductDto extends BaseUserResourceDtoSupport {
    @Expose
    public String name;
    @Expose
    public String uuid;
    @Expose
    public Date ctime;
    @Expose
    public List<RiskSecurityDto> riskStocks;
    @Expose
    public List<ProductStrategyDto> strategys;


    public static ProductDto demo(){
        ProductDto productDto = new ProductDto();
        productDto.name = "我的产品1";
        productDto.riskStocks = RiskSecurityDto.demo();
        productDto.strategys = ProductStrategyDto.demo();


        return productDto;
    }

}
