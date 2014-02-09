package utils;

import dbutils.MyDbUtil;
import dbutils.SqlLoader;
import models.iquantCommon.StrategyDailyYieldDto;
import play.modules.guice.InjectSupport;

import javax.inject.Inject;
import javax.inject.Named;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

/**
 * User: liuhongjiang
 * Date: 13-4-27
 * Time: 上午10:05
 */
@InjectSupport
public class DrawPictrueUtil {
    @Named("qicore")
    @Inject
    protected static MyDbUtil qicoreDbUtil;
    @Named("qic")
    @Inject
    protected static MyDbUtil qicDbUtil;
    //拿到初始资金
    public static BigDecimal getInitialCapital(String strategyId){
        String get_initial_capital_sql = SqlLoader.getSqlById("get_initial_capital");
        Map<String, Object> initialCapitalMap = qicoreDbUtil.querySingleMap(get_initial_capital_sql, strategyId);
        if(initialCapitalMap==null){
            return null;
        }
        return  (BigDecimal)initialCapitalMap.get("inicapital");

    }

    /*
    *java中对日期的加减操作
    *gc.add(1,-1)表示年份减一.
    *gc.add(2,-1)表示月份减一.
    *gc.add(3.-1)表示周减一.
    *gc.add(5,-1)表示天减一.
    *GregorianCalendar类的add(int field,int amount)方法表示年月日加减.
    *field参数表示年,月.日等.
    *amount参数表示要加减的数量.
    *
    */
    public static Date getTime(Date date ,int d ,int t){
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        GregorianCalendar gc =new GregorianCalendar();
        gc.setTime(date);
        gc.add(d,t);
        gc.set(gc.get(Calendar.YEAR),gc.get(Calendar.MONTH),gc.get(Calendar.DATE));
        return  gc.getTime();
        //return  sdf.format(gc.getTime());

    }

    /**
     * 根据指定格式获取时间 (用户格式化 绘图数据)
     * @param     date 给定时间
     * @return     指定格式的时间
     */
    public static  String[] getFormatDate(Date date){
        String[] formatDate;
        if(date==null){
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String year =  sdf.format(date);
        // Logger.debug("year=========="+year);
        SimpleDateFormat sdf1 = new SimpleDateFormat("MM");
        String month =sdf1.format(date);
        int m=0;
        try {
            m = Integer.parseInt(month);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        //在highcharts中 0表示1月  需要把实际月份-1 才能在绘图工具中正确显示
        m = m-1;
        //Logger.debug("M=========="+m);
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd");
        String day =sdf2.format(date);
        formatDate=new String[]{year,m+"",day};
        return   formatDate;
    }

    //格式化时间
    public static  String getFormatMaxorMinDate(Date maxDate){
        String[] formatDate = getFormatDate(maxDate)  ;
        if(formatDate.length!=3){
            return null;
        }
        String year =   formatDate[0];
        String month =  formatDate[1];
        String day =   formatDate[2];
        String s = "Date.UTC("+year+","+month+","+day+")";
        return s;
    }

    //取该策略最近三个月中绝对值最大的收益率
    public static float getmaxModYield(String strategyId, Date minDate){
        Float minYield = 0F;
        Float maxYield = 0F;
        String max_and_min_yield_sql = SqlLoader.getSqlById("max_and_min_yield_sql");
        Map<String, Object> max_and_min_yield_map = qicDbUtil.querySingleMap(max_and_min_yield_sql, strategyId, minDate);
        if(max_and_min_yield_map!=null&&max_and_min_yield_map.size()!=0){
            minYield = max_and_min_yield_map.get("minYield") == null ? 0F : (Float.parseFloat(max_and_min_yield_map.get("minYield").toString()));
            maxYield = max_and_min_yield_map.get("maxYield") == null ? 0F : (Float.parseFloat(max_and_min_yield_map.get("maxYield").toString()));
        }
        return Math.abs(maxYield)>Math.abs(minYield) ? Math.abs(maxYield):Math.abs(minYield);
    }

    //取该策略minDate-maxDate中绝对值最大的收益率(日线)
    public static float getmaxModYield(String strategyId, Date minDate ,Date maxDate){
        Float minYield = 0F;
        Float maxYield = 0F;
        String max_and_min_yield_sql = SqlLoader.getSqlById("max_and_min_yield_interval_sql");
        Map<String, Object> max_and_min_yield_map = qicDbUtil.querySingleMap(max_and_min_yield_sql, strategyId,minDate,maxDate);
        if(max_and_min_yield_map!=null&&max_and_min_yield_map.size()!=0){
            minYield = max_and_min_yield_map.get("minYield") == null ? 0F : (Float.parseFloat(max_and_min_yield_map.get("minYield").toString()));
            maxYield = max_and_min_yield_map.get("maxYield") == null ? 0F : (Float.parseFloat(max_and_min_yield_map.get("maxYield").toString()));        }
        return Math.abs(maxYield)>Math.abs(minYield) ? Math.abs(maxYield):Math.abs(minYield);
    }

    //取该策略minDate-maxDate中绝对值最大的收益率（周线）
    public static float getmaxModYieldForWeek(String strategyId, Date minDate ,Date maxDate){
        Float minYield = 0F;
        Float maxYield = 0F;
        String max_and_min_yield_sql = SqlLoader.getSqlById("queryMaxAndMinYieldForWeek");
        Map<String, Object> max_and_min_yield_map = qicDbUtil.querySingleMap(max_and_min_yield_sql, strategyId,minDate,maxDate);
        if(max_and_min_yield_map!=null&&max_and_min_yield_map.size()!=0){
            minYield = max_and_min_yield_map.get("minYield") == null ? 0F : (Float.parseFloat(max_and_min_yield_map.get("minYield").toString()));
            maxYield = max_and_min_yield_map.get("maxYield") == null ? 0F : (Float.parseFloat(max_and_min_yield_map.get("maxYield").toString()));        }
        return Math.abs(maxYield)>Math.abs(minYield) ? Math.abs(maxYield):Math.abs(minYield);
    }

    //组合数据
    public static  String combinationData(StrategyDailyYieldDto sdy){
        //得到已经格式化的时间数组
        if(sdy==null||sdy.updateDate==null){
            return "";
        }
        String[] formatDate = getFormatDate(sdy.updateDate);
        if(formatDate==null||formatDate.length!=3){
            return null;
        }
        String year =   formatDate[0];
        String month =  formatDate[1];
        String day =   formatDate[2];
        //拼接，sdy.yield（收益率）*100 取小数点后两位四舍五入
        String s = "[Date.UTC("+year+","+month+","+day+"),"+ String.format("%.2f",sdy.yield*100)+"],";
        return s;
    }


    //取当前策略数据库最大（最近）时间
    public static  Date getlatelyDate(String strategyId){
        String minDate=null;
        String get_maxDate_sql = SqlLoader.getSqlById("max_updatedate_sql");
        Map<String, Object> max_date_map = qicDbUtil.querySingleMap(get_maxDate_sql, strategyId);
        Date maxDate =  (Date)max_date_map.get("maxDate");
        //  Logger.debug("maxDate=========="+maxDate);

        return maxDate;
    }
}
