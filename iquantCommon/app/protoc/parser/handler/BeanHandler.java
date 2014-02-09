package protoc.parser.handler;

import com.google.gson.JsonElement;
import protoc.parser.JSONParseException;
import protoc.parser.JSONParser;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-6-29
 * Time: 上午11:15
 * 功能描述:
 */
public class BeanHandler<T> implements JSONHandler<T> {
    private JSONParser parser;
    private Class<T> type;

    public BeanHandler(JSONParser parser, Class<T> type) {
        this.parser = parser;
        this.type = type;
    }

    @Override
    public T handle(JsonElement jsonElement) throws JSONParseException {
        if (jsonElement == null) {
            return null;
        }
        return parser.toBean(jsonElement, this.type);

    }
}
