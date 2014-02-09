package bussiness.user.impl;

import bussiness.common.ISystemConfigService;
import bussiness.common.impl.BaseService;
import bussiness.user.IRoleInfoService;
import bussiness.user.IUserAuthorizationService;
import dbutils.SqlLoader;
import models.iquantCommon.*;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import play.Logger;
import play.libs.Mail;
import play.modules.guice.InjectSupport;
import util.CommonUtils;
import util.Constants;
import util.MessageBuilder;
import util.QicConfigProperties;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-7-16
 * Time: 下午1:39
 * 功能描述:
 */
@InjectSupport
public class UserAuthorizationService extends BaseService implements IUserAuthorizationService {
    @Inject
    static ISystemConfigService systemConfigService;
    @Inject
    static IRoleInfoService roleInfoService;
    //找到所有权限名称
    public  List<RoleInfo> findAllRole(){
        List<RoleInfo> list = roleInfoService.findAllRole();
        return list;
    }

    /**
     * 批量插入用户权限
     * @author 刘泓江
     * @param userId 用户id
     * @param roleId 角色id
     * @param flag 1 用户注册,2 激活用户
     * @return    -1  参数不全， -2 时间验证失败， 1 授权成功
     */

    public  int insertUserRole(List<Double> userId,List<Double> roleId,Date edate,int flag){
        int i1 = -1;
        int len = 0;
        String insertUserRoleSql = SqlLoader.getSqlById("insertUserRole");
        String deleteUserRoleSql = SqlLoader.getSqlById("deleteUserRole");
        String updateRoleDateSql = SqlLoader.getSqlById("updateUserRole");
        String findUserByIdSql = SqlLoader.getSqlById("findUserById");
        if(edate==null){
            edate = new Date("2099/09/09");
        }
        if(edate.before(new Date())){
            return -2;
        }
        if(userId == null ||roleId==null){
            return -1;
        }
        if (userId.size() > 0 && roleId.size() > 0) {
            len = (userId.size() * roleId.size());
        }
        Double[][] arrayId = new Double[len][2];

        int k = 0;
        for (int i = 0; i < userId.size(); i++) {
            for (int j = 0; j < roleId.size(); j++) {
                arrayId[k][0] = userId.get(i);
                arrayId[k][1] = roleId.get(j);
                k++;
            }
        }

        //更新时间
        for (Double uid :userId){
            qicDbUtil.update(updateRoleDateSql, edate, uid);
            //删除
            qicDbUtil.update(deleteUserRoleSql, uid);
        }
        //新增
        int[] count = qicDbUtil.batchQicDB(insertUserRoleSql, arrayId);
        if (count.length > 0 && count.length == len) {
            i1 = 1;
        }
        //发邮件给用户，告知账号已激活
        try {
            for(Double uid:userId){
                UserInfoDto userInfo = qicDbUtil.querySingleBean(findUserByIdSql, UserInfoDto.class, uid.intValue());
                if(userInfo!=null&&flag!=Constants.REGISTER_USER){
                    sendActivateMsg(userInfo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //从缓存中删除
        return i1;
    }

    /**
     * 更新用户数据权限
     * @param userId   用户ID
     * @param list
     * @return
     */

    public boolean changeUserDataPermission(long userId,List<UserDataDto> list){
        long count=0;
        String insertUserDataSql = SqlLoader.getSqlById("insertUserData");
        String deleteUserDataSql = SqlLoader.getSqlById("deleteUserData");
        qicDbUtil.update(deleteUserDataSql,userId);    //先删除 在新增
        for(UserDataDto userDataDto :list){
            // qicDbUtil.insert(insertUserDataSql,userId,userDataDto.did,userDataDto.startdate,userDataDto.enddate);
             count++;
        }
        return count==list.size();
    }

    /**
     * 查询用户数据权限
     * @param userId
     * @return
     */
    public List<UserDataDto> findUserDataPermission(long userId){
        String sql = SqlLoader.getSqlById("queryUserDataPermission");
        List<UserDataDto> list = qicDbUtil.queryBeanList(sql,UserDataDto.class,userId);
        return list;
    }


    /**
     * 用户授权 用户列表展示
     * @param userIdArray 用户ID数组
     * @return 用户名 账号 所属营业部
     */
    public  List<ActivateUserDto> getUserList(int[] userIdArray) {
        String selectedUserSql = SqlLoader.getSqlById("selectedUserSql");
        for(int item : userIdArray) {
            selectedUserSql += " a.id = " + item + " or";
        }
        if(selectedUserSql.endsWith("or")) {
            selectedUserSql = selectedUserSql.substring(0, selectedUserSql.length() - 3);
        }
        return qicDbUtil.queryBeanList(selectedUserSql, ActivateUserDto.class);
    }

    /**
     *
     * 用户授权 用户列表展示
     * @param startId 开始ID
     * @param endId  结束ID
     * @return 用户名 账号 所属营业部
     */
    public  List<ActivateUserDto> getUserList(int startId, int endId) {
        String selectMoreUsersql = SqlLoader.getSqlById("selectedMoreUsersql");
        return qicDbUtil.queryBeanList(selectMoreUsersql, ActivateUserDto.class, startId, endId);
    }

    //给已激活的账号的邮箱发送提示信息
    public  void sendActivateMsg(UserInfoDto userInfo) {
        final String sender = QicConfigProperties.get(Constants.EMAIL_SENDER);
        final String name = QicConfigProperties.get(Constants.EMALI_NAME);
        final String title = QicConfigProperties.get(Constants.EMALI_TITLE);
        SendMailDto sendMailDto = new SendMailDto();
        sendMailDto.accepterEmail =  userInfo.email;// 接收者
        sendMailDto.sender = sender;//发送者邮箱
        sendMailDto.name = name; //发送者姓名
        sendMailDto.title = title;//邮件标题
        try {
            String message= systemConfigService.get("activateMsg");//得到模板内容
            MessageBuilder messageBuilder = new MessageBuilder(message);
            messageBuilder.addParameter("userInfo", userInfo);
            String inputVal = messageBuilder.execute();
            sendMailDto.content = inputVal;
            //发送邮箱
            CommonUtils.sendMail(sendMailDto);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
