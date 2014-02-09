package models.iquantCommon;

import com.google.gson.annotations.SerializedName;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 12-12-20
 * Time: 下午1:56
 * 功能描述:  返回给回测服务器的数据传输模型
 */
public class BackTestStrategyDto  {

    @SerializedName( value="path")
    public String filePath;
    @SerializedName( value="sid")
    public String strategyUUid;
    @SerializedName( value="sname")
    public String strategyName;
    //策略状态
    public int state;
    @SerializedName( value="uploadTime")
    public String uploadTime;
    @SerializedName( value="backTestStartTime")
    public String backTestStartTime;
    @SerializedName( value="backTestEndTime")
    public String backTestEndTime;
}
