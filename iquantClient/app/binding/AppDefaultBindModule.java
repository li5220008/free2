package binding;

import business.DefaultRemoteRequestServiceSupport;
import business.IRemoteRequestServiceSupport;
import bussiness.SystemConfigService;
import bussiness.*;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-6-27
 * Time: 下午3:05
 * 功能描述:  ioc配置
 */
public class AppDefaultBindModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(FunctionService.class).toInstance(new FunctionService());
        bind(UserService.class).toInstance(new UserService());
        bind(MessagesInfosService.class).toInstance(new MessagesInfosService());
        bind(SystemConfigService.class).toInstance(new SystemConfigService());
        bind(IRemoteRequestServiceSupport.class).annotatedWith(Names.named("http")).to(DefaultRemoteRequestServiceSupport.class);
        bind(StrategyService.class).toInstance(new StrategyService());
        bind(StrategyFavoriteService.class).toInstance(new StrategyFavoriteService());
        bind(StrategySubscriptionService.class).toInstance(new StrategySubscriptionService());
        bind(StockPoolsService.class).toInstance(new StockPoolsService());
        bind(StockPoolCollectService.class).toInstance(new StockPoolCollectService());
        bind(StockPoolDiscussService.class).toInstance(new StockPoolDiscussService());
        bind(UserTemplateService.class).toInstance(new UserTemplateService());
        bind(StockPoolCombineInfoService.class).toInstance(new StockPoolCombineInfoService());
        bind(StockPoolBasicInfoService.class).toInstance(new StockPoolBasicInfoService());
        bind(LogInfosService.class).toInstance(new LogInfosService());
        bind(BackTestService.class).toInstance(new BackTestService());
        bind(RoleInfoService.class).toInstance(new RoleInfoService());
        bind(UserAuthorizationService.class).toInstance(new UserAuthorizationService());
        bind(UserInfoService.class).toInstance(new UserInfoService());
        bind(SaleDepartmentService.class).toInstance(new SaleDepartmentService());
        bind(ActivateUserService.class).toInstance(new ActivateUserService());
        bind(LookPwdService.class).toInstance(new LookPwdService());

    }
}
