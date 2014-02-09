package models.iquantCommon.productinfo;

import com.google.common.collect.Lists;
import com.google.gson.annotations.Expose;
import models.iquantCommon.BaseUserResourceDtoSupport;
import org.apache.commons.lang.math.RandomUtils;

import java.util.List;

/**
 * 产品策略使用帐号dto
 * User: wenzhihong
 * Date: 13-9-20
 * Time: 下午4:08
 */
public class ProductStrategyAccountDto extends BaseUserResourceDtoSupport {
    @Expose
    public Long productStrategyId; //产品策略id. 也就是策略实例id
    @Expose
    public Long accountId; // 帐号id
    @Expose
    public String name;
    //类型
    @Expose
    public int type;
    //账号
    @Expose
    public String account;
    @Expose
    public int accountOrder; //交易帐号序号

    public static List<ProductStrategyAccountDto> demo(int i) {
        ProductStrategyAccountDto o1 = new ProductStrategyAccountDto();
        o1.accountId = (long)i;
        o1.accountOrder = 1;
        return Lists.newArrayList(o1);
    }
}
