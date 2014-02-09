package util;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 12-12-24
 * Time: 下午4:23
 * 功能描述:操作鉴权
 */
public interface FunctionAuth {

    public boolean hasPrivilege(long preId, long uid);
}
