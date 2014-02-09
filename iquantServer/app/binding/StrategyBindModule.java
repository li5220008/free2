package binding;

import bussiness.strategy.*;
import bussiness.strategy.impl.*;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-7-12
 * Time: 上午9:15
 * 功能描述:
 */
public class StrategyBindModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(IBackTestService.class).to(BackTestService.class).in(Scopes.SINGLETON);
        bind(IDrawYieldChartService.class).to(DrawYieldChartService.class).in(Scopes.SINGLETON);
        bind(IStrategyService.class).to(StrategyService.class).in(Scopes.SINGLETON);
        bind(IUserStrategyManageService.class).to(UserStrategyManageService.class).in(Scopes.SINGLETON);
    }
}
