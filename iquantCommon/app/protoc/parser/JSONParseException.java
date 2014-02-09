package protoc.parser;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-6-29
 * Time: 上午11:06
 * 功能描述:
 */
public class JSONParseException extends RuntimeException {

    public JSONParseException() {

    }

    public JSONParseException(String message) {
        super(message);
    }

    public JSONParseException(String message, Throwable e) {
        super(message, e);
    }
}
