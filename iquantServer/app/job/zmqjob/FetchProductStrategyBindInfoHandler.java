package job.zmqjob;

import bussiness.product.IProductService;
import com.alibaba.fastjson.JSONObject;

import javax.inject.Inject;
import java.util.Map;

/**
 * 根据策略实现id(也就是产品策略绑定id)返回这个策略的详细信息(包含资金使用比例,帐号列表,交易标的(已经做了 A + B – C))
 * User: wenzhihong
 * Date: 13-9-22
 * Time: 下午4:33
 */
@ZmqHandler(cmd = ZmqProtocol.Cmd.FetchProductStrategyBindInfo)
public class FetchProductStrategyBindInfoHandler extends AbstractZmqHandler {

    @Inject
    static IProductService productService;

    public FetchProductStrategyBindInfoHandler(JSONObject inputData) {
        super(inputData);
    }

    @Override
    protected Object process() {
        Integer strategyInstanceId = inputData.getInteger("strategyInstanceId");
        Map<String,Object> data = productService.fetchProductStrategyBindInfo(strategyInstanceId,true);
        return data;
    }
}
