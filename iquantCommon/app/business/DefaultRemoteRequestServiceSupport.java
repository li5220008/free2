package business;

import com.google.gson.JsonElement;
import com.tom.spring.web.util.UriTemplate;
import play.Logger;
import play.classloading.enhancers.LVEnhancer;
import play.exceptions.UnexpectedException;
import play.libs.WS;
import protoc.HttpBody;
import protoc.Protocol;
import protoc.ResponseHeader;
import protoc.parser.ActionResult;
import protoc.parser.BasicParser;
import protoc.parser.JSONParser;
import protoc.parser.handler.*;
import util.GsonUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-6-28
 * Time: 上午10:50
 * 功能描述:  通过http协议请求远程服务器
 */
public class DefaultRemoteRequestServiceSupport implements IRemoteRequestServiceSupport {

    private JSONParser parser = new BasicParser();

    private JsonElement doGet(String url, Object... params) {
        Map<String,Object> map = vargs2Map(params);
      //  String realUrl = String.format(url, params);
        String realUrl = new UriTemplate(url).expand(map).toString();
        Logger.debug("get请求url：%s", realUrl);
        JsonElement jsonElement = WS.url(realUrl).setHeader("accept","application/json").get().getJson();
        Logger.debug("get响应数据:：%s", jsonElement);
        return jsonElement;
    }

    public JsonElement doGetWithMap(String url, Map<String, Object> param) {
        throw new UnsupportedOperationException("暂不支持");
    }

    private JsonElement doPost(String url, String... params) {
        Map<String,Object> map = vargs2Map(params);
        //  String realUrl = String.format(url, params);
        String realUrl = new UriTemplate(url).expand(map).toString();
        Logger.debug("post请求url：%s", realUrl);
        JsonElement jsonElement = WS.url(realUrl).setHeader("accept","application/json").post().getJson();
        Logger.debug("post响应数据:：%s", jsonElement);
        return jsonElement;
    }

    public JsonElement doPostWithMap(String url, Map<String, Object> param) {
        throw new UnsupportedOperationException("暂不支持");
    }

    private JsonElement doPostWithBody(String url, HttpBody body, Object... params) {
        Map<String,Object> map = vargs2Map(params);
        //  String realUrl = String.format(url, params);
        String realUrl = new UriTemplate(url).expand(map).toString();
        //Logger.debug("post with body 请求url：%s,请求数据:\r\n%s", realUrl, body.body);
        String postJson = null;
        if (body.body instanceof String) {
            postJson = body.body.toString();
        } else {
            postJson = GsonUtil.createWithoutNulls().toJson(body.body);
        }
        //Logger.debug("post with body 请求url：%s,请求数据:\r\n%s", realUrl, postJson); //打印post请求的json参数
        Logger.debug("post with body 请求url：%s", realUrl); //打印post请求的json参数
        JsonElement jsonElement = WS.url(realUrl).setHeader("accept","application/json").body(postJson).post().getJson();
        Logger.debug("post with body 响应数据:：%s", jsonElement);
        return jsonElement;
    }

    public JsonElement doPostWithBodyAndMap(String url, Object body, Map<String, Object> param) {
        throw new UnsupportedOperationException("暂不支持");
    }

    @Override
    public <T> ActionResult<T> getBean(String url, Class<T> type, Object... params) {
        JSONHandler<T> handler = new BeanHandler<T>(parser, type);
        JsonElement jsonElement = doGet(url, params);
        T result = handler.handle(getBody(jsonElement));
        return ActionResult.buildResult(result, getHeader(jsonElement));
    }

    @Override
    public <T> T get(String url, JSONHandler<T> handler, Object... params) {
        throw new UnsupportedOperationException("暂未实现");
    }

    @Override
    public <T> T get(String url, HttpBody body, JSONHandler<T> handler, Object... params) {
        throw new UnsupportedOperationException("暂未实现");
    }

    @Override
    public JsonElement getJson(String url, Object... params) {
        return doGet(url, params);
    }

