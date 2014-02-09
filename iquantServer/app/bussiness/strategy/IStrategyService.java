package bussiness.strategy;

import models.iquantCommon.*;
import play.libs.F;
import util.Page;

import java.util.List;
import java.util.Map;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-7-3
 * Time: 下午4:02
 * 功能描述:
 */
public interface IStrategyService {

    public StrategyBaseDto findStrategyByName(String sname);


    public StrategyBaseinfo findStrategyById(long id);

    /**
     * 查询一批策略
     *
     * @param ids
     * @return
     */
    public List<StrategyBaseinfo> findStrategysByIds(String ids[]);

    public List<StrategyBaseinfo> findStrategysByIds(Long ids[]);

    public StrategyDto findStrategyByUUID(String strUUID);

    public List<QiaStrategyDto> findQiaStrategyByStrategyIds(List<Long> stids);

    //策略持仓
    public List<StrategyPositionDto> getStrategyPosition(long stid, int pageNo);

    //QIA 策略持仓
    public List<StrategyPositionDto> getQiaPosition(long stid, int pageNo);

    //绩效指标
    public IndicatorDto getindicator(long stid, int type);

    //QIA 绩效指标
    public QiaIndicatorDto getQiaIndicatorDto(long stid, int type);

    //成交记录
    public List<ExecutionRecordDto> getExecutionRecord(Long stid, int pageNo);

    //委托记录
    public List<AuthorizeRecordDto> getAuthorizeRecord(long stid, int pageNo);

    /**
     * @param myselect 分类搜索
     * @param content  关键字
     * @param pageNo   当前页数
     * @return _1 为结果集, _2为 分页page信息,
     */
    public F.T2<List<StrategyBaseDto>, Page> strategyList(int myselect, String content, int pageNo);

    /**
     * 高级搜索
     *
     * @param cnd
     * @param pageNo 当前页
     * @return _1 为结果集, _2为总条数, _3 总共页数
     */
    public F.T2<List<StrategyBaseDto>, Page> advanceSearch(StrategySearchCnd cnd, int myselect, int pageNo);

    public F.T2<List<StrategyBaseDto>, Page> findStrategysByUser(Map<String, String> queryParams);

    public List<F.T2<StrategyBaseDto, StrategyOrderDto>> userOrderStrateList(long uid);

    /**
     * 查询待上架策略
     *
     * @param sp     参数类
     * @param pageNo 当前页
     * @return _1. 待上架策略对象集合, _2 Page 分页对象
     */
    public  F.T2<List<StrategyDto>, Page> waitList(StrategyPar sp, int pageNo, long uid) ;

    /**
     * 策略回收站列表
     *
     * @param sp     参数类
     * @param pageNo 当前页
     * @return _1. 策略回收站对象集合, _2 Page 分页对象
     */
    public  F.T2<List<StrategyDto>, Page> retrieveList(StrategyPar sp, int pageNo, long uid) ;

    /**
     * 上架策略列表
     *
     * @param sp     参数类
     * @param pageNo 当前页
     * @return _1. 策略列表对象集合, _2 Page 分页对象
     */
    public  F.T2<List<StrategyDto>, Page> groundingList(StrategyPar sp, int pageNo, long uid) ;

    public  boolean deleteStrategyFromQsm(String[] ids) ;

    public  F.T4<IndicatorDto, IndicatorDto, QiaIndicatorDto, QiaIndicatorDto> getIndicator(long stid, int enginetypeId) ;

    public  Integer countUserRunntimeStrategy(long uid) ;

    /**
     * 返回策略的标的信息. 1.0版本的
     */
    public  List<Map<String,Object>> findStrategyStockInfo(List<Long> ids,long uid);

    /**
     * 查询策略原始配制的标的信息
     * @param id 策略id
     */
    public Map<String,Object> fetchStrategySecurityOriginal(long id);


    public List<Map<String, Object>> findStrategyByStatus(int status,long uid);

//-------------------------------------------------------------------------------------------

    public  List<StrategyContrastDto> strategyContrast(String idArray[]);


    /**
     *     策略对比 图形展示
     *     这个方法的主要作用是组装数据
     *     需要的数据：
     *     1.格式化近三个月的起始时间 maxDate minDate
     *     2.列出最近三个月收益率的最大值 最小值  取绝对值最大的modYield
     *     3.把数据组装成[{name=“策略名1” date:[ Date.UTC(2010, 0, 1), 9.05],...},
     *                  {name=“策略名2” date:[ Date.UTC(2010, 1, 1), 9.05],...}]类型
     *     @param  idArray 策略ID数组
     *     @retrun  arr_strategys[]数组  放入的数据依次为： 最大日期（对应X轴最大值），
     *                                                   最小日期（对应X轴最小值），
     *                                                   最大收益率（对应Y轴最大值），
     *                                                   按highcharts格式组装好的一个或多个策略的单天收益率和时间，
     *                                                   Y轴最小间距，X轴最小间距
     *
     */

    public   String[] strategyContrastForPictrue(String idArray[]);

    //策略基本信息
    public StrategyBaseDto getStrategyDetail(long stid) ;

    //策略交易简单信息展示
    public  StrategyBaseDto getstratebasicinfo(long stid) ;

    //策略委托信号
    public  List<AuthorizeRecordDto> getorder_signal(long stid) ;

    /**
     * @param myselect 分类搜索
     * @param content  关键字
     * @param pageNo   当前页数
     * @return _1 为结果集, _2为 分页page信息,
     */
    public  F.T2<List<StrategyBaseDto>, Page> favoriteStrategyList(int myselect, String content, int pageNo, Long uid) ;

    /**
     * 高级搜索
     *
     * @param cnd
     * @param pageNo 当前页
     * @return _1 为结果集, _2为总条数, _3 总共页数
     */
    public  F.T2<List<StrategyBaseDto>, Page> favoriteStrategyAdvanceSearch(StrategySearchCnd cnd, int myselect, int pageNo, Long uid);

    /* 订阅策略列表
* @param myselect 分类
* @param content 关键字
* @param pageNo 页码
* @param uid 用户ID
* @return
    */
    public F.T2<List<StrategyBaseDto>, Page> subscriptionStrategyList(int sortType, String keyword, int pageNo, Long uid);

    /**
     * 高级搜索
     *
     * @param cnd    搜索DTO
     * @param pageNo 当前页
     * @return _1 为结果集, _2为总条数, _3 总共页数
     */
    public F.T2<List<StrategyBaseDto>, Page> subscriptionStrategyAdvanceSearch(StrategySearchCnd cnd, int sortType, int pageNo, Long uid);

    /**
     * 查询用户订阅
     * @param uid
     * @param stid
     * @return
     */
    public StrategyOrderDto findUserStrategyOrder(long uid, long stid);

    /**
     * 查询该策略所有的评论
     * @param id
     * @return
     */
    public  List<UserStrategyDiscuss> userDiscussList(Long id);

    /**
     * 查询用户的已上架策略
     * @param uid 用户ID
     * @return
     */
    public  List<Map<String,Object>> fetchStrategyByUserId(long uid);

    public long   addStrategyAccountTemplate(StrategyAccountTemplateDto strategyAccountTemplateDto);


    public long addStrategySecurityOrigunalInfo(StrategySecurityOriginalDto securityOriginalDto);

    public long fetchStartegyNumByServiceId(int serviceId);
}
