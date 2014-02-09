package models.iquantCommon;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-9-21
 * Time: 下午2:53
 * 功能描述:
 */
public class StrategyAccountTemplateDto extends BaseDtoSupport{
    public Long strategyId;
    public int accountOrder;
    public int accountType;
    public int status;
    public long createUid;

    public static  String accountType2String(int accountType){
       if(accountType == 0){
           return "Future";
       }else if(accountType ==1){
           return "Stock";
       }
        return "undefine";
    }
    public static int accountType2Int(String accountType){
       if("Stock".equals(accountType)){
           return 1;//股票
       }else if("Future".equals(accountType)){
           return 0;//期货
       }
        return -110;//无效值
    }
    public String toString(){
        return String.format("{accountType:%s,accountOrder:%s}",accountType,accountOrder);
    }
}
