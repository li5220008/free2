package models.iquantCommon;

import com.google.gson.annotations.Expose;

/**
 * 策略管理查询 参数类
 * User: liangbing
 * Date: 12-12-10
 * Time: 下午5:46
 */
public class StrategyPar {

    //关键字
    @Expose
    public String keyWords;

    //正方序排序
    @Expose
    public int orderFlag;

    //按对应字段排序
    @Expose
    public String orderSort;

    //表示是已上架,待上架,回收站
    @Expose
    public int flag;

    //交易类型
    @Expose
    public int tradeType;

    //交易品种
    @Expose
    public int tradeVariety;

    //策略语言
    @Expose
    public int strategyLanguage;

    //策略状态
    @Expose
    public int status;

    //服务器配置参数,配合页面策略上传功能显示 默认值为3。
    @Expose
    public Integer serviceParam = 3;
}
