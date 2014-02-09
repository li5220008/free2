package binding;

import bussiness.common.IFunctionService;
import bussiness.common.impl.FunctionService;
import bussiness.user.*;
import bussiness.user.impl.*;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-7-12
 * Time: 上午9:34
 * 功能描述:
 */
public class UserBindModule extends AbstractModule {
    @Override
    protected void configure() {
        //service配置
        bind(IUserInfoService.class).to(UserInfoService.class).in(Scopes.SINGLETON);
        bind(IUserDefineTemplateService.class).to(UserDefineTemplateService.class).in(Scopes.SINGLETON);

        bind(IMessageInfoService.class).to(MessageInfoService.class).in(Scopes.SINGLETON);
        bind(IUserAuthorizationService.class).to(UserAuthorizationService.class).in(Scopes.SINGLETON);
        bind(IRoleInfoService.class).to(RoleInfoService.class).in(Scopes.SINGLETON);
        bind(IFunctionService.class).to(FunctionService.class).in(Scopes.SINGLETON);
        bind(IActivateUserService.class).to(ActivateUserService.class).in(Scopes.SINGLETON);
        bind(ISaleDepartmentService.class).to(SaleDepartmentService.class).in(Scopes.SINGLETON);
        bind(IUserServerService.class).to(UserServerService.class).in(Scopes.SINGLETON);
    }
}

