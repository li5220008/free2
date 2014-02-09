package job.common;

import play.Play;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.test.Fixtures;

/**
 * 初使化数据库
 * User: wenzhihong
 * Date: 12-11-8
 * Time: 下午3:58
 */
@OnApplicationStart
public class InitDatabaseJob extends Job {
    @Override
    public void doJob() throws Exception {
       /*if (Play.runningInTestMode()) { //在测试模式下, 则会把数据给删除掉重建
           // 传入 null 为删除默认数据库的表, 用的是 TRUNCATE 语句. 这样的话, 自增长又回到了从1开始
           Fixtures.deleteDatabase(null);
            Fixtures.loadModels("initData.yml");
        }*/
    }
}
