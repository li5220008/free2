package job.task;

import bussiness.strategy.IStrategyService;
import bussiness.strategy.IUserStrategyManageService;
import com.google.gson.Gson;
import dbutils.MyDbUtil;
import dbutils.SqlLoader;
import models.iquantCommon.StrategyBaseinfo;
import models.iquantCommon.StrategyDownTaskContextDto;
import models.iquantCommon.StrategyDto;
import models.iquantCommon.UserStrategyOrderDto;
import play.modules.guice.InjectSupport;
import util.MessageBuilder;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

/**
 * User: 刘志，刘泓江
 * Date: 12-12-14
 * Time: 下午2:23
 */
@InjectSupport
public class StrategyDownTaskRunner implements TaskRunner {

    private StrategyDownTaskContextDto taskContext;
    @Inject
    static IStrategyService strategyService;
    @Inject
    static IUserStrategyManageService userStrategyManageService;
    protected static MyDbUtil qicDbUtil = new MyDbUtil();

    public void  processParameter(String parameterString) throws Exception{
        Gson gson = new Gson();
        taskContext = gson.fromJson(parameterString, StrategyDownTaskContextDto.class);
    }

    public void execute() throws Exception{
        final Long SPACING_INTERVAL =7*24*60*60*1000L;//间隔时间
        Date currentDate = new Date();
        String getOrderedUserSql = SqlLoader.getSqlById("getOrderedUser");
        String sendUserMessageSql = SqlLoader.getSqlById("sendUserMassage");
        String getStrategyDownTimeSql = SqlLoader.getSqlById("getStrategyDownTime");
                if(taskContext == null)
                    throw new Exception("task context is null.");
                if(taskContext.strategyIdArray == null || taskContext.strategyIdArray.length <= 0)
                    throw new Exception("no strategy id selected.");
                for(String sid : taskContext.strategyIdArray) {
                    StrategyDto strategyDto = qicDbUtil.querySingleBean(getStrategyDownTimeSql, StrategyDto.class, sid);
                    Date downTime = strategyDto.downTime;
                    /*Calendar calendar =Calendar.getInstance();
                     calendar.setTime(downTime);
                    calendar.get(Calendar.YEAR);
                    System.out.println("old year"+downTime.getYear());
                    System.out.println("new year"+ calendar.get(Calendar.YEAR));*/
                    if(downTime.getTime()<=currentDate.getTime()){
                        //当前时间>=策略下架时间 改策略状态为已下架
                        userStrategyManageService.updateStategyStatusbyId(StrategyDto.StrategyStatus.DOWNSHELF, sid);
                    }
                    Date orderDate = new Date(strategyDto.downTime.getTime() -SPACING_INTERVAL);//订阅时间 = 下架时间-7天
                    //查询 策略下架前七天 还在订阅的用户
                    List<UserStrategyOrderDto> userList = qicDbUtil.queryBeanList(getOrderedUserSql, UserStrategyOrderDto.class, sid, orderDate);
                    //组装用户消息
                    StrategyBaseinfo strategy = strategyService.findStrategyById(Long.parseLong(sid));
                    String message = taskContext.messageTemplate;//下架通知
                    MessageBuilder messageBuilder = new MessageBuilder(message);
                    messageBuilder.addParameter("strategy", strategy);
                    String inputVal = messageBuilder.execute();
                    for (UserStrategyOrderDto userStrategyOrderDto: userList) {
                        //将消息逐个写入用户消息表
                        qicDbUtil.update(sendUserMessageSql, userStrategyOrderDto.uid, inputVal);
                    }
                }
        strategyService.deleteStrategyFromQsm( taskContext.strategyIdArray);//从qsm库中删除策略

    }
}
