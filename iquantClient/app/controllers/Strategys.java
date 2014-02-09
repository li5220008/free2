package controllers;

import annotation.QicFunction;
import bussiness.StrategyContrastService;
import bussiness.StrategyService;
import bussiness.UserTemplateService;
import models.iquantCommon.StrategyBaseDto;
import models.iquantCommon.StrategyContrastDto;
import models.iquantCommon.StrategySearchCnd;
import models.iquantCommon.UserTemplate;
import org.apache.commons.io.FileUtils;
import play.Play;
import play.libs.F;
import play.libs.IO;
import util.Page;
import util.QicConfigProperties;

import javax.inject.Inject;
import java.io.*;
import java.util.List;
import java.util.Set;

/**
 * 策略展示与对比
 * User: liangbing
 * Date: 13-7-1
 * Time: 下午1:30
 */
public class Strategys extends BasePlayControllerSupport {


    @Inject
    static StrategyService strategyService;
    @Inject
    static UserTemplateService userTemplateService;

    /**
     * 策略展示
     */
    @QicFunction(id = 3, dependencies = {4, 5})
    public static void strategyList(int myselect, String content, boolean isTableList, int pageNo, Long uid) {

        F.T2<List<StrategyBaseDto>, Page> t2 = strategyService.strategyList(myselect, content, pageNo);
        List<StrategyBaseDto> sbdList = t2._1;
        Page page = t2._2;

        //这边处理list的ID 传入(1,2,3) 到时侯Server的SQL直接用这个就可以了
        String ids = strategyService.listToString(sbdList);
        Set<Integer> collectSet =strategyService.queryUserCollectSet(ids, uid);

        //用户的自定义搜索条件
        List<UserTemplate> utList = userTemplateService.fetchUserSearchCond(uid, 1);
        if (isTableList) {
            render("@strategyTableList", sbdList, myselect, content, collectSet, utList, page, isTableList);
        } else {
            render(sbdList, myselect, content, collectSet, utList, page, isTableList);
        }
    }


    /**
     * 高级搜索
     *
     * @param pageNo 第几页. 从1开始
     */
    @QicFunction(id = 3)
    public static void advanceSearch(StrategySearchCnd cnd, int myselect, boolean isTableList, int pageNo, Long uid) {

        F.T2<List<StrategyBaseDto>, Page> t2 = strategyService.advanceSearch(cnd, myselect, pageNo);
        List<StrategyBaseDto> sbdList = t2._1;
        Page page = t2._2;

        Set<Integer> collectSet = strategyService.queryUserCollectSet(strategyService.listToString(sbdList), uid);

        //用户的自定义搜索条件
        List<UserTemplate> utList = userTemplateService.fetchUserSearchCond(uid, 1);

        boolean advanceSearch = true;

        if (isTableList) {
            render("@strategyTableList", sbdList, pageNo, collectSet, utList, page, advanceSearch, cnd, myselect, isTableList);
        } else {
            render("@strategyList", sbdList, pageNo, collectSet, utList, page, advanceSearch, cnd, myselect, isTableList);

        }
    }


    /**
     * 策略对比
     * @Auther liuhongjiang
     * @param
     */
    @QicFunction(id=3)
    public static  void  strategyContrast(String idArray[]){
        List<StrategyContrastDto> list = StrategyContrastService.strategyContrast(idArray);
        List<String> dates = StrategyContrastService.strategyContrastForPictrue(idArray);
        render(list, dates);
    }


    public static void highcharts(){
        render();
    }
}
