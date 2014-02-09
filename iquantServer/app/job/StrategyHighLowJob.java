package job;

import bussiness.strategy.IStrategyService;
import dbutils.DBTemplateName;
import dbutils.MyDbUtil;
import dbutils.SqlLoader;
import models.iquantCommon.KeyValueDto;
import models.iquantCommon.StrategyBaseinfo;
import models.iquantCommon.StrategyDto;
import play.jobs.Job;
import play.jobs.On;

import javax.inject.Inject;
import java.util.*;

/**
 * 绩效扩展数据初始化
 * User: liangbing
 * Date: 12-12-20
 * Time: 上午10:27
 */
//@On("0 10 0 * * ?")
public class StrategyHighLowJob extends Job {
    @Inject
    static IStrategyService strategyService;
    protected static MyDbUtil qicDbUtil = new MyDbUtil();
    protected static MyDbUtil qicoreDbUtil = new MyDbUtil(DBTemplateName.QICORE);

    @Override
    public void doJob() {
        initData();
    }

    private void initData() {
        System.out.println("--------StrategyHighLowJob 定时任务执行 开始--------"+ new Date());
        int type = 1;//要删除数据的状态 1:历史数据,2:实时模拟
//      String delSql = SqlLoader.getSqlById("delHighLowSql");
        String sql = SqlLoader.getSqlById("strategyHighLowSql");
        List<StrategyDto> sdlist =selectHighLow();
        //根据Uuid得到StrategyId
        Map map = toUuidGetStrategyId();
        if(sdlist!=null && sdlist.size()>0){
            Object[][] params = new Object[sdlist.size()][38];
            for(int row = 0;row<sdlist.size();row++){
                StrategyDto strategyDto = sdlist.get(row);
                StrategyBaseinfo sds = strategyService.findStrategyById(Long.parseLong(map.get(strategyDto.strategyId).toString()));
                    params[row][0] =map.get(strategyDto.strategyId);
                    params[row][1] = strategyDto.retainedProfits;
                    params[row][2] = strategyDto.yield;
                    params[row][3] = strategyDto.yieldOfMonth;
                    params[row][4] = strategyDto.yieldOfYear;
                    params[row][5] =strategyDto.yieldOfMonthStandardDeviation;
                    params[row][6] =strategyDto.overallProfitability;
                    params[row][7] = strategyDto.overallDeficit;
                    params[row][8] =strategyDto.tradeCount;
                    params[row][9] = strategyDto.profitCount;
                    params[row][10] = strategyDto. deficitCount;
                    params[row][11] = strategyDto.maxSingleProfit;
                    params[row][12] = strategyDto.maxSingleDeficit;
                    params[row][13] =strategyDto.tradeDays;
                    params[row][14] = strategyDto.maxShortPositionTime;
                    params[row][15] = strategyDto.longPositionTradeCount;
                    params[row][16] =strategyDto.shortPositionTradeCount;
                    params[row][17] = strategyDto.positionCloseCount;
                    params[row][18] =strategyDto.maxSequentialProfitCount;
                    params[row][19] =strategyDto. maxSequentialDeficitCount;
                    params[row][20] =strategyDto.profitRatio;
                    params[row][21] =strategyDto.canhsiedRatio;
                    params[row][22] = strategyDto.maxSingleProfitRatio;
                    params[row][23] = strategyDto.maxSingleDeficitRatio;
                    params[row][24] = strategyDto.maxSequentialDeficitCapital;
                    params[row][25] =  strategyDto.sumOfCommission;
                    params[row][26] = strategyDto.grossProfit;
                    params[row][27] = strategyDto.profitLossRatio;
                    params[row][28] =strategyDto.avgProfitOfMonth;
                    params[row][29] = strategyDto. sharpeIndex;
                    params[row][30] =strategyDto. floatingProfitAndLoss;
                    params[row][31] = strategyDto.movingCost;
                    if(sds.upTime==null||sds.upTime.after(strategyDto.updateTime)){
                        params[row][32] = 1;//历史数据
                    }else{
                        params[row][32] = 2;//实时模拟
                        type =2;
                    }
                    params[row][33] = 0;
                    params[row][34] = 0;
                    params[row][35] = 0;
                    params[row][36] = 0;
                    params[row][37] = strategyDto.updateTime;
            }

            //删除里面的数据 按条件批量删除数据
            del(sdlist,type);
            //插入最新的数据
            qicDbUtil.batchQicDB(sql, params);

        }
        System.out.println("--------StrategyHighLowJob 定时任务执行 结束--------"+ new Date());
    }






