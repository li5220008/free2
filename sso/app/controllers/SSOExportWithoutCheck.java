package controllers;

import business.UserService;
import models.iquantCommon.UserDataDto;
import models.iquantCommon.UserInfo;
import play.Logger;
import play.Play;
import play.data.validation.Required;
import play.db.jpa.NoTransaction;
import play.i18n.Messages;
import play.libs.Crypto;
import play.modules.redis.Redis;
import play.mvc.Controller;
import play.mvc.Util;
import play.mvc.With;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;
import util.GsonUtil;
import util.LoginTokenCompose;
import util.RedisKeys;
import util.Tokens;

import javax.inject.Inject;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static util.ConstantInterface.*;

/**
 * sso暴露出来的接口,不需要loginToken校验
 * User: wenzhihong
 * Date: 12-4-18
 * Time: 上午11:55
 */
@With(value = {GeneralIntercept.class})
public class SSOExportWithoutCheck extends Controller {
    @Inject
    static UserService userService;
    /**
     * http上参数: u : 用户名
     * p: md5加密后的密码
     * mac : 登陆的mac地址
     * returnDataPermission:是否返回对应的数据权限
     * 登陆成功, 返回token
     * 不成功, 返回失败信息
     * 返回json对象
     */
    public static void login(@Required(message = USER_NAME_REQUIRED) String u,
                             @Required(message = PWD_REQUIRED) String p,
                             @Required(message = MAC_REQUIRED) String mac,
                             @Required(message = PID_REQUIRED) Long pid,
                             String type) {

        String crytoPwd = p; //这里假设传过来的密码是经过md5加密的
        doProcessLogin(u, crytoPwd, mac, pid,type);
    }

