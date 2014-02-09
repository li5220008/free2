package models.iquantCommon;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 策略超市 策略对比表格数据展示
 * User: liuhongjiang
 * Date: 12-11-8
 * Time: 上午11:26
 */
public class StrategyContrastDto {
    //机构名称
    @Expose
    @SerializedName("name")
    public String name;
    //收益率
    @SerializedName("Yield")
    @Expose
    public BigDecimal yield;
    //月化收益率
    @Expose
    @SerializedName("YieldOfMonth")
    public  BigDecimal yieldOfMonth;
    //年化收益率
    @Expose
    @SerializedName("YieldOfYear")
    public Double yieldOfYear;
    //盈利次数
    @Expose
    @SerializedName("ProfitCount")
    public Integer profitCount;
    //胜率
    @Expose
    @SerializedName("ProfitRatio")
    public Double profitRatio;
    //单次最大盈利
    @Expose
    @SerializedName("MAXSingleProfit")
    public BigDecimal maxSingleProfit;
    //单次最大亏损
    @Expose
    @SerializedName("MAXSingleDeficit")
    public BigDecimal maxSingleDeficit;
    //月度收益率标准差
    @Expose
    @SerializedName("YieldOfMonthStandardDeviation")
    public BigDecimal yieldOfMonthSD;
    //夏普比率 (夏普指数)
    @Expose
    @SerializedName("SharpeIndex")
    public Double sharpeIndex;
    //总交易天数
    @Expose
    @SerializedName("TradeDays")
    public Integer tradeDays;
    //控制页面策略名称颜色
    @Expose
    public String color;
    //交易类型
    @Expose
    public Integer tradeType;
    //交易品种
    @Expose
    public Integer tradeVariety;
    //上架时间
    @Expose
    public Date upTime;
    //策略引擎类型 1:qic,2:qia
    @Expose
    public int enginetypeId;
    //评论总人数
    @Expose
    public int discussCount;
    // 收藏总人数
    @Expose
    public int collectCount;
    //订阅总人数
    @Expose
    public int orderCount;
    //当前订阅人数
    @Expose
    public long currentOrder;
    //星级
    @Expose
    public float starLevel;

}
