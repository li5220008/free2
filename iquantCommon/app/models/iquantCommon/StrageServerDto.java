package models.iquantCommon;

import com.google.gson.annotations.Expose;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-4-1
 * Time: 下午4:40
 * 功能描述:
 */
public class StrageServerDto extends BackTestServerDto{

    //策略引擎类型id
    @Expose
    public int enginetypeId;
    //服务器中文名称
    @Expose
    public String serverName;
}
