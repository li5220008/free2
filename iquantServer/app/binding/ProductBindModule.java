package binding;

import bussiness.product.ICustomerInfoService;
import bussiness.product.IProductService;
import bussiness.product.IVirtualProductService;
import bussiness.product.impl.CustomerInfoService;
import bussiness.product.impl.ProductService;
import bussiness.product.impl.VirtualProductService;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-7-12
 * Time: 上午9:36
 * 功能描述:
 */
public class ProductBindModule extends AbstractModule {
    @Override
    protected void configure() {
        //service配置
        bind(IVirtualProductService.class).to(VirtualProductService.class).in(Scopes.SINGLETON);
        bind(ICustomerInfoService.class).to(CustomerInfoService.class).in(Scopes.SINGLETON);

        bind(IProductService.class).to(ProductService.class).in(Scopes.SINGLETON);
    }
}
