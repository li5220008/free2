package models.iquantCommon.productinfo;

import com.google.common.collect.Lists;
import com.google.gson.annotations.Expose;
import models.iquantCommon.BaseUserResourceDtoSupport;

import java.util.List;

/**
 * 风控证券
 * User: wenzhihong
 * Date: 13-9-20
 * Time: 下午4:02
 */
public class RiskSecurityDto extends BaseUserResourceDtoSupport {
    @Expose
    public Long productId;
    @Expose
    public String symbol;
    @Expose
    public String market;

    public static List<RiskSecurityDto> demo() {
        RiskSecurityDto o1 = new RiskSecurityDto();
        o1.market = "SSE";
        o1.symbol = "000001";

        RiskSecurityDto o2 = new RiskSecurityDto();
        o2.market = "SSE";
        o2.symbol = "000002";


        RiskSecurityDto o3 = new RiskSecurityDto();
        o3.market = "SSE";
        o3.symbol = "000003";


        return Lists.newArrayList(o2, o2, o3);
    }
}
