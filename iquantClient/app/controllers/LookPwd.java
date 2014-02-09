package controllers;

import bussiness.LookPwdService;
import bussiness.UserService;
import com.google.gson.Gson;
import models.iquantCommon.UserInfo;
import play.libs.Crypto;
import play.libs.F;
import play.mvc.Controller;
import protoc.URILib;
import protoc.parser.ActionResult;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * desc: 找回密码功能(用户可以通过  用户名,帐号,邮箱 找回密码)
 * User: weiguili(li5220008@163.com)
 * Date: 13-7-1
 * Time: 下午3:16
 */
public class LookPwd extends Controller {
    @Inject
    static UserService userService;
    @Inject
    static LookPwdService lookPwdService;

    /**
     * 页面展示
     */
    public static void list() {
        render();
    }

    /**
     * 找回密码
     * @param ui 用户信息
     */
    public static void lookPwd(UserInfo ui){

        String newPwd = String.valueOf(new Double(Math.random() * 1000000).intValue());
        Map<String, Object> json = new HashMap<String, Object>();
        F.T2<Boolean, UserInfo> t2 =  userService.findbyNameAndAccountAndEmail(ui.name,ui.account,ui.email);
        UserInfo userInfo = t2._2;
        if (t2._1) { //找到用户
            userInfo.setPwdWithHash(newPwd);
            if(userService.updateUserPwd(userInfo.id, userInfo.pwd)){
                lookPwdService.sendMsg(userInfo,newPwd); //发邮箱方法, email 和 新密码
                json.put("msg", "密码已通过邮箱发送给你,请查收!");
                json.put("flag", true);
            }else{
                json.put("msg", "更新数据库密码时候出错了!");
                json.put("flag", false);
            }
        } else {
            json.put("msg", "用户名或帐号或邮箱不正确,请重试!");
            json.put("flag", false);
        }
        renderJSON(json);
    }
}
