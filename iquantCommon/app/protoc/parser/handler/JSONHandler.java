package protoc.parser.handler;

import com.google.gson.JsonElement;
import protoc.parser.JSONParseException;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-6-29
 * Time: 上午10:52
 * 功能描述:
 */
public interface JSONHandler<T> {

     T handle(JsonElement jsonElement) throws JSONParseException;

}
