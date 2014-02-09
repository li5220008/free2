package models.iquantCommon;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * 操作日志dto
 * User: panzhiwei
 * Date: 12-12-12
 * Time: 下午1:55
 * To change this template use File | Settings | File Templates.
 */
public class LogInfoDto {
    //操作时间
    @SerializedName("cdate")
    @Expose
    public Date cdate;
    //操作人id
    @SerializedName("name")
    @Expose
    public String name;
    //操作功能id
    @SerializedName("func")
    @Expose
    public String func;
    //操作内容
    @SerializedName("content")
    @Expose
    public String content;
    //操作类型
    @SerializedName("type")
    @Expose
    public Integer type;

}
