package models.iquantCommon;

import com.google.gson.annotations.Expose;

import java.util.Date;

/**
 * 订阅返回信息
 * User: liangbing
 * Date: 13-7-4
 * Time: 下午1:36
 */
public class OrderMsgDto {

    @Expose
    public String message;

    @Expose
    public boolean success;

    @Expose
    public Date date;
}
