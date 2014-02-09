package protoc.parser.handler;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import protoc.parser.JSONParseException;

import java.util.List;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-6-29
 * Time: 下午12:18
 * 功能描述:
 */
public class SingleFieldListHandler implements JSONHandler {

    private String fieldName;

    public SingleFieldListHandler(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public List<String> handle(JsonElement jsonElement) throws JSONParseException {

        if (jsonElement == null) {
            return Lists.newArrayList();
        }
        List<String> list = Lists.newArrayList();
        for (JsonElement je : jsonElement.getAsJsonArray()) {
            String value = je.getAsJsonObject().get(fieldName).getAsString();
            list.add(value);
        }

        return list;
    }
}
