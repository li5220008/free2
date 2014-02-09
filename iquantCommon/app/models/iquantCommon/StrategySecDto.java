package models.iquantCommon;

/**
 * 策略里的标的信息
 * User: wenzhihong
 * Date: 13-5-9
 * Time: 下午3:45
 */
public class StrategySecDto {

    //合约乘数
    public Double contractMultiplier;

    //保证金比例
    public Double marginLevel;

    //最大持仓量
    public Double maxShare;

    //币种
    public String currency;

    //市场类型
    public String exchangeType;

    //标的id
    public String secId;

    //标的名称
    public String secName;
}
