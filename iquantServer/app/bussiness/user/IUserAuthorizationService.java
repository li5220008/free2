package bussiness.user;

import models.iquantCommon.ActivateUserDto;
import models.iquantCommon.RoleInfo;
import models.iquantCommon.UserDataDto;
import models.iquantCommon.UserInfoDto;
import play.libs.F;

import java.util.Date;
import java.util.List;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-7-16
 * Time: 上午10:40
 * 功能描述:
 */
public interface IUserAuthorizationService {


    //找到所有权限名称
    public  List<RoleInfo> findAllRole();

    /**
     * 批量插入用户权限
     * @author 刘泓江
     * @param userId 用户id
     * @param roleId 角色id
     * @param flag 1 用户注册,2 激活用户
     * @return    -1  参数不全， -2 时间验证失败， 1 授权成功
     */

    public  int insertUserRole(List<Double> userId,List<Double> roleId,Date edate,int flag);

    /**
     * 更新用户数据权限
     * @param userId   用户ID
     * @param list
     * @return
     */

    public boolean changeUserDataPermission(long userId,List<UserDataDto> list);
    /**
     * 查询用户数据权限
     * @param userId
     * @return
     */
    public List<UserDataDto> findUserDataPermission(long userId);

    /**
     * 用户授权 用户列表展示
     * @param userIdArray 用户ID数组
     * @return 用户名 账号 所属营业部
     */
    public  List<ActivateUserDto> getUserList(int[] userIdArray) ;

    /**
     *
     * 用户授权 用户列表展示
     * @param startId 开始ID
     * @param endId  结束ID
     * @return 用户名 账号 所属营业部
     */
    public  List<ActivateUserDto> getUserList(int startId, int endId) ;
    //给已激活的账号的邮箱发送提示信息
    public  void sendActivateMsg(UserInfoDto userInfo) ;


}
