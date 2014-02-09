package protoc.parser.handler;

import com.google.gson.JsonElement;
import protoc.parser.JSONParseException;
import protoc.parser.JSONParser;

import java.util.List;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-6-29
 * Time: 上午9:37
 * 功能描述:
 */
public class BeanListHandler<T> implements JSONHandler<List<T>> {
    private JSONParser parser;
    private Class<T> type;

    public BeanListHandler(JSONParser parser, Class<T> type) {
        this.parser = parser;
        this.type = type;
    }


    @Override
    public List<T> handle(JsonElement jsonElement) throws JSONParseException {

        return parser.toBeanList(jsonElement, type);
    }
}
