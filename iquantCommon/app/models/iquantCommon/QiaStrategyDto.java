package models.iquantCommon;



import com.google.gson.annotations.Expose;

import java.util.Date;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-4-2
 * Time: 下午1:33
 * 功能描述:  qia 策略
 */
public class QiaStrategyDto extends BaseDtoSupport {
    @Expose
    public String strategyId;
    @Expose
    public float type;
    @Expose
    public float sharpRatio;
    @Expose
    public float volatility;
    @Expose
    public float beta;
    @Expose
    public float averageSimpleRateOfReturn;
    @Expose
    public float calmarRatio;
    @Expose
    public float conditionalSharpRatio;
    @Expose
    public float excessReturnOnVar;
    @Expose
    public float highterPartialMoments;
    @Expose
    public float jensenRatio;
    @Expose
    public float kappa3;
    @Expose
    public float conditionalVar;
    @Expose
    public float lowerPartialMoments;
    @Expose
    public float maximumDrawdown;
    @Expose
    public float modifiedSharpRatio;
    @Expose
    public float maxSingleDeficitRatio;
    @Expose
    public float mvar;
    @Expose
    public float omega;
    @Expose
    public float sortinoRatio;
    @Expose
    public float treynorRatio;
    @Expose
    public float upsidePotentialRatio;
    @Expose
    public float var;
    @Expose
    public float skewness;
    @Expose
    public float kurtosis;
    @Expose
    public float corrWithMarket;
    @Expose
    public float hitRate;
    @Expose
    public float cumsumSimpleReturn;
    @Expose
    public Date updateTime;

}
