package models.iquantCommon;

import com.google.gson.annotations.Expose;

import java.math.BigDecimal;

/**
 * 股票池基本信息
 * User: liuhongjiang
 * Date: 12-11-19
 * Time: 下午2:02
 */
public class StockPoolBasicInfoDto {
    //股票池编号
    @Expose
    public Long stockPoolCode;
    //股票池名称
    @Expose
    public String stockPoolName;
    //组合股票数
    @Expose
    public Long stockNum;
    //更新频率
    @Expose
    public String updateFrequency;
    //股票策略（组合说明）
    @Expose
    public String strategy;
    //组合收益
    @Expose
    public BigDecimal annualizedYield;
    //附件下载
    @Expose
    public String filestoragePath;
    //研报摘要
    @Expose
    public String summary;
    //组合来源
    @Expose
    public String institutionName;
    //组合评级
    @Expose
    public BigDecimal starNum;

}
