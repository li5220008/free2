package bussiness.strategy;

import com.google.gson.JsonObject;
import models.iquantCommon.*;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * User: liuhongjiang
 * Date: 13-7-26
 * Time: 下午2:17
 */
public interface IUserStrategyManageService {
    //得到已收藏的策略,放入set中
    public Set<Integer> queryUserCollectSet(List<Long> ids, Long uid);
    //加入收藏
    public  void addstrategycollect(long stid,Long uid) throws Exception;

    //取消收藏
    public  void deletestrategycollect(long stid,Long uid) throws Exception;

    //根据策略id查询是否被收藏
    public  boolean iscollect(long stid, long uid) ;
    /**
     * 判断是否订阅
     * @param uid
     * @param stid
     * @return
     */
    public  boolean isorder(Long uid, Long stid);

    /**
     * 加入订阅
     * @param month
     * @param uid
     * @param stid
     */
    public Date addstrategyorder(int month, Long uid, Long stid) ;

    /**
     * 续订
     * @param month
     * @param uid
     * @param stid
     * @return
     */
    public  Date delaystrategyorder(int month, Long uid, Long stid);

    /**
     * 根据策略id得到订阅
     * @param uid
     * @param stid
     * @return
     */
    public StrategyOrderDto getStrategyOrderById(Long uid, Long stid);

    /**
     * author 潘志威
     * 加入回测
     *
     * @param ids
     */
    @Deprecated
    public  void addLookback(String ids[], int status) ;

    /**
     * author 潘志威
     * 删除策略
     *
     * @param ids
     */
    public  boolean delstrategy(String ids[]) ;

    /**
     * author 潘志威
     * 策略清空
     */
    public  void emptystrategy() ;

    /**
     * 处理 立即下架/延时下架
     * <p/>
     * 立即下架包括两个步骤
     * 1.如果当前时间大于最大收藏时间--》策略下架
     * 2.把每条下架信息保存到user_message表
     * <p/>
     * 延时下架 把相关信息存入任务调度表（scheduling_task）由定时任务类处理
     *
     * @param stIds    策略id数组
     * @param downTime 用户设置的 延时下架时间
     * @param flag    客户端传过来的标示 1 ：立即下架 2 ：延时下架
     * @return 7.策略在运行中,不允许下架 6.设置时间小于当前时间 5.策略为待下架状态不允许修改，4.模板内容非法 3.非法操作 2.策略当前有用户订阅，不能下架 1.下架成功
     * @author 刘泓江
     */
    public  int strategyDown(String[] stIds, Date downTime, String message, long uid, int flag) ;

    /**
     * author 潘志威
     * 策略上架
     *
     * @param ids
     * @param serverId qsm中的agentIP
     */
    public  boolean upstrategy(String ids[], Map<String, Integer> serverId);

    // 策略延时下架
    public  int StraDownDelayed(String[] stIds, StrategyDownTaskContextDto tastContext, String StrategyDownSql,
                                Date setTime, Date currentTime, String getOrderedUserSql, String sendUserMessageSql,
                                String message) ;
    /**
     * 判断该用户是否已经评论的该策略
     * @param stid
     * @param uid
     */
    public  Integer judge(Long stid,Long uid);

    /**
     * 保存该用户对该策略的评论
     * @param usd
     * @param uid
     * @param stid
     */
    public  void saveDiscuss(UserStrategyDiscuss usd,Long uid,Long stid);

    public  int syncBackTestResult(StrategyDto strategyDto) ;

    public  int syncQiaBackTestResult(JsonObject jo) ;

    public  int updateStategyStatus(StrategyDto.StrategyStatus status, String suuid) ;

    public  int updateStategyStatusbyId(StrategyDto.StrategyStatus status, String id) ;

    public  int deleteStrategyFromPerformance(String strUUID, StrategyDto.StrategyType strategyType);

    /**
     * 上传策略
     */
    public boolean insertStrategy(StrategyDto strategyDto,long uid,String filePath) throws SQLException;

    public boolean synStrateToQsm(List<StrategyBaseinfo> list, int serverId);

    public boolean syncProductToQsm(long strategyVirtualProductRelatedId, long strategyId);

    public boolean deleteProductFromQsm(long strategyVirtualProductRelatedId);

}
