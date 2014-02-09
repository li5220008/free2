package bussiness.strategy;

import models.iquantCommon.*;

import java.util.List;
import java.util.Map;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-7-4
 * Time: 上午9:50
 * 功能描述:
 */
public interface IBackTestService {


    public long findServerIdByAddr(String ip, int engineId, int serverType);

    public String findServerIpById(int serverId);

    /**
     * 根据服务器id查询待回测qicore策略列表
     *
     * @param serverId
     * @return
     */
    public List<BackTestStrategyDto> findBackTestStrategyByServerId(long serverId);

    /**
     * 根据服务器id查询待回测列表
     *
     * @param serverId
     * @return
     */
    public List<BackTestStrategyDto> findBackTestStrategyByServerId(long serverId, int engineId);

    /**
     * 供回测服务器开始回测的时候调用
     *
     * @param serverId
     * @param strategyId
     * @return
     */
    public boolean checkBackTestStatus(long serverId, String strategyId);

    /**
     * 更新策略状态   取走策略后要标识回测中
     *
     * @param strategyId
     * @param status
     * @return
     */
    public int[] updateStrategyStatus(String strategyId, StrategyDto.StrategyStatus status);

    /**
     * 更新策略状态   取走策略后要标识回测中
     *
     * @param strategyId
     * @param status
     * @return
     */
    public int updateStrategyStatus(String[] strategyId, StrategyDto.StrategyStatus status);

    /**
     * 更新策略状态   取走策略后要标识回测中
     *
     * @param list
     * @param status
     * @return
     */
    public int[] updateStrategyStatus(List<BackTestStrategyDto> list, StrategyDto.StrategyStatus status);

    /**
     * 添加回测服务器
     *
     * @param backTestServerDto
     * @return
     */
    public int addServer(StrageServerDto backTestServerDto);

    /**
     * 修改策略所对应的回测服务器ID
     *
     * @param serverId
     * @param suuid    策略人uuid
     * @return
     */
    public int updateStratedyServerId(long serverId, String suuid);

    /**
     * 批量修改策略所对应的回测服务器ID
     *
     * @param serverId
     * @param sid      自增长主键id
     * @return
     */
    public int[] updateStratedyServerIdByIntId(Map<String, Integer> serverId, String[] sid);

    /**
     * 更改回测服务器状态
     *
     * @param status
     * @param serverId
     * @return
     */
    public int updateServerStatus(int status, long serverId);

    /**
     * 查询所有的待回测服务器列表
     *
     * @return
     */
    public List<StrageServerDto> findAllServer();

    /**
     * 查询回测服务器列表
     *
     * @param status
     * @return
     */
    public List<StrageServerDto> findBackTestServerByStatus(BackTestServerDto.ServerStatusEnum status);

    /**
     * 查询代理服务器列表
     *
     * @param status
     * @return
     */
    public List<StrageServerDto> findAgentServerByStatus(BackTestServerDto.ServerStatusEnum status);

    /**
     * 按服务器状态和类型查找服务器
     *
     * @param status
     * @return
     */
    public List<StrageServerDto> findServerByStatusAndType(BackTestServerDto.ServerStatusEnum status, BackTestServerDto.ServerTypeEnum type);

    public List<StrageServerDto> findServerByTypeAndEngineeId(BackTestServerDto.ServerStatusEnum status, BackTestServerDto.ServerTypeEnum type, int enginetypeId);

    public int updateStrategyStatusByServerId(long serverId, StrategyDto.StrategyStatus oriStatus, StrategyDto.StrategyStatus newStatus);

    //由于一些不可抗因素导致回测失败需将策略状态进行回滚，以便再次回入回测
    public int rollBackAfterTestFailure(long serverId);

    /**
     * 更新策略的通过时间
     *
     * @param sid
     */
    public void updateStrategyPassTime(long[] sid);

    //清空缓存列表
    public void clearCache();

    public List<BackTestStrategyDto> findInRuningStrategy(int engineId, long serverId);

    /**
     * 获取qia服务器回测结果
     *
     * @param serverId
     * @return
     */
    public QiaBackTestResultDto getQiaStrategyBackTestStatus(int serverId);

    /**
     * 启动qia回测
     *
     * @param serverId
     * @return
     */
    public QiaBackTestResultDto startBackTestQiaStrategy(int serverId);

    /**
     * 删除服务器
     *
     * @param id
     */
    public void delBackTestServer(int id);
}
