package controllers;

import bussiness.PersonalModifyService;

import models.iquantCommon.UserInfoDto;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * 个人信息修改
 * User: panzhiwei
 * Date: 12-11-21
 * Time: 下午4:47
 * To change this template use File | Settings | File Templates.
 */
public class PersonalModify extends BasePlayControllerSupport {
    @Inject
    static PersonalModifyService personalModifyService;

    public static void personalModify(Long uid) {
        UserInfoDto userInfo = personalModifyService.findUserInfoById(uid);
        render(userInfo);
    }

    public static void modifySuccess(UserInfoDto userInfoDto, Long uid) {
        boolean b = personalModifyService.updateUserInfo(userInfoDto, uid);
        Map<String, Object> json = new HashMap<String, Object>();
        if (b) {
            json.put("success", true);
            json.put("message", "个人信息修改成功");
        } else {
            json.put("success", false);
            json.put("message", "个人信息修改失败");
        }

        renderJSON(json);
    }

    public static void checkPassword(String password, Long uid) {
        boolean flag = personalModifyService.findPwdById(password, uid);
        if (flag) {
            renderText(true);
        } else {
            renderText(false);
        }

    }

    public static void checkEmail(String email) {
        String[] emails = email.split(",");
        if (personalModifyService.checkEmail(emails[0], emails[1]) == null) {
            renderText(true);
        } else {
            renderText(false);
        }

    }
}
