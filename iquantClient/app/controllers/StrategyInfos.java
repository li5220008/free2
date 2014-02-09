package controllers;

import annotation.QicFunction;
import bussiness.BackTestService;
import bussiness.StrategyDetailService;
import bussiness.StrategyService;
import bussiness.SystemConfigService;
import models.iquantCommon.*;
import org.apache.commons.lang.StringUtils;
import play.Logger;
import play.Play;
import play.libs.F;
import play.libs.WS;
import play.mvc.Util;
import play.mvc.With;
import util.CommonUtils;
import util.Constants;
import util.Page;
import util.SystemResponseMessage;

import javax.inject.Inject;
import java.util.*;

/**
 * 策略列表
 * User: liangbing
 * Date: 12-12-10
 * Time: 下午1:44
 * 策略列表展示和搜索
 */
@With({AuthorBaseIntercept.class})
public class StrategyInfos extends BasePlayControllerSupport {
    @Inject
    static StrategyService strategyService;
    @Inject
    static SystemConfigService systemConfigService;
    @Inject
    static StrategyDetailService strategyDetailService;

    private static final int gflag = 1;//已上架策略tab
    private static final int wflag = 2;//待上架策略tab
    private static final int rflag = 3;//回收站tab

    /**
     * 已上架策略列表
     * @param sp     参数类
     * @param pageNo 当前页
     */
    @QicFunction(id=23)
    public static void list(StrategyPar sp, int pageNo,long uid) {
        if (sp == null) {
            sp = new StrategyPar();
        }
        sp.flag = gflag;
        F.T2<List<StrategyDto>, Page> t2 = StrategyService.groundingList(sp, pageNo, uid);
        List<StrategyDto> gdList = t2._1;
        Page page = t2._2;
        render( gdList, page, sp);
    }

    /**
     * 查询待上架策略
     * @param sp     参数类
     * @param pageNo 当前页
     */
    @QicFunction(id=23)
    public static void grounding(StrategyPar sp, int pageNo,long uid) {
        boolean flag = Boolean.valueOf(Play.configuration.getProperty("show.upload.btn"));
        if (sp == null) {
            sp = new StrategyPar();
        }
        sp.flag = wflag;
        F.T2<List<StrategyDto>, Page> t2 = StrategyService.waitList(sp, pageNo, uid);
        List<StrategyDto> waitList = t2._1;
        Page page = t2._2;
        //查当前登入用户  (add by liujl 2012-12-20,用于策略上传的时候将当前用户显示为策略提供者)
        UserInfoDto userInfoDto = new UserInfoDto();//todo//UserInfoService.findUserInfoById(params.get("uid", Long.class));
        //策略上架筛选服务器
        sp.serviceParam = Integer.parseInt(SystemConfigService.get("srategy.up.server.switch")==null?"3":SystemConfigService.get("srategy.up.server.switch"));
        render("@list", waitList, page, sp,userInfoDto,flag);

    }

    /**
     * 策略回收站
     * @param sp     参数类
     * @param pageNo 当前页
     */
    @QicFunction(id=23)
    public static void retrieve(StrategyPar sp, int pageNo,long uid) {
        if (sp == null) {
            sp = new StrategyPar();
        }
        sp.flag = rflag;
        F.T2<List<StrategyDto>, Page> t2 = StrategyService.retrieveList(sp, pageNo, uid);
        List<StrategyDto> retrieveList = t2._1;
        Page page = t2._2;
        render("@list", retrieveList, page, sp);
    }


    /**
     * 加入回测
     * @Author 潘志威
     *  2012-12-21 modify liujl
     * @param ids
     */
    @QicFunction(id=23)
    public static void addLookback(String ids[], Map<String,Integer> serverId,long uid) {
        //StrategyService.addLookback(ids, status);
        boolean isSuccess = BackTestService.updateStratedyServerIdByIntId(serverId, ids);
        if(!isSuccess){
            setSuccessFlag(false);
            setMessage("加入失败，只有回测失败或审核中的策略才能回测");
        }
        //  LogInfosService.writeSystemLog(uid, SystemLoggerMessage.DO_BACK_TEST, SystemLoggerMessage.BACK_TEST, SystemLoggerMessage.TYPE);//写入系统日志
        renderJSON(getSampleResponseMap());
    }


    /**
     * 删除策略
     * @Author 潘志威
     *
     * @param ids
     */
    @QicFunction(id=23)
    public static void delStrategy(String ids[],long uid) {
        StrategyService.delstrategy(ids);
       // LogInfosService.writeSystemLog(uid, SystemLoggerMessage.DO_DELETE_STRATEGY, SystemLoggerMessage.DELETE_STRATEGY, SystemLoggerMessage.TYPE);
        renderJSON(getSampleResponseMap());
    }

    /**
     * 策略清空
     * @Author 潘志威
     */
    @QicFunction(id=23)
    public static void emptyStrategy(long uid) {
        StrategyService.emptystrategy();
       // LogInfosService.writeSystemLog(uid, SystemLoggerMessage.DO_EMPTY_STRATEGY, SystemLoggerMessage.EMPTY_STRATEGY, SystemLoggerMessage.TYPE);
        renderJSON(getSampleResponseMap());
    }


