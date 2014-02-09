package models.iquantCommon.productinfo;

import com.google.common.collect.Lists;
import com.google.gson.annotations.Expose;
import models.iquantCommon.BaseUserResourceDtoSupport;

import java.util.List;

/**
 * User: wenzhihong
 * Date: 13-9-20
 * Time: 下午4:11
 */
public class ProductStrategySecurityDto extends BaseUserResourceDtoSupport {
    @Expose
    public Long productStrategyId; //产品策略id. 也就是策略实例id
    @Expose
    public String symbol;
    @Expose
    public String market;
    @Expose
    public double maxPosition; //最大持仓量

    public static List<ProductStrategySecurityDto> demo() {
        ProductStrategySecurityDto o1 = new ProductStrategySecurityDto();
        o1.symbol = "600000";
        o1.market = "SSH";
        o1.maxPosition = 10000;

        ProductStrategySecurityDto o2 = new ProductStrategySecurityDto();
        o2.symbol = "600001";
        o2.market = "SSH";
        o2.maxPosition = 10230;

        ProductStrategySecurityDto o3 = new ProductStrategySecurityDto();
        o3.symbol = "600004";
        o3.market = "SSH";
        o3.maxPosition = 10400;

        return Lists.newArrayList(o1, o2, o3);
    }
}
