package job;

import dbutils.MyDbUtil;
import play.jobs.Job;

import java.util.List;

/**
 * desc: 由于表关系改变——把产品下的交易账号搬迁到该产品最小的策略下
 * User: weiguili(li5220008@gmail.com)
 * Date: 13-8-30
 * Time: 下午3:41
 */
public class ProductMigration extends Job{
    protected static MyDbUtil qicDbUtil = new MyDbUtil();
    @Override
    public void doJob() throws Exception {
        List<Integer> productIds = qicDbUtil.queryBeanList("SELECT DISTINCT product_id FROM virtual_product_trade_account",Integer.class);
    }
}
