package models.iquantCommon;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 12-12-13
 * Time: 下午5:58
 * 功能描述: 配置表
 */
public class ConfigDto extends BaseDtoSupport {

    @SerializedName("key")
    @Expose
    public  String key;
    @SerializedName("value")
    @Expose
    public String value;
    @SerializedName("comments")
    @Expose
    public  String comments;
}
