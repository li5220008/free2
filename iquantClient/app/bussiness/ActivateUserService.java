package bussiness;

import models.iquantCommon.ActivateUserDto;
import models.iquantCommon.AvtivatePar;
import play.libs.F;
import protoc.HttpBody;
import protoc.URILib;
import protoc.parser.ActionResult;
import util.GsonUtil;
import util.Page;

import java.util.List;

/**
 * 后台管理 用户列表.
 * User: liangbing
 * Date: 12-12-4
 * Time: 上午11:26
 */
public class ActivateUserService  extends BasicService{

    /**
     * 待激活用户
     *
     * @param ap     参数对象
     * @param pageNo 页码
     * @return _1 列表list _2 page对象
     */
    public static F.T2<List<ActivateUserDto>, Page> userList(AvtivatePar ap, int pageNo) {
        String json = GsonUtil.createGson().toJson(ap);
        HttpBody httpBody = new HttpBody();
        httpBody.body = json;
        ActionResult<List<ActivateUserDto>> actionResult = remoteRequestService.getList(URILib.FETCH_ACTIVATE_USER_LIST,httpBody,ActivateUserDto.class,pageNo);
        Page page = new Page( actionResult.header.total , pageNo);
        return F.T2(actionResult.data, page);
    }

    /**
     * 用户列表
     *
     * @param ap     参数对象
     * @param pageNo 页码
     * @return _1 列表list _2 page对象
     */
    public static F.T2<List<ActivateUserDto>, Page> users(AvtivatePar ap, int pageNo) {

        String json = GsonUtil.createGson().toJson(ap);
        HttpBody httpBody = new HttpBody();
        httpBody.body = json;
        ActionResult<List<ActivateUserDto>> actionResult = remoteRequestService.getList(URILib.FETCH_USER_LIST,httpBody,ActivateUserDto.class,pageNo);
        Page page = new Page( actionResult.header.total , pageNo);
        return F.T2(actionResult.data, page);
    }


    /**
     * 到期用户
     *
     * @param ap     参数对象
     * @param pageNo 页码
     * @return _1 列表list _2 page对象
     */
    public static F.T2<List<ActivateUserDto>, Page> dueUsers(AvtivatePar ap, int pageNo) {
        String json = GsonUtil.createGson().toJson(ap);
        HttpBody httpBody = new HttpBody();
        httpBody.body = json;
        ActionResult<List<ActivateUserDto>> actionResult = remoteRequestService.getList(URILib.FETCH_DUE_USER_LIST,httpBody,ActivateUserDto.class,pageNo);
        Page page = new Page( actionResult.header.total , pageNo);
        return F.T2(actionResult.data, page);
    }


}
