package controllers;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.hibernate.PessimisticLockException;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.exception.SQLGrammarException;
import play.Logger;
import play.db.jpa.JPABase;
import play.i18n.Messages;
import play.mvc.After;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.Util;
import util.SystemResponseMessage;

import javax.persistence.PersistenceException;
import javax.persistence.QueryTimeoutException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 注意: 这里不放置拦截器, 只放置一些公用的操作方法.
 * 可以这么认为, 这里的方法都要用@Util来修饰
 * User: wenzhihong
 * Date: 12-5-18
 * Time: 上午9:43
 */
public class BaseController extends Controller {


    @Util
    static String fetchBody() {
        String body = params.get("body"); //这个body是会把 http body里的放入进去的.
        if (Logger.isDebugEnabled()) {
            Logger.debug("收到请求数据:\n%s", body);
        }
        return body;
    }

    @Util
    static void batchOpSuccess() {
        Map<String, Object> jsonMap = new HashMap<String, Object>(2);
        jsonMap.put("success", true);
        jsonMap.put("msg", "批量操作成功");
        renderJSON(jsonMap);
    }

    @Util
    static void opSuccess() {
        Map<String, Object> jsonMap = new HashMap<String, Object>(2);
        jsonMap.put("success", true);
        renderJSON(jsonMap);
    }

    @Util
    static void opSuccess(Long id) {
        Map<String, Object> jsonMap = new HashMap<String, Object>(2);
        jsonMap.put("success", true);
        jsonMap.put("id", id);
        renderJSON(jsonMap);
    }

    @Util
    static void opAddSuccess(Long id) {
        Map<String, Object> jsonMap = new HashMap<String, Object>(2);
        jsonMap.put("success", true);
        jsonMap.put("id", id);
        renderJSON(jsonMap);
    }

    @Util
    static void opEditSuccess(Long id) {
        Map<String, Object> jsonMap = new HashMap<String, Object>(2);
        jsonMap.put("success", true);
        jsonMap.put("id", id);
        renderJSON(jsonMap);
    }

    @Util
    static void opDelSuccess(Long id, int effect) {
        Map<String, Object> jsonMap = new HashMap<String, Object>(2);
        jsonMap.put("success", true);
        jsonMap.put("id", id);
        jsonMap.put("effect", effect);
        renderJSON(jsonMap);
    }

    @Util
    static void opFail(String msg, Object... args) {
        Map<String, Object> jsonMap = new HashMap<String, Object>(2);
        jsonMap.put("error", Messages.get(msg, args));
        renderJSON(jsonMap);
    }

    @Util
    static void entityNotFound() {
        opFail("api.cust.entityNoFound");
    }

    @Util
    static void secNotFound() {
        opFail("api.cust.secNoFound");
    }

    @Util
    static void jpaQueryError(JPABase.JPAQueryException e) {
        Logger.error(e, "japQueryError");
        opFail("jpaquery.error");
    }

    @Util
    static void jpaQueryTimeout(QueryTimeoutException e) {
        Logger.error(e, "jpa查询超时");
        opFail("jpaquery.timeout.error");
    }

    @Util
    static void persistenceError(PersistenceException e) {
        Logger.error(e, "持久化出错");
        //对错误进行分类细化
        Throwable cause = e.getCause();
        if (cause instanceof ConstraintViolationException) {
            opFail("jpa.persistence.constraintViolationException", cause.getMessage());
        } else if (cause instanceof PessimisticLockException) {
            opFail("jpa.persistence.pessimisticLockException", cause.getMessage());
        } else if (cause instanceof JDBCConnectionException) {
            JDBCConnectionException err = (JDBCConnectionException) cause;
            opFail("jpa.persistence.JDBCConnectionException");
        } else if (cause instanceof org.hibernate.QueryTimeoutException) {
            opFail("jpa.persistence.queryTimeoutException", cause.getMessage());
        } else if (cause instanceof SQLGrammarException) {
            opFail("jpa.persistence.SQLGrammarException", cause.getMessage());
        } else {
            opFail("jpa.persistence.error", cause.getMessage());
        }
    }

    @Util
    static void sqlExceptionError(SQLException e) {
        Logger.error(e, "sql错误");
        opFail("sqlexception.error");
    }

    @Util
    static void tokenRequired() {
        opFail("valid.token.required");
    }

    @Util
    static void jsonSyntaxExceptionError(Exception e) {
        Logger.error(e, "json语法错误");
        opFail("jsonSyntax.error");
    }

    /**
     * TODO
     * 最后一个错误处理, 可以考虑在以后集成邮件发送,或者报警功能
     */
    @Util
    static void finallyExceptionError(Exception e) {
        Logger.error(e, "服务器出错");
        opFail("finally.exception.error");
    }

    private static ThreadLocal<Map<String, Object>> msgThreadlocal = new ThreadLocal<Map<String, Object>>();

    @Before(priority = 3)
    public static void init() {
        //初始化
        if (msgThreadlocal.get() == null) {
            msgThreadlocal.set(new HashMap<String, Object>());
        } else {
            msgThreadlocal.get().clear();
        }
        msgThreadlocal.get().put("success", true);
        msgThreadlocal.get().put("message", SystemResponseMessage.SYSTEM_DEFAULT_MSG_RSP);
    }

    @Util
    public static void addErrorMsg(String key, String msg, Object... params){
        Map<String, Object> msgMap = msgThreadlocal.get();
        msgMap.put("success", false);
        msgMap.remove("message");
        List<Map<String,Object>> errors = (List<Map<String, Object>>) msgMap.get("errors");
        if (errors == null) {
            errors = Lists.newArrayList();
            msgMap.put("errors", errors);
        }
        Map<String, Object> error = Maps.newHashMap();
        error.put("key", key);

        if (params.length > 0) {
            String strMsg = msg;
            try {
                strMsg = String.format(msg, params);
            } catch (Exception e) {
                Joiner joiner = Joiner.on(',').useForNull("null");
                Logger.warn("按[%s]格式化消息出错:参数为:[%s]", msg, joiner.join(params));
                strMsg = msg;
            }
            error.put("msg", String.format(msg, params));
        }else {
            error.put("msg", msg);
        }
        errors.add(error);
    }

    /**
     * 是否有错误
     * @return
     */
    @Util
    public static boolean hasError(){
        List<Map<String,Object>> errors = (List<Map<String, Object>>) msgThreadlocal.get().get("errors");
        return errors != null && errors.size() > 0;
    }

    /**
     * 有错误的话,rend错误信息
     */
    @Util
    public static void rendOpFailIfHasError() {
        if (hasError()) {
            rendOpFail();
        }
    }

    @Util
    public static void rendOpFail(){
        renderJSON(msgThreadlocal.get());
    }

    @Util
    public static void setMessage(String message) {
        put("message", message);
    }

    @Util
    public static void setSuccessFlag(boolean isSuccess) {
        put("success", isSuccess);
    }

    @Util
    public static void setExtraData(String key, Object extraData) {
        put(key, extraData);
    }

    @Util
    public static Map getSampleResponseMap() {
        return msgThreadlocal.get();
    }

    @Util
    private static void put(String key, Object obj) {
        msgThreadlocal.get().put(key, obj);
    }

    @After
    public static void doSomethingAfter() {
        //记操作日志什么的,暂时没什么用
    }

    @Util
    protected static String getHostName() {

        String hostName = request.host;
        if (!hostName.startsWith("http")) {//https情况暂未考虑
            hostName = "http://" + hostName;
        }
        return hostName;
    }
}
