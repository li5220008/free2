package controllers;

import annotation.QicFunction;
import bussiness.StrategyDetailService;
import bussiness.UserInfoService;
import models.iquantCommon.*;
import util.CommonUtils;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 策略详情ct
 * User: liangbing
 * Date: 13-7-2
 * Time: 上午9:20
 */
public class StrategyDetail extends BasePlayControllerSupport {

    @Inject
    static StrategyDetailService strategyDetailService;
    @Inject
    static UserInfoService userInfoService;


    @QicFunction(id = 2)
    public static void detail(long stid,int enginetypeId, long uid) {
        //得到策略基本信息
        StrategyBaseDto strategyBaseDto = strategyDetailService.getStrategyDetail(stid);

        //查询策略是否被收藏
        boolean b = strategyDetailService.iscollect(stid, uid);

        //策略简单信息展示
        StrategyBaseDto strategyBaseinfo = strategyDetailService.getstratebasicinfo(stid);


        //绩效指标
        IndicatorDto indicator = new IndicatorDto();
        IndicatorDto indicatorhis = new IndicatorDto();
        //ＱＩＡ的绩效指标
        QiaIndicatorDto qiaIndicatorDto = new QiaIndicatorDto();
        QiaIndicatorDto qiaIndicatorDtohis = new QiaIndicatorDto();
        switch (enginetypeId) {
            case 1://QICore的绩效指标
                indicator = strategyDetailService.getindicator(stid, 2);
                indicatorhis = strategyDetailService.getindicator(stid, 1);
                break;
            case 2://QIA的绩效指标
                qiaIndicatorDto = strategyDetailService.getQiaIndicatorDto(stid, 2);
                qiaIndicatorDtohis = strategyDetailService.getQiaIndicatorDto(stid, 1);
                break;
            default:
        }

        //策略委托信号
        List<AuthorizeRecordDto> ordersignallist = new ArrayList<AuthorizeRecordDto>();
        if (enginetypeId == 1) {//只有QICore 才有委托信号
            ordersignallist = strategyDetailService.getorder_signal(stid);
        }
        // 收益率走势图
        Date upTime = strategyBaseDto.upTime;//上架时间


        //周线
        List<String> weekDataHTM = strategyDetailService.strategyDetailForWeekPictrue(String.valueOf(stid), true, upTime);//所有的历史回测数据
        List<String> weekDataRTM = strategyDetailService.strategyDetailForWeekPictrue(String.valueOf(stid), false, upTime);//所有实时模拟数据
        //日线
        List<String> dayDataHTM = strategyDetailService.strategyDetailForDayPictrue(String.valueOf(stid), true, upTime);//所有的历史回测数据
        List<String> dayDataRTM = strategyDetailService.strategyDetailForDayPictrue(String.valueOf(stid), false, upTime);//所有实时模拟数据


        //判断句是否订阅
        boolean isorder = strategyDetailService.isorder(uid, stid);

        //订阅到期时间
        Date strategy_etime = strategyDetailService.dueDate(uid, stid);

        int discussesBoolean = strategyDetailService.judgeDiscusses(stid, uid);

        render(b, strategyBaseDto, uid, stid, indicator, indicatorhis, ordersignallist, isorder,
                strategy_etime, discussesBoolean, dayDataHTM, dayDataRTM, weekDataHTM, weekDataRTM,
                 qiaIndicatorDto, qiaIndicatorDtohis, enginetypeId);
    }

    public static void refreshSignal(long stid) {
        List<AuthorizeRecordDto> ordersignallist = strategyDetailService.getorder_signal(stid);
        render(ordersignallist);
    }

    //策略持仓
    @QicFunction(id = 2)
    public static void holdPosition(long id, int enginetypeId, int pageNo) {
        List<StrategyPositionDto> strategyPositionDtoList = new ArrayList<StrategyPositionDto>();
        if (enginetypeId == 1) {
            strategyPositionDtoList = strategyDetailService.getStrategyPosition(id, pageNo);
        } else if (enginetypeId == 2) {
            strategyPositionDtoList = strategyDetailService.getQiaPosition(id, pageNo);
        }
        render(strategyPositionDtoList, enginetypeId, id);
    }

