package controllers;

import annotation.QicFunction;
import bussiness.FunctionService;
import bussiness.RoleInfoService;
import bussiness.UserAuthorizationService;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.inject.Inject;
import models.iquantCommon.*;
import play.data.binding.As;
import play.libs.F;
import play.mvc.Util;
import util.Page;
import util.SystemLoggerMessage;
import util.SystemResponseMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 角色用户列表
 * User: liangbing
 * Date: 12-12-7
 * Time: 上午11:30
 * 根据角色ID 获取有该角色的所有用户
 */
public class RoleInfos extends BasePlayControllerSupport {
    private static final int ROLE_FUNCTION_TAB = 0;
    private static final int ROLE_USER_LIST_TAB = 1;
    @Inject
    static UserAuthorizationService userAuthorizationService;
    @Inject
    static RoleInfoService roleInfoService;
    @Inject
    static FunctionService functionService;
    //角色管理入口方法
    @QicFunction(id=22)
    public static void roleList() {
        Integer tab = params.get("tab", Integer.class);
        Long roleId = params.get("roleId", Long.class);
        int pageNo = 1;
        if (params._contains("pageNo")) {
            pageNo = params.get("pageNo", Integer.class);
        }
        switch (tab = tab == null ? 0 : tab) {
            case ROLE_USER_LIST_TAB:
                findRoleList(tab, roleId == null ? 1 : roleId, pageNo);
                break;
            case ROLE_FUNCTION_TAB:
                findFunctionsListByRole(tab, roleId == null ? 1 : roleId);
                break;
            default:
                //Http.Response.current().status=403;//403后面查查
                renderText(SystemResponseMessage.ILLEGAL_REQUEST_RSP);
        }

    }

    //liangbin的方法
    @Util
    private static void findRoleList(int tab, long roleId, int pageNo) {
        F.T2<List<ActivateUserDto>, Page> t2 = roleInfoService.roleList(roleId, pageNo == 0 ? 1 : pageNo);
        List<ActivateUserDto> usersList = t2._1;
        Page page = t2._2;
        List<RoleInfo> roleList = userAuthorizationService.findAllRole();
        RoleInfoDto ri = roleInfoService.getRoleBasicInfo(roleId);
        render(usersList, page, roleId == 0 ? roleList.get(0).id : roleId,ri,roleList, tab);
    }

    /**
     * @param name 角色名
     * @Author 刘泓江
     * 新建/重命名 角色名
     */
    @QicFunction(id=22)
    public static void renameOrNewRole(String name, Long id,long uid) {
        Boolean flag = false;

        Map<String, Object> json = new HashMap<String, Object>();
        RoleInfoDto roleInfo = roleInfoService.getRoleByName(name);
        if (roleInfo == null) {//判断角色名是否存在
            if (id != -999) {//修改原来的 update
                if (roleInfoService.updateRoleName(name,id)) {

                    flag = true;
                    json.put("msg", "修改成功");
                    json.put("samename", flag);
                    json.put("id", id);
                } else {
                    json.put("msg", "修改失败");
                }
            } else {//修改新增的 insert
                if (roleInfoService.addRoleName(name)) {
                    flag = true;
                    roleInfo = roleInfoService.getRoleByName(name);
                    json.put("msg", "新增成功");
                    json.put("id", roleInfo.id);//返回自增长的ID
                    json.put("samename", flag);
                } else {
                    json.put("msg", "新增失败");
                }
            }
        } else {
            json.put("msg", "名称已存在");
            json.put("sameName", flag);
        }
        json.put("op", flag);
        renderJSON(json);
    }

    /**
     * @param id 角色ID
     * @Author 刘泓江
     * 删除角色名 以及相关的级联关系
     */
    @QicFunction(id=22)
    public static void deleteRoleName(Long id,long uid) {
        boolean flag = false;
        Map<String, Object> json = new HashMap<String, Object>();

        try {
            roleInfoService.deleteRole(id,uid);
            flag = true;
            json.put("msg", "删除成功");
        } catch (Exception e) {
            json.put("msg", "删除失败");
            e.printStackTrace();
        }finally {
            json.put("op", flag);
            renderJSON(json);
        }
    }

