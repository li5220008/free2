package models.iquantCommon;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * 绩效指标
 * User: panzhiwei
 * Date: 12-11-13
 * Time: 下午4:29
 * To change this template use File | Settings | File Templates.
 */
public class IndicatorDto {
    @Expose
    public String id;
    @Expose
    public String uuid;
    //净利润
    @SerializedName("retainedProfits")
    @Expose
    public Float retainedProfits;
    //收益率
    @SerializedName("yield")
    @Expose
    public Float yield;
    //月化收益率
    @SerializedName("yieldOfMonth")
    @Expose
    public Float yieldOfMonth;
    //月度收益标准差
    @SerializedName("yieldOfMonthStandardDeviation")
    @Expose
    public Float yieldOfMonthStandardDeviation;
    //总盈利
    @SerializedName("overallProfitability")
    @Expose
    public Float overallProfitability;
    //总亏损
    @SerializedName("overallDeficit")
    @Expose
    public Float overallDeficit;
    //总交易天数
    @SerializedName("tradeDays")
    @Expose
    public int tradeDays;
    //最大空仓时间
    @SerializedName("mAXShortPositionTime")
    @Expose
    public int mAXShortPositionTime;
    //交易次数
    @SerializedName("tradeCount")
    @Expose
    public int tradeCount;
    //多头交易次数
    @SerializedName("longPositionTradeCount")
    @Expose
    public int longPositionTradeCount;
    //空头交易次数
    @SerializedName("shortPositionTradeCount")
    @Expose
    public int shortPositionTradeCount;
    //盈利次数
    @SerializedName("profitCount")
    @Expose
    public int profitCount;
    //亏损次数
    @SerializedName("deficitCount")
    @Expose
    public int deficitCount;
    //持平次数
    @SerializedName("positionCloseCount")
    @Expose
    public int positionCloseCount;
    //最大连续盈利次数
    @SerializedName("mAXSequentialProfitCount")
    @Expose
    public int mAXSequentialProfitCount;
    //最大连续亏损次数
    @SerializedName("mAXSequentialDeficitCount")
    @Expose
    public int mAXSequentialDeficitCount;
    //盈利比率
    @SerializedName("profitRatio")
    @Expose
    public Float profitRatio;
    //总盈利/总亏损
    @SerializedName("canhsiedRatio")
    @Expose
    public Float canhsiedRatio;
    //单词最大盈利
    @SerializedName("mAXSingleProfit")
    @Expose
    public Float mAXSingleProfit;
    //单词最大亏损
    @SerializedName("mAXSingleDeficit")
    @Expose
    public Float mAXSingleDeficit;
    //最大盈利/总盈利
    @SerializedName("mAXSingleProfitRatio")
    @Expose
    public Float mAXSingleProfitRatio;
    //最大亏损/总亏损
    @SerializedName("mAXSingleDeficitRatio")
    @Expose
    public Float mAXSingleDeficitRatio;
    //最大连续亏损额
    @SerializedName("mAXSequentialDeficitCapital")
    @Expose
    public Float mAXSequentialDeficitCapital;
    //手续费合计
    @SerializedName("sumOfCommission")
    @Expose
    public Float sumOfCommission;
    //毛利润
    @SerializedName("grossProfit")
    @Expose
    public Float grossProfit;
    //净利润/单次最大亏损
    @SerializedName("profitLossRatio")
    @Expose
    public Float profitLossRatio;
    //月度平均盈利
    @SerializedName("avgProfitOfMonth")
    @Expose
    public Float avgProfitOfMonth;
    //年化收益率
    @SerializedName("yieldOfYear")
    @Expose
    public Float yieldOfYear;
    //夏普指数
    @SerializedName("sharpeIndex")
    @Expose
    public Float sharpeIndex;
    //浮动盈亏
    @SerializedName("floatingProfitAndLoss")
    @Expose
    public Float floatingProfitAndLoss;
    //滑价成本
    @SerializedName("movingCost")
    @Expose
    public Float movingCost;

}

