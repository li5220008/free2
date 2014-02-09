package models.iquantCommon;

import com.google.gson.annotations.Expose;

/**
 * 策略搜索条件dto
 * User: wenzhihong
 * Date: 12-11-11
 * Time: 上午9:59
 */
public class StrategySearchCnd {
    //交易类型
    @Expose
    public Integer tradeType;

    //交易品种
    @Expose
    public Integer tradeVariety;

    //收益率 小值
    @Expose
    public Float yieldDown;

    //收益率 大值
    @Expose
    public Float yieldUp;

    //获胜率 小值
    @Expose
    public Float profitRatioDown;

    //获胜率 大值
    @Expose
    public Float profitRatioUp;

    //用户评级 小值
    @Expose
    public Integer starDown;

    //用户评级 大值
    @Expose
    public Integer starUp;
}
