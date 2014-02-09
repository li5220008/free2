package bussiness.user.impl;

import bussiness.common.impl.BaseService;
import bussiness.product.IProductService;
import bussiness.user.IUserInfoService;
import dbutils.SqlLoader;
import models.iquantCommon.*;
import play.Logger;
import play.db.DB;
import play.libs.Crypto;
import play.modules.guice.InjectSupport;
import util.Constants;
import util.LoginTokenCompose;
import util.Tokens;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-5-13
 * Time: 下午3:21
 * 功能描述:
 */
@InjectSupport
public class UserInfoService extends BaseService implements IUserInfoService {
    @Inject
    static IProductService productService;
    /**
     * 根据用户ID 查询用户的角色信息
     *
     * @param uid
     * @return
     */
    public static List<UserRoleDto> findUserRole(long uid) {
        String sql = SqlLoader.getSqlById("findUserRole");
        return qicDbUtil.queryBeanList(sql, UserRoleDto.class, uid);
    }

    public static List<FunctionInfo> findRoleFunctionInfo(long rid) {
        String sql = SqlLoader.getSqlById("findFunctionInfoByRoleId");
        List<FunctionInfo> functionInfoDtoList = qicDbUtil.queryBeanList(sql, FunctionInfo.class, rid);
        return functionInfoDtoList;
    }

    //缓存 后期优化重点
    public static List<FunctionInfo> getUserFunctionIds(long uid) {
        String sql = SqlLoader.getSqlById("getUserFunctionIds");
        return qicDbUtil.queryBeanList(sql, FunctionInfo.class, uid);
    }


    public UserInfo findByAccount(String account) {
        //UserInfo userInfo = UserInfo.find("byAccount", account).first();
        //return userInfo;
        String sql = SqlLoader.getSqlById("findUserInfoByAccount");
        UserInfo userInfo = qicDbUtil.querySingleBean(sql, UserInfo.class, account);
        return userInfo;
    }

    @Override
    public UserInfo findUserById(long uid) {
        String sql = SqlLoader.getSqlById("findUserInfoById");
        UserInfo userInfo = qicDbUtil.querySingleBean(sql, UserInfo.class, uid);
        return userInfo;
    }

    @Override
    public boolean updateUserPwd(long uid, String newPwd) {
        String sql = SqlLoader.getSqlById("updateUserPwd");
        return qicDbUtil.update(sql, newPwd, uid) == 1 ? true : false;
    }

    /**
     * 根据用户ID查询 用户的菜单
     *
     * @param uid
     * @return
     */
    public List<FunctionInfoDto> findUserFunctionInfoById(long uid) {
        String sql = SqlLoader.getSqlById("findUserFunctionInfoById");
        return qicDbUtil.queryBeanList(sql, FunctionInfoDto.class, uid);
    }

    /**
     * 根据用户ID 查询用户的角色信息
     *
     * @param uid
     * @return
     */
    public List<UserRoleDto> findUserRoleInfoById(long uid) {
        String sql = SqlLoader.getSqlById("findUserRole");
        return qicDbUtil.queryBeanList(sql, UserRoleDto.class, uid);
    }

    /**
     * 修改用户基本信息
     *
     * @param userInfoDto
     * @return
     */
    public boolean updateUserInfo(UserInfoDto userInfoDto) {
        //如果用户没有输入密码则查出原来的密码  后期再改成在页面保存原密码
        String sql;
        Object[] params = null;
        //“用户所属部门”在页面上是可选项，为了能让update SQL成功执行 这里做一些特殊处理
        if ("".equals(userInfoDto.saleDept.trim())) {
            userInfoDto.saleDept = null;
        }
        if (null == userInfoDto.password || "".equals(userInfoDto.password.trim())) {
            //没修改密码的情况
            sql = SqlLoader.getSqlById("updateUserInfoWithoutPwd");
            params = new Object[]{
                    userInfoDto.name,
                    userInfoDto.account,
                    userInfoDto.phone,
                    userInfoDto.email,
                    userInfoDto.idCard,
                    userInfoDto.saleDept,
                    userInfoDto.address,
                    userInfoDto.postCode,
                    userInfoDto.capitalAccount,
                    userInfoDto.id
            };
            return qicDbUtil.update(sql, params) > 0;

        } else {//修改了密码的情况
            params = new Object[]{
                    userInfoDto.name,
                    userInfoDto.account,
                    Crypto.passwordHash(userInfoDto.password),
                    userInfoDto.phone,
                    userInfoDto.email,
                    userInfoDto.idCard,
                    userInfoDto.saleDept,
                    userInfoDto.address,
                    userInfoDto.postCode,
                    userInfoDto.capitalAccount,
                    userInfoDto.id
            };
            sql = SqlLoader.getSqlById("updateUserInfo");
            return qicDbUtil.update(sql, params) > 0;

        }


    }

