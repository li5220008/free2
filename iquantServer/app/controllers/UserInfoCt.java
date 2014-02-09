package controllers;

import bussiness.licences.ILicencesService;
import bussiness.user.IActivateUserService;
import bussiness.user.ISaleDepartmentService;
import bussiness.user.IUserInfoService;
import bussiness.user.impl.UserInfoService;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import models.iquantCommon.*;
import play.Logger;
import play.data.binding.As;
import play.libs.F;
import util.CommonUtils;
import util.GsonUtil;
import util.Page;

import javax.inject.Inject;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-6-28
 * Time: 上午9:59
 * 功能描述:  用户信息API
 */
public class UserInfoCt extends BaseController {
    @Inject
    static IUserInfoService userInfoService;
    @Inject
    static IActivateUserService activateUserService;
    @Inject
    static ISaleDepartmentService saleDepartmentService;
    @Inject
    static ILicencesService licencesService;

    /**
     * 根据账号查询用户信息
     * @param account 账号
     */
    public static void fetchUserByAcccount(String account) {
        UserInfo userInfo = userInfoService.findByAccount(account);
        responseJSON(userInfo);

    }
    public static void checkSumValid(String account){
        boolean flag = false;
        UserInfo userInfo = userInfoService.findByAccount(account);
        if(userInfo!=null){
            flag =  licencesService.isCheckSumValid(userInfo.uuid,userInfo.id,userInfo.account,userInfo.email,userInfo.checkSum);
        }
        responseJSON(flag);
    }
    /**
     * 更改用户密码
     * @param uid
     * @param newPwd
     */
    public static void updateUserPWD(long uid, String newPwd){
        Boolean success = userInfoService.updateUserPwd(uid, newPwd);
        responseJSON(success);
    }

    /**
     * 待激活用户
     *
     * @param pageNo 页码
     * @return _1 列表list _2 page对象
     */
    public static void activateUsersList(int pageNo){
        String body = fetchBody();
        AvtivatePar ap = GsonUtil.createWithoutNulls().fromJson(body,AvtivatePar.class);
        F.T2<List<ActivateUserDto>,Page> result = activateUserService.userList(ap, pageNo);
        responseJSON(result._1,result._2);

    }
    /**
     * 激活用户
     *
     * @param pageNo 页码
     * @return _1 列表list _2 page对象
     */
    public static void usersList(int pageNo){
        String body = fetchBody();
        AvtivatePar ap = GsonUtil.createWithoutNulls().fromJson(body,AvtivatePar.class);
        F.T2<List<ActivateUserDto>,Page> result = activateUserService.users(ap, pageNo);
        responseJSON(result._1,result._2);

    }
    /**
     * 到期用户
     *
     * @param pageNo 页码
     * @return _1 列表list _2 page对象
     */
    public static void dueUsersList(int pageNo){
        String body = fetchBody();
        AvtivatePar ap = GsonUtil.createWithoutNulls().fromJson(body,AvtivatePar.class);
        F.T2<List<ActivateUserDto>,Page> result = activateUserService.dueUsers(ap, pageNo);
        responseJSON(result._1,result._2);

    }

    /**
     * 查询所有的部门信息
     * @return
     */
    public static void findAllDepartment(){
        List<SaleDepartMentDto> list = saleDepartmentService.findAll();
        responseJSON(list);
    }

    /**
     * 根据用户ID 查询用户的角色信息
     *
     * @param uid
     * @return
     */
    public static void findUserRoleInfoById(long uid){
        List<UserRoleDto> list = userInfoService.findUserRoleInfoById(uid);
        responseJSON(list);
    }

    /**
     * 根据角色ID查询菜单
     *
     * @param rid
     * @return
     */
    public static void findRoleFunctionInfo(long rid){
        List<FunctionInfo> list = UserInfoService.findRoleFunctionInfo(rid);
        responseJSON(list);
    }

    /**
     * 根据用户ID查询用户信息
     *
     * @param uid
     * @return
     */
    public static void findUserInfoById(long uid){
        UserInfo userInfo = userInfoService.findUserById(uid);
        responseJSON(userInfo);
    }

    /**
     * 根据用户ID查询 用户的菜单
     *
     * @param uid
     * @return
     */
    public static void findUserFunctionInfoById(long uid){
        List<FunctionInfoDto> list = userInfoService.findUserFunctionInfoById(uid);
        responseJSON(list);
    }

    /**
     * 修改用户基本信息
     *
     * @return
     */
    public static void updateUserInfo(){
        String body = fetchBody();
        UserInfoDto userInfoDto = GsonUtil.createWithoutNulls().fromJson(body,UserInfoDto.class);
        Boolean flag = userInfoService.updateUserInfo(userInfoDto);
        responseJSON(flag);
    }

   /* public static void fetchFunctionInfo(){

    }*/

