package binding;

import business.DefaultRemoteRequestServiceSupport;
import business.IRemoteRequestServiceSupport;
import business.UserService;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;


/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-6-28
 * Time: 上午9:52
 * 功能描述:
 */
public class AppDefaultBindModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(UserService.class).toInstance(new UserService());
        bind(IRemoteRequestServiceSupport.class).annotatedWith(Names.named("http")).to(DefaultRemoteRequestServiceSupport.class);
    }
}
