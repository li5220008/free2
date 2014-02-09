package bussiness.user.impl;

import bussiness.common.ILogInfoService;
import bussiness.common.impl.BaseService;
import bussiness.user.IRoleInfoService;
import dbutils.SqlLoader;
import models.iquantCommon.*;
import play.libs.F;
import play.modules.guice.InjectSupport;
import util.Page;
import util.SystemLoggerMessage;

import javax.inject.Inject;
import java.util.List;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-7-16
 * Time: 下午1:49
 * 功能描述:
 */
@InjectSupport
public class RoleInfoService extends BaseService implements IRoleInfoService {

    @Inject
    static ILogInfoService logInfoService;


    /**
     * 根据roleId 获取用户信息
     * @param roleId 角色ID
     * @param pageNo 当前页
     * @return _1.用户集合 _2.page对象
     */
    public  F.T2<List<ActivateUserDto>, Page> roleList(Long roleId, int pageNo) {
        String sqlList = SqlLoader.getSqlById("roleInfoSql");
        List<ActivateUserDto> audList = null;
        StringBuilder coutSql = new StringBuilder("select count(*) from (\n" + sqlList + "\n) distTable  \n");
        Long total = qicDbUtil.queryCount(coutSql.toString(), roleId);
        Page page = new Page(total.intValue(), pageNo);
        sqlList += " limit " + page.beginIndex + "," + page.pageSize + "\n";
        if (total > 0) {
            audList = qicDbUtil.queryBeanList(sqlList, ActivateUserDto.class, roleId);
        }
        return F.T2(audList, page);
    }
    /**
     * 查找所有角色
     */
    public  List<RoleInfo> findAllRole() {
        String sqlList = SqlLoader.getSqlById("findAllRole");
        return qicDbUtil.queryBeanList(sqlList, RoleInfo.class);

    }

    /**
     * 根据角色ID查找权限
     * @param rid
     * @return
     */
    public   List<FunctionInfo> findFunctionInfoByRoleId(long rid){
        String sql = SqlLoader.getSqlById("findFunctionInfoByRoleId");
        List<FunctionInfo> functionInfoDtoList = qicDbUtil.queryBeanList(sql, FunctionInfo.class, rid);
        return functionInfoDtoList;
    }

    /**
     * 删除角色的权限
     * @param rid
     * @return true 删除成功 false 删除失败或没有需要删除的对像
     */
    public  boolean deleteFunctionInfoByRoleId(long rid){
        String sql = SqlLoader.getSqlById("deleteFunctionInfoByRoleId");
        return qicDbUtil.update(sql, rid)>0;
    }

    /**
     * 批量添加角色权限
     * @param role 角色  带授权信息
     */
    public boolean addFunctionInfoByRoleId(RoleInfoDto role){
        if(role == null){
            return false;
        }
        //删除角色的权限
        deleteFunctionInfoByRoleId(role.id);
        //重新赋权限
        if(role.functions ==  null || role.functions.size() == 0){
            //没用权限
            return false;
        }
        String sql = SqlLoader.getSqlById("addFunctionInfoByRoleId");
        int size = role.functions.size();
        Object[][] params = new Object[size][2];
        //填充二维数组
        for(int i = 0;i< size;i++){
            params[i][0] = role.id;
            params[i][1] = role.functions.get(i).id;
        }
        //角色权限发生变化 从缓存中清除
        int[] count = qicDbUtil.batchQicDB(sql, params);
        if(count!=null && count.length>0){
            return true;
        }
        return false;
    }


    /**
     * 角色基本信息修改
     * @param txtarea
     * @param id
     */

    public  void saveRoleBasicInfo(String txtarea,long id){
        String sql = SqlLoader.getSqlById("saverolebasicinfo");
        qicDbUtil.update(sql, txtarea, id);
    }

    /**
     * 角色基本信息查询
     * @param id
     */
    public  RoleInfoDto getRoleBasicInfo(long id){
        String sql = SqlLoader.getSqlById("getrolebasicinfo");
        RoleInfoDto roleInfoDto = qicDbUtil.querySingleBean(sql, RoleInfoDto.class, id);
        return roleInfoDto;
    }

    /**
     * 查询最近20个已授权用户信息
     * @return
     */
    public  List<UserInfo> queryLastTwentyUser(Long rid){
        String queryLastTwentyUserSql = SqlLoader.getSqlById("queryLastTwentyUser");
        return qicDbUtil.queryBeanList(queryLastTwentyUserSql, UserInfo.class, rid);
    }

    /**
     * 查询最近20个当前角色用户信息
     * @param rid 角色ID
     * @return
     */
    public  List<UserInfo> queryLastTwentyRoleUser(Long rid){
        String queryLastTwentyRoleUserSql = SqlLoader.getSqlById("queryLastTwentyRoleUser");
        return qicDbUtil.queryBeanList(queryLastTwentyRoleUserSql, UserInfo.class, rid);
    }


