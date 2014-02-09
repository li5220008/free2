package job;

import ChartDirector.Chart;
import ChartDirector.LineLayer;
import ChartDirector.TTFText;
import ChartDirector.XYChart;
import bussiness.strategy.IDrawYieldChartService;
import bussiness.strategy.IStrategyService;
import dbutils.DBTemplateName;
import dbutils.MyDbUtil;
import dbutils.SqlLoader;
import models.iquantCommon.KeyValueDto;
import models.iquantCommon.StrategyBaseinfo;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.AbstractKeyedHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import play.Logger;
import play.exceptions.DatabaseException;
import play.jobs.Job;
import play.jobs.On;
import play.templates.JavaExtensions;
import util.CommonUtils;
import util.QicConfigProperties;

import javax.inject.Inject;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 每日收益率数据计算及策略收益率小图生生成
 * User: wenzhihong
 * Date: 12-11-7
 * Time: 下午5:29
 */
@On("0 20 0 * * ?")
public class StrategyYieldSmallPicJob extends Job {
    @Inject
    static IDrawYieldChartService drawYieldChartService;
    @Inject
    static IStrategyService strategyService;

    protected static MyDbUtil qicDbUtil = new MyDbUtil();
    protected static MyDbUtil qicoreDbUtil = new MyDbUtil(DBTemplateName.QICORE);

    @Override
    public void doJob() throws Exception {
        //calcYieldData();
        //calcYieldDataAllCategory();qia不用计算收益率
        generateYieldSmallPic();
    }

    //生成指定策略的数据跟画图
    public static void calcYieldDataAndDrawPic(List<String> drawStrategyList){
      //  calcYieldDataAllWithUUIDs(drawStrategyList);qia不用计算收益率
        generateStrategysYieldSmallPic(drawStrategyList);
    }

    /**
     *从资金变化表计算收益率 (所有的策略)
     */
    private static void calcYieldDataAllCategory(){
        //策略初始资金
        String initCapitalStrategySql = SqlLoader.getSqlById("hasInitCapitalStrategy");
        //策略id做为key值, 初始资金为value
        Map<String, Double> initCapitalMap = qicoreDbUtil.queryWithHandler(initCapitalStrategySql, new AbstractKeyedHandler<String, Double>() {
            @Override
            protected String createKey(ResultSet rs) throws SQLException {
                return rs.getString("sid");//UUID
            }

            @Override
            protected Double createRow(ResultSet rs) throws SQLException {
                return rs.getBigDecimal("initCapital").doubleValue();
            }
        });

        //得到每个策略的收益率最大日期
        String maxDateSql = SqlLoader.getSqlById("max_yield_date");
        Map<String, String> maxDateMap = qicDbUtil.queryWithHandler(maxDateSql, new AbstractKeyedHandler<String, String>() {
            @Override
            protected String createKey(ResultSet rs) throws SQLException {
                return rs.getString("strategyID").trim();//策略ID
            }

            @Override
            protected String createRow(ResultSet rs) throws SQLException {
                return rs.getString("maxDate");
            }
        });

        calcYieldDataFromCapitalChangeWithUUID(initCapitalMap, maxDateMap);
    }

