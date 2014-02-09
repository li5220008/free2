package job.zmqjob;

import bussiness.tradeAccount.ITradeAccountService;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import play.Logger;

import javax.inject.Inject;
import java.util.Map;

/**
 * 更新帐号的初始资金
 * User: wenzhihong
 * Date: 13-11-7
 * Time: 下午12:31
 */
@ZmqHandler(cmd = ZmqProtocol.Cmd.UpdateTradeAccountInitCapital)
public class UpdateTradeAccountInitCapitalHandler extends AbstractZmqHandler {

    @Inject
    static ITradeAccountService tradeAccountService;

    public UpdateTradeAccountInitCapitalHandler(JSONObject inputData) {
        super(inputData);
    }

    @Override
    protected Object process() {
        Long accountId = inputData.getLong("accountId");
        Double initCapital =  inputData.getDouble("initCapital");

        int effect = -1;
        if (accountId != null && initCapital != null) {
            effect = tradeAccountService.updateTradeAccountInitCapital(accountId, initCapital);
        }else {
            Logger.warn("更新帐号的初始资金: 没有收到accountId或者initCapital参数, 返回effect为-1");
        }

        Map<String, Object> data = Maps.newHashMap();
        data.put("effect", effect);

        return data;
    }
}
