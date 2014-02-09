package models.iquantCommon;
import util.LoginTokenCompose;

import java.util.Date;

/**
 * 用户资源相关的dto
 * User: wenzhihong
 * Date: 13-4-28
 * Time: 下午2:33
 */
public class BaseUserResourceDtoSupport  extends BaseDtoSupport {

    /**
     * 创建时间
     */
    public transient Date ctime = new Date();

    /**
     * 用户id
     */
    public transient Long userId;

    /**
     * 系统产品id
     */
    public transient Long sysProductId;

    /**
     * 从loginTokenCompose里取值,填充userId跟productId两个属性. 方便操作
     */
    public void fillUserAndProductProperty() {
        LoginTokenCompose compose = LoginTokenCompose.current();
        this.userId = compose.uid;
        this.sysProductId = compose.pid;
    }

    public void fillUserAndProductPropertyNoPid() {
        LoginTokenCompose compose = LoginTokenCompose.current();
        this.userId = compose.uid;
    }

    /**
     * 是否属于自身资源
     */
    public boolean isMyselfResource(LoginTokenCompose compose) {
        return userId == compose.uid;
    }

    /**
     * 是否属于自身资源
     */
    public boolean isMyselfResource() {
        LoginTokenCompose compose = LoginTokenCompose.current();
        return compose != null && userId == compose.uid;
    }

    /**
     * 是否允许操作
     */
    public static boolean permit(BaseUserResourceDtoSupport r, LoginTokenCompose compose) {
        return r != null && r.userId == compose.uid;
    }
}