    //根据指定的uuid进行计算
    private static void calcYieldDataAllWithUUIDs(List<String> uuidList){
        Map getStrategyId = toUuidGetStrategyId();

        StringBuilder ids = new StringBuilder();

        StringBuilder strid = new StringBuilder();
        for (String s : uuidList) {

            ids.append(',').append("'" + s + "'");

            //将UUID转换为strategyId;
            String strategyId = getStrategyId.get(s)==null?"0":getStrategyId.get(s).toString();
            strid.append(',').append("'"+strategyId+"'");

        }
        String idList = null;
        if(ids.length() > 0){
            idList = ids.substring(1);
        }else{
            idList = "'hoho-no-exist'"; //不存在啊, 所以设置一个这个的值. 哈哈
        }

        //StrategyID用,分开
        String strategyIdList = null;
        if(strid.length()>0){
            strategyIdList=strid.substring(1);
        }else{
            strategyIdList="'no-exit'";
        }


        //策略初始资金
        String initCapitalStrategySql = SqlLoader.getSqlById("hasInitCapitalStrategyWithUUIDs").replaceAll("#idList#", idList);
        //策略id做为key值, 初始资金为value
        Map<String, Double> initCapitalMap = qicoreDbUtil.queryWithHandler(initCapitalStrategySql, new AbstractKeyedHandler<String, Double>() {
            @Override
            protected String createKey(ResultSet rs) throws SQLException {
                return rs.getString("sid");//uuid
            }

            @Override
            protected Double createRow(ResultSet rs) throws SQLException {
                return rs.getBigDecimal("initCapital").doubleValue();
            }
        });


        //得到每个策略的收益率最大日期
        String maxDateSql = SqlLoader.getSqlById("max_yield_date_WithUUIDs").replaceAll("#idList#", strategyIdList);
        Map<String, String> maxDateMap = qicDbUtil.queryWithHandler(maxDateSql, new AbstractKeyedHandler<String, String>() {
            @Override
            protected String createKey(ResultSet rs) throws SQLException {
                return rs.getString("strategyID").trim();//策略ID
            }

            @Override
            protected String createRow(ResultSet rs) throws SQLException {
                return rs.getString("maxDate");
            }
        });

        calcYieldDataFromCapitalChangeWithUUID(initCapitalMap, maxDateMap);
    }


    private static void calcYieldDataFromCapitalChangeWithUUID(Map<String, Double> initCapitalMap, Map<String, String> maxDateMap) {
        final String before3MonthDate = DateFormatUtils.format(DateUtils.addMonths(new Date(), -3), "yyyy-MM-dd"); //三个月前
        String maxDateStr = before3MonthDate;

        //StrategyId  和 Uuid的对应关系,根据strategyId得到Uuid
        Map toUuidGetstrategyId = toUuidGetStrategyId();

        //插入数据的sql
        String insertSql = SqlLoader.getSqlById("insert_strategy_daily_yield");
        //计算收益率的sql模板
        String yieldCalcSqlTmp = SqlLoader.getSqlById("yieldCalcFromCapital");
        for (String sid : initCapitalMap.keySet()) {
            final String fsid = sid; //UUID
            double initCapital = initCapitalMap.get(sid); //初始资金
            String initCapitalStr = JavaExtensions.format(initCapital, "#.000000");
            //把UUID转化为StrategyId
            final int strategyId= toUuidGetstrategyId.get(sid)==null?0:Integer.valueOf(toUuidGetstrategyId.get(sid).toString());
            if(strategyId == 0){
                continue;
            }

            String strategyMaxDate = maxDateMap.get(String.valueOf(strategyId).trim());//根据strategyId得到每个策略的收益率最大日期

            if (strategyMaxDate == null) { //没有的话, 取三个月前的日期
                maxDateStr = before3MonthDate;
            }else{
                maxDateStr = DateFormatUtils.format(DateUtils.addDays(CommonUtils.parseDate(strategyMaxDate), 1), "yyyy-MM-dd"); //最大日期 + 1
            }
            String sql =  yieldCalcSqlTmp.replaceAll("#initCapital#", initCapitalStr);
            Object[][] dailyFieldRows = qicoreDbUtil.queryWithHandler(sql, new ResultSetHandler<Object[][]>() {

                @Override
                public Object[][] handle(ResultSet rs) throws SQLException {
                    List<Object[]> strategyDailyFiledList = new ArrayList<Object[]>(100);
                    while (rs.next()) {
                        Object[] item = new Object[5];
                        StrategyBaseinfo sds = strategyService.findStrategyById(strategyId);
                        if (sds == null) {
                            continue;
                        }
                        item[0] = strategyId;//策略ID
                        item[1] = rs.getFloat("yield");
                        item[2] = rs.getString("transactDate");
                        if (sds.upTime == null || sds.upTime.after(CommonUtils.parseDate(rs.getString("transactDate")))) {
                            item[3] = 1;//运行类型：1.策略回验期收益，2：策略运行期收益
                        } else {
                            item[3] = 2;//运行类型：1.策略回验期收益，2：策略运行期收益
                        }
                        item[4] = 1;//收益类型：1.多空收益（即总收益），2.多头收益，3.空头收益
                        strategyDailyFiledList.add(item);
                    }
                    return strategyDailyFiledList.toArray(new Object[strategyDailyFiledList.size()][]);
                }
            }, sid, maxDateStr);

            try {
                qicDbUtil.queryRunner.batch(qicDbUtil.getConnection(), insertSql, dailyFieldRows);
            } catch (SQLException e) {
                throw new DatabaseException(e.getMessage(), e);
            }
        }
    }



