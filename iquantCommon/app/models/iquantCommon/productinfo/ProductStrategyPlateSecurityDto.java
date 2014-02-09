package models.iquantCommon.productinfo;

import com.google.common.collect.Lists;
import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * 产品策略板块树
 * User: wenzhihong
 * Date: 13-9-20
 * Time: 下午4:16
 */
public class ProductStrategyPlateSecurityDto {
    @Expose
    public Long id;
    @Expose
    public Long plateId; //板块代码
    @Expose
    public String symbol;
    @Expose
    public String market;
    @Expose
    public int status; //0正常，1屏蔽

    public static List<ProductStrategyPlateSecurityDto> demo(){
        ProductStrategyPlateSecurityDto o1 = new ProductStrategyPlateSecurityDto();
        o1.symbol = "000002";
        o1.market = "SSE";
        o1.status = 0;

        ProductStrategyPlateSecurityDto o2 = new ProductStrategyPlateSecurityDto();
        o2.symbol = "000003";
        o2.market = "SSE";
        o2.status = 1;

        ProductStrategyPlateSecurityDto o3 = new ProductStrategyPlateSecurityDto();
        o3.symbol = "000004";
        o3.market = "SSE";
        o3.status = 1;

        ProductStrategyPlateSecurityDto o5 = new ProductStrategyPlateSecurityDto();
        o5.symbol = "000005";
        o5.market = "SSE";
        o5.status = 0;

        return Lists.newArrayList(o1, o2, o3, o5);
    }
}
