package job.task;

/**
 * User: 刘志
 * Date: 12-12-14
 * Time: 下午2:03
 */
public interface TaskRunner {
    void processParameter(String parameterString) throws Exception;

    void execute() throws Exception;
}
