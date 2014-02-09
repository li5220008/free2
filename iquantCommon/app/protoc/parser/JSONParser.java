package protoc.parser;

import com.google.gson.JsonElement;

import java.util.List;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-6-29
 * Time: 上午9:32
 * 功能描述: json解析接口
 */
public interface JSONParser {



    public <T> T toBean(JsonElement jsonElement, Class<T> type);

    public <T> List<T> toBeanList(JsonElement jsonElement, Class<T> type);

    public String createKey(JsonElement jsonElement, String keyFieldName);

    public <T> T createValue(JsonElement jsonElement, Class<T> type);
}
