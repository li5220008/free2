package models.iquantCommon;

import util.LoginTokenCompose;

/**
 * 用户自选股组明细列表
 * User: wenzhihong
 * Date: 13-4-28
 * Time: 下午2:39
 */
public class CustomerSecurityListDto{
    public Long id;


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

    //代码
    public String scode;

    //市场
    public String exchange;

    //所属组id
    public Long groupId;

    public String ctime;

    public String comment;
}
