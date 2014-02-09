package models.iquantCommon;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 12-12-15
 * Time: 上午11:19
 * 功能描述: 上传策略时qsm同步数据用
 */
public class QsmStrategyDto {
    /**
     * 策略ID uuid
     */
    public String strategyId;
    /**
     * 策略名称
     */
    public String strategyName;
    /**
     * 策略加载路径
     */
    public String filePath;
    /**
     * 运行服务器ip
     */
    public String agentIp;
}
