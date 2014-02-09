package models.iquantCommon.productinfo;

import com.google.common.collect.Lists;
import com.google.gson.annotations.Expose;
import models.iquantCommon.BaseUserResourceDtoSupport;

import java.util.List;

/**
 * 产品策略dto
 * User: wenzhihong
 * Date: 13-9-20
 * Time: 下午4:00
 */
public class ProductStrategyDto extends BaseUserResourceDtoSupport {
    //产品id
    @Expose
    public Long productId;

    //策略id
    @Expose
    public Long strategyId;
    //策略名称
    @Expose
    public String name;

    //资金使用比例
    @Expose
    public Double fundsProportion;
    @Expose
    public List<ProductStrategyAccountDto> accounts;
    @Expose
    public List<ProductStrategyPlateDto> plates;
    @Expose
    public List<ProductStrategySecurityDto> securities;


    public static List<ProductStrategyDto> demo() {
        ProductStrategyDto o1 = new ProductStrategyDto();
        o1.strategyId = 1L;
        o1.fundsProportion = 0.24;
        o1.accounts = ProductStrategyAccountDto.demo(1);
        o1.plates = ProductStrategyPlateDto.demo();
        o1.securities = ProductStrategySecurityDto.demo();

        ProductStrategyDto o2 = new ProductStrategyDto();
        o2.strategyId = 2L;
        o2.fundsProportion = 0.56;
        o2.accounts = ProductStrategyAccountDto.demo(2);
        o2.plates = ProductStrategyPlateDto.demo();
        o2.securities = ProductStrategySecurityDto.demo();


        ProductStrategyDto o3 = new ProductStrategyDto();
        o3.strategyId = 3L;
        o3.fundsProportion = 0.99;
        o3.accounts = ProductStrategyAccountDto.demo(3);
        o3.plates = ProductStrategyPlateDto.demo();
        o3.securities = ProductStrategySecurityDto.demo();

        return Lists.newArrayList(o1);
    }
}
