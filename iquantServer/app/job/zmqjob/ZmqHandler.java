package job.zmqjob;

import java.lang.annotation.ElementType;

/**
 * User: wenzhihong
 * Date: 13-9-22
 * Time: 下午4:23
 */
@java.lang.annotation.Target({ElementType.TYPE})
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface ZmqHandler {
    public int cmd();
}
