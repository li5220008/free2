package models.iquantCommon;

import com.google.gson.annotations.Expose;

/**
 * description: 用户注册DTO
 * User: weiguili(li5220008@163.com)
 * Date: 12-12-25
 * Time: 上午10:28
 */
public class UserRegisterDto {

    /**
     * 姓名
     */
    @Expose
    public String name;

    /**
     * 账号
     */
    @Expose
    public String account;

    /**
     * 密码
     */
    @Expose
    public String pwd;

    /**
     * 重复密码
     */
    @Expose
    public String rePwd;

    /**
     * 联系电话
     */
    @Expose
    public String phone;

    /**
     * 邮箱
     */
    @Expose
    public String email;

    /**
     * 身份号
     */
    @Expose
    public String idcard;

    /**
     * 营业部门
      */
    @Expose
    public int saleDep;

    /**
     * 资金账号
     */
    @Expose
    public String capitalAccount;

    /**
     * 地址
     */
    @Expose
    public String address;

    /**
     * 邮编
     */
    @Expose
    public String postCode;

    /**
     * 所属营业部门
     */
    @Expose
    public SaleDepartment saleDepartment;

    @Expose
    public String eDate;   //权限到期日期,用于注册发email給用户
    /**
     * 状态
     */
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
