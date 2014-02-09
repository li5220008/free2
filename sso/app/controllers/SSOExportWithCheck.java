package controllers;

import play.modules.redis.Redis;
import play.mvc.With;
import redis.clients.jedis.Pipeline;
import util.LoginTokenCompose;
import util.RedisKeys;

/**
 * sso暴露出来的接口,需要loginToken校验
 * User: wenzhihong
 * Date: 12-4-18
 * Time: 上午11:53
 */
@With(value = {LoginTokenCheckIntercept.class, GeneralIntercept.class})
public class SSOExportWithCheck extends BaseController {

    /**
     * 退出操作
     * 成功退出返回 ok
     */
    public static void loginOut() {
        LoginTokenCompose compose = LoginTokenCompose.current();

        //把mac地址和token都从redis去掉
        Pipeline p = Redis.pipelined();
        String userTokenKey = RedisKeys.userMacTokenKey(compose.mac);
        p.del(new String[]{userTokenKey});

        String userMacSetKey = RedisKeys.userMacSetKey(compose.userName);
        p.srem(userMacSetKey, compose.mac);
        p.sync();

        //这里认为总是成功
        renderText("ok");
    }
}
