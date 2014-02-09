package bussiness;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import models.iquantCommon.*;
import org.apache.commons.lang.StringUtils;
import play.Logger;
import play.libs.F;
import play.modules.guice.InjectSupport;
import protoc.HttpBody;
import protoc.Protocol;
import protoc.ResponseHeader;
import protoc.URILib;
import protoc.parser.ActionResult;
import util.*;

import javax.inject.Inject;
import java.util.List;

/**
 * 用户相关的业务操作
 * User: liangbing
 * Date: 13-6-27
 * Time: 下午3:11
 */
@InjectSupport
public class UserService extends BasicService{
    @Inject
    static SystemConfigService systemConfigService;
    /**
     * 登陆验证方法
     * @param name 用户名
     * @param pwd 密码
     * @param macAddr mac 地址
     * @param pid
     * @return t2._1 用户名和密码是否正确  ,t2._2 操作信息
     */
    public static F.T2<Boolean, String> validateLogin(String name, String pwd, String macAddr,long pid) {
        String mac = null; //TODO 这里随机产生一个
        if (!StringUtils.isBlank(macAddr)) {
            mac = macAddr;
        }else{
            mac = createMacArr();
        }
        JsonElement json = remoteRequestService.getJson(URILib.SSO_LOGIN, name, pwd, mac, pid);
        boolean success = false;
        String msg = "";
        if (json.isJsonObject()) {
            JsonObject jsonObject = json.getAsJsonObject();
            success = jsonObject.get(Protocol.GlobalFieldName.SUCCESS).getAsBoolean();
            if (success) {
                msg = jsonObject.get(Protocol.GlobalFieldName.TOKEN).getAsString();
            } else {
                msg = jsonObject.get(Protocol.GlobalFieldName.MESSAGE).getAsString();
            }
        }
        return F.T2(success, msg);
    }


      public static void list(int pageNo, Long uid)  {
          ActionResult<List<UserTemplate>> result =remoteRequestService.getList("url", UserTemplate.class,String.valueOf(pageNo),String.valueOf(uid));

          List<UserTemplate> userTemplateList = result.data;

          ResponseHeader header = result.header;



      }

    /**
     * 创建MAC地址
     * @return
     */
    private static String createMacArr(){
        String remoteIp =null; //IpInterceptor.getRemoteIp();
        if (remoteIp == null) {
            Logger.error("没有取到客户端ip, 给个默认值:256");
            remoteIp = "256";
        }
        String tmp = "00" + Integer.toHexString(remoteIp.hashCode() % 256);

        return "E06F05EA4E" + tmp.substring(tmp.length()-2);
    }

    /**
     * 根据用户名、账号、邮箱 查找到该用户
     * @param name
     * @param account
     * @param email
     * @return
     */
    public static F.T2<Boolean, UserInfo> findbyNameAndAccountAndEmail(String name, String account, String email){
        ActionResult<UserInfo> result = remoteRequestService.getBean(URILib.FETCH_USER_WITH_ACCOUNT, UserInfo.class, account);
        boolean success = false;
        UserInfo userInfo = result.data;
        if (userInfo!=null)
        if (userInfo.name.equals(name) && userInfo.email.equals(email)) {
            success = true;
        }
        return F.T2(success, userInfo);
    }

    /**
     * 更新用户密码
     7* @param uid
     * @param newPwd
     * @return
     */
    public static boolean updateUserPwd(long uid, String newPwd){

        JsonElement json = remoteRequestService.getJson(URILib.UPDATE_USER_PWD, uid, newPwd);
        boolean success = false;
        if (json.isJsonObject()) {
            JsonObject jsonObject = json.getAsJsonObject();
            success = jsonObject.get(Protocol.GlobalFieldName.STATUS).getAsInt() == 0 ? true :false ;
        }
        return success;
    }

    public static long addUser(UserRegisterDto userRegisterDto){
        HttpBody httpBody = new HttpBody();
        String json = GsonUtil.createWithoutNulls().toJson(userRegisterDto) ;
        httpBody.body = json;
        return Long.valueOf(remoteRequestService.getSingleValue(URILib.ADD_USER,httpBody).data);
    }

    public List<SaleDepartment> fetchAllSaleDepartment() {
        return remoteRequestService.getList(URILib.FETCH_ALL_SALEDEPARTMENT, SaleDepartment.class).data;
    }

    /**
     * 注册用户給用户发邮件提示
     * @param userInfo
     */
    public static void sendEmail(UserRegisterDto userInfo){
        SendMailDto sendMailDto = new SendMailDto();
        sendMailDto.accepterEmail =  userInfo.email;// 接收者

        final String sender = QicConfigProperties.get(Constants.EMAIL_SENDER);
        final String name = QicConfigProperties.get(Constants.EMALI_NAME);
        final String title = "帐号注册成功";

        sendMailDto.sender = sender;//发送者邮箱
        sendMailDto.name =name; //发送者姓名
        sendMailDto.title = title;//邮件标题

        try {
            String message = systemConfigService.get("registerUser");//得到模板内容
            MessageBuilder messageBuilder = new MessageBuilder(message);
            messageBuilder.addParameter("userInfo",userInfo);
            String inputVal = messageBuilder.execute();
            sendMailDto.content = inputVal;
            //发送邮箱
            CommonUtils.sendMail(sendMailDto);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
