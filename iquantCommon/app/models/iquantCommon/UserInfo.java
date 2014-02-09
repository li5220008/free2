package models.iquantCommon;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import play.libs.Crypto;
import protoc.Protocol;

import java.util.Date;
import java.util.Set;

/**
 * 用户信息
 * User: wenzhihong
 * Date: 12-11-7
 * Time: 上午9:29
 */

public class UserInfo {
    @SerializedName(Protocol.GlobalFieldName.ID)
    @Expose
    public Long id;
    @SerializedName(Protocol.UserInfo.NAME)
    @Expose
    public String name;
    @SerializedName(Protocol.UserInfo.ACCOUNT)
    @Expose
    public String account; //帐号
    @SerializedName(Protocol.UserInfo.PASSWORD)
    @Expose
    public String pwd;

    //把密码加密
    public void setPwdWithHash(String pwd){
        this.pwd = Crypto.passwordHash(pwd);
    }
    public String phone;
    @Expose
    public String email;

    public String idcard;


    public String capitalAccount;

    public String address;

    public String post;
    @SerializedName(Protocol.UserInfo.SDATE)
    @Expose
    public Date sdate; //启用时间
    @SerializedName(Protocol.UserInfo.EDATE)
    @Expose
    public Date edate; //结束时间


    @SerializedName(Protocol.UserInfo.APPLYDATE)
    @Expose
    public Date applyDate; //申请时间
    @SerializedName(Protocol.UserInfo.STATE)
    @Expose
    public Integer status; //状态

    public Integer utype; //用户类型. 1. 营业部用户, 2. 系统用户



    public SaleDepartment saleDep; //营业部

    @Expose
    public int maxLogin = 1;


    public Set<RoleInfo> roles;
    @Expose
    public String checkSum;
    @Expose
    public String uuid;




    public enum UserStatus{
        WITHOUTACTIVITY(2),//未激活
        ACTIVIY(10),//正常
        DISABLED(1),//禁用
        DELETED(-100);//软删除的
        UserStatus(int value){
            this.value = value;
        }
        public int value;
    }
}