    /**
     * 给定账号或姓名 查询已授权用户
     * @param condition 用户名/账号
     * @return
     */
    public  List<UserInfo> queryUserByCondition(String condition){
        String queryLastTwentyRoleUserSql = SqlLoader.getSqlById("queryUserByName");
        return qicDbUtil.queryBeanList(queryLastTwentyRoleUserSql, UserInfo.class, condition, condition);
    }

    /**
     * 给定账号或姓名 查询当前角色用户
     * @param condition 用户名/账号
     * @param roleId 用户名/账号
     * @return
     */
    public  List<UserInfo> queryRoleUserByCondition(String condition,Long roleId){
        String queryLastTwentyRoleUserSql = SqlLoader.getSqlById("queryRoleUser");
        return qicDbUtil.queryBeanList(queryLastTwentyRoleUserSql, UserInfo.class, condition, condition, roleId);
    }

    /**
     * 更换用户角色
     * @param uids 用户列表ID数组
     * @param urids 角色用户列表ID数组
     * @param roleId 用户名/账号
     * @return
     */
    public  boolean changeRole(List<Double> urids,List<Double> uids, Long roleId,long sysUid){
        String queryUserRoleByIdSql = SqlLoader.getSqlById("queryUserRoleById");
        String insertUserRolebyIdSql = SqlLoader.getSqlById("insertUserRolebyId");
        String deleteUserRolebyIdSql = SqlLoader.getSqlById("deleteUserRolebyId");
        if(urids==null || uids==null || roleId==null || sysUid==0){
            return  false;
        }
        for(Double urid : urids){//对双向列表右边的用户列表赋与当前角色
            if(urid!=null){
                if(qicDbUtil.queryBeanList(queryUserRoleByIdSql, UserRoleDto.class,urid, roleId).size()==0){//如果不存在就插入
                    qicDbUtil.update(insertUserRolebyIdSql, urid, roleId);
                }
            }
        }
        for(Double uid : uids){//对双向列表左边的用户列表删除当前角色
            if(uid!=null){
                qicDbUtil.update(deleteUserRolebyIdSql, uid, roleId);
            }
        }
        logInfoService.writeSystemLog(sysUid, SystemLoggerMessage.DO_CHANGE_ROLE, SystemLoggerMessage.CHANGE_ROLE, SystemLoggerMessage.TYPE);//写入系统日志
        return true;
    }

    /**
     * 删除角色 以及级联关系
     * @Author liuhongjiang
     * @param id 角色id
     */
    public  void deleteRole(Long id,long uid){
        String sql = SqlLoader.getSqlById("deleteRoleName");//删除角色名
        String userRoleSql = SqlLoader.getSqlById("deleteUserRoleByRoleId");//删除用户角色
        String functionRoleSql = SqlLoader.getSqlById("deleteFunctionInfoByRoleId");//删除角色权限
        String findUserByRoleIdSql = SqlLoader.getSqlById("findUserByRoleId");
        String findRoleByUserIdSql = SqlLoader.getSqlById("findRoleByUserId");
        String updateUserStatusSql = SqlLoader.getSqlById("updateUserStatus");
        //查询拥有该角色的用户
        List<UserRoleDto> list = qicDbUtil.queryBeanList(findUserByRoleIdSql, UserRoleDto.class, id);
        //删除角色名称 删除相关级联
        qicDbUtil.update(userRoleSql, id);
        qicDbUtil.update(functionRoleSql, id);
        qicDbUtil.update(sql, id);
        if(list!=null&&list.size()>0){
            int status = 2; //用户待激活状态
            for(UserRoleDto userRole :list){
                //如果该用户处于无权限状态，修改用户状态为待激活
                if(qicDbUtil.queryBeanList(findRoleByUserIdSql, UserRoleDto.class, userRole.uid).size()==0){
                    qicDbUtil.update(updateUserStatusSql, status, userRole.uid);
                }
            }
        }
        //从缓存中删除
        logInfoService.writeSystemLog(uid, SystemLoggerMessage.DO_DELETE_ROLE, SystemLoggerMessage.DELETE_ROLE, SystemLoggerMessage.TYPE);//写入系统日志

    }

    //根据角色ID更新角色名称
    public boolean updateRoleName(String name, Long id){
        String updateRoleNameSql = SqlLoader.getSqlById("updateRoleName");
        return qicDbUtil.update(updateRoleNameSql, name, id)>0 ;
    }

    //新增角色名称
    public boolean addRoleName(String name){
        String renameRoleSql = SqlLoader.getSqlById("insertRoleName");//增加角色名
        return qicDbUtil.update(renameRoleSql, name) > 0 ;
    }

    //根据角色名称 查找单个角色信息
    public RoleInfoDto getRoleByName(String name){
        String updateRoleNameSql = SqlLoader.getSqlById("findRoleByName");
        return qicDbUtil.querySingleBean(updateRoleNameSql,RoleInfoDto.class,name);
    }
}
