package job.zmqjob;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import org.zeromq.ZMQ;
import play.Logger;
import play.Play;
import play.classloading.ApplicationClasses;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.libs.F;
import util.CommonUtils;
import util.GsonUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * 启动zmq的job
 * 注意: 这里只用zmq的request-response模式的通信
 * User: wenzhihong
 * Date: 13-9-22
 * Time: 下午3:11
 */
@OnApplicationStart(async = true)
public class ZmqBootJob extends Job {
    public static org.apache.log4j.Logger log4j = org.apache.log4j.Logger.getLogger(ZmqBootJob.class);

    static boolean log4jDebugEnable = log4j.isDebugEnabled();

    static Thread zmqThread = null;

    //包含 zmqHandler 的 命令号到类的映射
    public static final Map<Integer, Class<AbstractZmqHandler>> zmqHandlerMap = Maps.newHashMap();

    public static String zmpCharset = "gbk";
    static {
        zmpCharset = Play.configuration.getProperty("zmp.str.chartset", "gbk");
    }

    @Override
    public void doJob() throws Exception {
        String bootZmq = Play.configuration.getProperty("boot.zmq", "false");
        boolean isBootZmq = false;
        try {
            isBootZmq =  Boolean.parseBoolean(bootZmq);
        } catch (Exception e) {
            Logger.warn("application.conf里的配制项 boot.zmq 的值, 只能是 true / false. 现在为 %s , 不能转为boolean变量, 设置为false", bootZmq);
        }

        String zmpPortStr = Play.configuration.getProperty("request.zmp.port", "55555");
        int zmpPort = 55555;

        try {
            zmpPort = Integer.parseInt(zmpPortStr);
        } catch (NumberFormatException e) {
            Logger.warn("application.conf里的配制项 request.zmp.port 的值, 只能是 数字. 现在为 %s , 不能转为 int 变量, 设置为 5555", zmpPortStr);
        }

        if (isBootZmq) {
            initHandler();
            Logger.info("启动zmq...");
            final ZMQ.Context context = ZMQ.context(1);
            final ZMQ.Socket socket = context.socket(ZMQ.REP);
            socket.bind ("tcp://*:" + zmpPort);

            //增加关闭应用时的回调
            zmqThread = Thread.currentThread();

            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    if (zmqThread != null) {
                        zmqThread.interrupt();
                        zmqThread = null;
                    }
                }
            });

            while (!zmqThread.isInterrupted()) {
                Logger.info("zmp已启动, 在端口%d上侦听", zmpPort);
                byte[] recv = socket.recv(0);
                String recvStr = new String(recv, zmpCharset);
                if (log4jDebugEnable) {
                    log4j.debug(String.format("接收到的参数:\n%s", recvStr));
                }
                String processResult = "";
                try {
                    ZmqProtocol.Request request = JSON.parseObject(recvStr, ZmqProtocol.Request.class);
                    Class<AbstractZmqHandler> handlerClass = zmqHandlerMap.get(request.getCmd());
                    if (handlerClass == null) {
                        log4j.warn(String.format("处理接收到的参数:\n%s\n找不到对应的命令处理器"));
                        failMsg("找不到对应的命令处理器,请检查命令号是否正确");
                    } else {
                        //接下来这里处理任务
                        F.Promise<Object> handlePromise = null;
                        AbstractZmqHandler handler = null;
                        try {
                            Constructor<AbstractZmqHandler> constructor = handlerClass.getConstructor(JSONObject.class);
                            handler = constructor.newInstance(request.getData());
                        } catch (Exception e) {
                            log4j.error("处理handler反射错误", e);
                            failMsg("系统错误");
                        }

                        if (handler != null) {
                            handlePromise = handler.now();
                            if (handlePromise != null) {
                                Object handlerResultData = handlePromise.get();
                                ZmqProtocol.Response response = new ZmqProtocol.Response();
                                response.sucess();
                                response.setData(handlerResultData);
                                processResult = GsonUtil.createGson().toJson(response);
                            }
                        }
                    }
                } catch (Exception e) {
                    log4j.error(String.format("处理接收到的参数:\n%s\n错误", recvStr), e);
                    processResult = failMsg("操作失败");
                }

                if (log4jDebugEnable) {
                    log4j.debug(String.format("处理器处理后的结果:\n%s", processResult));
                }
                socket.send(processResult.getBytes(zmpCharset), 0);
            }

            //Logger.info("关闭zmp...");
            //关闭zmq
            socket.close();
            context.term();
        }else {
            Logger.info("设置了zmq不启动...");
        }

    }

    private String failMsg(String msg) {
        String processResult;ZmqProtocol.Response response = new ZmqProtocol.Response();
        response.fail();
        response.setData(msg);
        processResult = GsonUtil.createGson().toJson(response);
        return processResult;
    }

    /**
     * 初始化处理器
     */
    private void initHandler() {
        //找到 ProcessTable 的注解, 加入到 tableHandlerMaps 里
        List<ApplicationClasses.ApplicationClass> handlerClassList = Play.classes.getAnnotatedClasses(ZmqHandler.class);
        for (ApplicationClasses.ApplicationClass handler : handlerClassList) {
            Class handlerJavaClass = handler.javaClass;
            if( handlerJavaClass != null && AbstractZmqHandler.class.isAssignableFrom(handlerJavaClass)) {
                ZmqHandler annotation = (ZmqHandler) handlerJavaClass.getAnnotation(ZmqHandler.class);
                int cmd = annotation.cmd();
                zmqHandlerMap.put(cmd, handlerJavaClass);
            }

        }
    }
}
