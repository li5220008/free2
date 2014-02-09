package bussiness;

import models.iquantCommon.*;
import protoc.HttpBody;
import protoc.URILib;
import protoc.parser.ActionResult;
import util.CommonUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 策略详情业务方法
 * User: liangbing
 * Date: 13-7-4
 * Time: 上午9:36
 */
public class StrategyDetailService extends BasicService {
    //策略基本信息
    public StrategyBaseDto getStrategyDetail(long stid) {
        StrategyBaseDto strategyBaseDto = new StrategyBaseDto();
        String url_strategy_base_dto = URILib.FETCH_STRATEGY_BASEINFO;//todo

        ActionResult<StrategyBaseDto> strategyBaseDtoActionResult = remoteRequestService.getBean(url_strategy_base_dto, StrategyBaseDto.class, stid);
        if (strategyBaseDtoActionResult != null) {
            strategyBaseDto = strategyBaseDtoActionResult.data;
        }

        if (strategyBaseDto.discussCount == 0)
            strategyBaseDto.starLevel = 0;
        else
            strategyBaseDto.starLevel = (float) strategyBaseDto.discussTotal / strategyBaseDto.discussCount;
        return strategyBaseDto;
    }

    //根据策略id查询是否被收藏
    public boolean iscollect(long stid, long uid) {
        String url_is_collect = URILib.JUDGE_IS_CONLLECT;//todo
        Boolean bool = Boolean.valueOf(remoteRequestService.getSingleValue(url_is_collect, stid, uid).data);
        return bool;
    }

    //策略交易简单信息展示 //todo 商量一下
    public StrategyBaseDto getstratebasicinfo(long stid) {
        StrategyBaseDto strategyBaseDto = new StrategyBaseDto();
        String url_strategy_base = "";


        return strategyBaseDto;
    }


    //QIC绩效指标
    public IndicatorDto getindicator(long stid, int ctype) {
        IndicatorDto indicator = new IndicatorDto();
        String url_indicator = URILib.FETCH_QIC_INDICATOR;
        int stype = 1;
        ActionResult<IndicatorDto> actionResult = remoteRequestService.getBean(url_indicator, IndicatorDto.class,stype, stid,ctype);
        if (actionResult != null) {
            indicator = actionResult.data;
        }
        return indicator;
    }

    //QIA 绩效指标
    public QiaIndicatorDto getQiaIndicatorDto(long stid, int ctype) {
        QiaIndicatorDto indicator = new QiaIndicatorDto();
        String url_qia_indicator = URILib.FETCH_QIA_INDICATOR;
        int stype = 2;
        ActionResult<QiaIndicatorDto> qiaIndicatorDtoActionResult = remoteRequestService.getBean(url_qia_indicator, QiaIndicatorDto.class,stype,  stid, ctype);
        if (qiaIndicatorDtoActionResult != null) {
            indicator = qiaIndicatorDtoActionResult.data;
        }
        return indicator;
    }

    //策略委托信号
    public List<AuthorizeRecordDto> getorder_signal(long stid) {
        List<AuthorizeRecordDto> authorizeRecordDtoList = new ArrayList<AuthorizeRecordDto>();
        String url_authorize = URILib.STRATEGY_AUTHORIZE_RECORD;
        ActionResult<List<AuthorizeRecordDto>> authorizeRecordDtoActionResult = remoteRequestService.getList(url_authorize, AuthorizeRecordDto.class, stid);
        if (authorizeRecordDtoActionResult != null) {
            authorizeRecordDtoList = authorizeRecordDtoActionResult.data;
        }
        return authorizeRecordDtoList;
    }

    //todo 画图用的数据
    public List<String> strategyDetailForWeekPictrue(String stid, boolean isBackTest, Date time) {
        List<String> strings = new ArrayList<String>();
        String url_for_week = URILib.STRATEGY_STRATEGY_DETAIL_WEEK_DATA;
        String upTime = CommonUtils.getFormatDate(CommonUtils.DATE_FORMAT_STR_ARR[1],time);
        ActionResult<List<String>> actionResult = remoteRequestService.getList(url_for_week,String.class,stid,isBackTest,upTime);

        if(actionResult!=null){
            strings = actionResult.data;
        }

        return strings;
    }

    //todo 画图要用的数据
    public List<String> strategyDetailForDayPictrue(String stid, boolean isBackTest, Date time) {
        //strategyId = "96";
        List<String> strings = new ArrayList<String>();
        String url_for_day = URILib.STRATEGY_STRATEGY_DETAIL_DAILY_DATA;
        String upTime = CommonUtils.getFormatDate(CommonUtils.DATE_FORMAT_STR_ARR[1],time);
        ActionResult<List<String>> actionResult = remoteRequestService.getList(url_for_day,String.class,stid,isBackTest,upTime);

        if(actionResult!=null){
            strings = actionResult.data;
        }
        return strings;
    }

    /**
     * 判断是否订阅
     *
     * @param uid
     * @param stid
     * @return
     */
    public boolean isorder(Long uid, Long stid) {
        String url_is_order = URILib.JUDGE_IS_ORDER;//todo
        boolean bool = false;
        bool = Boolean.valueOf(remoteRequestService.getSingleValue(url_is_order, uid,stid).data);
        return bool;
    }

    /**
     * 订阅到期时间
     * @param uid
     * @param stid
     * @return
     */
    public Date dueDate(long uid, long stid) {
        Date date =null;
        String url_dueDate = URILib.ORDER_DUE_DATE;
        ActionResult<String> stringActionResult = remoteRequestService.getSingleValue(url_dueDate, uid, stid);
        if (stringActionResult != null&&stringActionResult.data!=null) {
            date = CommonUtils.parseDate(stringActionResult.data);
        }

        return date;
    }

