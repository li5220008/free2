package protoc;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-3-27
 * Time: 上午10:35
 * 功能描述:
 */
public final class Protocol {

    public  static final String PROTCOL_VERSION = "1.0";
    public static final int STATUS_FAILRE = -1;
    public static final int STATU_SSUCCESS = 0;
    public static final String DEFAULT_ERROR_MESSAGE = "ERR_CODE-1000001";

    /**
     * 全局共用字段名称约定
     */
    public static class GlobalFieldName{

        public static  final String  DATA = "data";
        public static  final String  PAGESIZE = "pageSize";
        public static  final String  PAGENO= "pageNo";
        public static final String   BEGININDEX= "beginIndex";
        public static final String   ENDINDEX= "endIndex";
        public static  final String  STATUS = "status";
        public static  final String  TOTAL= "rowTotal";
        public static  final String  MESSAGE= "message";
        public static  final String  ERRORS= "errors";
        public static  final String  FIELD= "field";
        public static  final String  TOKEN= "token";
        public static  final String  SUCCESS= "success";
        public static  final String  ID= "id";

    }
  //------------------------------策略--------------------------//
    //策略列表
    public static class StrategyListInfo{

        public static  final String  ID = "id";
        public static  final String  UUID = "uuid";
        public static  final String  SNAME = "sname";
        public static  final String  PROVIDER = "provider";
        public static  final String  STYPE = "stype";
        public static  final String  TRADEVARIETY = "tradeVariety";
        public static  final String  COLLECTCOUNT = "collectCount";
        public static  final String  UPTIME = "upTime";
        public static  final String  DISCUSSCOUNT = "discussCount";
        public static  final String  ENGINETYPEID = "enginetypeId";
        public static  final String  DESP = "desp";
        public static  final String  ORDERCOUNT = "orderCount";
        public static  final String  STARLEVEL = "starLevel";
        public static  final String  YIELD = "yield";
        public static  final String  PROFITRATIO = "profitRatio";
        public static  final String  SHARPE = "sharpe";
    }

    //策略高级搜索列表
    public static class StrategyAdvanceSearchInfo{

        public static  final String  ID = "id";
        public static  final String  UID = "uid";
        public static  final String  NAME = "name";
        public static  final String  TYPE = "stype";
        public static  final String  CONTENT = "content";
    }
    //获取用户已收藏策略ID
    public static class UserCollect{

        public static  final String  IDS = "ids";
    }
    //高级搜索模板
    public static class UserTemplateInfo{

        public static  final String  ID = "id";
        public static  final String  UID = "uid";
        public static  final String  TYPE = "type";
        public static  final String  NAME = "name";
        public static  final String  CONTENT = "content";
    }
    //策略对比表格数据
    public static class StrategyTableInfo{

        public static  final String  NAME = "name";
        public static  final String  UPTIME = "upTime";
        public static  final String  ENGINETYPEID = "enginetypeId";
        public static  final String  DISCUSSCOUNT = "discussCount";
        public static  final String  COLLECTCOUNT = "collectCount";
        public static  final String  ORDERCOUNT = "orderCount";
        public static  final String  STARLEVEL = "starLevel";
        public static  final String  YIELDOFYEAR = "yieldOfYear";
        public static  final String  PROFITRATIO = "profitRatio";
        public static  final String  SHARPEINDEX = "sharpeIndex";
    }
    //策略基本信息
    public static class StrategyBaseInfo{

        public static  final String  ID = "id";
        public static  final String  SNAME = "sname";
        public static  final String  STYPE = "stype";
        public static  final String  UPTIME = "upTime";
        public static  final String  PROVIDER = "provider";
        public static  final String  TRADEVARIETY = "tradeVariety";
        public static  final String  LOOKBACKSTIME = "lookbackStime";
        public static  final String  LOOKBACKETIME = "lookbackEtime";
        public static  final String  DESP = "desp";
        public static  final String  PROVIDERDESP = "providerDesp";
        public static  final String  DISCUSSTOTAL = "discussTotal";
        public static  final String  DISCUSSCOUNT = "discussCount";
        public static  final String  ENGINETYPEID = "engineTypeId";
        public static  final String  STARLEVEL = "starLevel";
    }

//------------------------股票池-----------------------------//

    //股票池列表||热门股票池列表||高级搜索功能
    public static class StockPoolListInfo{

        public static  final String  STOCKPOOLCODE = "stockPoolCode;";
        public static  final String  ID = "id";
        public static  final String  POOLNAME = "poolName";
        public static  final String  SOURCE = "source";
        public static  final String  ANNUALIZEDYIELD = "annualizedYield";
        public static  final String  YEARJENSENRATIO = "yearJensenRatio";
        public static  final String  STARLEVEL = "starLevel";
        public static  final String  UPDATEDATE = "updateDate";
        public static  final String  STOCKNUM = "stockNum";
        public static  final String  COLLECTCOUNT = "collectCount";
        public static  final String  ORGID = "orgId";


    }
    //股票池高级搜索列表
    public static class StockPoolAdvanceSearchList{

        public static  final String  NAME = "name";
        public static  final String  TYPE = "type";
        public static  final String  CONTENT = "content";

    }



//----------------------用户类型----------------------------------//

    //用户
    public static class UserInfo{

        public static  final String  ACCOUNT = "account";
        public static  final String  STATE = "state";
        public static  final String  NAME = "name";
        public static  final String  SDATE = "sdate";
        public static  final String  EDATE = "edate";
        public static  final String  APPLYDATE = "applyDate";
        public static  final String  PASSWORD = "password";


    }

    //根据关键字获取系统配置信息
    public static  class fetchSystemConfig{
        public static  final String  MESSAGE = "message";
    }

    //根据用户ID获取用户消息列表
    public static  class UserMsgList{
        public static  final String  MSG = "msg";
        public static  final String  MSGTIME = "msgTime";
        public static  final String  STATUS = "status";
        public static  final String  TITLE = "title";
    }

    //根据消息ID获取消息
    public static  class MsgStatus{
        public static  final String  MSG = "msg";
        public static  final String  MSGTIME = "msgTime";
        public static  final String  STATUS = "status";
        public static  final String  TITLE = "title";
    }

}
