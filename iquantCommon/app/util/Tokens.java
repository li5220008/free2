package util;

import com.google.common.collect.Sets;
import org.apache.commons.lang.StringUtils;
import play.Logger;
import play.Play;
import play.exceptions.UnexpectedException;
import play.libs.Crypto;
import play.modules.redis.Redis;
import play.mvc.Http;
import play.mvc.Scope;

import java.util.Date;
import java.util.Set;

/**
 * 这里是进行token的产生与解析集中的地方
 * User: wenzhihong
 */
public class Tokens {
    //要覆盖token里的uid的http参数名称,
    public static final String OVERRIDE_TOKEN_UID_PARAM_NAME = "__opUid";

    public static final String TOKEN_IN_REND = "__ntToken";

    /**
     * 用于标识过期的token. 这是一个特殊的值
     */
    public final static String EXPIRE_TOKEN = "__EXPIRE_TOKEN";

    public static final String SPLIT_CON = "@@";

    public static final String LOGIN_TOKEN_PRE = Play.configuration.getProperty("app.login.token.pre", "gtaNT");

    //token生存的秒数
    public static final int LOGIN_TOKEN_LIVE_SECOND = Integer.parseInt(Play.configuration.getProperty("app.login.token.liveSecond", String.valueOf(60 * 60 * 1)));

    //token生存的毫秒数
    public static final long LOGIN_TOKEN_LIVE_MSEC = LOGIN_TOKEN_LIVE_SECOND * 1000L;

    public static final String PWD_TOKEN_PRE = Play.configuration.getProperty("app.pwd.token.pre", "gtaPWD");
    public static final long PWD_TOKEN_LIVE_MSEC = Long.parseLong(Play.configuration.getProperty("app.pwd.token.liveSecond", String.valueOf(10L*60L))) * 1000;

    /**
     * 是否到redis上校验token是否有效,也就是是否还存在
     */
    public static boolean checkTokenFromRedis = Boolean.parseBoolean(Play.configuration.getProperty("token.check.redis", "false"));


    /**
     * 登陆token, 这个token会用于以后的所有的请求
     * @param userName 用户名
     * @param uid  用户id
     * @param mac  登陆的mac地址
     * @return
     */
    public static String createLoginToken(String userName, Long uid,  String mac, Long pid) {
        StringBuilder tokenSeed = new StringBuilder(LOGIN_TOKEN_PRE); //0

        tokenSeed.append(SPLIT_CON);
        tokenSeed.append(new Date().getTime()); //1

        tokenSeed.append(SPLIT_CON);
        tokenSeed.append(userName); //2

        tokenSeed.append(SPLIT_CON);
        tokenSeed.append(uid); //3

        tokenSeed.append(SPLIT_CON);
        tokenSeed.append(mac); //4

        tokenSeed.append(SPLIT_CON);
        tokenSeed.append(pid); //5

        String token = Crypto.encryptAES(tokenSeed.toString());
        return token;
    }

    /**
     *  解密登陆token信息
     * @param token  token字符串
     * @return 返回解密后的LoginTokenCompose对象, 如果解密不成功, 返回null
     */
    public static LoginTokenCompose decryptLoginToken(String token){
        if (StringUtils.isEmpty(token)) {
            Logger.info("token:%s", token);
            return null;
        }
        String decStr = null;
        try {
            decStr = Crypto.decryptAES(token);
        } catch (UnexpectedException e) { //解析失败, 也就是说还原不回来, 也认为是失败
            Logger.warn("解析登陆token失败");
            return null;
        }
        String[] splitStrArr = decStr.split(SPLIT_CON);
        if (splitStrArr.length == 6 && LOGIN_TOKEN_PRE.equals(splitStrArr[0])) {
            long createTime = 0;
            try {
                createTime = Long.parseLong(splitStrArr[1]);
            } catch (NumberFormatException e) {
                Logger.warn("解析long型(创建时间)(%s)出错", splitStrArr[1]);
                return null;
            }

            long uid = 0;
            try {
                uid = Long.parseLong(splitStrArr[3]);
            } catch (NumberFormatException e) {
                Logger.warn("解析long型(u_id)(%s)出错", splitStrArr[3]);
                return null;
            }

            long pid = 0;
            try {
                pid = Long.parseLong(splitStrArr[5]);
            } catch (NumberFormatException e) {
                Logger.warn("解析long型(p_id)(%s)出错", splitStrArr[5]);
                return null;
            }

            if (tokenIsExpire(createTime)) {
                LoginTokenCompose compose = new LoginTokenCompose();
                compose.userName = splitStrArr[2];
                compose.uid = uid;
                compose.pid = pid;
                compose.mac = splitStrArr[4];
                compose.createTime = createTime;

                //检查是否有http上的参数要对tokenCompose上的uid进行覆盖
                Http.Request request = Http.Request.current();
                if (StringUtils.isNotBlank(request.querystring)) {
                    String[] keyValues = request.querystring.split("&");
                    String opUid = null;
                    for (String keyValue : keyValues) {
                        int i = keyValue.indexOf('=');
                        String key=null;
                        String value=null;
                        if ( i > 0) {
                            key = keyValue.substring(0,i);
                            value = keyValue.substring(i+1);
                        } else {
                            key = keyValue;
                        }
                        if (OVERRIDE_TOKEN_UID_PARAM_NAME.equals(key)) {
                            opUid = value;
                            break;
                        }
                    }
                    if (opUid != null) {
                        opUid = opUid.trim();
                        try {
                            long opUidVal = Long.parseLong(opUid);
                            if (opUidVal != compose.uid) {
                                Logger.info("在http的queryString里包含要覆盖token里的uid参数[%s],且值为[%s], 现在进行覆盖, 原始uid值为[%d]",
                                                                    OVERRIDE_TOKEN_UID_PARAM_NAME, opUid, compose.uid);
                                compose.uid = opUidVal;
                            }
                        } catch (NumberFormatException e) {
                            Logger.warn("解析opUid[%s]为long类型出错", opUid);
                        }
                    }
                }

                return compose;
            }else{
                //处理过期的token, 1. 把token的key删除掉, 同时把在set里面的给去除掉
                String account =  splitStrArr[2];
                String mac = splitStrArr[4];
                String userMacSetKey = RedisKeys.userMacSetKey(account);
                String userMacTokenKey = RedisKeys.userMacTokenKey(mac);
                Redis.del(new String[]{userMacTokenKey});
                Redis.srem(userMacSetKey, mac);
            }
        }

        return null;
    }

