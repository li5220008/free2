package business;

import com.google.gson.JsonElement;
import protoc.HttpBody;
import protoc.parser.ActionResult;
import protoc.parser.handler.JSONHandler;

import java.util.List;
import java.util.Map;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-6-28
 * Time: 上午10:39
 * 功能描述:
 * 此接口中几乎所有返回类型为F.T2(arg1,arg2)型式
 * 其中 arg1协议的一些全局变量，比如status,message  pageSize pageNo  errors 等等。详见协议文档
 * arg2 则为业务需逻辑需要的真正数据，
 */
public interface IRemoteRequestServiceSupport {

    /**
     * @param url    请求的url
     * @param type   返回类型
     * @param params 拼接到url的参数
     * @param <T>
     * @return
     */
    public <T> ActionResult<T> getBean(String url, Class<T> type, Object... params);

    public <T> T get(String url, JSONHandler<T> handler, Object... params);

    public <T> T get(String url, HttpBody body, JSONHandler<T> handler, Object... params);

    public JsonElement getJson(String url, Object... params);

    public JsonElement getJson(String url, HttpBody body, Object... params);

    public <T> ActionResult<T> getBean(String url, HttpBody body, Class<T> type, Object... params);

    public <T> ActionResult<List<T>> getList(String url, Class<T> type, Object... params);

    public <T> ActionResult<List<T>> getList(String url, HttpBody body, Class<T> type, Object... params);

    public <T> ActionResult<List<Map<String, T>>> getListMap(String url, String keyFieldName, Class<T> type, Object... params);

    public <T> ActionResult<List<Map<String, T>>> getListMap(String url, HttpBody body, String keyFieldName, Class<T> type, Object... params);

    public ActionResult<List<String>> getListWithoutField(String url, Object... params);

    public ActionResult<List<String>> getListWithoutField(String url, HttpBody body, Object... params);

    public ActionResult<List<String>> getListWithSingleField(String url, String fieldName, Object... params);

    public ActionResult<List<String>> getListWithSingleField(String url, HttpBody body, String fieldName, Object... params);

    public ActionResult<String> getSingleValue(String url, HttpBody body, Object... params);

    public ActionResult<String> getSingleValue(String url, Object... params);

    public ActionResult<String> getSingleFieldValue(String url, String fieldName, HttpBody body, Object... params);

    public ActionResult<String> getSingleFieldValue(String url, String fieldName, Object... params);

    public ActionResult getJustHeader(String url, HttpBody body, Object... params);

    public ActionResult getJustHeader(String url, Object... params);
}
