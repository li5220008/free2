package bussiness.user;

import models.iquantCommon.*;

import java.util.List;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-6-28
 * Time: 上午11:48
 * 功能描述:
 */
public interface IUserInfoService {

    public UserInfo findByAccount(String account);

    public UserInfo findUserById(long uid);

    public boolean updateUserPwd(long uid, String newPwd);


    /**
     * 根据用户ID查询 用户的菜单
     *
     * @param uid
     * @return
     */
    public List<FunctionInfoDto> findUserFunctionInfoById(long uid);

    /**
     * 根据用户ID 查询用户的角色信息
     *
     * @param uid
     * @return
     */
    public List<UserRoleDto> findUserRoleInfoById(long uid);


    /**
     * 修改用户基本信息
     *
     * @param userInfoDto
     * @return
     */
    public boolean updateUserInfo(UserInfoDto userInfoDto);


    /**
     * 根据email查找用户(email在系统中是唯一的)
     *
     * @param email
     * @return
     */
    public UserInfoDto findUserByEmail(String email);


    /**
     * 新建用户
     *
     * @param userInfo
     * @return
     */
    public List<Long> addUser(UserInfoDto userInfo);

    /**
     * 删除用户
     *
     * @param ids 删除用户ids
     */
    public void delUser(String ids);


    /**
     * 用户状态修改
     *
     * @param ids    用户id数组
     * @param status 修改状态
     */
    public void userStateModify(String[] ids, int status);

    /**
     * 批量添加用户 insert ignore into tb......
     *
     * @param userInfos
     * @return 新增的用户数量
     */
    public List<Long> addUserBatch(List<UserInfoDto> userInfos) throws Exception ;


    /**
     * 到期用户延期
     *
     * @param ids
     * @param delaydate
     */
    public void userdelay(String[] ids, String delaydate);


    public UserInfoDto findUserByEmailExcludeSelf(String newEmail, String selfEmail);

    public UserInfoDto findUserByAccountExcludeSelf(String newAccount, String selfAccount);

    /**
     * 获得用户数据权限信息
     * @param id 用户ID
     */
    public List<UserDataDto> findUserDataInfoByUid(long id);

    //查询用户总数
    public long getUserCount();

    public UserInfoDto  personalmodify(Long uid);

    public boolean updateUserInfo(UserInfoDto userInfoDto,Long uid);

    public boolean findPwdById(String password, Long uid);

    public UserInfoDto checkEmail(String newEmail,String selfEmail);

    /**
     * 注册用户
     * @param userRegisterDto
     * @return 是否成功
     */
    public long addUser(UserRegisterDto userRegisterDto);

    public List<SaleDepartment> fetchAllDepartment();


}
