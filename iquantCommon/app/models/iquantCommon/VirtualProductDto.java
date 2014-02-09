package models.iquantCommon;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * 虚拟产品
 * User: wenzhihong
 * Date: 13-5-2
 * Time: 下午6:50
 */
public class VirtualProductDto extends BaseUserResourceDtoSupport {
    public String name;

    public String uuid;

    //包含的资金帐号id
    public List<TradeAccountDto> tradeAccounts = Lists.newLinkedList();

    //包含的策略id
    public List<Long> strategyIds = Lists.newLinkedList();
}
