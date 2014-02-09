package bussiness.tradeAccount;

import models.iquantCommon.TradeAccountDto;

import java.util.List;
import java.util.Map;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-7-19
 * Time: 下午4:02
 * 功能描述:
 */
public interface ITradeAccountService {

    /**
     * 添加交易账号
     * @param accountDto
     * @return
     */
    public  long addTradeAccount(TradeAccountDto accountDto);

    /**
     * 删除交易账号
     * @param id
     * @param uid
     * @return
     */
    public int delTradeAccount(long id,long uid);

    /**
     * 修改交易账号
     * @param accountDto
     * @return
     */
    public int updateTradeAccount(TradeAccountDto accountDto);

    /**
     * 查询用户交易账号
     * @param uid
     * @return
     */
    public List<Map<String, Object>> findUserTradeAccount(long uid);


    //--------------------------iquant交易设置1.1-----------------------------
    /**
     * 查询用户交易账号
     * @param uid
     * @return
     */
    public List<TradeAccountDto> findUserTradeAccountList(long uid);

    /**
     * 添加交易账号
     * @param accountDto
     * @return
     */
    public  long addTradeAccount11(TradeAccountDto accountDto);

    /**
     * 修改交易账号
     * @param accountDto
     * @return
     */
    public int updateTradeAccount11(TradeAccountDto accountDto);

    /**
     * 删除交易账号
     * @param id
     * @param uid
     * @return
     */
    public int delTradeAccount11(long id,long uid);

    /**
     * 根据账户ID 查询该账户信息
     * @param id
     * @return
     */
    public TradeAccountDto findTradeAccount(long id,long uid);

    /**
     *  返回系统当前已使用的全部交易帐号的信息
     * @return
     */
    public List<TradeAccountDto>  fetchAllInUsedTradeAccount();

    /**
     * 按accountId更新交易帐号的初始资金
     * @param accountId 帐号id
     * @param initCapital 初始资金
     * @return
     */
    public int updateTradeAccountInitCapital(Long accountId, Double initCapital);

}
