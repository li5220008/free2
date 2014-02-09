package models.iquantCommon;

import com.google.gson.annotations.Expose;

/**
 * 功能点
 * User: wenzhihong
 * Date: 12-11-7
 * Time: 上午9:59
 */

public class FunctionInfo  {
    @Expose
    public Long id;
    @Expose
    public String name;
    @Expose
    public String action;
    @Expose
    public String code;
    //用不上
    public FunctionInfo parent;
    public Product product;

    @Expose
    public long fpid;
}
