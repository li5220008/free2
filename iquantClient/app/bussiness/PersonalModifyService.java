package bussiness;

import models.iquantCommon.UserInfoDto;
import play.libs.Crypto;
import protoc.HttpBody;
import protoc.URILib;
import protoc.parser.ActionResult;
import util.GsonUtil;

/**
 * 个人资料修改
 * User: panzhiwei
 * Date: 12-12-31
 * Time: 下午2:44
 * To change this template use File | Settings | File Templates.
 */
public class PersonalModifyService extends BasicService {

    public UserInfoDto findUserInfoById(Long uid) {
        String url = URILib.FETCH_PERSONAL_MODIFY;
        return remoteRequestService.getBean(url, UserInfoDto.class, uid).data;
    }

    public boolean updateUserInfo(UserInfoDto userInfoDto, Long uid) {
        String url = URILib.UPDATE_PERSONAL_MODIFY;
        HttpBody httpBody = new HttpBody();
        String json = GsonUtil.createWithoutNulls().toJson(userInfoDto);
        httpBody.body = json;
        return Boolean.valueOf(remoteRequestService.getSingleValue(url,httpBody,uid).data);
    }

    public boolean findPwdById(String password, Long uid) {
        String url = URILib.FIND_PWD_BYID;
        return Boolean.valueOf(remoteRequestService.getSingleValue(url, UserInfoDto.class, password, uid).data);
    }

    public UserInfoDto checkEmail(String newEmail, String selfEmail) {
        String url = URILib.CHECK_EMAIL;
        return remoteRequestService.getBean(url, UserInfoDto.class, newEmail, selfEmail).data;
    }


}