    /**
     * 上传完策略文件后-----添加策略
     *
     * @param strategyDto
     * @Author 刘建力
     */
    @QicFunction(id=23)
    public static void addStrategy(StrategyDto strategyDto) {
        //先拷备文件到正式目录,再添加

        String filePath = params.get("files");
        long uid = params.get("uid",Long.class);
        String now = CommonUtils.yyyyMMdd.format(new Date());
        try {
            if(StringUtils.isBlank(strategyDto.customerLookbackEndTime) || StringUtils.isBlank(strategyDto.customerLookbackStartTime)){
                setSuccessFlag(false);
                setMessage(SystemResponseMessage.DATE_ERROR_RSP);
            }else if(strategyDto.customerLookbackEndTime.compareTo(now)>0 || strategyDto.customerLookbackStartTime.compareTo(strategyDto.customerLookbackEndTime)>0){

                setSuccessFlag(false);
                setMessage(SystemResponseMessage.DATE_ERROR_RSP);
            }else{
                long dValue =  CommonUtils.yyyyMMdd.parse(strategyDto.customerLookbackEndTime).getTime()-   CommonUtils.yyyyMMdd.parse(strategyDto.customerLookbackStartTime).getTime();
                if(dValue < 3* 24 * 60 * 60 * 1000l){
                    setSuccessFlag(false);
                    setMessage(SystemResponseMessage.DATE_RNAGE_ERROR_RSP);
                }else{
                 boolean  ret = StrategyService.insertStrategy(strategyDto, filePath, uid);
                 setSuccessFlag(ret);
                 setMessage(ret? SystemResponseMessage.STRATEGY_UPLOAD_SUCCESS_RSP: SystemResponseMessage.STRATEGY_UPLOAD_FAILUER_RSP);
                }
            }
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
            setSuccessFlag(false);
            setMessage(SystemResponseMessage.STRATEGY_UPLOAD_FAILUER_RSP);
        }
        renderJSON(getSampleResponseMap());

    }

    public static void findStrategyByName(String sname) {
        if (strategyService.findStrategyByName(sname) != null) {
            renderText(false);
        } else {
            renderText(true);
        }
    }


    /**
     * 策略上架
     * @Author 潘志威
     *
     * @param ids
     */
    @QicFunction(id=23)
    public static void upStrategy(String ids[],Long uid,Map<String,Integer> serverId) {

        Integer i = judgeStrategyIsOut30(uid);
        if(i+ids.length>30){//上传前先判断该用户上传的策略总数是否超过了30个,若超过30则不允许用户再上传策略
            setMessage(SystemResponseMessage.STRATEGY_COUNT_BIG_30);
            setSuccessFlag(false);
        }else{//若小于30,执行下面上传操作
            String params = "";
            List<StrategyBaseinfo> strategyBaseinfos = strategyService.findStrategysByIds(ids);
            for (StrategyBaseinfo strategyBaseinfo : strategyBaseinfos) {
                params += strategyBaseinfo.stUuid + ",";
            }
            params = params.substring(0, params.length() - 1);
            String url = systemConfigService.get(Constants.show_createOneStrategyPic_path);
            String result = WS.url(url, params).get().getString();
            if ("true".equalsIgnoreCase(result)) {//如果图片生成成功,执行上架操作
                boolean flag = StrategyService.upstrategy(ids, serverId);
                if (flag) {
                    setMessage(SystemResponseMessage.STRATEGY_UP_RSP);
                    setSuccessFlag(true);
                } else {
                    setMessage(SystemResponseMessage.STRATEGY_UP_FAILURE_RSP);
                    setSuccessFlag(false);
                }
            } else {//图片生成失败,提示用户
                setMessage(SystemResponseMessage.STRATEGY_PIC_FAILURE);
                setSuccessFlag(false);
            }
        }
       // LogInfosService.writeSystemLog(uid, SystemLoggerMessage.DO_UP_STRATEGY, SystemLoggerMessage.UP_STRATEGY, SystemLoggerMessage.TYPE);
        renderJSON(getSampleResponseMap());
    }

    /**
     * 策略下架
     * @author 刘泓江
     * @param stId 用户选择的策略ID
     * @param setTime 用户设定时间
     * @param textValue 下架消息
     */
    @QicFunction(id=23)
    public static void strategyDownInfo(String stId, Date setTime, String textValue,long uid,int mark){
        Map<String,Object> json = new HashMap<String, Object>();
         int iFlag = StrategyService.strategyDown(stId, setTime, textValue, uid, mark);
        if(iFlag==1){
            json.put("flag",true);
            json.put("msg","下架成功");
        }else if(iFlag==2){
            json.put("flag",false);
            json.put("msg","该策略当前有用户订阅，不能下架");
        }else if(iFlag==4){
            json.put("flag",false);
            json.put("msg","请按提示填写下架消息！");
        }else if(iFlag==5){
            json.put("flag",false);
            json.put("msg","待下架状态不允许修改！");
        }else if(iFlag==6){
            json.put("flag",false);
            json.put("msg","设置时间必须大于当前时间！");
        }else{
            json.put("flag",false);
            json.put("msg","非法操作");
        }
            renderJSON(json);
    }

