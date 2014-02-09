package protoc.parser.handler;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import protoc.parser.JSONParseException;

import java.util.List;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-6-29
 * Time: 下午12:19
 * 功能描述: 适用以下场景
 *
 * {
 *     ....
 *     data:[1,2,3,4]
 *
 * }
 */
public class ListWithoutFieldHandler implements JSONHandler {
    @Override
    public List<String> handle(JsonElement jsonElement) throws JSONParseException {

        if (jsonElement == null) {
            return Lists.newArrayList();
        }

        List<String> list = Lists.newArrayList();

        for (JsonElement je : jsonElement.getAsJsonArray()) {

            list.add(je.getAsString());

        }

        return list;
    }
}
