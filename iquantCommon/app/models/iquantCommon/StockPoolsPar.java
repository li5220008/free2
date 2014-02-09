package models.iquantCommon;

import com.google.gson.annotations.Expose;

/**
 * 股票池查询参数
 * User: liangbing
 * Date: 11-11-11
 * Time: 上午11:26
 */
public class StockPoolsPar {
    @Expose
    public String content;
    @Expose
    public String strategyName="";
    @Expose
    public String orderSort;
    @Expose
    public int flag;
    @Expose
    public String checkflag;

}
