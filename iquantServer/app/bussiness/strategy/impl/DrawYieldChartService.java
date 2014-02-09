package bussiness.strategy.impl;

import bussiness.common.impl.BaseService;
import bussiness.strategy.IDrawYieldChartService;
import dbutils.SqlLoader;
import models.iquantCommon.StrategyDailyYieldDto;
import play.Logger;
import play.libs.F;
import utils.DrawPictrueUtil;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 组装数据 绘制收益走势图 show/manage 共用
 * User: liuhongjiang
 * Date: 12-11-10
 * Time: 上午11:12
 */
public class DrawYieldChartService extends BaseService implements IDrawYieldChartService{
    /**
     *
     * @param isHistory 是否为历史回测
     * @param strategyId 策略ID
     * @param upTime 上架时间
     * @return 策略在历史回测/实时模拟区间的时间 ：_1 最大时间；_2 最小时间
     */
    public  F.T2<Date,Date> getMinAndMaxDate(boolean isHistory,String strategyId,Date upTime){
        Date now = new Date();
        String queryHistoryDateSql = SqlLoader.getSqlById("queryHistoryUpdateDate");
        String queryRealDateSql = SqlLoader.getSqlById("queryRealTimeMinUpdateDate");
        Map<String, Object> maxAndMinHistoryDateMap = qicDbUtil.querySingleMap(queryHistoryDateSql, strategyId);
        Map<String, Object> maxAndMinRealDateDateMap = qicDbUtil.querySingleMap(queryRealDateSql, strategyId);
        Date maxDate;
        Date minDate;
        Date minDateForRealTime = now;//当前策略实时模拟区间的最小时间
        Date maxDateForRealTime = now;//当前策略实时模拟区间的最大时间
        Date minDateForHistory = new Date("2010/01/01");// 当前策略回测区间的最小时间 注意：这里设置了一个特定时间 防止历史回测最小时间比实时模拟最大时间大而导致页面前端绘图插件出错
        Date maxDateForHistory = now;//当前策略回测区间的最大时间
        if(maxAndMinHistoryDateMap!=null ){
            minDateForHistory = maxAndMinHistoryDateMap.get("minDate")==null?now:(Date)maxAndMinHistoryDateMap.get("minDate");
            maxDateForHistory = maxAndMinHistoryDateMap.get("maxDate")==null?now:(Date)maxAndMinHistoryDateMap.get("maxDate");
        }
        if(maxAndMinRealDateDateMap!=null ){
            minDateForRealTime = upTime==null?now:upTime;
            maxDateForRealTime = maxAndMinRealDateDateMap.get("maxDate")==null?now:(Date)maxAndMinRealDateDateMap.get("maxDate");
        }
        if(isHistory){
            minDate =minDateForHistory;//根据策略ID查询数据库取最小值
            maxDate =maxDateForHistory;
        }else{
            minDate =minDateForRealTime;
            maxDate =maxDateForRealTime;
        }
        if(minDate.getTime()>=maxDate.getTime()){
            //Logger.info("数据有问题 ： 逻辑最大时间小于逻辑最小时间 为了避免页面绘图错误，直接返回当前系统时间");
            return  F.T2(now,now);

        }

        return F.T2(maxDate,minDate);
    }

    /**
     *     单个策略 图形展示（日线）
     *     这个方法的主要作用是组装数据
     *     需要的数据：
     *     1.格式化近三个月的起始时间 maxDate minDate
     *     2.列出最近三个月收益率的最大值 最小值  取绝对值最大的数modYield
     *     3.把数据组装成[{name=“策略名1” date:[ Date.UTC(2010, 0, 1), 9.05],...},
     *     {name=“策略名2” date:[ Date.UTC(2010, 1, 1), 9.05],...}]类型
     *     @param  strategyId 策略ID
     *     @param  isHistory 是否是历史回测
     *     @param  upTime 上架时间
     *     @retrun  arr_strategys[]数组  按顺序依次为： 最大日期，
     *                                                最小日期，
     *                                                最大收益率，
     *                                                按绘图格式组装好的单天或者单周收益率和时间，
     *                                                收益率均值，
     *                                                初始资金
     */