    /**
     * @author 刘泓江
     * 策略下架通知模板
     */
    @QicFunction(id=23)
    public static void strategyDown() {
        String content = systemConfigService.get(Constants.STRATEGY_DOWN_TEMPLATE_KEY);
        render(content);
    }

    /**
     * @author 魏贵礼
     * @param strategyId 策略ID
     */
    @QicFunction(id=23)
    public static void strategyDetail(long strategyId){
        if (strategyId < 0) strategyId = 0;
        StrategyBaseinfo strategyBaseinfo = strategyService.findStrategyById(strategyId);
        render(strategyBaseinfo);
    }

    //策略持仓
    @QicFunction(id=23)
    public static void holdPosition(long id,int enginetypeId,int pageNo){
        List<StrategyPositionDto> strategyPositionDtoList = new ArrayList<StrategyPositionDto>();
        if(enginetypeId==1){
            strategyPositionDtoList = strategyDetailService.getStrategyPosition(id, pageNo);
        }else if(enginetypeId==2){
            strategyPositionDtoList = strategyDetailService.getQiaPosition(id, pageNo);
        }
        render(strategyPositionDtoList,enginetypeId,id);
    }

    //持仓滚动分页
    @QicFunction(id=23)
    public static void moreHoldPosition(long id, int enginetypeId, int pageNo) {
        List<StrategyPositionDto> strategyPositionDtoList = new ArrayList<StrategyPositionDto>();
        if(enginetypeId==1){
            strategyPositionDtoList = strategyDetailService.getStrategyPosition(id, pageNo);
        }else if(enginetypeId==2){
            strategyPositionDtoList = strategyDetailService.getQiaPosition(id, pageNo);
        }
        render(strategyPositionDtoList,enginetypeId,id);
    }

    @QicFunction(id=23)
    public static void strategyIndicator(long stid,int enginetypeId){
        IndicatorDto indicator = strategyDetailService.getindicator(stid, 2);;
        IndicatorDto indicatorhis = strategyDetailService.getindicator(stid, 1);
        QiaIndicatorDto qiaIndicatorDto = strategyDetailService.getQiaIndicatorDto(stid, 2);
        QiaIndicatorDto qiaIndicatorDtohis = strategyDetailService.getQiaIndicatorDto(stid, 1);
        render(indicator,indicatorhis,qiaIndicatorDto,qiaIndicatorDtohis,enginetypeId);
    }

    //委托记录
    @QicFunction(id=23)
    public static void entrustRecord(long id, int pageNo){
        List<AuthorizeRecordDto> authorizeRecordDtoList = strategyDetailService.getAuthorizeRecord(id, pageNo);
        render(authorizeRecordDtoList, id);
    }

    //委托滚动分页
    @QicFunction(id=23)
    public static void moreEntrustRecord(long id, int pageNo) {
        List<AuthorizeRecordDto> authorizeRecordDtoList = strategyDetailService.getAuthorizeRecord(id, pageNo);
        render(authorizeRecordDtoList,id);
    }

    //成交记录
    @QicFunction(id=23)
    public static void bargainRecord(long id, int pageNo){
        List<ExecutionRecordDto> executionRecordDtoList = strategyDetailService.getExecutionRecord(id, pageNo);
        render(executionRecordDtoList, id);
    }

    //成交滚动分页
    @QicFunction(id=23)
    public static void moreBargainRecord(long id, int pageNo) {
        List<ExecutionRecordDto> executionRecordDtoList = strategyDetailService.getExecutionRecord(id, pageNo);
        render(executionRecordDtoList, id);
    }

    // 收益率走势图
    @QicFunction(id=23)
    public static void drawChart(String strategyId){
        Date upTime = strategyService.findStrategyById(Long.valueOf(strategyId)).upTime;
        //周线
        List<String> weekDataHTM = strategyDetailService.strategyDetailForWeekPictrue(strategyId, true, upTime);//所有的历史回测数据
        List<String> weekDataRTM = strategyDetailService.strategyDetailForWeekPictrue(strategyId, false, upTime);//所有实时模拟数据
        //日线
        List<String> dayDataHTM = strategyDetailService.strategyDetailForDayPictrue(strategyId, true, upTime);//所有的历史回测数据
        List<String> dayDataRTM = strategyDetailService.strategyDetailForDayPictrue(strategyId, false, upTime);//所有实时模拟数据
        render(weekDataHTM,weekDataRTM,dayDataHTM,dayDataRTM);

    }


    @Util
    public static Integer judgeStrategyIsOut30(long uid) {
        Integer i = StrategyService.judgeStrategyIsOut30(uid);
        return i;
    }
}
