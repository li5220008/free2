package models.iquantCommon;

import java.util.List;

/**
 * 股票池分类
 * User: wenzhihong
 * Date: 12-11-16
 * Time: 下午1:42
 */
public class StockPoolClassify {
    public String name;
    public String code;
    public String pcode;

    public List<StockPoolClassify> children;

    @Override
    public String toString() {
        return "StockPoolClassify{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", pcode='" + pcode + '\'' +
                ", children=" + children +
                '}';
    }
}
