package controllers.external;

import bussiness.user.IUserInfoService;
import controllers.LoginTokenCheckIntercept;
import models.iquantCommon.FunctionInfoDto;
import play.mvc.Controller;
import play.mvc.With;
import util.LoginTokenCompose;

import javax.inject.Inject;
import java.util.*;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 12-12-24
 * Time: 下午4:03
 * 功能描述:
 */
@With(value = {LoginTokenCheckIntercept.class})
public class GetFunctions extends Controller {
    @Inject
    static IUserInfoService userInfoService;

    public static void getUserFunctions() {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("status", -1);
        try {
            //下次改成用token
            jsonMap.put("status", 0);
            LoginTokenCompose compose = LoginTokenCompose.current();
            long uid = compose.uid;
            List<FunctionInfoDto> list = userInfoService.findUserFunctionInfoById(uid);
            Set<Long> jsonId = new HashSet<Long>();
            for (FunctionInfoDto dto : list) {
                jsonId.add(dto.id);
                jsonId.add(dto.pid);
            }
            jsonId.remove(0);
            jsonMap.put("message", "获取成功");
            jsonMap.put("data", jsonId);
        } catch (Exception e) {
            jsonMap.put("status", -1);
            jsonMap.put("message", "请先登入");
            e.printStackTrace();
        }
        renderJSON(jsonMap);
    }
}