    public   String[] strategyDetailForDayPictrue(String strategyId,boolean isHistory,Date upTime){
        String[] arr_strategys = new String[5];
        if (strategyId==null){
            return arr_strategys;
        }
        String sname="--";//默认策略名
        float maxYield;
        String strategy = "";//组装好的数据放在这里
        String pictrue_sql_for_day = SqlLoader.getSqlById("pictrue_sql_for_day");
        //取当前策略数据库最大（最近）时间
        Date minDate = getMinAndMaxDate(isHistory,strategyId,upTime)._2;//最小时间
        Date maxDate = getMinAndMaxDate(isHistory,strategyId,upTime)._1;//最大时间

        //取最近三个月的收益率
        List<StrategyDailyYieldDto>  yield_list_for_day = qicDbUtil.queryBeanList(pictrue_sql_for_day, StrategyDailyYieldDto.class, strategyId, minDate, maxDate);
        Iterator<StrategyDailyYieldDto> it =  yield_list_for_day.iterator();
        while (it.hasNext()){
            StrategyDailyYieldDto sdy = it.next();
            sname =sdy.sname;
            String s = DrawPictrueUtil.combinationData(sdy);
            strategy +=(s+"\n");
        }
        int flag = strategy.lastIndexOf(",");//剔除字符串中最后一个“,”
        if(flag == -1){
            strategy = "{name:'"+sname+"', data:[[Date.UTC(2012,01,01),0.00]]}";
        }else{
            strategy = "{name:'"+sname+"', data:["+strategy.substring(0,flag)+"]}";//数据拼接完成
        }
        maxYield =  DrawPictrueUtil.getmaxModYield(strategyId, minDate, maxDate);//取当前策略绝对值最大的yield
        //取最大时间
        String str_maxDate = "";
        if(maxDate!=null){
            str_maxDate = DrawPictrueUtil.getFormatMaxorMinDate(maxDate);
        }
        //取最小时间
        String str_minDate ="";
        if(minDate!=null){
            str_minDate = DrawPictrueUtil.getFormatMaxorMinDate(minDate);
        }
        String str_maxYield = String.format("%.2f",maxYield*100);
        //（最大收益率/3）来表示图中收益率Y轴坐标间隔 保留小数点后两位 无论小数点后第三位是否大于5都入位
        String str_average_Yield = new BigDecimal((maxYield/3)*100).setScale(2, BigDecimal.ROUND_CEILING).toString();
        if(Logger.isDebugEnabled()){
            Logger.debug("-------------日线-------------------");
            Logger.debug("strategys============" + strategy);
            Logger.debug("maxYield============"+maxYield);
            Logger.debug("str_maxDate============"+str_maxDate);
            Logger.debug("str_minDate============"+str_minDate);
            Logger.debug("str_preYield==============="+str_average_Yield);
        }
        //最大日期，最小日期，最大收益率，按绘图格式组装好的单天或者单周收益率和时间(series)， 收益率均值，回测资金均值，初始资金
        arr_strategys = new String[]{str_maxDate,str_minDate,str_maxYield,strategy,str_average_Yield};
        return  arr_strategys;
    }


    /**
     *     单个策略 图形展示（周线）
     *     这个方法的主要作用是组装数据
     *     需要的数据：
     *     1.格式化近三个月的起始时间 maxDate minDate
     *     2.列出最近三个月收益率的最大值 最小值  取绝对值最大的数modYield
     *     3.把数据组装成[{name=“策略名1” date:[ Date.UTC(2010, 0, 1), 9.05],...},
     *     {name=“策略名2” date:[ Date.UTC(2010, 1, 1), 9.05],...}]类型
     *     @param  strategyId 策略ID
     *     @param  isHistory 是否是历史回测
     *     @param  upTime 上架时间
     *     @retrun  arr_strategys[]数组  按顺序依次为： 最大日期，
     *                                                最小日期，
     *                                                最大收益率，
     *                                                按绘图格式组装好的单天或者单周收益率和时间(series)，
     *                                                收益率均值，
     *                                                初始资金
     */

    public   String[] strategyDetailForWeekPictrue(String strategyId,boolean isHistory,Date upTime){
        String[] arr_strategys = new String[5] ;
        if (strategyId==null){
            return arr_strategys;
        }
        String sname="--"; //默认策略名
        float maxYield ;
        String strategy = "";//组装好的数据放在这里
        String pictrue_sql_for_week = SqlLoader.getSqlById("pictrue_sql_for_week");
         //取当前策略数据库最大（最近）时间
         //maxDate  =  StrategyContrastService.getlatelyDate(strategyId);
        Date minDate = getMinAndMaxDate(isHistory,strategyId,upTime)._2;//最小时间
        Date maxDate = getMinAndMaxDate(isHistory,strategyId,upTime)._1;//最大时间
        //取一段时间的收益率
        List<StrategyDailyYieldDto> yield_list_for_week = qicDbUtil.queryBeanList(pictrue_sql_for_week, StrategyDailyYieldDto.class, strategyId, minDate, maxDate);
        Iterator<StrategyDailyYieldDto> it =  yield_list_for_week.iterator();
        while (it.hasNext()){
            StrategyDailyYieldDto sdy = it.next();
            sname = sdy.sname;
            String s = DrawPictrueUtil.combinationData(sdy) ;
            strategy +=(s+"\n");
        }
        int flag = strategy.lastIndexOf(",");//剔除字符串中最后一个“,”
        if(flag == -1){//数据库没有数据 给一个初始化值 页面图形就能正确展示
            strategy = "{name:'"+sname+"', data:[[Date.UTC(2012,01,01),0.00]]}";
        }else{
            strategy = "{name:'"+sname+"', data:["+strategy.substring(0,flag)+"]}";//数据拼接完成
        }
        maxYield = DrawPictrueUtil.getmaxModYieldForWeek(strategyId, minDate, maxDate);//取当前策略绝对值最大的yield
        //取最大时间
        String str_maxDate = "";
        if(maxDate!=null){
            str_maxDate = DrawPictrueUtil.getFormatMaxorMinDate(maxDate);
        }
        //取最小时间
        String str_minDate ="";
        if(minDate!=null){
            str_minDate = DrawPictrueUtil.getFormatMaxorMinDate(minDate);
        }
        String str_maxYield = String.format("%.2f",maxYield*100);
        //（最大收益率/3）来表示图中Y轴坐标间隔 保留小数点后两位 无论小数点后第三位是否大于5都入位
        String str_average_Yield = new BigDecimal((maxYield/3)*100).setScale(2, BigDecimal.ROUND_CEILING).toString();
        if(Logger.isDebugEnabled()){
            Logger.debug("-------------周线-------------------");
            Logger.debug("minDate=========="+minDate);
            Logger.debug("strategys============" + strategy);
            Logger.debug("maxYield============"+maxYield);
            Logger.debug("str_maxDate============"+str_maxDate);
            Logger.debug("str_minDate============"+str_minDate);
            Logger.debug("str_preYield==============="+str_average_Yield);
        }
        arr_strategys = new String[]{str_maxDate,str_minDate,str_maxYield,strategy,str_average_Yield};
        return  arr_strategys;
    }

}
