package controllers;

import org.apache.commons.lang.StringUtils;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Scope;
import util.Tokens;

/**
 * 在后面的每个请求里, 都要带上loginToken参数. 特别是给api调用时
 * 登陆token检查
 * User: wenzhihong
 */
public class LoginTokenCheckIntercept extends Controller {
    @Before(priority = 30)
    static void checkHasLoginToken() {
        String token = params.get("ntToken");
        if (StringUtils.isBlank(token)) {
            token = params.get("loginToken");
        }

        boolean fromCookie = false;
        if (StringUtils.isBlank(token)) { //都没有的话, 从cookie中取
            Http.Cookie ntTokenCookie = request.cookies.get("ntToken");
            if (ntTokenCookie != null) {
                token = ntTokenCookie.value;
                fromCookie = true;
            }
        }

        //登陆token解密信息,并存放于render中
        boolean isValidate = Tokens.checkValidateLoginTokenAndSaveToRender(token, Scope.RenderArgs.current());
        if (!isValidate) {
            forbidden("token失效, 请重新取token");
        }

        if (!fromCookie && StringUtils.isNotBlank(token)) {
            //设置cookie
            response.setCookie("ntToken", token);
        }
    }
}