    /**
     * token是否过期
     * @param createTime token创建时间
     */
    public static boolean tokenIsExpire(long createTime){
        long curTime = System.currentTimeMillis();
        long disMsec = curTime - createTime;
        if (disMsec < 0) {
            Logger.warn("服务器时间不同步createTime=%d,systemTime=%d,disTime=%d", createTime, curTime, disMsec);
        }

        return disMsec < LOGIN_TOKEN_LIVE_MSEC;
    }

    /**
     * 检查登陆的token是否有效性
     * @param token token字符串
     * @return  有效true, 无效false
     */
    public static boolean checkValidateLoginToken(String token) {
        return checkValidateLoginTokenAndSaveToRender(token, null);
    }

    /**
     * 检查登陆的token是否有效性, 并把解析出来的对象放在Render作用域上
     * @param token token字符串
     * @return  有效true, 无效false
     */
    static Set<String> apiIpAddresses = Sets.newHashSet("192.168.103.109", "10.228.2.36", "202.104.122.211");
    public static boolean checkValidateLoginTokenAndSaveToRender(String token, Scope.RenderArgs renderArgs){
        LoginTokenCompose compose = decryptLoginToken(token);

        if(renderArgs != null && compose != null){
            renderArgs.put(TOKEN_IN_REND, token);
            compose.saveToRender(renderArgs);
        }

        if (compose == null) {
            return false;
        }else{
            //TODO 这里加上对api直接跳过的请求处理
            String remoteAddress = Http.Request.current().remoteAddress;
            if(remoteAddress != null && apiIpAddresses.contains(remoteAddress)){
                Logger.info("api服务器过来的请求, 不到redis上校验, 直接通过");
                return true;
            }

            if (checkTokenFromRedis) {
                String userMacSetkey = RedisKeys.userMacTokenKey(compose.mac);
                String t = Redis.get(userMacSetkey); //看token在redis上是否存在
                if(t == null){
                    Logger.info("token[%s]符合格式, 但是已被清除出redis,也属于不合法token", token);
                }
                return t != null;
            }else {
                return true;
            }
        }
    }

    /**
     * 产生找回密码token
     * @param userName
     * @return
     */
    public static String createPwdToken(String userName){
        String tokenSeed = PWD_TOKEN_PRE + SPLIT_CON + new Date().getTime() + SPLIT_CON + userName;
        String token = Crypto.encryptAES(tokenSeed);
        return token;
    }

    /**
     * 检查找回密码token有效性,并返回userName
     * 这里要特别注意返回的userName可能为Tokens.EXPIRE_TOKEN,它表示token过期
     * @param token
     * @return 正确解析且token还没有过期,返回userName, token过期,则返回 Tokens.EXPIRE_TOKEN; 其它则返回null
     */
    public static String checkValidatePwdToken(String token){
        if(StringUtils.isEmpty(token)){
            return null;
        }
        String decStr = null;
        try {
            decStr = Crypto.decryptAES(token);
        } catch (UnexpectedException e) {
            Logger.warn("解析找回密码token失败");
            return null;
        }
        String[] splitStrArr = decStr.split(SPLIT_CON);
        if (splitStrArr.length == 3 && PWD_TOKEN_PRE.equals(splitStrArr[0])) {
            long createTime = 0;
            try {
                createTime = Long.parseLong(splitStrArr[1]);
            } catch (NumberFormatException e) {
                Logger.error("解析long型(%s)出错", splitStrArr[1]);
                return null;
            }
            long disMsec = System.currentTimeMillis() - createTime;
            if (disMsec > 0 && disMsec < PWD_TOKEN_LIVE_MSEC) {
                return splitStrArr[2];
            } else {
                return EXPIRE_TOKEN;
            }
        }else{
            return null;
        }
    }


}