    /**
     * @param roleId
     * @return
     * @Author 刘建力
     */
    @Util
    private static void findFunctionsListByRole(int tab, long roleId) {
        //一定要new一个List出来 ，不能直接使用Service查出来的,多个人使用的时候会有问题
        List<FunctionInfoDto> roleFunList = roleInfoService.findFunctionInfoByRoleId(roleId);
        List<FunctionInfoDto> sysFunLists =new FunctionService().getAllSystemFunctions();
        List<RoleInfo> roleList = userAuthorizationService.findAllRole();//
        markCheckedStatus(roleFunList, sysFunLists);
        //将双引号换成单引号 防止页面上生成&quot;代号
        String sysFunList = new Gson().toJson(sysFunLists).replaceAll("\"", "'");
        RoleInfoDto roleInfoDto = roleInfoService.getRoleBasicInfo(roleId);
        if(roleInfoDto == null){
             roleInfoDto = new RoleInfoDto();
        }
        render(tab, roleList, sysFunList, roleId, roleInfoDto);
    }

    /**
     * 标识某个菜单是否选中  帮助页面显示用的,也可以放到页都没上用js做
     *
     * @param roleList
     * @param systemList
     * @Author 刘建力
     */
    @Util
    private static List<FunctionInfoDto> markCheckedStatus(List<FunctionInfoDto> roleList, List<FunctionInfoDto> systemList) {

        for (FunctionInfoDto sys : systemList) {
            for (FunctionInfoDto role : roleList) {
                if (role.id == sys.id) {
                    sys.isChecked = true;
                    break;
                }
            }
        }
        return systemList;

    }

    /**
     * 修改角色权限
     *
     * @param roleDto
     * @Author 刘建力
     */
    @QicFunction(id=22)
    public static void updateRoleFunctions(RoleInfoDto roleDto,long uid) {
        roleInfoService.addFunctionInfoByRoleId(roleDto);
        setMessage(SystemResponseMessage.AUTHORIZE_SUCCESS_RSP);
        renderJSON(getSampleResponseMap());
    }

    /**
     * 保存角色基本信息
     *
     * @param txtarea
     * @param id
     * @Author 潘志威
     */
    @QicFunction(id=22)
    public static void rolebasicinfosave(String txtarea, long id,long uid) {
        roleInfoService.saveRoleBasicInfo(txtarea, id);
        renderJSON(getSampleResponseMap());
    }


    /**
     * @param id 当前角色ID
     * @Author 刘泓江
     * 查询最近20个已授权用户和当前角色用户
     */
    @QicFunction(id=22)
    public static void changeRoleList(Long id) {
        List<UserInfo> activedUserList = roleInfoService.queryLastTwentyUser(id);//最近20个已授权用户
        List<UserInfo> roleList = roleInfoService.queryLastTwentyRoleUser(id);//最近20个当前角色用户
        render(activedUserList, roleList);
    }

    /**
     * @param condition 账号/用户名
     * @Author 刘泓江
     * 根据账号查询已激活用户信息
     */
    @QicFunction(id=22)
    public static void getUserByCondition(String condition) {
        List<UserInfo> userList = roleInfoService.queryUserByCondition(condition);
        List<F.T3<Long, String, String>> ujson = new ArrayList<F.T3<Long, String, String>>();
        for (UserInfo userInfo : userList) {
            ujson.add(F.T3(userInfo.id, userInfo.name, userInfo.account));
        }
        renderJSON(ujson);
    }

    /**
     * @param condition 账号/用户名
     * @param roleId    当前角色ID
     * @Author 刘泓江
     * 根据账号查询当前角色用户信息
     */
    @QicFunction(id=22)
    public static void getRoleUserByCondition(String condition, Long roleId) {
        List<UserInfo> userRoleList = roleInfoService.queryRoleUserByCondition(condition, roleId);
        List<F.T3<Long, String, String>> urjson = new ArrayList<F.T3<Long, String, String>>();
        for (UserInfo userInfo : userRoleList) {
            urjson.add(F.T3(userInfo.id, userInfo.name, userInfo.account));
        }
        renderJSON(urjson);
    }

    /**
     * 增加/删除 当前角色
     * @param urid 角色用户列表ID数组
     * @param uids  用户列表ID数组
     * @param rid  当前角色ID
     * @Author 刘泓江
     */
    @QicFunction(id=22)
    public static void changeRole(@As(",") Long[] urid, @As(",") Long[] uids, Long rid) {
        long uid = params.get("uid",Long.class);
        Map<String, Object> json = new HashMap<String, Object>();
        if (roleInfoService.changeRole(urid, uids, rid,uid)) {
            json.put("flag", true);
            json.put("msg", "操作成功");
        } else {
            json.put("flag", false);
            json.put("msg", "操作失败");
        }
        renderJSON(json);
    }
}
