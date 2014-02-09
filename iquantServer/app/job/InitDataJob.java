package job;

import dbutils.SqlLoader;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import utils.QicConfigProperties;

/**
 * 应用程序启动时做数据初使化
 * User: wenzhihong
 * Date: 12-11-7
 * Time: 下午5:06
 * To change this template use File | Settings | File Templates.
 */
@OnApplicationStart
public class InitDataJob extends Job {
    @Override
    public void doJob() throws Exception {
        SqlLoader.init();
        // load system config =====add by liujl
        QicConfigProperties.getInstance().load();
    }
}
