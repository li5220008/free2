package controllers;

import bussiness.strategy.*;
import bussiness.strategy.impl.StrategyService;
import job.StrategyYieldSmallPicJob;
import models.iquantCommon.*;
import play.data.binding.As;
import play.libs.F;
import util.GsonUtil;
import util.Page;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-7-4
 * Time: 下午1:41
 * 功能描述: 策略列表相关API
 */
public class StrategyCt extends BaseController {

    @Inject
    static IStrategyService strategyService;
    @Inject
    static IDrawYieldChartService drawYieldChartService;

    public static void fetchStrategyList(int orderType, String keyword, int pageNo) {
        F.T2<List<StrategyBaseDto>, Page> result = strategyService.strategyList(orderType, keyword, pageNo);
        responseJSON(result._1, result._2);

    }

    /**
     * 获取策略对比策略列表信息
     *
     * @param stids 策略id
     */
    public static void fetchCompareStrategyList(@As(",") String[] stids) {
        List<StrategyContrastDto> list = strategyService.strategyContrast(stids);
        responseJSON(list);
    }

    /**
     * 获取策略列表对比x轴日期信息
     *
     * @param stids 策略id
     */
    public static void fetchCompareStrategyDates(@As(",") String[] stids) {
        String[] dates = strategyService.strategyContrastForPictrue(stids);
        responseJSON(dates);
    }

    /**
     * 绩效展示 周线数据
     *
     * @param stid       策略id
     * @param isBackTest 是否历史回测数据
     * @param upTime     上架时间
     */
    public static void fetchStrategyDetailWeekData(String stid, boolean isBackTest, @As(format = "yyyy-MM-dd:hh:mm:ss") Date upTime) {
        String[] weekDate = drawYieldChartService.strategyDetailForWeekPictrue(stid, isBackTest, upTime);
        responseJSON(weekDate);
    }

    /**
     * 绩效展示 日线数据
     *
     * @param stid       策略id
     * @param isBackTest 是否历史回测数据
     * @param upTime     上架时间
     */
    public static void fetchStrategyDetailDailyData(String stid, boolean isBackTest, @As(format = "yyyy-MM-dd:hh:mm:ss") Date upTime) {
        String[] dailyDate = drawYieldChartService.strategyDetailForDayPictrue(stid, isBackTest, upTime);
        responseJSON(dailyDate);
    }

    /**
     * 查询策略列表(高级搜索)
     *
     * @param sortType
     * @param pageNo
     */
    public static void fetchStrategyAdvance(int sortType, int pageNo) {

        String body = fetchBody();
        StrategySearchCnd strategySearchCnd = GsonUtil.createWithoutNulls().fromJson(body, StrategySearchCnd.class);
        F.T2<List<StrategyBaseDto>, Page> result = strategyService.advanceSearch(strategySearchCnd, sortType, pageNo);
        responseJSON(result._1, result._2);

    }

    /**
     * 查询策略简单信息
     *
     * @param stid
     */
    public static void fetchStrategyBaseInfo(long stid) {
        StrategyBaseDto strategyBaseDto = strategyService.getstratebasicinfo(stid);
        responseJSON(strategyBaseDto);
    }

    /**
     * 查询策略基本信息
     * @param stid
     */
    public static void getStrategyBaseInfo(long stid)  {
        StrategyBaseDto strategyBaseDto = strategyService.getStrategyDetail(stid);
        responseJSON(strategyBaseDto);
    }

    /**
     * 查询策略绩效信息
     *
     * @param stype 策略类型 2. qia  1. qicore
     * @param stid  策略id
     * @param ctype 绩效类型  1. 历史回测   2. 实时模拟
     */
    public static void fetchStrategyIndicator(int stype, long stid, int ctype) {

        switch (stype) {
            case 1://qicore
                IndicatorDto indicator = strategyService.getindicator(stid, ctype);
                responseJSON(indicator);
                break;
            case 2://qia
                QiaIndicatorDto qiaIndicatorDto = strategyService.getQiaIndicatorDto(stid, ctype);
                responseJSON(qiaIndicatorDto);
                break;
            default:
                responseError("参数stype值错误");

        }

    }

    /**
     * 查看策略委拖信号
     *
     * @param stid 策略id
     */
    public static void fetchAuthorizeRecord(long stid, int pageNo) {
        List<AuthorizeRecordDto> list = strategyService.getAuthorizeRecord(stid, pageNo);
        responseJSON(list);

    }