    /**
     * 根据email查找用户信息
     * @param email
     */
    public static void findUserByEmail(String email){
        UserInfoDto userInfoDto = userInfoService.findUserByEmail(email);
        responseJSON(userInfoDto);
    }

    /**
     * 批量删除用户（软删除）
     * @param ids
     */
    public static void delUserById(String ids){
        userInfoService.delUser(ids);
        responseJSON(true);
    }

    /**
     * 批量修改用户状态
     * @param ids 用户id 用逗号分隔
     * @param status
     */
    public static void userStateModifyById(@As(",")String[] ids ,int status){
        userInfoService.userStateModify(ids,status);
        responseJSON(true);
    }

    /**
     * 到期用户延期
     *
     * @param strId
     * @param delaydate
     */
    public static void userdelayById(@As(",")String[] strId, String delaydate){
        userInfoService.userdelay(strId,delaydate);
        responseJSON(true);
    }

    /**
     * 批量插入用户信息
     *
     */
    public static void addUserBatch(){
        String body = fetchBody();
        Type type = new TypeToken<List<UserInfoDto>>() {}.getType();
        List<UserInfoDto> userList = GsonUtil.createWithoutNulls().fromJson(body, type);
        List<Long> longList = null;
        try {
            longList = userInfoService.addUserBatch(userList);
        } catch (Exception e) {
            e.printStackTrace();
            Logger.error(e, "程序异常:%s", e.getMessage());
            responseError();
        }
        responseJSON(longList);
    }

    /**
     * 获得用户数据权限信息
     * @param id 用户ID
     */
    public static void fetchUserDataInfoById(long id){
         List<UserDataDto> list = userInfoService.findUserDataInfoByUid(id);
         responseJSON(list);
    }

    /**
     * 查看用户信息
     * @param uid
     */
    public static void fetchPersonalmodify(long uid) {
        UserInfoDto userInfoDto = userInfoService.personalmodify(uid);
        responseJSON(userInfoDto);
    }

    /**
     * 更新用户信息
     * @param uid
     */
    public static void updatePersonalmodify(long uid) {
        String body = fetchBody();
        UserInfoDto userInfoDto = GsonUtil.createWithoutNulls().fromJson(body, UserInfoDto.class);
        boolean flag = userInfoService.updateUserInfo(userInfoDto, uid);
        responseJSON(flag);
    }

    /**
     * 根据uid找密码
     * @param password
     * @param uid
     */
    public static void findPwdById(String password, long uid) {
        boolean flag = userInfoService.findPwdById(password, uid);
        responseJSON(flag);
    }

    /**
     * 检查email是否存在
     * @param newEmail
     * @param selfEmail
     */
    public static void checkEmail(String newEmail, String selfEmail) {
        UserInfoDto userInfoDto = userInfoService.checkEmail(newEmail, selfEmail);
        responseJSON(userInfoDto);
    }

    /**
     * 注册用户
     * @return 是否成功
     */
    public static void addUser(){
        String body = fetchBody();
        UserRegisterDto userRegisterDto = GsonUtil.createWithoutNulls().fromJson(body, UserRegisterDto.class);
        //下面两行判断用户的帐号和邮箱是否已经存在,如果存在就不允许插入
        UserInfoDto userByEmail = userInfoService.findUserByEmail(userRegisterDto.email);
        UserInfo byAccount = userInfoService.findByAccount(userRegisterDto.account);
        if(userByEmail==null&&byAccount==null){
            long flag = userInfoService.addUser(userRegisterDto);
            responseJSON(flag);
        }
    }

    public static void fetchAllDepartment() {
        List<SaleDepartment> list = userInfoService.fetchAllDepartment();
        responseJSON(list);
    }

    /**
     * 给终端的发邮件接口
     */
    public static void  sendEmailToSystem(){
        HashMap<Object, Object> map = Maps.newHashMap();
        String bodyJson = fetchBody();
        Gson gson = GsonUtil.createGson();
        SendMailDto sendMailDto = gson.fromJson(bodyJson, SendMailDto.class);
        sendMailDto.name = "iquant";
        sendMailDto.sender ="iquant@gtafe.com";
        UserInfo userInfo = userInfoService.findByAccount(sendMailDto.userAccount);
        Logger.info("开始发送邮件，用户account: %s",sendMailDto.userAccount);

        if(userInfo!=null){
            sendMailDto.userEmail = userInfo.email;
            Logger.info("用户的邮箱%s，用户account: %s",userInfo.email,sendMailDto.userAccount);
        }
        if(CommonUtils.sendFAQMail(sendMailDto)){
            map.put("success","success");
            Logger.info("发送邮件成功，用户account: %s",sendMailDto.userAccount);
        }else {
            map.put("success","failure");
            Logger.info("发送邮件失败，用户account: %s",sendMailDto.userAccount);
        }
        responseJSON(map);
    }


}
