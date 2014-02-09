package models.iquantCommon;

import com.google.gson.annotations.SerializedName;

/**
 * 策略列表信息
 * User: liangbing
 * Date: 13-2-21
 * Time: 下午2:56
 */
public class StrategyInfoTableDto {


    public String id;
    public String uuid;
    //策略名称
    @SerializedName("sname")
    public String sname;
    //策略提供者
    @SerializedName("provider")
    public String provider;
    //交易品种
    @SerializedName("tradeVariety")
    public Integer tradeVariety;
    //策略类型
    @SerializedName("stype")
    public Integer stype;
    //净利润
    @SerializedName("retainedProfits")
    public float retainedProfits;
    //收益率
    @SerializedName("yield")
    public float yield;
    //月化收益率
    @SerializedName("yieldOfMonth")
    public float yieldOfMonth;
    //年化收益率
    @SerializedName("yieldOfYear")
    public float yieldOfYear;
    //月化收益率标准差
    @SerializedName("yomsd")
    public float yomsd;
    //夏普指数
    @SerializedName("sharpeIndex")
    public float sharpeIndex;
    //总盈利
    @SerializedName("overallProfitability")
    public float overallProfitability;
    //总亏损
    @SerializedName("overallDeficit")
    public float overallDeficit;
    //交易次数
    @SerializedName("tradeCount")
    public int tradeCount;
    //盈利次数
    @SerializedName("profitCount")
    public int profitCount;
    public int deficitCount;//亏损次数
    public float maxSingleProfit;//单次最大盈利
    public float maxSingleDeficit;//单次最大亏损
    public int tradeDays;//总交易天数
    public int maxShortPositionTime;//最大空仓时间
    public int longPositionTradeCount;//多头交易次数
    public int shortPositionTradeCount;//空头交易次数
    public int positionCloseCount;//持平次数
    public int maxSequentialProfitCount;//最大连续盈利次数
    public int maxSequentialDeficitCount;//最大连续亏损次数
    public float profitRatio;//盈利比率(胜率)
    public float canhsiedRatio;//盈亏比
    public float maxSingleProfitRatio;//最大盈利/总盈利
    public float maxSingleDeficitRatio;//最大亏损/总亏损
    public float maxSequentialDeficitCapital;//最大连续亏损额
    public float sumOfCommission;//手续费合计
    public float grossProfit;//毛利润
    public float profitLossRatio;//净利润/单次最大亏损
    public float avgProfitOfMonth;//月度平均盈利
    public float floatingProfitAndLoss;//浮动盈亏

}






