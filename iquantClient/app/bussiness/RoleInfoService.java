package bussiness;

import models.iquantCommon.*;
import play.libs.F;
import protoc.HttpBody;
import protoc.URILib;
import protoc.parser.ActionResult;
import util.GsonUtil;
import util.Page;

import java.util.List;
import java.util.Map;

/**
 * 获取对应角色的用户列表
 * User: liangbing
 * Date: 12-12-7
 * Time: 上午11:40
 */
public class RoleInfoService  extends BasicService{

    /**
     * 根据roleId 获取用户信息
     * @param roleId 角色ID
     * @param pageNo 当前页
     * @return _1.用户集合 _2.page对象
     */
    public static F.T2<List<ActivateUserDto>, Page> roleList(Long roleId, int pageNo) {
        ActionResult<List<ActivateUserDto>> actionResult = remoteRequestService.getList(URILib.FETCH_USERINFO_BY_ROLEID, ActivateUserDto.class, roleId,pageNo);
        int total = actionResult.header.total;
        Page page = new Page(total, pageNo);
        return F.T2(actionResult.data, page);
    }
    /**
      * 查找所有角色
     */
    public static List<RoleInfo> findAllRole() {
        return remoteRequestService.getList(URILib.FETCH_ALL_ROLE,RoleInfo.class).data;

    }

    /**
     * 根据角色ID查找权限
     * @param rid
     * @return
     */
    public static  List<FunctionInfoDto> findFunctionInfoByRoleId(long rid){
        return remoteRequestService.getList(URILib.FETCH_FUNCTIONINFO_BY_ROLEID,FunctionInfoDto.class,rid).data;
    }

    /**
     * 删除角色的权限
     * @param rid
     * @return true 删除成功 false 删除失败或没有需要删除的对像
     */
    public static boolean deleteFunctionInfoByRoleId(long rid){
        return Boolean.valueOf(remoteRequestService.getSingleValue(URILib.DELETE_ROLEFUNCTION_BY_ROLEID,rid).data);
    }

    /**
     * 批量添加角色权限
     * @param role 角色  带授权信息
     */
    public  static void addFunctionInfoByRoleId(RoleInfoDto role){
        HttpBody httpBody = new HttpBody();
        String json = GsonUtil.createWithoutNulls().toJson(role);
        httpBody.body = json;
        //角色权限发生变化 从缓存中清除
        UserInfoBaseService.deleteRoleFromCache(role.id);
        remoteRequestService.getSingleValue(URILib.BATCH_FUNCTIONINFO_BY_ROLEID, httpBody);
    }


    /**
     * 角色基本信息修改
     * @param txtarea
     * @param id
     */

    public static void saveRoleBasicInfo(String txtarea,long id){
        remoteRequestService.getSingleValue(URILib.UPDATE_ROLEINFO_BY_RID,txtarea,id);
    }

    /**
     * 角色基本信息查询
     * @param id
     */
    public static RoleInfoDto getRoleBasicInfo(long id){
        return remoteRequestService.getBean(URILib.FETCH_ROLEINFO_BY_ROLEID,RoleInfoDto.class,id).data;
    }

    /**
     * 查询最近20个已授权用户信息
     * @return
     */
    public static List<UserInfo> queryLastTwentyUser(Long rid){
        String type ="1";
        return remoteRequestService.getList(URILib.FETCH_USERINFO_BY_ROLEID_AND_TYPE, UserInfo.class, rid, type).data;
    }

    /**
     * 查询最近20个当前角色用户信息
     * @param rid 角色ID
     * @return
     */
    public static List<UserInfo> queryLastTwentyRoleUser(Long rid){
        String type ="2";
        return remoteRequestService.getList(URILib.FETCH_USERINFO_BY_ROLEID_AND_TYPE,UserInfo.class,rid,type).data;
    }


    /**
     * 给定账号或姓名 查询已授权用户
     * @param keyword 用户名/账号
     * @return
     */
    public static List<UserInfo> queryUserByCondition(String keyword){
        return remoteRequestService.getList(URILib.FETCH_USERINFO_BY_NAME_OR_ACCOUNT,UserInfo.class,keyword).data;
    }

    /**
     * 给定账号或姓名 查询当前角色用户
     * @param keyword 用户名/账号
     * @param roleId 用户名/账号
     * @return
     */
    public static List<UserInfo> queryRoleUserByCondition(String keyword,Long roleId){
         return remoteRequestService.getList(URILib.FETCH_ROLEUSER_BY_NAME_OR_ACCOUNT,UserInfo.class,keyword,roleId).data;
    }

    /**
     * 更换用户角色
     * @param uid 用户列表ID数组
     * @param urid 角色用户列表ID数组
     * @param roleId 用户名/账号
     * @return
     */
    public static boolean changeRole(Long[] urid,Long[] uid, Long roleId,long sysUid){
        Map map = vargsToMap(urid,uid);
        String json = GsonUtil.createWithoutNulls().toJson(map);
        HttpBody httpBody = new HttpBody();
        httpBody.body = json;
        for(long id : uid){
          UserInfoBaseService.deleteUserFromCache(id);
        }
        Boolean flag = Boolean.valueOf(remoteRequestService.getSingleValue(URILib.CHANGE_USER_ROLE,httpBody,roleId,sysUid).data);

        return flag;
    }

    /**
     * 删除角色 以及级联关系
     * @Author liuhongjiang
     * @param id 角色id
     */
    public static void deleteRole(Long id,long uid){
        remoteRequestService.getSingleValue(URILib.DELETE_ROLE, id, uid);
        //从缓存中删除
        UserInfoBaseService.deleteRoleFromCache(id);
    }

    //根据角色ID更新角色名称
    public static boolean updateRoleName(String name, Long id){
        return Boolean.valueOf(remoteRequestService.getSingleValue(URILib.UPDATE_ROLENAME_BY_RID,name,id).data);
    }

    //新增角色名称
    public static boolean addRoleName(String name){
        return Boolean.valueOf(remoteRequestService.getSingleValue(URILib.ADD_ROLENAME,name).data);
    }

    //根据角色名称 查找单个角色信息
    public static RoleInfoDto getRoleByName(String name){
        return remoteRequestService.getBean(URILib.FETCH_ROLEINFO_BY_NAME,RoleInfoDto.class,name).data;
    }
}