    @Override
    public JsonElement getJson(String url, HttpBody body, Object... params) {
        return doPostWithBody(url, body, params);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public <T> ActionResult<T> getBean(String url, HttpBody body, Class<T> type, Object... params) {
        JSONHandler<T> handler = new BeanHandler<T>(parser, type);
        JsonElement jsonElement = doPostWithBody(url, body, params);
        T result = handler.handle(getBody(jsonElement));
        return ActionResult.buildResult(result, getHeader(jsonElement));
    }

    @Override
    public <T> ActionResult<List<T>> getList(String url, Class<T> type, Object... params) {
        JSONHandler<List<T>> handler = new BeanListHandler<T>(parser, type);
        JsonElement jsonElement = doGet(url, params);
        List<T> result = handler.handle(getBody(jsonElement));
        return ActionResult.buildResult(result, getHeader(jsonElement));
    }


    @Override
    public <T> ActionResult<List<T>> getList(String url, HttpBody body, Class<T> type, Object... params) {
        JSONHandler<List<T>> handler = new BeanListHandler<T>(parser, type);
        JsonElement jsonElement = doPostWithBody(url, body, params);
        List<T> result = handler.handle(getBody(jsonElement));
        return ActionResult.buildResult(result, getHeader(jsonElement));
    }

    @Override
    public <T> ActionResult<List<Map<String, T>>> getListMap(String url, String keyFieldName, Class<T> type, Object... params) {
        JSONHandler<List<Map<String, T>>> handler = new ListMapHandler<T>(parser, type, keyFieldName);
        JsonElement jsonElement = doGet(url, params);
        List<Map<String, T>> result = handler.handle(getBody(jsonElement));
        return ActionResult.buildResult(result, getHeader(jsonElement));
    }

    @Override
    public <T> ActionResult<List<Map<String, T>>> getListMap(String url, HttpBody body, String keyFieldName, Class<T> type, Object... params) {
        JSONHandler<List<Map<String, T>>> handler = new ListMapHandler<T>(parser, type, keyFieldName);
        JsonElement jsonElement = doPostWithBody(url, body, params);
        List<Map<String, T>> result = handler.handle(getBody(jsonElement));
        return ActionResult.buildResult(result, getHeader(jsonElement));
    }

    @Override
    public ActionResult<List<String>> getListWithoutField(String url, Object... params) {
        JSONHandler handler = new ListWithoutFieldHandler();
        JsonElement jsonElement = doGet(url, params);
        List<String> result = (List<String>) handler.handle(getBody(jsonElement));
        return ActionResult.buildResult(result, getHeader(jsonElement));
    }

    @Override
    public ActionResult<List<String>> getListWithoutField(String url, HttpBody body, Object... params) {
        JSONHandler handler = new ListWithoutFieldHandler();
        JsonElement jsonElement = doPostWithBody(url, body, params);
        List<String> result = (List<String>) handler.handle(getBody(jsonElement));
        return ActionResult.buildResult(result, getHeader(jsonElement));
    }

    @Override
    public ActionResult<List<String>> getListWithSingleField(String url, String fieldName, Object... params) {
        JSONHandler handler = new SingleFieldListHandler(fieldName);
        JsonElement jsonElement = doGet(url, params);
        List<String> result = (List<String>) handler.handle(getBody(jsonElement));
        return ActionResult.buildResult(result, getHeader(jsonElement));
    }

    @Override
    public ActionResult<List<String>> getListWithSingleField(String url, HttpBody body, String fieldName, Object... params) {
        JSONHandler handler = new SingleFieldListHandler(fieldName);
        JsonElement jsonElement = doPostWithBody(url, body, params);
        List<String> result = (List<String>) handler.handle(getBody(jsonElement));
        return ActionResult.buildResult(result, getHeader(jsonElement));
    }

    @Override
    public ActionResult<String> getSingleValue(String url, HttpBody body, Object... params) {
        JsonElement jsonElement = doPostWithBody(url, body, params);
        JsonElement data = getBody(jsonElement);
        String value = null;
        if (data != null) {
            value = data.getAsString();
        }
        return ActionResult.buildResult(value, getHeader(jsonElement));
    }

    @Override
    public ActionResult<String> getSingleValue(String url, Object... params) {
        JsonElement jsonElement = doGet(url, params);
        JsonElement data = getBody(jsonElement);
        String value = null;
        if (data != null) {
            value = data.getAsString();
        }
        return ActionResult.buildResult(value, getHeader(jsonElement));
    }

    @Override
    public ActionResult<String> getSingleFieldValue(String url, String fieldName, HttpBody body, Object... params) {
        JsonElement jsonElement = doPostWithBody(url, body, params);
        JsonElement data = getBody(jsonElement);
        String value = null;
        if (data != null) {
            value = data.getAsJsonObject().get(fieldName).getAsString();
        }
        return ActionResult.buildResult(value, getHeader(jsonElement));
    }

    @Override
    public ActionResult<String> getSingleFieldValue(String url, String fieldName, Object... params) {
        JsonElement jsonElement = doGet(url, params);
        JsonElement data = getBody(jsonElement);
        String value = null;
        if (data != null) {
            value = data.getAsJsonObject().get(fieldName).getAsString();
        }
        return ActionResult.buildResult(value, getHeader(jsonElement));
    }

    @Override
    public ActionResult getJustHeader(String url, HttpBody body, Object... params) {
        JsonElement jsonElement = doPostWithBody(url, body, params);
        return ActionResult.buildResult(null, getHeader(jsonElement));  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ActionResult getJustHeader(String url, Object... params) {
        JsonElement jsonElement = doGet(url, params);
        return ActionResult.buildResult(null, getHeader(jsonElement));  //To change body of implemented methods use File | Settings | File Templates.
    }

    private ResponseHeader getHeader(JsonElement jsonElement) {
        ResponseHeader header = GsonUtil.createWithoutNulls().fromJson(jsonElement, ResponseHeader.class);
        return header;
    }

    private JsonElement getBody(JsonElement jsonElement) {
        return jsonElement.getAsJsonObject().get(Protocol.GlobalFieldName.DATA);
    }
    private Map<String,Object>  vargs2Map(Object... args){
        Map<String, Object> templateBinding = new HashMap<String, Object>(4);
        Stack<LVEnhancer.MethodExecution> stack = LVEnhancer.LVEnhancerRuntime.getCurrentMethodParams();
        if (stack.size() > 0) {
            LVEnhancer.MethodExecution me = stack.get(stack.size() - 4).getCurrentNestedMethodCall();
            LVEnhancer.LVEnhancerRuntime.ParamsNames paramsNames = new LVEnhancer.LVEnhancerRuntime.ParamsNames(me.getSubject(), me.getParamsNames(), me.getVarargsNames());
            String[] names = paramsNames.varargs;
            if (args != null && args.length > 0 && names == null) {
                throw new UnexpectedException("no varargs names while args.length > 0 !");
            }
            for (int i = 0; i < args.length; i++) {
                templateBinding.put(names[i], args[i]);
            }
        }
        return templateBinding;
    }

}