    /**
     * 查询策略持仓
     *
     * @param stid   策略id
     * @param pageNo 页码
     * @param stype  类型 1 qicore  2 qia
     */
    public static void fetchStrategHoldPosition(long stid, int pageNo, int stype) {
        List<StrategyPositionDto> strategyPositionDtoList = null;
        switch (stype) {
            case 1:
                strategyPositionDtoList = strategyService.getStrategyPosition(stid, pageNo);
                break;
            case 2:
                strategyPositionDtoList = strategyService.getQiaPosition(stid, pageNo);
                break;
            default:
                responseError("stype参数错误");
        }
        responseJSON(strategyPositionDtoList);

    }

    /**
     * 获取策略委托记录
     *
     * @param stid   策略 id
     * @param pageNo 页码
     */
    public static void fetchEntrustRecord(long stid, int pageNo) {
        List<AuthorizeRecordDto> list = strategyService.getAuthorizeRecord(stid, pageNo);
        responseJSON(list);
    }

    /**
     * 查询策略成交记录
     *
     * @param stid   策略 id
     * @param pageNo 页码
     */
    public static void fetchBargainRecord(long stid, int pageNo) {
        List<ExecutionRecordDto> executionRecordDtoList = strategyService.getExecutionRecord(stid, pageNo);
        responseJSON(executionRecordDtoList);
    }

    /**
     * 查询策略评论列表
     *
     * @param stid 策略ID
     */
    public static void fetchUserCommentList(long stid) {
        List<UserStrategyDiscuss> list = strategyService.userDiscussList(stid);
        responseJSON(list);
    }

    /**
     * 回收站策略列表
     *
     * @param pageNo
     * @param uid
     */
    public static void fetchRecycleList(int pageNo, long uid) {
        String json = fetchBody();
        StrategyPar strategyPar = GsonUtil.createWithoutNulls().fromJson(json, StrategyPar.class);
        F.T2<List<StrategyDto>, Page> result = strategyService.retrieveList(strategyPar == null ? new StrategyPar() : strategyPar, pageNo, uid);
        responseJSON(result._1, result._2);

    }

    /**
     * 已上架策略列表
     *
     * @param pageNo 页码
     * @param uid    用户id
     */
    public static void fetchUpStrategyList(int pageNo, long uid) {
        String json = fetchBody();
        StrategyPar strategyPar = GsonUtil.createWithoutNulls().fromJson(json, StrategyPar.class);
        F.T2<List<StrategyDto>, Page> result = strategyService.groundingList(strategyPar == null ? new StrategyPar() : strategyPar, pageNo, uid);
        responseJSON(result._1, result._2);
    }

    /**
     * 查询待上架策略列表
     *
     * @param pageNo
     * @param uid
     */
    public static void fetchWaitList(int pageNo, long uid) {
        String json = fetchBody();
        StrategyPar strategyPar = GsonUtil.createWithoutNulls().fromJson(json, StrategyPar.class);
        F.T2<List<StrategyDto>, Page> result = strategyService.waitList(strategyPar == null ? new StrategyPar() : strategyPar, pageNo, uid);
        responseJSON(result._1, result._2);
    }

    /**
     * 根据策略id查询策略
     *
     * @param id
     */
    public static void fetchStrategyById(long id) {
        StrategyBaseinfo strategyBaseinfo = strategyService.findStrategyById(id);
        responseJSON(strategyBaseinfo);
    }

    /**
     * 根据策略ids集合查询策略
     *
     * @param ids
     */
    public static void fetchStrategyByIds(@As(",") Long[] ids) {
        List<StrategyBaseinfo> strategyBaseinfos = strategyService.findStrategysByIds(ids);
        responseJSON(strategyBaseinfos);
    }

    /**
     * 根据策略名查询策略
     * @param sname
     */
    public static void fetchStrategyByName(String sname){
        StrategyBaseDto strategyBaseDto =   strategyService.findStrategyByName(sname);
        responseJSON(strategyBaseDto);
    }

    /**
     *  生成策略图片
     * @param stids 策略id集合  uuid
     */
    public static void generatePic(List<String> stids){
        try{
            StrategyYieldSmallPicJob.calcYieldDataAndDrawPic(stids);
            renderText(true) ;
        }catch(Exception e){
           renderText(false) ;
        }
    }
}