   /* private void calcYieldData() {
        String sql = SqlLoader.getSqlById("need_calc_yield_strategy_uuid");
        //得到所有要计算收益率的策略id
        List<String> strategyUUIDList = qicoreDbUtil.queryQicoreDbWithHandler(sql, new ColumnListHandler<String>());

        if(strategyUUIDList.size() > 0){
            //得到每个策略的收益率最大日期
            String maxDateSql = SqlLoader.getSqlById("max_yield_date");
            Map<String, String> maxDateMap = qicDbUtil.queryQicDbWithHandler(maxDateSql, new AbstractKeyedHandler<String, String>() {
                @Override
                protected String createKey(ResultSet rs) throws SQLException {
                    return rs.getString("strategyID");
                }

                @Override
                protected String createRow(ResultSet rs) throws SQLException {
                    return rs.getString("maxDate");
                }
            });

            final String before3MonthDate = DateFormatUtils.format(DateUtils.addMonths(new Date(), -3), "yyyy-MM-dd"); //三个月前
            String maxDateStr = before3MonthDate;
            String insertSql = SqlLoader.getSqlById("insert_strategy_daily_yield");
            for (String uuid : strategyUUIDList) {
                final String stUUID = uuid;
                String strategyMaxDate = maxDateMap.get(uuid);
                if (strategyMaxDate == null) { //没有的话, 取三个月前的日期
                    maxDateStr = before3MonthDate;
                }else{
                    maxDateStr = DateFormatUtils.format(DateUtils.addDays(CommonUtils.parseDate(strategyMaxDate), 1), "yyyy-MM-dd"); //最大日期 + 1
                }

                String hightYieldList = SqlLoader.getSqlById("strategy_yield_high_list");
                Object[][] dailyFieldRows = qicoreDbUtil.queryQicoreDbWithHandler(hightYieldList, new ResultSetHandler<Object[][]>() {
                    String date = "";
                    String oldDate = "";

                    @Override
                    public Object[][] handle(ResultSet rs) throws SQLException {
                        List<Object[]> strategyDailyFiledList = new ArrayList<Object[]>(100);
                        while (rs.next()) {
                            date = rs.getString("updateDate");
                            if (date.equals(oldDate) == false) { //不相等, 说明是新的
                                Object[] item = new Object[3];
                                item[0] = stUUID;
                                item[1] = rs.getFloat("yield");
                                item[2] = date;
                                strategyDailyFiledList.add(item);
                            }

                            oldDate = date;
                        }

                        return strategyDailyFiledList.toArray(new Object[strategyDailyFiledList.size()][]);
                    }
                }, uuid, maxDateStr);

                try {
                    qicDbUtil.queryRunner.batch(qicDbUtil.getQicDBConnection(), insertSql, dailyFieldRows);
                } catch (SQLException e) {
                    throw new DatabaseException(e.getMessage(), e);
                }
            }

        }
    }*/


    private static void generateYieldSmallPic() {
        //要画图的策略Id
        String needDrawStrategySql = SqlLoader.getSqlById("need_draw_pic_strategy");
        List<String> drawStrategyList = qicDbUtil.queryWithHandler(needDrawStrategySql, new ColumnListHandler<String>("strategyID"));
        generateStrategysYieldSmallPic(drawStrategyList);
    }

    //根据策略ID得到上架时间
    public static Map<String,Object> findupTimeById(String stUuid) {
        String sql = SqlLoader.getSqlById("findupTimeById");//查询当前策略的上架时间
        return qicDbUtil.querySingleMap(sql, stUuid);
    }

