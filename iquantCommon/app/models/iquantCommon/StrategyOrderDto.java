package models.iquantCommon;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * 策略订阅dto
 * User: panzhiwei
 * Date: 12-12-18
 * Time: 下午2:09
 * To change this template use File | Settings | File Templates.
 */
public class StrategyOrderDto {
    public long id;
    //用户id
    @SerializedName("uid")
    public Long uid;
    //策略id
    @SerializedName("stid")
    public Long stid;
    //订阅开始时间
    @SerializedName("order_stime")
    public Date order_stime;
    //订阅结束时间
    @SerializedName("order_etime")
    public Date order_etime;

}
