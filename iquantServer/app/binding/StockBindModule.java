package binding;

import bussiness.stock.*;
import bussiness.stock.impl.*;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-7-12
 * Time: 上午9:17
 * 功能描述:
 */
public class StockBindModule extends AbstractModule {
    @Override
    protected void configure() {
        //service配置

        bind(IStockPoolService.class).to(StockPoolService.class).in(Scopes.SINGLETON);
        bind(IUserStockPoolManageService.class).to(UserStockPoolManageService.class).in(Scopes.SINGLETON);

    }
}
