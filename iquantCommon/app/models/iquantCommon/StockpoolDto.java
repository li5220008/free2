package models.iquantCommon;

import com.google.gson.annotations.Expose;

import java.util.Date;

/**
 * 股票池基本信息
 * User: liangbing
 * Date: 12-11-19
 * Time: 上午11:34
 * To change this template use File | Settings | File Templates.
 */
public class StockpoolDto {
    @Expose
    public String stockPoolCode;
    @Expose
    public String id;//股票池ID
    @Expose
    public String poolName;//组合名称
    @Expose
    public String source;//来源
    @Expose
    public float annualizedYield;//年化收益率
    @Expose
    public float yearJensenRatio;//夏普比率
    @Expose
    public double starLevel;//评级
    @Expose
    public Date updateDate;//更新日期
    @Expose
    public int stockNum;//组合股票数
    @Expose
    public int collectCount;//收藏人气
    @Expose
    public String orgId;//机构id
}
