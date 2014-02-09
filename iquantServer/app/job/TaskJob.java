package job;

import dbutils.MyDbUtil;
import dbutils.SqlLoader;
import job.task.TaskRunner;
import models.iquantCommon.SchedulingTask;
import play.jobs.Job;
import play.jobs.On;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;


/**
 * User: 刘志，刘泓江
 * Date: 12-12-14
 * Time: 下午2:09
 */
@On("0 0 0 * * ?")
public class TaskJob extends Job {
    @Named("qic")
    @Inject
    protected static MyDbUtil qicDbUtil;
    @Override
    public void doJob() throws Exception {
        List<SchedulingTask> list = getCanExecuteSchedulingTaskList();
        for(SchedulingTask item : list) {
            executeSchedulingTask(item);
        }
    }

    private List<SchedulingTask> getCanExecuteSchedulingTaskList() {
        String schedulingTaskSql = SqlLoader.getSqlById("getScdulingTaskInfo");
        return qicDbUtil.queryBeanList(schedulingTaskSql, SchedulingTask.class);
    }

    private void executeSchedulingTask(SchedulingTask task) {
        String changeTaskStatusSql = SqlLoader.getSqlById("changeTaskStatus");
        if(task == null)
            return;
        String safeModuleName = "job.task." + task.module.replace(".", "_");
        TaskRunner taskRunner;
        try{
            taskRunner = (TaskRunner)Class.forName(safeModuleName).newInstance();
        } catch (Exception e) {
            task.status = 4;
            // 异常 将调度任务状态改为执行失败
            qicDbUtil.update(changeTaskStatusSql, task.status, task.id);
            return;
        }
        try{
            taskRunner.processParameter(task.parameter);
            taskRunner.execute();
            //将调度任务状态改为已执行
            task.status = 2;
            qicDbUtil.update(changeTaskStatusSql, task.status, task.id);
        } catch (Exception e) {
            task.status = 4;
            // 异常 将调度任务状态改为执行失败
            qicDbUtil.update(changeTaskStatusSql, task.status, task.id);
            return;
        }
    }
}
