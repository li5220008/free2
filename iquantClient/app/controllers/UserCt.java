package controllers;

import bussiness.UserAuthorizationService;
import bussiness.UserInfoService;
import bussiness.UserService;
import models.iquantCommon.SaleDepartment;
import models.iquantCommon.UserRegisterDto;
import org.apache.commons.lang.time.DateUtils;
import play.Logger;
import play.data.validation.Valid;
import play.libs.F;
import play.mvc.Controller;
import util.CommonUtils;
import util.Constants;
import util.LoginTokenCompose;
import util.Tokens;

import javax.inject.Inject;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户相关操作
 * User: liangbing
 * Date: 13-6-27
 * Time: 下午3:12
 */
public class UserCt extends Controller {
    @Inject
    static UserService userService;
    @Inject
    static UserInfoService userInfoService;
    @Inject
    static UserAuthorizationService userAuthorizationService;


    public static void gotoLogin() {
        render();
    }


    /**
     * 用户登陆验证
     *
     * @param name    用户名
     * @param pwd     密码
     * @param macAddr mac地址
     */
    public static void login(String name, String pwd, String macAddr) {

        Long pid = 2L; //先用一个固定的
        F.T2<Boolean, String> t2 = userService.validateLogin(name, pwd, macAddr, pid);
        Boolean success = t2._1;//用户名和密码是否正确
        String msg = t2._2;//操作信息
        if (success) {
            LoginTokenCompose loginTokenCompose = Tokens.decryptLoginToken(msg);
            session.put(AuthBaseIntercept.USER_ID_SESSION_KEY, loginTokenCompose.uid); //写入session数据
            response.setCookie("ntToken", msg);
            renderTemplate("@list");
        } else {
            params.flash("name");
            validation.addError("errorMsg", msg);
            renderTemplate("@gotoLogin");
        }
    }

    public static void register(@Valid UserRegisterDto userRegisterDto) {
        if (userRegisterDto == null) {
            render();
        }
        Map<String, Object> json = new HashMap();
        int l = (int)userService.addUser(userRegisterDto);//注册用户返回用户的Id
        int uid[] ={l}; //用户ID
        int rid[] ={7};//试用权限ID
        Date endDate = DateUtils.addMonths(new Date(), 6); //当前时间向后推半年
        userRegisterDto.eDate = CommonUtils.getFormatDate("yyyy-MM-dd", endDate);
        int flag =  userAuthorizationService.insertUserRole(uid, rid, endDate, Constants.REGISTER_USER);
        if(flag==1){
            Logger.info("userAuthorizationService.insertUserRole","授权成功");
        }else{
            Logger.info("userAuthorizationService.insertUserRole","授权发生异常");
        }
        userService.sendEmail(userRegisterDto);//新增用户,給它赋予试用用户权限半年

        if (l>0) {
            json.put("message", "注册成功");
        } else {
            json.put("message", "注册失败");
        }
        renderJSON(json);
    }

    /**
     * 检验Email
     * @param email
     */
    public static void findUserByEmail(String email) {
        boolean flag = userInfoService.findUserByEmail(email) == null;
        renderText(flag);
    }

    /**
     * 验证账户
     *
     * @param account
     */
    public static void findUserByAccount(String account) {
        boolean flag = userInfoService.findUserByAccount(account) == null;
        renderText(flag);

    }

}