    /**
     * 画策略的绩效图
     * @param drawStrategyList  策略id List
     */
    private static void generateStrategysYieldSmallPic(List<String> drawStrategyList) {
       final String DIR = QicConfigProperties.getStrategySmallPictureDir();
        File file = new File(DIR);
        if(!file.exists()) {
            file.mkdirs();
        }
        String yieldDataSql = SqlLoader.getSqlById("strategy_daily_yied_3month");
        for (String sid : drawStrategyList) {
            final String stUUID = sid; //策略id
            Map getStrategyId = toUuidGetStrategyId();//通过UUID获得StrategyId
            final String strategyId = getStrategyId.get(sid)!=null?getStrategyId.get(sid).toString():"0";//如果没有找到对应的策略ID,默认给它一个0
            //取数据
            final List<Date> dateList = new ArrayList<Date>(100);
            final List<Double> yieldList = new ArrayList<Double>(100);
            final AtomicInteger firstIndex = new AtomicInteger(0);
            qicDbUtil.queryWithHandler(yieldDataSql, new ResultSetHandler<Object>() {
                @Override
                public Object handle(ResultSet rs) throws SQLException {
                    Date sdate = null;
                    int index = 0;
                    boolean isFind = false;
                    double preValue = 0;
                    Map<String, Object> upTimeMap = findupTimeById(stUUID);//根据策略ID得到上架时间
                    Date upTime = null;//策略对应的上架时间
                    if (upTimeMap != null) {
                        upTime = (Date) upTimeMap.get("upTime");
                    }
                    Date historyEndTime = drawYieldChartService.getMinAndMaxDate(true, strategyId, upTime)._1;//回测结束时间
                    Date realBeginTime = drawYieldChartService.getMinAndMaxDate(false, strategyId, upTime)._2;//实时模拟开始时间
                    while (rs.next()) {
                        Date ud = rs.getDate("updateDate");
                        double yieldV = rs.getDouble("yield");
                        if (index == 0) {
                            sdate = ud; //起始日期
                            dateList.add(ud);
                            if (ud.after(historyEndTime) && ud.before(realBeginTime)) {//对应时间不在回测时间区间或者实时模拟时间区间 用默认值代替收益率
                                yieldList.add(Chart.NoValue);
                            } else {
                                yieldList.add(yieldV);
                            }
                        } else {
                            int compareResult = DateUtils.truncatedCompareTo(sdate, ud, Calendar.DATE);
                            while (compareResult <= 0) {
                                if (DateUtils.toCalendar(sdate).get(Calendar.DAY_OF_MONTH) == 1 && (isFind == false)) { //第一个月的第一天
                                    firstIndex.set(index);
                                    isFind = true;
                                }
                                if (compareResult == 0) {
                                    dateList.add(ud);
                                    if (ud.after(historyEndTime) && ud.before(realBeginTime)) {//对应时间不在回测时间区间或者实时模拟时间区间 用默认值代替收益率
                                        yieldList.add(Chart.NoValue);
                                    } else {
                                        yieldList.add(yieldV); //同一天的话,把值加上
                                    }

                                } else {
                                    dateList.add(sdate);
                                    if (sdate.after(historyEndTime) && sdate.before(realBeginTime)) {//对应时间不在回测时间区间或者实时模拟时间区间 用默认值代替收益率
                                        yieldList.add(Chart.NoValue);
                                    } else {
                                        yieldList.add(yieldV);
                                    }
                                }
                                index++;
                                sdate = DateUtils.addDays(sdate, 1); //增加一天
                                compareResult = DateUtils.truncatedCompareTo(sdate, ud, Calendar.DATE);
                            }

                        }
                        preValue = yieldV; //前值
                    }
                    return null;
                }
            }, strategyId);//参数改为策略ID

            int picWith = 281; //图片宽度
            int picHeiht = 207; //图片高度
            int picGridColor = 0x9D9D9D; //风格线的颜色
            int picColor = 0xffffff; //整张图片的背景色
            int plotLeft = 45; //开始画图的左点位置
            int plotRight = 22; //图到右边的距离
            int plotTop = 10;  //开始画图的高的位置
            int plotBottom = 19; //图到下边的距离
            int plotHeight = picHeiht - plotTop - plotBottom; //画图的高度
            //int plotColor = 0xF9FBFC; //画图区域的背景色
            int plotColor = 0x000000; //画图区域的背景色
            int lineColor = 0x002a80c9; //曲线颜色
            int borderColor = Chart.Transparent; //边框颜色
            int gradientStart =  0x608ac6e9;//渐变色开始
            int gradientEnd =  0x60e3f3fa;//渐变色开始
            int yAxisHalfGap = 3; //y轴上一半几等分
            String yAxisFormat = "##0.00"; //y轴的格式化
            float fontSize = 7.5f;
            String fontName = "宋体"; //字体名字, 为空的话, 是Arial字体

            if (yieldList.size() == 0) { //没有数据,画空图
                //XYChart c = new XYChart(picWith, picHeiht, Chart.brushedMetalColor(picColor, 45));
                XYChart c = new XYChart(picWith, picHeiht);
                c.setBackground(c.linearGradientColor(0, 0, picWith, picHeiht, 0x323232, 0x1b1b1b, true));

                TTFText testTTF = c.getDrawArea().text3("0000", fontName, fontSize);
                plotLeft = testTTF.getWidth() + 5; //计算出最宽的
                int plotWith = picWith - plotLeft - plotRight; //画图的宽度
                plotTop = testTTF.getHeight();
                plotHeight = picHeiht - plotTop - plotBottom; //画图的高度
                //c.setPlotArea(plotLeft, plotTop, plotWith, plotHeight, plotColor, -1, -1, c.dashLineColor(picGridColor), c.dashLineColor(picGridColor));
                c.setPlotArea(plotLeft, plotTop, plotWith, plotHeight, plotColor, -1, -1, c.dashLineColor(picGridColor,Chart.DotLine), Chart.Transparent);
                String fileName = DIR + "/st_" + stUUID + ".png";
                c.makeChart(fileName);
                continue;
            }

            //画图
            //y轴
            //取出收益率的绝对值最大
            double[] yiedlArr = new double[yieldList.size()];
            double[] yieldAbsArr = new double[yieldList.size()];
            int count = 0;
            for (Double f : yieldList) {
                if(f==Chart.NoValue){
                    yiedlArr[count] = f;
                }else {
                    float fv = f != null ? f.floatValue() * 100 : 0;
                    //这里需要保留小数点后两位，否则绘图曲线误差太大
                    yiedlArr[count] = Double.parseDouble(String.format("%.3f",fv));
                    yieldAbsArr[count] = Math.abs(fv);
                }
                count++;
            }
            float yieldAbsMax = CommonUtils.minMax(yieldAbsArr)._2.floatValue();
            yieldAbsMax = yieldAbsMax * 1.1f;

            //格式化y轴值
            float avg = yieldAbsMax / yAxisHalfGap;
            double[] ylabelVal = new double[yAxisHalfGap * 2 + 1];
            //处理0以下的值
            for (int i = 0; i < yAxisHalfGap; i++) {
                ylabelVal[i] = -yieldAbsMax + i * avg;
            }
            ylabelVal[yAxisHalfGap] = 0;
            //处理0上的值
            for (int i = 0; i < yAxisHalfGap; i++) {
                ylabelVal[yAxisHalfGap + 1 + i] = yieldAbsMax - (yAxisHalfGap - 1 - i) * avg;
            }
            String[] ylabelStr = new String[ylabelVal.length];
            DecimalFormat df = new DecimalFormat(yAxisFormat);
            for (int i = 0; i < ylabelVal.length; i++) {
                ylabelStr[i] = df.format(ylabelVal[i]);
            }

            //以下for循环是为了修复在chartDirector中, 要有第一个 - (减号) 不显示的问题. 好像是被忽略了. 加多一个即可显示
            for (int i = 0; i < yAxisHalfGap; i++) {
                ylabelStr[i] = "-" + ylabelStr[i];
            }

            //XYChart c = new XYChart(picWith, picHeiht, Chart.brushedMetalColor(picColor, 1, 90), borderColor);
            XYChart c = new XYChart(picWith, picHeiht);
            c.setBackground(c.linearGradientColor(0, 0, picWith, picHeiht, 0x323232, 0x1b1b1b, true));

            TTFText testTTF = c.getDrawArea().text3(ylabelStr[0], fontName, fontSize);
            plotLeft = testTTF.getWidth() + 5; //计算出最宽的
            int plotWith = picWith - plotLeft - plotRight; //画图的宽度
            plotTop = testTTF.getHeight();
            plotHeight = picHeiht - plotTop - plotBottom; //画图的高度

            //c.setPlotArea(plotLeft, plotTop, plotWith, plotHeight, plotColor, -1, -1, c.dashLineColor(picGridColor), c.dashLineColor(picGridColor));
            c.setPlotArea(plotLeft, plotTop, plotWith, plotHeight, plotColor, -1, -1, c.dashLineColor(picGridColor,Chart.DotLine), Chart.Transparent);
            c.yAxis().setLabelStyle(fontName, fontSize).setFontColor(0xFFFFFF); //y轴使用7.5大小的Arial字体.
            c.yAxis().setLinearScale(-yieldAbsMax, yieldAbsMax, ylabelStr);

            //x轴
            /*
            String[] xdateArr = new String[dateList.size()];
            for (int i = 0; i < dateList.size(); i++) {
                xdateArr[i] = DateFormatUtils.format(dateList.get(i), "yyyy-MM");
            }
            c.xAxis().setLabels(xdateArr);
            c.xAxis().setLabelStep(31, 0, firstIndex.get());
            */

            //另外一种算法
            Set<String> xdateSet = new LinkedHashSet<String>();
            for (Date d : dateList) {
                xdateSet.add(DateFormatUtils.format(d, "yyyy-MM"));
            }
            String[] xdateArr = xdateSet.toArray(new String[xdateSet.size()]);
            c.xAxis().setLinearScale(0, yieldAbsArr.length - 1, xdateArr); //设置x轴

            c.xAxis().setLabelStyle(fontName, fontSize).setFontColor(0xFFFFFF);

            //c.xAxis().setLabelStyle
            //c.xAxis().setMargin(8, 8);
            LineLayer lineLayer = c.addLineLayer(yiedlArr, lineColor);
            lineLayer.setLineWidth(2);
           // lineLayer.setGapColor(c.dashLineColor(0x00ff00)); //无值的颜色.这里用平滑过渡色. 也就是跟主颜色一样.

            //这个是线图加上区间颜色的
            /*
            SplineLayer splineLayer = c.addSplineLayer(yiedlArr, lineColor);
            splineLayer.setGapColor(Chart.SameAsMainColor);
            splineLayer.setLineWidth(2);
            Mark mark = c.yAxis().addMark(ylabelVal[0], -1, "");
            mark.setLineWidth(0);
            mark.setMarkColor(Chart.Transparent, Chart.Transparent);
            c.addInterLineLayer(splineLayer.getLine(), mark.getLine(), c.linearGradientColor(0, plotTop, 0, picHeiht - plotBottom, gradientStart, gradientEnd), Chart.Transparent);
            */


            //以下是面积图的画法
            /*
            AreaLayer areaLayer = c.addAreaLayer(yiedlArr, c.linearGradientColor(0, plotTop, 0, picHeiht - plotBottom, gradientStart, gradientEnd));
            areaLayer.setBorderColor(lineColor);
            areaLayer.setGapColor(Chart.SameAsMainColor);
            areaLayer.setBaseLine(ylabelVal[0]);
            */

            String fileName = DIR + "/st_" + stUUID + ".png";
            c.makeChart(fileName);
        }
    }


    /**
     *
     * @return Strategy_baseInfo 中 Strategy_id 和 id 对应的关系;
     * 通过UUID 得到 StrategyId 的值;
     */
    private static Map<String,String> toUuidGetStrategyId(){
        String sql = SqlLoader.getSqlById("getStrategyIDAndUuid");
        Map map = new HashMap();
        List<KeyValueDto> kv = qicDbUtil.queryBeanList(sql, KeyValueDto.class);
        for(int i=0;i<kv.size();i++){
            KeyValueDto keyValue = kv.get(i);
            map.put(keyValue.uuid, keyValue.strategyId);
        }
        return map;
    }

    /**
     *
     * @return Strategy_baseInfo 中 Strategy_id 和 id 对应的关系;
     * 通过UUID 得到 StrategyId 的值;
     */
    private static Map<String,String> toStrategyIdGetUuid(){
        String sql = SqlLoader.getSqlById("getStrategyIDAndUuid");
        Map map = new HashMap();
        List<KeyValueDto> kv = qicDbUtil.queryBeanList(sql, KeyValueDto.class);
        for(int i=0;i<kv.size();i++){
            KeyValueDto keyValue = kv.get(i);
            map.put(keyValue.strategyId,keyValue.uuid);
        }
        return map;
    }

}
