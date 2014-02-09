package models.iquantCommon;

import com.google.gson.annotations.Expose;

/**
 * 股票池组合列表dto
 * User: panzhiwei
 * Date: 12-11-21
 * Time: 上午10:07
 * To change this template use File | Settings | File Templates.
 */
public class StockPoolCombineInfoDto {
    @Expose
    public String id;
    @Expose
    public String scode;//股票代码
    @Expose
    public String name;//股票名称
    public float current;//最新
    public float change;//涨跌
    public float changeRate;//涨幅%
    public int volume;  //成交量
    public float amount;  //成交额
    public float buyprice;//买入价
    public float sellprice;//卖出价
    public float buynum;//买入量
    public float sellnum;//卖出量
    public float prvclose;//昨收
    public float open;//今开
    public float high;//最高
    public float low;//最低
    public String exchangeCode;
    public String shortName;
}
