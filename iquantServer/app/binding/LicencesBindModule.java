package binding;

import bussiness.licences.ILicencesService;
import bussiness.licences.impl.LicencesService;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

/**
 * User: 潘志威
 * Date: 13-8-27
 * Time: 下午5:51
 * To change this template use File | Settings | File Templates.
 */
public class LicencesBindModule extends AbstractModule {
    @Override
    protected void configure(){
       bind(ILicencesService.class).to(LicencesService.class).in(Scopes.SINGLETON);
    }
}
