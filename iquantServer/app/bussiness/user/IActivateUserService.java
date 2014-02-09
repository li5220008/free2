package bussiness.user;

import models.iquantCommon.ActivateUserDto;
import models.iquantCommon.AvtivatePar;
import play.libs.F;
import util.Page;

import java.util.List;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-7-16
 * Time: 下午1:54
 * 功能描述:
 */
public interface IActivateUserService {

    /**
     * 待激活用户
     *
     * @param ap     参数对象
     * @param pageNo 页码
     * @return _1 列表list _2 page对象
     */
    public  F.T2<List<ActivateUserDto>, Page> userList(AvtivatePar ap, int pageNo);

    /**
     * 用户列表
     *
     * @param ap     参数对象
     * @param pageNo 页码
     * @return _1 列表list _2 page对象
     */
    public  F.T2<List<ActivateUserDto>, Page> users(AvtivatePar ap, int pageNo) ;


    /**
     * 到期用户
     *
     * @param ap     参数对象
     * @param pageNo 页码
     * @return _1 列表list _2 page对象
     */
    public  F.T2<List<ActivateUserDto>, Page> dueUsers(AvtivatePar ap, int pageNo) ;

}
