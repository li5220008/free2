package util;

import play.mvc.Scope;

/**
 * 用户登陆token的信息组成.
 * 这里加个类,是为了方便组织数据
 * User: wenzhihong
 * Date: 12-5-4
 * Time: 上午10:34
 */
public class LoginTokenCompose {
    /**
     * 返回当前的LoginTokenCompose.
     * 在有controllers.LoginTokenCheck拦截器之后,调用才有效.
     * @return
     */
    public static final LoginTokenCompose current(){
        return Scope.RenderArgs.current().get(RENDER_KEY, LoginTokenCompose.class);
    }

    /**
     * 解析出来的对象存放在play.mvc.Scope.RenderArgs上的key值
     */
    public static final String RENDER_KEY = "__LoginTokenCompose";

    public void saveToRender(){
        Scope.RenderArgs.current().put(RENDER_KEY, this);
    }

    public void saveToRender(Scope.RenderArgs renderArgs){
        renderArgs.put(RENDER_KEY, this);
    }

    /**
     * 用户id
     */
    public long uid;

    /**
     * 产品id
     */
    public long pid;

    /**
     * 登陆名
     */
    public String userName;

    /**
     * token 创建时间
     */
    public long createTime;

    /**
     * mac地址
     */
    public String mac;

}
