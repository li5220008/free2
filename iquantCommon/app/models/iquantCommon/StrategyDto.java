package models.iquantCommon;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * 策略信息Dto
 * User: liangbing
 * Date: 12-12-10
 * Time: 下午2:21
 * 页面展示数据
 */
public class StrategyDto extends BaseDtoSupport {
    public static final int QICORE_ENGINEE_ID=1;
    public static final int QIA_ENGINEE_ID=2;

    //策略id
    @Expose
    public String strategyId;
    //状态
    @Expose
    @SerializedName("status")
    public int status;

    //策略名称
    @Expose
    @SerializedName("sname")
    public String sname;

    //策略提供者
    @Expose
    @SerializedName("provider")
    public String provider;

    //交易品种
    @Expose
    @SerializedName("tradeVariety")
    public Integer tradeVariety;

    //策略类型
    @Expose
    @SerializedName("stype")
    public Integer stype;

    //净利润
    @Expose
    @SerializedName("retainedProfits")
    public Float retainedProfits;

    //收益率
    @Expose
    @SerializedName("yield")
    public Float yield;

    //年化收益率
    @Expose
    @SerializedName("yieldOfYear")
    public Float yieldOfYear;

    //月化收益率标准差
    @Expose
    @SerializedName("yomsd")
    public Float yomsd;

    //总盈利
    @Expose
    @SerializedName("overallProfitability")
    public Float overallProfitability;

    //总亏损
    @Expose
    @SerializedName("overallDeficit")
    public Float overallDeficit;

    //月化收益率
    @Expose
    @SerializedName("yieldOfMonth")
    public Float yieldOfMonth;

    //夏普指数
    @Expose
    @SerializedName("sharpeIndex")
    public Float sharpeIndex;

    //交易次数
    @Expose
    @SerializedName("tradeCount")
    public int tradeCount;

    //盈利次数
    @SerializedName("profitCount")
    public int profitCount;

    //收藏次数
    @Expose
    @SerializedName("collectCount")
    public int collectCount;

    //当前订阅总数
    @Expose
    @SerializedName("subscriber")
    public int subscriber;

    //订阅最迟到期时间
    @Expose
    @SerializedName("endDate")
    public Date endDate;

    //总订阅数
    @Expose
    @SerializedName("allSubscriber")
    public int allSubscriber;
    //策略提供者信息简介
    @Expose
    public String providerDesp;
    //策略简介
    @Expose
    public String desp;
    //下架时间
    @Expose
    public Date downTime;
    //上传时间
    @Expose
    public Date uploadTime;
    //回测开始时间
    @Expose
    public String lookbackStime;
    //回测结束时间
    @Expose
    public String lookbackEtime;
    //回测开始时间
    @Expose
    public String customerLookbackStartTime;
    //回测结束时间
    @Expose
    public String customerLookbackEndTime;
    //数据回传时间
    @Expose
    public String backTestDataSyncTime;
    //上架时间
    @Expose
    public Date upTime;
    //上架时间
    @Expose
    public Date delTime;
    //
    public String getLanguange(){
       return  this.enginetypeId == 1 ?"QICORE" :"QIA";
    }
    public int deficitCount;//亏损次数
    public Float maxSingleProfit;//单次最大盈利
    public Float maxSingleDeficit;//单次最大亏损
    public int tradeDays;//总交易天数
    public int maxShortPositionTime;//最大空仓时间
    public int longPositionTradeCount;//多头交易次数
    public int shortPositionTradeCount;//空头交易次数
    public int positionCloseCount;//持平次数
    public int maxSequentialProfitCount;//最大连续盈利次数
    public int maxSequentialDeficitCount;//最大连续亏损次数
    @Expose
    public Float profitRatio;//盈利比率(胜率)
    public Float canhsiedRatio;//盈亏比
    public Float maxSingleProfitRatio;//最大盈利/总盈利
    public Float maxSingleDeficitRatio;//最大亏损/总亏损
    public Float maxSequentialDeficitCapital;//最大连续亏损额
    public Float sumOfCommission;//手续费合计
    public Float grossProfit;//毛利润
    public Float profitLossRatio;//净利润/单次最大亏损
    public Float avgProfitOfMonth;//月度平均盈利
    public Float floatingProfitAndLoss;//浮动盈亏
    public Float movingCost;//滑动成本
    public Float lastSequentialDeficitCapital;//不知道什么意思,先加上
    public Float lastSequentialProfitCount;//不知道什么意思,先加上
    public Float lastSequentialDeficitCount;//不知道什么意思,先加上
    public Float yieldOfMonthStandardDeviation;//..一个很复杂的计算
    public Float totalAsset;//不知道什么意思,先加上
    public QiaStrategyDto qiaStrategyDto;
    @Expose
    public Date updateTime;



    //策略星级
    @Expose
    public Float starLevel;
    //总评论人数
    @Expose
    public int discussCount;


    //策略引擎类型id
    @Expose
    public int enginetypeId;
   /* public float  getYieldOfYear(){
        if(enginetypeId == QIA_ENGINEE_ID && qiaStrategyDto !=null){
            return qiaStrategyDto.averageSimpleRateOfReturn*252;
        }else{
            return yieldOfYear;
        }
    }
    public float  getSharpeIndex(){
        if(enginetypeId == QIA_ENGINEE_ID && qiaStrategyDto !=null){
            return qiaStrategyDto.sharpRatio;
        }else{
            return sharpeIndex;
        }
    }
    public float  getProfitRatio(){
        if(enginetypeId == QIA_ENGINEE_ID && qiaStrategyDto !=null){
            return qiaStrategyDto.hitRate;
        }else{
            return profitRatio;
        }
    }*/

    /**
     * 交易类型
     */
    public enum TradeType {
        STOCK_CHOICE(1),//择股型
        TIME_CHOICE(2),//择时型
        TRADE_CHOICE(3),//交易型
        OTHER(4);//其它

        TradeType(int value) {
            this.value = value;
        }

        public int value;
    }

    /**
     * 交易品种
     */
    public enum TradeVariety {
        STOCK(1),
        FUTURES(2),
        MIXED(3);

        TradeVariety(int value) {
            this.value = value;
        }

        public int value;
    }

    //策略状态: 1.待审核(也就是上传完成), 2. 沙箱测试  3. 回测中  4. 上架  5 下架  6. 已回测 7待下架 8回测失败 待下架-100审核未通过
    public enum StrategyStatus {
        CHECKING(1),
        SANDBOXTESTING(2),
        BACKTESTING(3),
        UPSHELF(4),
        DOWNSHELF(5),
        FINISHTEST(6),
        WAITINGUPSHELF(7),
        BACKTESTINGFAILER(8),
        DELETED(-100);


        StrategyStatus(int value) {
            this.value = value;
        }

        public int value;
    }

    public enum StrategyType {
        QICORE,
        QIA,
        EASYLANGUAGE;
    }
}
