package models.iquantCommon;

import com.google.gson.annotations.Expose;
import play.data.binding.As;

/**
 * 股票池搜索条件dto
 * User: wenzhihong
 * Date: 12-11-11
 * Time: 下午3:53
 */
public class StockPoolSearchCnd {
    //推荐机构, 这样用逗号分割开来,就可以转成数组了
    @As(",")
    @Expose
    public String[] recommendOrgs;

    //研报更新周期
    @Expose
    public Integer reportUpdatePeriod;

    //用户评级 小值
    @Expose
    public Integer starDown;

    //用户评级 大值
    @Expose
    public Integer starUp;

    //年化收益率 小值
    @Expose
    public Float yearYieldDown;

    //年化收益率 大值
    @Expose
    public Float yearYieldUp;

    //sharp比率 小值
    @Expose
    public Float sharpRateDown;

    //sharp比率 大值
    @Expose
    public Float sharpRateUp;

}
