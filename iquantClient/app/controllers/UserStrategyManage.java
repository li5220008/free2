package controllers;

import annotation.QicFunction;
import bussiness.StrategyService;
import bussiness.UserInfoService;
import models.iquantCommon.StrategyBaseDto;
import models.iquantCommon.StrategyDto;
import models.iquantCommon.UserInfoDto;
import org.apache.commons.lang.StringUtils;
import play.Logger;
import play.libs.F;
import util.CommonUtils;
import util.Page;
import util.SystemResponseMessage;

import javax.inject.Inject;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 12-12-18
 * Time: 上午11:09
 * 功能描述: 用户策略管理相关类
 */

public class UserStrategyManage extends BasePlayControllerSupport {

    @Inject
    static StrategyService strategyService;
    @Inject
    static UserInfoService userInfoService;
    /**
     * 我上传的策略列表
     */
    @QicFunction(id = 7)
    public static void myStrategyList(Map<String, String> map) {
        if(map == null){
            map = new HashMap<String,String>();
        }
        map.put("uid", params.get("uid"));//得到当前用户的uid
        map.put("orderType", params.get("orderType") == null ? "0" : params.get("orderType"));
        map.put("orderCol", params.get("orderCol") == null ? "1" : params.get("orderCol"));
        map.put("pageNo", params.get("pageNo") == null ? "1" : params.get("pageNo"));
        map.put("status", params.get("status") == null ? "-1" : params.get("status"));
        map.put("keyword", params.get("keyword") == null ? "" : params.get("keyword"));
        map.put("tradeType", params.get("tradeType") == null ? "0" : params.get("tradeType"));
        map.put("tradeVariety", params.get("tradeVariety") == null ? "0" : params.get("tradeVariety"));
        map.put("strategyLanguage", params.get("strategyLanguage") == null ? "0" : params.get("strategyLanguage"));

        F.T2<List<StrategyBaseDto>, Page> list = strategyService.findStrategysByUser(map);
        List<StrategyBaseDto> strategyBaseDtos = list._1;
        Page page = list._2;
        map.remove("pageNo");
        UserInfoDto curLoginUser = userInfoService.findUserInfoById(Long.valueOf(params.get("uid")));
        render(strategyBaseDtos,page,map,curLoginUser);
    }


    /**
     * 上传完策略文件后-----添加策略
     *
     * @param strategyDto
     * @Author 刘建力
     */
    @QicFunction(id=7)
    public static void addStrategy(StrategyDto strategyDto) {
        //先拷备文件到正式目录,再添加
        String filePath = params.get("files");
        long uid = params.get("uid",Long.class);
        String now = CommonUtils.yyyyMMdd.format(new Date());
        try {
            if(StringUtils.isBlank(strategyDto.customerLookbackEndTime) || StringUtils.isBlank(strategyDto.customerLookbackStartTime)){

            }else if(strategyDto.customerLookbackEndTime.compareTo(now)>0 || strategyDto.customerLookbackStartTime.compareTo(strategyDto.customerLookbackEndTime)>0){

                setSuccessFlag(false);
                setMessage(SystemResponseMessage.DATE_ERROR_RSP);
            }else{
                long dValue =  CommonUtils.yyyyMMdd.parse(strategyDto.customerLookbackEndTime).getTime()-   CommonUtils.yyyyMMdd.parse(strategyDto.customerLookbackStartTime).getTime();
                if(dValue < 3* 24 * 60 * 60 * 1000l){
                    setSuccessFlag(false);
                    setMessage(SystemResponseMessage.DATE_RNAGE_ERROR_RSP);
                }else{
                    boolean  ret = strategyService.insertStrategy(strategyDto, filePath, uid);
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


    @QicFunction(id=7)
    public static void findStrategyByName(String sname) {
        if (strategyService.findStrategyByName(sname) != null) {
            renderText(false);
        } else {
            renderText(true);
        }
    }

}
