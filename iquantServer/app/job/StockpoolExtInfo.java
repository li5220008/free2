package job;

import dbutils.MyDbUtil;
import dbutils.SqlLoader;
import models.iquantCommon.StockpoolDto;
import play.jobs.Job;
import play.jobs.On;

import java.util.List;

/**
 * 股票池拓展信息表数据定时查询..
 * User: liangbing
 * Date: 12-12-19
 * Time: 上午10:28
 */
@On("0 20 0 * * ?")
public class StockpoolExtInfo extends Job {
    protected static MyDbUtil qicDbUtil = new MyDbUtil();
    @Override
    public void doJob() {
        initData();
    }

    private void initData() {
        String delSql = SqlLoader.getSqlById("stockpooldeleteSql");
        String sql = SqlLoader.getSqlById("stockpoolExtSql");
        String sql1 = SqlLoader.getSqlById("selectStockpoolSql");

        List<StockpoolDto> sdList = qicDbUtil.queryBeanList(sql1, StockpoolDto.class);
        if (sdList != null && sdList.size() > 0) {
            Object[][] params = new Object[sdList.size()][6];
            for (int row = 0; row < sdList.size(); row++) {
                //stockPoolCode,source,annualizedYield,yearJensenRatio,updateDate,orgId
                StockpoolDto stockpoolDto = sdList.get(row);
                params[row][0] = stockpoolDto.stockPoolCode;
                params[row][1] = stockpoolDto.source;
                params[row][2] = stockpoolDto.annualizedYield;
                params[row][3] = stockpoolDto.yearJensenRatio;
                params[row][4] = stockpoolDto.updateDate;
                params[row][5] = stockpoolDto.orgId;
            }
            //删除里面的数据
            qicDbUtil.execute(delSql);
            //插入最新的数据
            qicDbUtil.batch(sql, params);
        }

    }
}
