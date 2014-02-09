package models.iquantCommon;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * User: Administrator
 * Date: 13-9-24
 * Time: 上午10:31
 */
public class StrategySecurityOriginalDto extends BaseDtoSupport{

    public Long strategyId;   //策略实例ID

    public Integer type;         //证券类型,0:期货, 1:股票, 2:板块， 3:主力/连续

    public Long systemPlateId;  //系统板块id

    public Integer maxPosition;   //最大持仓量

    public String market;         //市场

    public String symbol;        // 股票代码

    public StrategySecurityOriginalDto(){}

    //系统板块id, 这里是根据需求, 很固定下来的.
    static final List<String> SYSTEM_PLATE_IDS = Lists.newArrayList("1012003002017", "1012003002086", "1001001", "1001001001", "1001001002", "1001002", "1001002001", "1001002002");
    static final List<String> SYSTEM_PLATE_NAMES = Lists.newArrayList("沪深300",     "沪深300",        "所有A股",  "上海A股",    "深圳A股",    "所有B股",  "上海B股",    "深圳B股");

    public static final Map<String,String> PLATEID_PLATE_CN_NAME_MAPPING = Maps.newHashMap();
    public static final Map<String,String> QIA_PLATENAME_PLATEID_MAPPING = Maps.newHashMap();
    static{
        PLATEID_PLATE_CN_NAME_MAPPING.put("1012003002017","沪深300");
        PLATEID_PLATE_CN_NAME_MAPPING.put("1012003002086","沪深300");
        PLATEID_PLATE_CN_NAME_MAPPING.put("1001001","所有A股");
        PLATEID_PLATE_CN_NAME_MAPPING.put("1001001001","上海A股");
        PLATEID_PLATE_CN_NAME_MAPPING.put("1001001002","深圳A股");
        PLATEID_PLATE_CN_NAME_MAPPING.put("1001002","所有B股");
        PLATEID_PLATE_CN_NAME_MAPPING.put("1001002001","上海B股");
        PLATEID_PLATE_CN_NAME_MAPPING.put("1001002002","深圳B股");
    }
    static{
        QIA_PLATENAME_PLATEID_MAPPING.put("AllAStock","1001001");//所有A股
        QIA_PLATENAME_PLATEID_MAPPING.put("SHA","1001001001");//上海A股
        QIA_PLATENAME_PLATEID_MAPPING.put("SZA","1001001002");//深圳A股
        QIA_PLATENAME_PLATEID_MAPPING.put("AllBStock","1001002");//所有B股
        QIA_PLATENAME_PLATEID_MAPPING.put("SHB","1001002001");//上海B股
        QIA_PLATENAME_PLATEID_MAPPING.put("SZB","1001002002");//深圳B股
        QIA_PLATENAME_PLATEID_MAPPING.put(" HS300","1012003002017");//沪深300
    }
    static final Pattern MAINCONTRACT_REGULAR = Pattern.compile(".+(cc|gta)[0-9]+.*");  //正则 字符串中间出现“cc+数字”或者“gta+数字” 被认为是可匹配
    static final Pattern SECURITY_REGULAR = Pattern.compile("^([A-Za-z])+\\w*");  //正则 匹配以字母开头


    public StrategySecurityOriginalDto(String symbol,Integer maxPosition, String market){
        this.maxPosition = maxPosition;
        this.market = market;
        this.symbol = symbol;

        if (MAINCONTRACT_REGULAR.matcher(symbol.toLowerCase()).matches()) {
            this.type = 3; //主力/连续
        } else if (QIA_PLATENAME_PLATEID_MAPPING.get(symbol.trim())!=null) {
            int index = SYSTEM_PLATE_IDS.indexOf(symbol.trim());
            this.type = 2; //板块
            this.systemPlateId = Long.parseLong(QIA_PLATENAME_PLATEID_MAPPING.get(symbol.trim()));
           // this.symbol = SYSTEM_PLATE_NAMES.get(index);
            this.symbol = QIA_PLATENAME_PLATEID_MAPPING.get(symbol.trim());
        }/*else if (SYSTEM_PLATE_IDS.contains(symbol)) {
            int index = SYSTEM_PLATE_IDS.indexOf(symbol);
            this.type = 2; //板块
            this.systemPlateId = Long.parseLong(symbol);
            this.symbol = SYSTEM_PLATE_NAMES.get(index);
        }*/ else if (SECURITY_REGULAR.matcher(symbol).matches()) {
            this.type = 0; //期货
        } else {
            this.type = 1; //股票
        }
    }
}