      /**
     * 删除已经更新的数据,保存没有更新的数据
     * @param sdlist
     */
    private  void del(List<StrategyDto> sdlist,int type){

        //高低频表里面的UUID转为StrategyID
        List<StrategyDto> strategyDtos = new ArrayList<StrategyDto>();
        Map map = toUuidGetStrategyId();
        for(int i =0;i<sdlist.size();i++){
            StrategyDto sd = sdlist.get(i);
            sd.strategyId = map.get(sd.strategyId)==null?"no-no-strategyId":map.get(sd.strategyId).toString();
            strategyDtos.add(sd);
        }

        if(strategyDtos!=null&&strategyDtos.size()>0){
        int pageNo = 1;
        int index = 0;
        if(strategyDtos.size()<30){
            doDel(strategyDtos.subList(0, strategyDtos.size()),type);
        }else{
            for (int i = 0; i <=strategyDtos.size(); ) {
                doDel(strategyDtos.subList(i, pageNo * 30),type);
                index = pageNo * 30;
                pageNo++;
                i += 30;
                if (strategyDtos.size() - index < 30) {
                    doDel(strategyDtos.subList(i, strategyDtos.size()),type);
                    break;
                }
            }
            }
        }

    }
    private static void doDel(List<StrategyDto> sublist,int type){

        Object[][] params = new Object[sublist.size()][2];
        for(int i = 0;i<sublist.size();i++){
            StrategyDto dto  = sublist.get(i);
            params[i][0] =dto.strategyId;
            params[i][1] =type;
        }
        String delSql = SqlLoader.getSqlById("delHighLowSql");
        qicDbUtil.batchQicDB(delSql, params);
    }

    /**
     * 查询高低频表数据 合并 都存在的时候才更新数据
     * @return
     */
    public List<StrategyDto> selectHighLow(){
        String highSql = SqlLoader.getSqlById("selecthighSql");
        String lowSql = SqlLoader.getSqlById("selectlowSql");
        List<StrategyDto> highlowlist =new ArrayList<StrategyDto>();
        List<StrategyDto> highlist = qicoreDbUtil.queryBeanList(highSql, StrategyDto.class);
        List<StrategyDto> lowlist = qicoreDbUtil.queryBeanList(lowSql, StrategyDto.class);
        Map<String,StrategyDto> lowMap = covertListToMap(lowlist);
        Map<String,StrategyDto> highMap = covertListToMap(highlist);
        Map map = toUuidGetStrategyId();
        for(String key :lowMap.keySet()){

            StrategyDto dto = highMap.get(key);
            if(dto != null){
                StrategyDto low = lowMap.get(key);
                dto.yieldOfMonthStandardDeviation=low.yieldOfMonthStandardDeviation;
                dto.tradeDays=low.tradeDays;
                dto.maxShortPositionTime=low.maxShortPositionTime;
                dto.sharpeIndex=low.sharpeIndex;
                dto.movingCost=low.movingCost;
                if(map.get(key)!=null){
                    highlowlist.add(dto);
                }
            }
        }

        return highlowlist;
    }



  /*  *//**
     * 查询高低频表数据 合并
     * @return
     *//*
    public List<StrategyDto> selectHighLow(){
        String highSql = SqlLoader.getSqlById("selecthighSql");
        String lowSql = SqlLoader.getSqlById("selectlowSql");
        List<StrategyDto> highlist = qicoreDbUtil.queryQicoreDBBeanList(highSql,StrategyDto.class);
        List<StrategyDto> lowlist = qicoreDbUtil.queryQicoreDBBeanList(lowSql,StrategyDto.class);
        Map<String,StrategyDto> lowMap = covertListToMap(lowlist);
        for(int i=0;i<highlist.size();i++){
            StrategyDto highDto = highlist.get(i);
            StrategyDto lowDto =lowMap.get(highDto.strategyId);
            if(lowDto != null){
                highDto.yieldOfMonthStandardDeviation=lowDto.yieldOfMonthStandardDeviation;
                highDto.tradeDays=lowDto.tradeDays;
                highDto.maxShortPositionTime=lowDto.maxShortPositionTime;
                highDto.sharpeIndex=lowDto.sharpeIndex;
                highDto.movingCost=lowDto.movingCost;
                lowMap.remove(highDto.strategyId);
            }
        }
        for(StrategyDto dto :lowMap.values()){
            highlist.add(dto);
        }
        return highlist;
    }*/


    private Map<String,StrategyDto> covertListToMap(List<StrategyDto> lowlist){
        Map<String,StrategyDto> map = new HashMap<String,StrategyDto>();
        for(StrategyDto dto : lowlist){
            map.put(dto.strategyId,dto);
        }
        return  map;
    }

    /**
     *
     * @return Strategy_baseInfo 中 Strategy_id 和 id 对应的关系;
     * 通过UUID 得到 StrategyId 的值;
     */
    private Map<String,String> toUuidGetStrategyId(){
        String sql = SqlLoader.getSqlById("getStrategyIDAndUuid");
        Map map = new HashMap();
        List<KeyValueDto> kv = qicDbUtil.queryBeanList(sql, KeyValueDto.class);
        for(int i=0;i<kv.size();i++){
            KeyValueDto keyValue = kv.get(i);
            map.put(keyValue.uuid, keyValue.strategyId);
        }
        return map;
    }


}