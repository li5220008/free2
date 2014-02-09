package models.iquantCommon;

import com.google.gson.annotations.Expose;

/**
 * 资金帐号
 * User: wenzhihong
 * Date: 13-5-2
 * Time: 下午6:52
 */
public class TradeAccountDto extends BaseUserResourceDtoSupport {
    @Expose
    public String name;
    @Expose
    public String account;
    @Expose
    public String password;

    //是否使用. 0: 没有使用, 1: 使用
    @Expose
    public Integer used;

    //账户类型   0:期货, 1:股票
    @Expose
    public Integer type;

    //交易柜台
    @Expose
    public String clientId ;

    //期货公司客户号ID 由外部提供
    @Expose
    public String targetCompId;

    //投机/套保标识. 投机(Speculation)/套保(Hedge)
    // 0. 投机(Speculation)
    // 1. 套保(Hedge)
    @Expose
    public Integer hedgeType;

    //交易账号序号
    @Expose
    public Integer accountOrder;

    //初始资金
    public Double initCapital;

}
