package protoc.parser.handler;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import protoc.parser.JSONParseException;
import protoc.parser.JSONParser;

import java.util.List;
import java.util.Map;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-6-29
 * Time: 上午11:32
 * 功能描述: 以结果集中某个字段作为key  对应的记录作为value
 */
public class ListMapHandler<T> implements JSONHandler<List<Map<String, T>>> {
    private JSONParser parser;
    private Class<T> type;
    private String keyFieldName;

    public ListMapHandler(JSONParser parser, Class<T> type, String keyFieldName) {

        this.parser = parser;
        this.type = type;
        this.keyFieldName = keyFieldName;

    }

    @Override
    public List<Map<String, T>> handle(JsonElement jsonElement) throws JSONParseException {

        if (jsonElement == null) {
            return Lists.newArrayList();
        }


        List<Map<String, T>> resultList = Lists.newArrayList();

        for (JsonElement je : jsonElement.getAsJsonArray()) {
            Map<String, T> map = Maps.newHashMap();
            String key = parser.createKey(je, this.keyFieldName);
            T value = parser.createValue(je, this.type);
            resultList.add(map);
        }

        return resultList;
    }
}
