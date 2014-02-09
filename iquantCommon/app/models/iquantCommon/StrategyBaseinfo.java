package models.iquantCommon;

import com.google.gson.annotations.Expose;

import java.util.Date;

/**
 * 策略基本信息
 * User: wenzhihong
 * Date: 12-11-7
 * Time: 上午10:15
 */
public class StrategyBaseinfo extends BaseDtoSupport {
    public static final int QICORE_ENGINEE_ID=1;
    public static final int QIA_ENGINEE_ID=2;
    @Expose
    public String stUuid; //策略的uuid,用java代码生成, qicore要用到这个值,通过这个值进行关联. 用 play.libs.UUID() 生成 36位的
    @Expose
    public String name;
    @Expose
    //1. 选股型 2. 择时型 3. 交易型 4. 其他
    public Integer tradeType;


    @Expose
    public Integer tradeVariety; //投资品种
    @Expose
    public Date upTime; //上架时间
    @Expose
    public Date downTime; //下架时间
    @Expose
    public String provider; //策略提供者
    @Expose
    public String providerDesp; //策略提供者的简单描述
    @Expose
    public String desp; //策略简介
    @Expose
    public Date lookbackStime; //策略回测开始时间
    @Expose
    public Date lookbackEtime; //策略回测结束时间
    @Expose
    public Integer status;  //策略状态: 1.待审核(也就是上传完成), 2. 沙箱测试  3. 回测中  4. 上架  5 下架 6已回测 7 待下架 -100审核未通过 8 回测失败
    @Expose
    public int discussTotal = 0; //总评论分(也就是所有的评论总分)
    @Expose
    public int discussCount = 0; //评论人数
    @Expose
    public int collectCount = 0; //收藏人数
    @Expose
    public int orderCount = 0;//订阅人数
    @Expose
    public int enginetypeId;//策略引擎ID

    public UserInfo upUser;  //策略上传者


    public String tradeTypeCnName(){
        String msg = "未知";
        if (tradeType != null) {
            switch (tradeType.intValue()){
                case 1 :
                    msg = "选股型";
                    break;
                case 2 :
                    msg = "择时型";
                    break;
                case 3 :
                    msg = "交易型";
                    break;
                case 4 :
                    msg = "其他";
                    break;
            }
        }
        return msg;
    }

    public String tradeVarietyCnName() {
        String msg = "未知";
        if (tradeVariety != null) {
            switch (tradeVariety.intValue()){
                case 1 :
                    msg = "股票";
                    break;
                case 2 :
                    msg = "期货";
                    break;
                case 3 :
                    msg = "混合";
                    break;
            }
        }
        return msg;
    }
}
