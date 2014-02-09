package models.iquantCommon;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: panzhiwei
 * Date: 12-12-25
 * Time: 上午11:41
 * To change this template use File | Settings | File Templates.
 */
public class UserMessagesDto {
    @Expose
    public Long id;
    //用户id
    @SerializedName("uid")
    @Expose
    public Long uid;
    //用户通知
    @SerializedName("msg")
    @Expose
    public String msg;
    //通知时间
    @SerializedName("msgTime")
    @Expose
    public Date msgTime;
    //状态 已读/未读
    @SerializedName("status")
    @Expose
    public int status;
    //标题
    @SerializedName("title")
    @Expose
    public String title;


    public enum MessagesStatus{
        UNREAD(1),
        READ(2);

        MessagesStatus(int value){
            this.value = value;
        }
        public int value;
    }

}
