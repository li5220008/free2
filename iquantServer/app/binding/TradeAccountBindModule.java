package binding;

import bussiness.tradeAccount.ITradeAccountService;
import bussiness.tradeAccount.impl.TradeAccountService;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-7-19
 * Time: 下午5:41
 * 功能描述:
 */
public class TradeAccountBindModule extends AbstractModule {

    @Override
    protected void configure() {
        //service配置
        bind(ITradeAccountService.class).to(TradeAccountService.class).in(Scopes.SINGLETON);

    }
}