    /**
     * 判断当前节点是否在用户的权限列表中
     *
     * @param list
     * @param id
     * @return
     */
    private boolean isExist(List<FunctionInfoDto> list, long id) {
        //先注释
        /*      if (id == FunctionService.TREE_ROOT_ID) {
            return true;
        } else {
            for (FunctionInfoDto tmp : list) {
                if (tmp.id == id) {
                    return true;
                }
            }
        }*/
        return false;
    }

    /**
     * 删除不在用户权限列表中的菜单节点
     *
     * @param subList
     * @param functionInfoDtoList
     * @deprecated 算法有问题 不用了 以后有时间再解决
     */
    private void filterUserTreeFromSystemTree(List<FunctionInfoDto> subList, List<FunctionInfoDto> functionInfoDtoList) {

        for (int i = 0; subList != null && i < subList.size(); ) {
            boolean isExist = isExist(functionInfoDtoList, subList.get(i).id);
            if (!isExist) {
                //找到没有权限的节点从树中移掉
                i = 0;//之前算法有问题 ，去掉list中的一个元素之后要重新归零 2012-12-10
                subList.remove(i);
            } else {
                //递归判断子节点是否有权限
                filterUserTreeFromSystemTree(subList.get(i).subs, functionInfoDtoList);
                ++i;
            }
        }

    }


    /**
     * 根据email查找用户(email在系统中是唯一的)
     *
     * @param email
     * @return
     */
    public UserInfoDto findUserByEmail(String email) {

        String sql = SqlLoader.getSqlById("findUserByEmail");
        UserInfoDto userInfoDto = qicDbUtil.querySingleBean(sql, UserInfoDto.class, email);
        return userInfoDto;
    }


