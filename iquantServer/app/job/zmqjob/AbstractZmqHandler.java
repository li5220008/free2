package job.zmqjob;

import com.alibaba.fastjson.JSONObject;
import play.jobs.Job;

/**
 * User: wenzhihong
 * Date: 13-9-22
 * Time: 下午4:08
 */
public abstract class AbstractZmqHandler extends Job<Object> {

    //上传命令的数据
    protected JSONObject inputData;

    public AbstractZmqHandler(JSONObject inputData) {
        this.inputData = inputData;
    }

    @Override
    public Object doJobWithResult() throws Exception {
        return process();
    }

    //处理过程
    protected abstract Object process();
}
