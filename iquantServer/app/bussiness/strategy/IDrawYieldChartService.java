package bussiness.strategy;

import play.libs.F;

import java.util.Date;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-7-4
 * Time: 上午10:19
 * 功能描述:
 */
public interface IDrawYieldChartService {



    /**
     *
     * @param isHistory 是否为历史回测
     * @param strategyId 策略ID
     * @param upTime 上架时间
     * @return 策略在历史回测/实时模拟区间的时间 ：_1 最大时间；_2 最小时间
     */
    public  F.T2<Date,Date> getMinAndMaxDate(boolean isHistory,String strategyId,Date upTime);

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

    public   String[] strategyDetailForDayPictrue(String strategyId,boolean isHistory,Date upTime);


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

    public   String[] strategyDetailForWeekPictrue(String strategyId,boolean isHistory,Date upTime);

}
