package bussiness;

import com.google.common.base.Joiner;
import models.iquantCommon.ActivateUserDto;
import models.iquantCommon.RoleInfo;
import protoc.HttpBody;
import protoc.URILib;
import util.GsonUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 用户授权
 * User: liuhongjiang
 * Date: 12-12-5
 * Time: 下午6:34
 */
public class UserAuthorizationService extends UserInfoBaseService {
    //找到所有权限名称
    public static List<RoleInfo> findAllRole(){
        List<RoleInfo> list = RoleInfoService.findAllRole();
        return list;
    }

    /**
     * 批量插入用户权限
     * @author 刘泓江
     * @param userId 用户id
     * @param roleId 角色id
     * @return  flag  -1  参数不全， -2 时间验证失败， 1 授权成功
     */
    public static int insertUserRole(int[] userId,int[] roleId,Date endDate,int flag){
        String edate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(endDate);
        Map map = vargsToMap(userId,roleId);
        String json  = GsonUtil.createWithoutNulls().toJson(map);
        HttpBody httpBody = new HttpBody();
        httpBody.body=json;
        for(int id :userId){
            //清空能缓存
            deleteUserFromCache(id);
        }
        return Integer.valueOf(remoteRequestService.getSingleValue(URILib.BATCH_INSERT_USER_ROLE,httpBody,edate,flag).data);
    }


    /**
     * 用户授权 用户列表展示
     * @param userIds 用户ID数组
     * @return 用户名 账号 所属营业部
     */
    public static List<ActivateUserDto> getUserList(Integer[] userIds) {
        Joiner joiner = Joiner.on(",");
        String userIdArray=joiner.join(userIds);
        return remoteRequestService.getList(URILib.FETCH_USER_LIST_BY_USERIDARRAY,ActivateUserDto.class,userIdArray).data;
    }

    /**
     *
     * 查找startId和endId之间的用户信息
     * @param startId 开始ID
     * @param endId  结束ID
     * @return 用户名 账号 所属营业部
     */
    public static List<ActivateUserDto> getUserList(int startId, int endId) {
        return remoteRequestService.getList(URILib.FETCH_DEPARTMENTUSER,ActivateUserDto.class,startId,endId).data;
    }

    /*   //给已激活的账号的邮箱发送提示信息
    public static void sendActivateMsg(UserInfoDto userInfo) {
        HtmlEmail email = new HtmlEmail();
        email.setCharset("UTF-8");// 编码格式
        email.setHostName("smtp.163.com");// smtp服务器
        try {
            email.addTo(userInfo.email);// 接收者
            email.setFrom("gta_qic@163.com", "超级管理员");// 发送者，姓名
            email.setSubject("账号激活通知");// 邮件标题
            email.setAuthentication("gta_qic@163.com", "gta123");// 用户名，密码
            String message= SystemConfigService.get("activateMsg");//得到模板类容
            MessageBuilder messageBuilder = new MessageBuilder(message);
            messageBuilder.addParameter("userInfo",userInfo);
            String inputVal = messageBuilder.execute();
            email.setMsg(inputVal);// 发送内容
            email.send();
        } catch (EmailException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}