    /**
     * 新建用户
     *
     * @param userInfo
     * @return
     */
    public List<Long> addUser(UserInfoDto userInfo) {

        List<UserInfoDto> userInfoDtos = new ArrayList<UserInfoDto>();
        userInfoDtos.add(userInfo);
        List<Long> idlist = null;
        try {
            idlist = addUserBatch(userInfoDtos);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return idlist;
        //qicDbUtil.updateQicDB(addusersql, userInfo.name, userInfo.account, userInfo.password, userInfo.phone, userInfo.email, userInfo.idcard, userInfo.saleDep.id, userInfo.capitalAccount, userInfo.address, userInfo.post, sdate, edate,UserInfoDto.UserStatus.WITHOUTACTIVITY.value);
    }

    /**
     * 删除用户
     *
     * @param id 删除用户id数组s
     */
    public void delUser(String id) {
        long systemTime = System.currentTimeMillis();
        String delPre = "del";
        String deleteUserStrategySql = SqlLoader.getSqlById("deleteUserStrategy");
        String delusersql = SqlLoader.getSqlById("softDeleteUser");

        for (String uid : id.split(",")) {
            UserInfo userInfoDto = findUserById(Long.parseLong(uid));
            String newAccount = delPre + userInfoDto.account + "_" + systemTime;
            String newEmail = delPre + userInfoDto.email + "_" + systemTime;
            List<Long> userProductIdList = productService.fetchProductIdByUid(Long.parseLong(uid));
            for(Long productId : userProductIdList){
                Logger.info("删除用户的相关产品,产品id:%s,用户id：%s",productId,uid);
                productService.deleteProduct(productId,Long.parseLong(uid));
            }
            qicDbUtil.update(delusersql,newAccount,newEmail,uid);//软删除用户名，用户邮箱
            //删除产品
            qicDbUtil.update(deleteUserStrategySql, uid);//删除用户的策略


        }
    }


    /**
     * 用户状态修改
     *
     * @param ids    用户id数组
     * @param status 修改状态
     */
    public void userStateModify(String[] ids, int status) {
        String sql = SqlLoader.getSqlById("updateUserStatusById");
        for (String id : ids) {
            qicDbUtil.update(sql, status, id);
        }
    }


    /**
     * 批量添加用户 insert ignore into tb......
     *
     * @param userInfos
     * @return 新增的用户数量
     */
    public List<Long> addUserBatch(List<UserInfoDto> userInfos) throws Exception {
        if (null == userInfos || userInfos.size() == 0) {
            return new ArrayList<Long>(0);
        }
        String sql = SqlLoader.getSqlById("addUserBatch");
        Connection conn = DB.getDBConfig().getConnection();
        String[] columnNames = {"id"};
        PreparedStatement pstmt = conn.prepareStatement(sql, columnNames);
        for (UserInfoDto tmp : userInfos) {
            pstmt.setString(1, tmp.name);
            pstmt.setString(2, tmp.account);
            pstmt.setString(3, Crypto.passwordHash(tmp.password));
            pstmt.setString(4, tmp.phone);
            pstmt.setString(5, tmp.email);
            pstmt.setString(6, tmp.idCard);
            pstmt.setString(7, tmp.saleDept);
            pstmt.setString(8, tmp.address);
            pstmt.setString(9, tmp.postCode);
            pstmt.setString(10, tmp.capitalAccount);
            pstmt.setString(11, Constants.USER_SDATE);
            pstmt.setString(12, Constants.USER_EDATE);
            pstmt.setInt(13, UserInfoDto.UserStatus.WITHOUTACTIVITY.value);
            pstmt.addBatch();
        }
        pstmt.executeBatch();
        // conn.commit();
        List<Long> keys = new ArrayList<Long>(userInfos.size());
        //取得自动生成的主键值的结果集
        ResultSet rs = pstmt.getGeneratedKeys();
        while (rs.next()) {
            keys.add(rs.getLong(1));
            Logger.info(rs.getLong(1) + "");
        }
        return keys;
    }


    /**
     * 到期用户延期
     *
     * @param ids
     * @param delaydate
     */
    public void userdelay(String[] ids, String delaydate) {
        String sql = SqlLoader.getSqlById("updateUserEdateById");
        for (String id : ids) {
            qicDbUtil.update(sql, delaydate, id);
        }
    }

    public UserInfoDto findUserByEmailExcludeSelf(String newEmail, String selfEmail) {
        String sql = SqlLoader.getSqlById("findUserByEmailExcludeSelf");
        UserInfoDto userInfoDto = qicDbUtil.querySingleBean(sql, UserInfoDto.class, newEmail, selfEmail);
        return userInfoDto;
    }

    public UserInfoDto findUserByAccountExcludeSelf(String newAccount, String selfAccount) {
        String sql = SqlLoader.getSqlById("findUserByAccountExcludeSelf");
        UserInfoDto userInfoDto = qicDbUtil.querySingleBean(sql, UserInfoDto.class, newAccount, selfAccount);
        return userInfoDto;
    }

    /**
     * 获得用户数据权限信息
     * @param id 用户ID
     */
    public List<UserDataDto> findUserDataInfoByUid(long id){
        String sql = SqlLoader.getSqlById("findUserDataInfoByUid");
        return qicDbUtil.queryBeanList(sql,UserDataDto.class,id);
    }

    @Override
    public long getUserCount() {
        String sql = SqlLoader.getSqlById("getUserSum");
        long count = qicDbUtil.queryCount(sql);
        return count;
    }

    /**
     * 根据UID查询个人信息
     * @param uid
     * @return
     */
    @Override
    public UserInfoDto  personalmodify(Long uid){
        String sql = SqlLoader.getSqlById("findUserInfoDtoById");
        UserInfoDto userInfo = qicDbUtil.querySingleBean(sql,UserInfoDto.class,uid);
        return userInfo;
    }

    @Override
    public boolean updateUserInfo(UserInfoDto userInfoDto,Long uid) {
        int count;
        String sql = SqlLoader.getSqlById("updateUserInfoById");
        if(userInfoDto.rePassword.equals("")){
             count = qicDbUtil.updateDB(sql,userInfoDto.name,userInfoDto.email,userInfoDto.phone,userInfoDto.address,Crypto.passwordHash(userInfoDto.password),uid);
        }
        else{
            count = qicDbUtil.updateDB(sql,userInfoDto.name,userInfoDto.email,userInfoDto.phone,userInfoDto.address,Crypto.passwordHash(userInfoDto.rePassword),uid);
        }
           return count>0;
    }

    @Override
    public boolean findPwdById(String password, Long uid) {
        String sql = SqlLoader.getSqlById("findUserInfoDtoById");
        UserInfoDto userInfoDto = qicDbUtil.querySingleBean(sql, UserInfoDto.class, uid);
        String oldPwd = userInfoDto.password;
        if ( Crypto.passwordHash(password).trim().equals(oldPwd.trim())) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public UserInfoDto checkEmail(String newEmail,String selfEmail){
        String sql = SqlLoader.getSqlById("checkEmail");
        return qicDbUtil.querySingleBean(sql,UserInfoDto.class,newEmail,selfEmail);
    }

    /**
     * 注册用户
     * @param userRegisterDto
     * @return 是否成功
     */
    public long addUser(UserRegisterDto userRegisterDto){
        StringBuffer sql = new StringBuffer(SqlLoader.getSqlById("registerAddUser"));
        List<Object> sqlParams = new ArrayList<Object>();
        sqlParams.add(userRegisterDto.name);
        sqlParams.add(userRegisterDto.account);
        sqlParams.add(Crypto.passwordHash(userRegisterDto.pwd)); //加密
        sqlParams.add(userRegisterDto.phone);
        sqlParams.add(userRegisterDto.email);
        sqlParams.add(userRegisterDto.idcard);
        sqlParams.add(userRegisterDto.address);
        sqlParams.add(userRegisterDto.postCode);
        sqlParams.add(userRegisterDto.capitalAccount);
        sqlParams.add(Constants.USER_SDATE);
        sqlParams.add(Constants.USER_EDATE);
        sqlParams.add(UserRegisterDto.UserStatus.WITHOUTACTIVITY.value);
        return qicDbUtil.insert(sql.toString(), sqlParams.toArray());
    }

    public List<SaleDepartment> fetchAllDepartment(){
        String sql = SqlLoader.getSqlById("fetchAllDepartment");
        return qicDbUtil.queryBeanList(sql, SaleDepartment.class);
    }


}
