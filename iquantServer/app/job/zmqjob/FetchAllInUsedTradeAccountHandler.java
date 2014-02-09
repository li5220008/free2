package job.zmqjob;

import bussiness.tradeAccount.ITradeAccountService;
import com.alibaba.fastjson.JSONObject;
import models.iquantCommon.TradeAccountDto;
import util.GsonUtil;

import javax.inject.Inject;
import java.util.List;

/**
 * 系统当前已使用的全部交易帐号的信息
 * User: wenzhihong
 * Date: 13-9-22
 * Time: 下午4:14
 */
@ZmqHandler(cmd = ZmqProtocol.Cmd.FetchAllInUsedTradeAccount)
public class FetchAllInUsedTradeAccountHandler extends AbstractZmqHandler {

    @Inject
    static ITradeAccountService tradeAccountService;

    public FetchAllInUsedTradeAccountHandler(JSONObject inputData) {
        super(inputData);
    }

    @Override
    protected Object process() {
        List<TradeAccountDto> accounts = tradeAccountService.fetchAllInUsedTradeAccount();
        return accounts;
    }
}
