package models.iquantCommon;

import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

/**
 * 产品描述
 * User: wenzhihong
 * Date: 12-4-12
 * Time: 下午4:56
 */
public class Product extends BaseDtoSupport {
    public String name;

    public Date utime = new Date();

    public Set<FunctionInfo> functions;

    public Product(String name) {
        this.name = name;
        functions = new TreeSet<FunctionInfo>();
    }

}
