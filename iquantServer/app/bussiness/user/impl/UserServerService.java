package bussiness.user.impl;

import bussiness.common.impl.BaseService;
import bussiness.user.IUserServerService;
import models.iquantCommon.JobDto.ProductStrategyIdDto;
import models.iquantCommon.JobDto.VirtualProductTradeAccountDto;
import models.iquantCommon.UserServerDto;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import play.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * desc: 服务器配置业务类
 * User: weiguili(li5220008@gmail.com)
 * Date: 13-8-28
 * Time: 下午1:38
 */
public class UserServerService extends BaseService implements IUserServerService {


    @Override
    public UserServerDto addServer(UserServerDto userServerDto){
        long id = qicDbUtil.insertWithNameParam("addUserServer", userServerDto);
        userServerDto.id = id;
        return userServerDto;
    }
    @Override
    public int updateServer(UserServerDto userServerDto){
        return qicDbUtil.updateWithNameParam("updateServer", userServerDto);
    }
    @Override
    public UserServerDto fetchServerById(long id){
        Map map = new HashMap();
        map.put("id", id);
        UserServerDto userServer = qicDbUtil.querySingleBeanWithNameParamMap("fetchUserServer", UserServerDto.class, map);
        return userServer;
    }

    @Override
    public void testProductMigration() {
        List<Long> productIds = qicDbUtil.queryWithHandler("SELECT DISTINCT product_id FROM virtual_product_trade_account", new ColumnListHandler<Long>());//将product_id(产品ID)进行DISTINCT

        for(Long productid : productIds){
            List<VirtualProductTradeAccountDto> listVPTAD = qicDbUtil.queryBeanList("SELECT id id, product_id productId, account_id accountId, create_uid createUid, ctime, utime, `status` FROM virtual_product_trade_account WHERE product_id=?",VirtualProductTradeAccountDto.class,productid); //根据产品ID取到对应的实体信息

            ProductStrategyIdDto psid = qicDbUtil.querySingleBean("SELECT id, MIN(strategy_id) strategyId FROM virtual_product_strategy WHERE product_id =?", ProductStrategyIdDto.class ,productid);
            Logger.debug(String.valueOf(psid.id));//根据产品ID取到对应的最小的一个策略ID对应的ID。

            if(listVPTAD.size()>0 && listVPTAD !=null){//在这里循环组装插入 策略——账号表。
                for(VirtualProductTradeAccountDto vptad : listVPTAD){
                    Logger.debug(vptad.toString()+"------------------------------");
                    long ids = qicDbUtil.insert("INSERT INTO strategy_trade_account (product_strategy_id, account_id, create_uid, ctime, utime, `status`) VALUES (?,?,?,?,?,?)",psid.id,vptad.accountId,vptad.createUid,vptad.ctime,vptad.utime,vptad.status);
                }
            }
        }
    }
}
