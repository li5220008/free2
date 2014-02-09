package models.iquantCommon;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * User: liangbing
 * Date: 12-11-8
 * Time: 上午8:58
 * 策略超市dto类
 */
public class StrategyBaseDto extends BaseDtoSupport{
    public static final Integer QICORE_ENGINEE_ID=1;
    public static final Integer QIA_ENGINEE_ID=2;

    @Expose
    public String uuid;

    //策略名称
    @SerializedName("sname")
    @Expose
    public String sname;

    //策略星级
    @SerializedName("starLevel")
    @Expose
    public float starLevel;

    //策略类型
    @SerializedName("stype")
    @Expose
    public Integer stype;

    //策略提供者
    @SerializedName("provider")
    @Expose
    public String provider;

    //交易品种
    @SerializedName("tradeVariety")
    @Expose
    public Integer tradeVariety;

    //收藏数量
    @SerializedName("collectCount")
    @Expose
    public int collectCount = 0;

    //交易次数
    @SerializedName("tradeCount")
    @Expose
    public int tradeCount;

    //上架时间
    @SerializedName("upTime")
    @Expose
    public Date upTime;

    //下架时间
    @SerializedName("downTime")
    @Expose
    public Date downTime;

    //年化收益率
    @SerializedName("yield")
    @Expose
    public float yield;

    //月度收益标准差
    @SerializedName("yomsd")
    @Expose
    public float yomsd;

    //获胜率
    @SerializedName("profitRatio")
    @Expose
    public float profitRatio;

    //策略状态
    @Expose
    public int status;

    //总订阅人数
    @Expose
    public int orderCount;

    //上传时间
    @Expose
    public  Date uploadTime;

    //截止目前还有效的订阅数
    @Expose
    public int validOrderCount;

    //通过时间
    @Expose
    public Date passTime;

    //策略提借人员信息简介
    @Expose
    public String providerDesp;

    //策略简介
    @Expose
    public String desp;

    //回测开始时间
    @Expose
    public Date lookbackStime;

    //回测结束时间
    @Expose
    public Date lookbackEtime;

    //总评论人数
    @Expose
    public int discussCount;

     //总评分
     @Expose
    public int discussTotal;

    //策略引擎类型id
    @Expose
    public int enginetypeId;

    //夏普比率
    @Expose
    public float sharpe;



    /**
     * 交易类型
     */
    public static enum TradeType {
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
    public static enum TradeVariety {
        STOCK(1),
        FUTURES(2),
        MIXED(3);

        TradeVariety(int value) {
            this.value = value;
        }

        public int value;
    }

    //策略状态: 1.待审核(也就是上传完成), 2. 沙箱测试  3. 回测中  4. 上架  5 下架 6已回测 7 待下架 -100审核未通过 8 回测失败
    public static enum StrategyStatus {
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
}