    /**
     * http上参数: u : 用户名
     * p: 密码, 不经过md5加密
     * mac : 登陆的mac地址
     * returnDataPermission:是否返回对应的数据权限
     * 登陆成功, 返回token
     * 不成功, 返回失败信息
     * 返回json对象
     */
    public static void loginWithoutCryp(@Required(message = USER_NAME_REQUIRED) String u,
                                        @Required(message = PID_REQUIRED) String p,
                                        @Required(message = MAC_REQUIRED) String mac,
                                        @Required(message = PID_REQUIRED) Long pid,
                                        String type) {

        String crytoPwd = Crypto.passwordHash(p);
        doProcessLogin(u, crytoPwd, mac, pid,type);
    }
    private static List<UserDataDto> getDataPermissionInfo(UserInfo userInfo){
        long uid = userInfo.id;
        return userService.findUserDataInfo(uid);
    }
    /**
     * 这只是一个用于提取共用的登陆处理信息方法
     */
    @Util
    private static void doProcessLogin(String u, String crytoPwd, String mac, Long pid,String type) {
        boolean loginSucc = false; //是否登陆成功
        String mes = null;
        //是否允许mac挤出
        boolean permitCrowdOut = Boolean.parseBoolean(Play.configuration.getProperty("login.permit.rand_crowd_out", "false"));
        UserInfo userInfo = userService.findByAccount(u);
        if (userInfo == null) { //找不到用户
            mes = Messages.get("api.login.user.nofound");
        }else if(userInfo.status == UserInfo.UserStatus.WITHOUTACTIVITY.value){
            mes = Messages.get("api.login.user.forbbiden");

        }else if(userInfo.sdate == null || userInfo.sdate.after(new Date())){
            mes = Messages.get("api.login.user.forbbiden");

        }else if(userInfo.edate == null || userInfo.edate.before(new Date())){
            mes = Messages.get("api.login.user.timeout");

        }else if (crytoPwd.equals(userInfo.pwd) == false) { //密码不对
            mes = Messages.get("api.login.user.errorPwd");
        }

        if (mes != null) {
            Map<String, Object> resultJson = new HashMap<String, Object>(2);
            resultJson.put("success", false);
            resultJson.put("message", mes);
            renderJSON(resultJson);
        }
        //检查checkSum
      /*  if(! userService.checkByAccount(u)){
            Map<String, Object> resultJson = new HashMap<String, Object>(2);
            resultJson.put("success", false);
            resultJson.put("message", "api.login.user.errorCheckSum");
            renderJSON(resultJson);
        }*/

        //提前先创建token, 因为下面的redis涉及到网络操作.
        String token = Tokens.createLoginToken(userInfo.account, userInfo.id, mac, pid);

        String userMacSetKey = RedisKeys.userMacSetKey(userInfo.account);

        boolean checkMaxLogin = Boolean.parseBoolean(Play.configuration.getProperty("login.check.maxLogin", "false"));
        Logger.info("用户%s,最大登入数为:%s",  userInfo.account,userInfo.maxLogin);
        if(checkMaxLogin) {

            //<editor-fold desc="到redis里去校验 用户 是否达到最大登陆数, 及是否已包含在mac地址列表里. 更新redis里的信息">
            Pipeline p = Redis.pipelined();
            Response<Long> macSetSizeRes = p.scard(userMacSetKey);
            Response<Boolean> macIsInRes = p.sismember(userMacSetKey, mac);
            p.sync();

            Long macSetSize = macSetSizeRes.get();
            Boolean macIsIn = macIsInRes.get();

            boolean crowdOut = false;

            if (macIsIn) {
                Logger.info("mac[%s]已在%s的mac地址列表里", mac, userInfo.account);
                loginSucc = true;
            } else if (macSetSize == 0) {
                Logger.info("%s首次登陆, mac[%s]", userInfo.account, mac);
                loginSucc = true;
            } else if (macSetSize < userInfo.maxLogin) { //还有登陆数可用
                Logger.info("%s还有登陆数可用,当前登陆数为%d,最大登陆数为%d. mac[%s]", userInfo.account, macSetSize, userInfo.maxLogin, mac);
                loginSucc = true;
            } else {
                loginSucc = false;
                if (permitCrowdOut) { //挤下用户
                    crowdOut = true;
                    loginSucc = true;
                } else {
                    Logger.info("%s mac[%s]不能登陆,因已达到最大登陆数%d", userInfo.account, mac, userInfo.maxLogin);
                    mes = Messages.get("api.login.user.maxlogin");
                }
            }

            //往redis上更新信息
            if (loginSucc) {
                Pipeline updatePipe = Redis.pipelined();
                Response<String> crowOutMacRes = null;
                if (crowdOut) {
                    crowOutMacRes = updatePipe.spop(userMacSetKey); //随机移除
                }
                if (macIsIn == false) { //mac地址不在列表里, 则添加
                    updatePipe.sadd(userMacSetKey, mac);
                }

                //更新token信息
                String userMacTokenKey = RedisKeys.userMacTokenKey(mac);
                updatePipe.setex(userMacTokenKey, Tokens.LOGIN_TOKEN_LIVE_SECOND, token);

                updatePipe.sync();

                //把被挤下的mac的相应的token移除
                if (crowdOut) {
                    String crowOutMac = crowOutMacRes.get();
                    if (crowOutMac != null) {
                        Redis.del(new String[]{RedisKeys.userMacTokenKey(crowOutMac)});
                        Logger.info("%s挤下mac[%s],让当前mac[%s]登陆", userInfo.account, crowOutMac, mac);
                    }
                }
            }
            //</editor-fold>

        }

        Map<String, Object> jsonMap = new HashMap<String, Object>(2);

        if (loginSucc) {
            jsonMap.put("success", true);
            jsonMap.put("token", token);
            if(type!=null && "api".equals(type.toLowerCase().trim())){
                Logger.info("api调用 获得用户数据权限信息");
                List<UserDataDto> list = getDataPermissionInfo(userInfo);
                jsonMap.put("power",list);
            }
        } else {
            jsonMap.put("success", false);
            jsonMap.put("message", mes);
        }

        if(type!=null && "showedate".equals(type.toLowerCase().trim())){
            jsonMap.put("edate",userInfo.edate);
        }

        Logger.info("登入结果：是否成功:%s，取得的token：%s", loginSucc,token);
        String jsonStr = GsonUtil.createWithoutNullsDisableHtmlEscaping().toJson(jsonMap);
        System.out.println(jsonStr);
        renderJSON(jsonStr);
    }

    /**
     * 检验登陆token是否有效. 返回json对象
     *
     * @param token
     * @clientId 客户端传给我的clientId, 用于标识请求
     */
    @NoTransaction
    public static void checkLoginToken(@Required(message = TOKEN_REQUIRED) String token, String clientId,String type) {
        Logger.info(" start ip:%s验验证token,token:%s", request.remoteAddress,token);
        int validResult = Tokens.checkValidateLoginToken(token) ? 1 : 0;
        Map<String, Object> jsonMap = new HashMap<String, Object>(2);
        jsonMap.put("clientId", clientId);
        jsonMap.put("validResult", validResult);
        Logger.info("finish ip:%s验验证token,token:%s,结果:%s", request.remoteAddress,token,validResult);
        if(type!=null && "api".equals(type.toLowerCase().trim())){
            Logger.info("api调用 获得用户数据权限信息");
            String u="";
            LoginTokenCompose loginTokenCompose = Tokens.decryptLoginToken(token);
            if(loginTokenCompose!=null) {
                u =  loginTokenCompose.userName;
                UserInfo userInfo = userService.findByAccount(u);
                List<UserDataDto> list = getDataPermissionInfo(userInfo);
                jsonMap.put("username",u);
                jsonMap.put("power",list);
            }
        }
        String jsonStr = GsonUtil.createWithoutNullsDisableHtmlEscaping().toJson(jsonMap);
        renderJSON(jsonStr);
    }
}
