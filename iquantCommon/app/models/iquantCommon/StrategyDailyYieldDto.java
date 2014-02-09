package models.iquantCommon;

import com.google.gson.annotations.Expose;

import java.util.Date;

/**
 *
 * 策略日收益率
 * User: liuhongjiang
 * Date: 12-11-10
 * Time: 上午11:50
 */
public class StrategyDailyYieldDto {
    //策略名称
    @Expose
    public String sname;
    //收益率
    @Expose
    public float yield;
    //更新时间
    @Expose
    public Date  updateDate;

}
