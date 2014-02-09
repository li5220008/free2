package protoc.parser;

import protoc.ResponseHeader;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-7-1
 * Time: 上午8:49
 * 功能描述:
 */
public class ActionResult<T> {

    public ResponseHeader header;
    public T    data;
    public ActionResult(T data,ResponseHeader responseHeader){
        this.data = data;
        this.header = responseHeader;
    }
    public static <T> ActionResult<T> buildResult(T data,ResponseHeader responseHeader){
        return new ActionResult<T>(data,responseHeader);
    }

}
