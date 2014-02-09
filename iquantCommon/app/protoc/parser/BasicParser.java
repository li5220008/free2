package protoc.parser;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import util.GsonUtil;

import java.util.List;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-6-29
 * Time: 上午9:24
 * 功能描述: json解析实现类
 */
public class BasicParser implements JSONParser {

    /**
     * 将json转化成bean
     *
     * @param je
     * @param type
     * @param <T>
     * @return
     */
    public <T> T toBean(JsonElement je, Class<T> type) {

        if (je.isJsonArray()) {
            throw new JSONParseException("预期数据源为Object结构，但参数为数组结构,解析器凌乱了");
        }
        return GsonUtil.createWithoutNulls().fromJson(je, type);

    }

    @Override
    public <T> List<T> toBeanList(JsonElement jsonElement, Class<T> type) {

        if (jsonElement == null) {
            return Lists.newArrayList();
        }

        if (!jsonElement.isJsonArray()) {
            throw new JSONParseException("预期数据源为数组结构，但参数为Object结构,解析器凌乱了");
        }
        JsonArray ja = jsonElement.getAsJsonArray();
        List<T> list = Lists.newArrayList();
        for (JsonElement obj : ja) {
            T t = toBean(obj, type);
            list.add(t);
        }
        return list;
    }

    @Override
    public String createKey(JsonElement jsonElement, String keyFieldName) {
        return jsonElement.getAsJsonObject().get(keyFieldName).getAsString();
    }

    @Override
    public <T> T createValue(JsonElement jsonElement, Class<T> type) {
        return toBean(jsonElement, type);
    }

}
