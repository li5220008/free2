package models.iquantCommon.productinfo;

import com.google.common.collect.Lists;
import com.google.gson.annotations.Expose;
import models.iquantCommon.BaseUserResourceDtoSupport;

import java.util.List;

/**
 * 产品策略板块信息
 * User: wenzhihong
 * Date: 13-9-20
 * Time: 下午4:14
 */
public class ProductStrategyPlateDto extends BaseUserResourceDtoSupport {
    @Expose
    public Long productStrategyId; //产品策略id. 也就是策略实例id
    @Expose
    public String name;
    @Expose
    public double maxPosition; //最大持仓量
    @Expose
    public double systemPlateId; //系统板块id
    @Expose
    public int traceSystemPlate; //是否跟踪系统板块.0不跟踪, 1跟踪
    @Expose
    public List<ProductStrategyPlateSecurityDto> plateStocks;

    public static List<ProductStrategyPlateDto> demo() {
        ProductStrategyPlateDto o1 = new ProductStrategyPlateDto();
        o1.name = "自定义板块1";
        o1.maxPosition = 0.456;
        o1.traceSystemPlate = 0;
        o1.systemPlateId = -1;
        o1.plateStocks = ProductStrategyPlateSecurityDto.demo();

        ProductStrategyPlateDto o2 = new ProductStrategyPlateDto();
        o2.name = "系统板块2";
        o2.maxPosition = 0.777;
        o2.systemPlateId = 1012003002017L;
        o2.traceSystemPlate = 1;

        ProductStrategyPlateDto o3 = new ProductStrategyPlateDto();
        o3.name = "系统板块3";
        o3.maxPosition = 0.888;
        o3.systemPlateId = 1012003002086L;
        o3.traceSystemPlate = 1;


        return Lists.newArrayList(o1, o2, o3);
    }
}
