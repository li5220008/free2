package models.iquantCommon;

import com.google.gson.annotations.Expose;

import java.util.Date;
import java.util.List;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 12-12-4
 * Time: 上午11:28
 * 功能描述: 用于修改用户信息时展示
 */
public class UserInfoDto extends BaseDtoSupport {
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
    public String password;
    /**
     * 联系电话
     */
    @Expose
    public String phone;
    /**
     * 电子邮件
     */
    @Expose
    public String email;
    /**
     * 身份证号码
     */
    @Expose
    public String idCard;
    /**
     * 所属营业部
     */
    @Expose
    public String saleDept;
    /**
     * 资金账号
     */
    @Expose
    public String capitalAccount;
    /**
     * 联系地址
     */
    @Expose
    public String address;
    /**
     * 邮编
     */
    @Expose
    public String postCode;
    /**
     * 所有菜单
     */

    /**
     * 重复密码
     */
    @Expose
    public String rePassword;
    /**
     * 起始时间
     */
    @Expose
    public Date sDate;
    /**
     * 结束时间
     */
    @Expose
    public Date eDate;
    /**
     * 状态
     */
    public UserStatus status;

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

    /**
     * 机构名称
     */
    @Expose
    public String institutionName;
    /**
     * 机构联系人
     */
    @Expose
    public String contactName;
    /**
     * 授权总个数
     */
    @Expose
    public int limitCount;

    @Expose
    public String checkSum;

    public String userUuid;

    @Expose
    public UserServerDto userServer;

    /**
     * 角色Id
     */
    @Expose
    public List<Double> roleId;

}