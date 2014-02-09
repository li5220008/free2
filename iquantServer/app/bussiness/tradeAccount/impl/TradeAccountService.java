package bussiness.tradeAccount.impl;

import bussiness.common.impl.BaseService;
import bussiness.tradeAccount.ITradeAccountService;
import com.google.common.collect.Maps;
import dbutils.SqlLoader;
import models.iquantCommon.TradeAccountDto;

import java.util.List;
import java.util.Map;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-7-19
 * Time: 下午4:02
 * 功能描述:
 */
public class TradeAccountService extends BaseService implements ITradeAccountService {
    @Override
    public long addTradeAccount(TradeAccountDto accountDto) {
        Long id = qicDbUtil.insertWithNameParam("addTradeAccount", accountDto);
        return id;
    }

    @Override
    public int delTradeAccount(long id, long uid) {
        String sql = SqlLoader.getSqlById("softDelTradeAccount");
        int effect = qicDbUtil.update(sql, id, uid);
        return effect;
    }

    @Override
    public int updateTradeAccount(TradeAccountDto account) {
        int effect = qicDbUtil.updateWithNameParam("updateTradeAccount", account);
        return effect;
    }

    @Override
    public List<Map<String, Object>> findUserTradeAccount(long uid) {
        String sql = SqlLoader.getSqlById("fetchTradeAccountNoBindToProduct");
        List<Map<String, Object>> mapList = qicDbUtil.queryMapList(sql, uid);
        return mapList;
    }

    //-----------------------交易设置1.1-----------------------------------------//
    public long addTradeAccount11(TradeAccountDto accountDto) {
        if(accountDto != null && accountDto.type == 1){ //如果是股票 就把hedgeType设置为空
           accountDto.hedgeType = null;
        }
        Long id = qicDbUtil.insertWithNameParam("addTradeAccount11", accountDto);
        accountDto.id = id;//将新插入id赋给对象,否则外面取不到值
        return id;
    }

    public int delTradeAccount11(long id, long uid) {
        String sql = SqlLoader.getSqlById("softDelTradeAccount11");
        int effect = qicDbUtil.update(sql, id, uid);
        return effect;
    }

    public int updateTradeAccount11(TradeAccountDto account) {
        if(account != null && account.type == 1){ //如果是股票 就把hedgeType设置为空
            account.hedgeType = null;
        }
        Map<String,Object> map = Maps.newHashMap();
        map.put("account",account);
        int effect = qicDbUtil.updateWithNameParamMap("updateTradeAccount11", map, account);
        return effect;
    }

    /**
     * 根据用户ID 查询账户列表
     * @param uid
     * @return
     */
    public List<TradeAccountDto> findUserTradeAccountList(long uid) {
        String sql = SqlLoader.getSqlById("fetchTradeAccountListByUid");
        //List<TradeAccountDto> list = qicDbUtil.queryBeanList(sql, TradeAccountDto.class, uid);
        Map<String,Object> params = Maps.newHashMap();
        params.put("uid", uid);
        List<TradeAccountDto> list = qicDbUtil.queryBeanListWithNameParam("fetchTradeAccountListByUid", TradeAccountDto.class, params);

        return list;
    }

    /**
     * 根据账户ID 查询该账户信息
     * @param id 账户ID
     * @param uid
     * @return
     */
    public TradeAccountDto findTradeAccount(long id,long uid) {
        String sql = SqlLoader.getSqlById("fetchTradeAccountByAccountId");
        TradeAccountDto tradeAccountDto = qicDbUtil.querySingleBean(sql, TradeAccountDto.class, id, uid);
        return tradeAccountDto;
    }

    /**
     *  返回系统当前已使用的全部交易帐号的信息
     * @return
     */
    public List<TradeAccountDto>  fetchAllInUsedTradeAccount(){
        String sql = SqlLoader.getSqlById("fetchAllInUsedTradeAccount");
        return qicDbUtil.queryBeanList(sql,TradeAccountDto.class);
    }

    @Override
    public int updateTradeAccountInitCapital(Long accountId, Double initCapital) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("id", accountId);
        param.put("initCapital", initCapital);
        return qicDbUtil.updateWithNameParamMap("updateTradeAccountInitCapital", param);
    }

}
