package models.iquantCommon;

import com.google.gson.annotations.Expose;

import java.util.Date;

/**
 * QIA 绩效数据DTO
 * User: liangbing
 * Date: 13-4-1
 * Time: 下午2:48
 */
public class QiaIndicatorDto {

    @Expose
    public Float sharpRatio;
    @Expose
    public Float volatility;
    @Expose
    public Float beta;
    @Expose
    public Float averageSimpleRateOfReturn;
    @Expose
    public Float calmarRatio;
    @Expose
    public Float conditionalSharpRatio;
    @Expose
    public Float excessReturnOnVar;
    @Expose
    public Float highterPartialMoments;
    @Expose
    public Float jensenRatio;
    @Expose
    public Float kappa3;
    @Expose
    public Float conditionalVar;
    @Expose
    public Float lowerPartialMoments;
    @Expose
    public Float maximumDrawdown;
    @Expose
    public Float modifiedSharpRatio;
    @Expose
    public Float mvar;
    @Expose
    public Float omega;
    @Expose
    public Float sortinoRatio;
    @Expose
    public Float treynorRatio;
    @Expose
    public Float upsidePotentialRatio;
    @Expose
    public Float var;
    @Expose
    public Float skewness;
    @Expose
    public Float kurtosis;
    @Expose
    public Float corrWithMarket;
    @Expose
    public Float hitRate;
    @Expose
    public Float cumsumSimpleReturn;
    public Date updateTime;

    //public float annuallySimpleRateOfReturn;   //年化收益率;

    public Float getAverageSimpleRateOfReturn() {
        return averageSimpleRateOfReturn * 252;
    }
}