    /**
     * 判断该用户是否已经评论的该策略
     *
     * @param stid
     * @param uid
     */
    public Integer judgeDiscusses(Long stid, Long uid) {
        String url_judge = URILib.JUDGE_DISCUESS_STRATEGY;
        int isDiscussVal = Integer.parseInt(remoteRequestService.getSingleValue(url_judge, stid, uid).data);
        return isDiscussVal;
    }


    //策略持仓
    public List<StrategyPositionDto> getStrategyPosition(long stid, int pageNo) {
        List<StrategyPositionDto> strategyPositionDtoList = new ArrayList<StrategyPositionDto>();
        String url_position = URILib.FETCH_QIC_HOLD_POSITION_LIST;
        int stype = 1;
        ActionResult<List<StrategyPositionDto>> actionResult = remoteRequestService.getList(url_position, StrategyPositionDto.class,stype ,stid,pageNo);
        if (actionResult != null) {
            strategyPositionDtoList = actionResult.data;
        }
        return strategyPositionDtoList;
    }

    //QIA 策略持仓
    public List<StrategyPositionDto> getQiaPosition(long stid, int pageNo) {
        List<StrategyPositionDto> strategyPositionDtoList = new ArrayList<StrategyPositionDto>();
        String url_qia_postition = URILib.FETCH_QIA_HOLD_POSITION_LIST;
        int stype = 2;
        ActionResult<List<StrategyPositionDto>> actionResult = remoteRequestService.getList(url_qia_postition, StrategyPositionDto.class,stype , stid, pageNo);

        if (actionResult != null) {
            strategyPositionDtoList = actionResult.data;
        }

        return strategyPositionDtoList;
    }


    //委托记录
    public List<AuthorizeRecordDto> getAuthorizeRecord(long stid, int pageNo) {

        List<AuthorizeRecordDto> authorizeRecordDtoList = new ArrayList<AuthorizeRecordDto>();

        String url_auth_record = URILib.FETCH_ENTRUST_RECORD_LIST;

        ActionResult<List<AuthorizeRecordDto>> actionResult = remoteRequestService.getList(url_auth_record, AuthorizeRecordDto.class,stid, pageNo);
        if (actionResult != null) {
            authorizeRecordDtoList = actionResult.data;
        }

        return authorizeRecordDtoList;
    }

    //成交记录
    public List<ExecutionRecordDto> getExecutionRecord(Long stid, int pageNo) {
        List<ExecutionRecordDto> executionRecordDtoList = new ArrayList<ExecutionRecordDto>();
        String url_execution_recod = URILib.FETCH_BARGAIN_RECORD_LIST;
        ActionResult<List<ExecutionRecordDto>> actionResult = remoteRequestService.getList(url_execution_recod, ExecutionRecordDto.class, stid, pageNo);
        if (actionResult != null) {
            executionRecordDtoList = actionResult.data;
        }
        return executionRecordDtoList;
    }


    /**
     * 查询该策略所有的评论
     *
     * @param stid
     * @return
     */
    public List<UserStrategyDiscuss> userDiscussList(Long stid) {
        List<UserStrategyDiscuss> usdList = new ArrayList<UserStrategyDiscuss>();
        String url_user_discuss_list = URILib.FETCH_USER_CONTENT_LIST;
        ActionResult<List<UserStrategyDiscuss>> actionResult = remoteRequestService.getList(url_user_discuss_list, UserStrategyDiscuss.class, stid);

        if (actionResult != null) {
            usdList = actionResult.data;
        }

        return usdList;
    }


    /**
     * 保存该用户对该策略的评论
     *
     * @param usd
     * @param uid
     * @param stid
     */
    public boolean saveDiscuss(UserStrategyDiscuss usd, Long uid, Long stid) {

        boolean msg = false;

        String url_save_discuss = URILib.SAVE_USER_DISCUSS;

        HttpBody httpBody = new HttpBody();
        httpBody.body = usd;

        ActionResult<String> stringActionResult = remoteRequestService.getSingleValue(url_save_discuss, httpBody, uid,stid);

        if (stringActionResult != null) {
            msg = Boolean.valueOf(stringActionResult.data);
        }
        return msg;
    }


    /**
     * 获取订阅信息
     * @param month
     * @param uid
     * @param stid
     * @return
     */
     public OrderMsgDto addOrder(int month, Long uid, Long stid){

         OrderMsgDto orderMsgDto = new OrderMsgDto();

         String url_add_order = URILib.ORDER_STRATEGY;

         ActionResult<OrderMsgDto> orderMsgDtoActionResult = remoteRequestService.getBean(url_add_order,OrderMsgDto.class,month,uid,stid);
         if(orderMsgDtoActionResult!=null){
                orderMsgDto = orderMsgDtoActionResult.data;
         }
         return orderMsgDto;
     }

    /**
     * 延迟订阅
     * @param month
     * @param uid
     * @param stid
     * @return
     */
    public OrderMsgDto delayOrder(int month, Long uid, Long stid) {
        OrderMsgDto orderMsgDto = new OrderMsgDto();
        String url_delay_order = URILib.ORDER_DELAY_STRATEGY;
        ActionResult<OrderMsgDto> orderMsgDtoActionResult = remoteRequestService.getBean(url_delay_order,OrderMsgDto.class,month,uid,stid);
        if(orderMsgDtoActionResult!=null){
            orderMsgDto = orderMsgDtoActionResult.data;
        }
        return orderMsgDto;
    }

}
