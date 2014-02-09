package controllers;

import bussiness.common.IFunctionService;
import bussiness.user.IRoleInfoService;
import bussiness.user.IUserAuthorizationService;
import com.google.gson.reflect.TypeToken;
import models.iquantCommon.*;
import play.data.binding.As;
import play.libs.F;
import util.GsonUtil;
import util.Page;

import javax.inject.Inject;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-7-16
 * Time: 下午2:28
 * 功能描述:
 */
public class RoleInfoCt extends BaseController {
    @Inject
    static IUserAuthorizationService userAuthorizationService;
    @Inject
    static IRoleInfoService roleInfoService;
    @Inject
    static IFunctionService functionService;

    /**
     * 根据roleId 获取用户信息
     *
     * @param roleId 角色ID
     * @param pageNo 当前页
     * @return _1.用户集合 _2.page对象
     */
    public static void fetchRoleListByRid(Long roleId, int pageNo) {
        F.T2<List<ActivateUserDto>, Page> result = roleInfoService.roleList(roleId, pageNo);
        responseJSON(result._1, result._2);
    }

    /**
     * 查找所有角色
     */
    public static void findAllRole() {
        List<RoleInfo> list = roleInfoService.findAllRole();
        responseJSON(list);
    }

    /**
     * 根据角色ID查找权限
     *
     * @param rid
     * @return
     */
    public static void findRoleFunctionInfo(long rid) {
        List<FunctionInfo> list = roleInfoService.findFunctionInfoByRoleId(rid);
        responseJSON(list);
    }

    /**
     * 删除角色的权限
     *
     * @param rid
     * @return true 删除成功 false 删除失败或没有需要删除的对像
     */
    public static void deleteFunctionInfoByRoleId(long rid) {
        Boolean flag = roleInfoService.deleteFunctionInfoByRoleId(rid);
        responseJSON(flag);
    }

    /**
     * 批量添加角色权限
     *
     */
    public static void addFunctionInfoByRoleId() {
        String body = fetchBody();
        RoleInfoDto roleInfo = GsonUtil.createWithoutNulls().fromJson(body,RoleInfoDto.class);
        boolean flag = roleInfoService.addFunctionInfoByRoleId(roleInfo);
        responseJSON(flag);
    }

    /**
     * 角色基本信息修改
     *
     * @param id
     * @param txtarea
     */

    public static void saveRoleBasicInfo(String txtarea,long id) {
        roleInfoService.saveRoleBasicInfo(txtarea, id);
        responseJSON(true);
    }

    /**
     * 角色基本信息查询
     *
     * @param id
     */
    public static void fetchRoleBasicInfo(long id) {
        RoleInfoDto roleInfoDto = roleInfoService.getRoleBasicInfo(id);
        responseJSON(roleInfoDto);
    }

    /**
     * @param type 1.查询最近20个已授权用户信息
     *             2.查询最近20个当前角色用户信息
     * @return
     */
    public static void queryLastTwentyUser(Long rid, int type) {
        List<UserInfo> list;
        switch (type) {
            case 1:
                list = roleInfoService.queryLastTwentyUser(rid);
                responseJSON(list);
            case 2:
                list = roleInfoService.queryLastTwentyRoleUser(rid);
                responseJSON(list);
            default:
                responseError("参数type值错误");

        }
    }


    /**
     * 给定账号或姓名 查询已授权用户
     *
     * @param keyword 用户名/账号
     * @return
     */
    public static void queryUserByCondition(String keyword) {
        List<UserInfo> list = roleInfoService.queryUserByCondition(keyword);
        responseJSON(list);
    }

    /**
     * 给定账号或姓名 查询当前角色用户
     *
     * @param keyword 用户名/账号
     * @param roleId  用户名/账号
     * @return
     */
    public static void queryRoleUserByCondition(String keyword, Long roleId) {
        List<UserInfo> list = roleInfoService.queryRoleUserByCondition(keyword, roleId);
        responseJSON(list);
    }

    /**
     * 更换用户角色
     * @param roleId 角色ID
     * @param sysUid 操作者的id 用于记录操作日志
     * @return
     */
    public static void changeRole(Long roleId,Long sysUid) {
        String body = fetchBody();
        Map map = GsonUtil.createWithoutNulls().fromJson(body, Map.class);
        List<Double> urid = (List<Double>)map.get("urid");  //所选角色用户列表ID数组
        List<Double> uid = (List<Double>)map.get("uid");  //用户列表ID数组
        boolean flag = roleInfoService.changeRole(urid, uid, roleId, sysUid);
        responseJSON(flag);
    }

    /**
     * 删除角色 以及级联关系
     *
     * @param id  角色id
     * @param uid 用户id
     * @Author liuhongjiang
     */
    public static void deleteRole(Long id, long uid) {
        roleInfoService.deleteRole(id, uid);
        responseJSON("true");
    }

    /**
     * 批量插入用户权限
     */
    public static void insertUserRole(@As(format = "yyyy-MM-dd hh:mm:ss") Date edate,int flag) {
        String body = fetchBody();
        Map map = GsonUtil.createWithoutNulls().fromJson(body, Map.class);
        List<Double> userId = ( List<Double>) map.get("userId");
        List<Double>roleId = ( List<Double>) map.get("roleId");
        int count = userAuthorizationService.insertUserRole(userId, roleId, edate,flag);
        responseJSON(count);
    }

    /**
     * 新增/修改用户数据权限
     */
    public static void changeUserDataPermission(long uid) {
        String body = fetchBody();
        Type type = new TypeToken<List<UserDataDto>>(){}.getType();
        List<UserDataDto> list = GsonUtil.createWithoutNulls().fromJson(body,type);
        boolean flag = userAuthorizationService.changeUserDataPermission(uid,list);
        responseJSON(flag);
    }

    public static void findUserDatePermission(long uid){
        List<UserDataDto> list = userAuthorizationService.findUserDataPermission(uid);
        responseJSON(list);
    }

    /**
     * 用户授权 用户列表展示
     *
     * @param userIdArray 用户ID数组
     * @return 用户名 账号 所属营业部
     */
    public static void fetchUserListByUserIdArray(@As(",")int[] userIdArray) {
        List<ActivateUserDto> list = userAuthorizationService.getUserList(userIdArray);
        responseJSON(list);
    }

    /**
     * 查找startId和endId之间的用户信息
     *
     * @param startId 开始ID
     * @param endId   结束ID
     * @return
     */
    public static void fetchUserListBetweenId(int startId, int endId) {
        List<ActivateUserDto> list = userAuthorizationService.getUserList(startId, endId);
        responseJSON(list);
    }

    /**
     * 根据角色ID更新角色名称
     *
     * @param name
     * @param id
     * @return
     */
    public static void updateRoleNameByRid(String name, long id) {
        boolean flag = roleInfoService.updateRoleName(name, id);
        responseJSON(flag);

    }

    /**
     * 新增角色名称
     *
     * @param name
     * @return
     */
    public static void addRoleName(String name) {
        boolean flag = roleInfoService.addRoleName(name);
        responseJSON(flag);
    }

    /**
     * 根据角色名称 查找单个角色信息
     *
     * @param name
     * @return
     */
    public static void fetchRoleInfoByName(String name) {
        RoleInfoDto roleInfoDtor = roleInfoService.getRoleByName(name);
        responseJSON(roleInfoDtor);
    }

    public static void fetchFunctionInfo(){
        List<FunctionInfoDto> list = functionService.getAllSystemFunctions();
        responseJSON(list);
    }
}