    //持仓滚动分页
    @QicFunction(id = 2)
    public static void moreHoldPosition(long id, int enginetypeId, int pageNo) {
        List<StrategyPositionDto> strategyPositionDtoList = new ArrayList<StrategyPositionDto>();
        if (enginetypeId == 1) {
            strategyPositionDtoList = strategyDetailService.getStrategyPosition(id, pageNo);
        } else if (enginetypeId == 2) {
            strategyPositionDtoList = strategyDetailService.getQiaPosition(id, pageNo);
        }
        render(strategyPositionDtoList, enginetypeId, id);
    }

    //委托记录
    @QicFunction(id = 2)
    public static void entrustRecord(long id, int pageNo) {
        List<AuthorizeRecordDto> authorizeRecordDtoList = strategyDetailService.getAuthorizeRecord(id, pageNo);
        render(authorizeRecordDtoList, id);
    }

    //委托滚动分页
    @QicFunction(id = 2)
    public static void moreEntrustRecord(long id, int pageNo) {
        List<AuthorizeRecordDto> authorizeRecordDtoList = strategyDetailService.getAuthorizeRecord(id, pageNo);
        render(authorizeRecordDtoList, id);
    }

    //成交记录
    @QicFunction(id = 2)
    public static void bargainRecord(long id, int pageNo) {
        List<ExecutionRecordDto> executionRecordDtoList = strategyDetailService.getExecutionRecord(id, pageNo);
        render(executionRecordDtoList, id);
    }


    //成交滚动分页
    @QicFunction(id = 2)
    public static void moreBargainRecord(long id, int pageNo) {
        List<ExecutionRecordDto> executionRecordDtoList = strategyDetailService.getExecutionRecord(id, pageNo);
        render(executionRecordDtoList, id);

    }


    //我要评论历史数据
    @QicFunction(id = 4)
    public static void userComment(long id, long uid) {
        List<UserStrategyDiscuss> discussesList = strategyDetailService.userDiscussList(id);
        int discussesBoolean = strategyDetailService.judgeDiscusses(id, uid);
        render(discussesBoolean, discussesList);
    }

    /**
     * 保存评论
     *
     * @param uid
     * @param stid
     * @param usd
     */
    @QicFunction(id = 4)
    public static void userDiscuss(Long uid, Long stid, UserStrategyDiscuss usd) {
        Map<String, Object> json = new HashMap<String, Object>();
        if (uid != null && stid != null) {
            usd.disTime = new Date();
            boolean b = strategyDetailService.saveDiscuss(usd, uid, stid);
            UserInfoDto userInfoDto = userInfoService.findUserInfoById(uid);
            if(b){
                json.put("op", true);
                json.put("msg", "提交成功");
                json.put("dname", userInfoDto.name);
                json.put("star", usd.star);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String time = df.format(usd.disTime);
                json.put("time", time);
                json.put("content", usd.content);
            }else{
                json.put("op", false);
                json.put("msg", "提交失败");
            }
        }
        renderJSON(json);
    }

    /**
     * 加入订阅
     *
     * @param month
     * @param uid
     * @param stid
     */
    @QicFunction(id = 4)
    public static void addStrategyOrder(int month, Long uid, Long stid) {
        Map<String, Object> json = new HashMap<String, Object>();
        OrderMsgDto orderMsgDto = strategyDetailService.addOrder(month,uid,stid);
        json.put("message",orderMsgDto.message);
        json.put("success",orderMsgDto.success);
        json.put("date",CommonUtils.getFormatDate("yyyy-MM-dd",orderMsgDto.date));

        renderJSON(json);
    }

    /**
     * 续订   续订和加入订阅是一个操作,Server里面去取下order_etime 就可以了.
     * @param month
     * @param uid
     * @param stid
     */
    @QicFunction(id = 4)
    public static void delayStrategyOrder(int month, Long uid, Long stid) {
        Map<String, Object> json = new HashMap<String, Object>();
        OrderMsgDto orderMsgDto = strategyDetailService.delayOrder(month, uid, stid);
        json.put("message",orderMsgDto.message);
        json.put("success",orderMsgDto.success);
        json.put("date", CommonUtils.getFormatDate("yyyy-MM-dd",orderMsgDto.date));

        renderJSON(json);
    }

    //绩效数据 历史回测和实时数据切换
    @QicFunction(id=4)
    public static void indicator(long id,int type){
        QiaIndicatorDto qiaIndicatorDto=  strategyDetailService.getQiaIndicatorDto(id, type);
        render(qiaIndicatorDto,type);
    }

}
