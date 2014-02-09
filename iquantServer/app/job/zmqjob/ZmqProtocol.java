package job.zmqjob;

import com.alibaba.fastjson.JSONObject;

/**
 * User: wenzhihong
 * Date: 13-9-22
 * Time: 下午4:15
 */
public class ZmqProtocol {
    //请求
    public static class Request {
        private int cmd;
        private JSONObject data;

        public int getCmd() {
            return cmd;
        }

        public void setCmd(int cmd) {
            this.cmd = cmd;
        }

        public JSONObject getData() {
            return data;
        }

        public void setData(JSONObject data) {
            this.data = data;
        }
    }

    //响应
    public static class Response{
        private int sucess; //1成功, 0失败
        private Object data;

        public void sucess(){
            this.sucess = 1;
        }

        public void fail(){
            this.sucess = 0;
        }

        public int getSucess() {
            return sucess;
        }

        public void setSucess(int sucess) {
            this.sucess = sucess;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }
    }

    /**
     * 命令名称约定
     */
    public static class Cmd {
        public static final int FetchAllInUsedTradeAccount = 1;
        public static final int FetchProductStrategyBindInfo = 2;
        public static final int UpdateTradeAccountInitCapital = 3;

    }
}
