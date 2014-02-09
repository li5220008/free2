package binding;

import bussiness.common.ILogInfoService;
import bussiness.common.ISystemConfigService;
import bussiness.common.impl.LogInfosService;
import bussiness.common.impl.SystemConfigService;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-6-27
 * Time: 下午3:05
 * 功能描述:  DI配置
 */
public class CommonBindModule extends AbstractModule {
    @Override
    protected void configure() {
        //service配置
        bind(ISystemConfigService.class).to(SystemConfigService.class).in(Scopes.SINGLETON);
        bind(ILogInfoService.class).to(LogInfosService.class).in(Scopes.SINGLETON);
    }
}
