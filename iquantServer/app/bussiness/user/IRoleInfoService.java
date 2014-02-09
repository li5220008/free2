package bussiness.user;

import models.iquantCommon.*;
import play.libs.F;
import util.Page;

import java.util.List;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-7-16
 * Time: 下午1:45
 * 功能描述:
 */
public interface IRoleInfoService {

    /**
     * 根据roleId 获取用户信息
     *
     * @param roleId 角色ID
     * @param pageNo 当前页
     * @return _1.用户集合 _2.page对象
     */
    public F.T2<List<ActivateUserDto>, Page> roleList(Long roleId, int pageNo);

    /**
     * 查找所有角色
     */
    public List<RoleInfo> findAllRole();

    /**
     * 根据角色ID查找权限
     *
     * @param rid
     * @return
     */
    public List<FunctionInfo> findFunctionInfoByRoleId(long rid);

    /**
     * 删除角色的权限
     *
     * @param rid
     * @return true 删除成功 false 删除失败或没有需要删除的对像
     */
    public boolean deleteFunctionInfoByRoleId(long rid);

    /**
     * 批量添加角色权限
     *
     * @param role 角色  带授权信息
     */
    public boolean addFunctionInfoByRoleId(RoleInfoDto role);


    /**
     * 角色基本信息修改
     *
     * @param txtarea
     * @param id
     */

    public void saveRoleBasicInfo(String txtarea, long id);

    /**
     * 角色基本信息查询
     *
     * @param id
     */
    public RoleInfoDto getRoleBasicInfo(long id);

    /**
     * 查询最近20个已授权用户信息
     *
     * @return
     */
    public List<UserInfo> queryLastTwentyUser(Long rid);

    /**
     * 查询最近20个当前角色用户信息
     *
     * @param rid 角色ID
     * @return
     */
    public List<UserInfo> queryLastTwentyRoleUser(Long rid);


    /**
     * 给定账号或姓名 查询已授权用户
     *
     * @param condition 用户名/账号
     * @return
     */
    public List<UserInfo> queryUserByCondition(String condition);

    /**
     * 给定账号或姓名 查询当前角色用户
     *
     * @param condition 用户名/账号
     * @param roleId    用户名/账号
     * @return
     */
    public List<UserInfo> queryRoleUserByCondition(String condition, Long roleId);

    /**
     * 更换用户角色
     *
     * @param uids    用户列表ID数组
     * @param urids   角色用户列表ID数组
     * @param roleId 用户名/账号
     * @return
     */
    public boolean changeRole(List<Double> urids, List<Double> uids, Long roleId, long sysUid);

    /**
     * 删除角色 以及级联关系
     *
     * @param id 角色id
     * @Author liuhongjiang
     */
    public void deleteRole(Long id, long uid);

    /**
     * 根据角色ID更新角色名称
     *@param name
     *@param id
     */
    public boolean updateRoleName(String name, Long id);

    /**
     *  新增角色名称
     * @param name
     * @return
     */
    public boolean addRoleName(String name);

    /**
     * 根据角色名称 查找单个角色信息
     * @param name
     * @return
     */
    public RoleInfoDto getRoleByName(String name);
}
