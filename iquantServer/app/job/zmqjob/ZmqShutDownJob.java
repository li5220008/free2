package job.zmqjob;

import play.jobs.Job;
import play.jobs.OnApplicationStop;

/**
 * User: wenzhihong
 * Date: 13-9-24
 * Time: 上午11:13
 */
@OnApplicationStop
public class ZmqShutDownJob extends Job{
    @Override
    public void doJob() throws Exception {
        if (ZmqBootJob.zmqThread != null) {
            ZmqBootJob.zmqThread.interrupt();
            ZmqBootJob.zmqThread = null;
        }
    }
}
