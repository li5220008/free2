package bussiness;

import business.IRemoteRequestServiceSupport;
import com.google.inject.name.Named;
import play.classloading.enhancers.LVEnhancer;
import play.exceptions.UnexpectedException;
import play.modules.guice.InjectSupport;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * service都应该继承该类
 * User: liangbing
 * Date: 13-7-2
 * Time: 上午9:09
 */
@InjectSupport
public class BasicService {

    @Inject
    @Named("http")
    static IRemoteRequestServiceSupport remoteRequestService;

    public static Map<String, Object> vargsToMap(Object ... args){
        Map<String, Object> templateBinding = new HashMap<String, Object>(4);
        Stack<LVEnhancer.MethodExecution> stack = LVEnhancer.LVEnhancerRuntime.getCurrentMethodParams();
        if (stack.size() > 0) {
            LVEnhancer.MethodExecution me = stack.get(stack.size() - 2).getCurrentNestedMethodCall();
            LVEnhancer.LVEnhancerRuntime.ParamsNames paramsNames = new LVEnhancer.LVEnhancerRuntime.ParamsNames(me.getSubject(), me.getParamsNames(), me.getVarargsNames());
            String[] names = paramsNames.varargs;
            if (args != null && args.length > 0 && names == null) {
                throw new UnexpectedException("no varargs names while args.length > 0 !");
            }
            for (int i = 0; i < args.length; i++) {
                templateBinding.put(names[i], args[i]);
            }
        }
        return   templateBinding;
    }

}
