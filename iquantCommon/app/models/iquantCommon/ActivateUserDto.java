package models.iquantCommon;

import com.google.gson.annotations.Expose;

import java.util.Date;

/**
 * 用户信息
 * User: liangbing
 * Date: 12-12-4
 * Time: 上午10:41
 */
public class ActivateUserDto {
    @Expose
    public String id;//用户ID
    @Expose
    public String account;//帐号
    @Expose
    public String name;//姓名
    @Expose
    public String idCard;//身份证号
    @Expose
    public String phone;//联系电话
    @Expose
    public String saleName;//营业部
    @Expose
    public String capitalAccount;//资金帐号
    @Expose
    public Date applyDate;//申请日期
    @Expose
    public Integer status;//用户状态
    @Expose
    public String roleName;//所属角色名称
    @Expose
    public Date startDate;//授权开始日期
    @Expose
    public Date endDate;//到期日期

}
