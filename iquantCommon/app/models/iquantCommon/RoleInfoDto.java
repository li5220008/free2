package models.iquantCommon;


import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 12-12-4
 * Time: 下午1:52
 * 功能描述: 角色信息
 */
public class RoleInfoDto  {
    @Expose
    public long id;
    @Expose
    public String name;
    @Expose
    public String desp;
    @Expose
    public List<FunctionInfoDto> functions;
}
