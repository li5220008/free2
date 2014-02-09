package controllers;

import com.google.gson.JsonParseException;
import com.google.gson.stream.MalformedJsonException;
import play.Play;
import play.db.jpa.JPABase;
import play.exceptions.JavaExecutionException;
import play.exceptions.PlayException;
import play.mvc.Before;
import play.mvc.Catch;
import play.mvc.Http;
import play.mvc.Util;
import util.GsonUtil;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import javax.persistence.QueryTimeoutException;
import java.sql.SQLException;

/**
 * 放置一般的拦截器, 一般来说除token的校验外, 都应该放在这里
 * 关于priority的分布
 * before, after, finally, catch 的 priority 0 -- 29 做为系统保留使用
 *
 * User: wenzhihong
 * Date: 12-5-18
 * Time: 上午9:27
 */
public class GeneralIntercept extends BaseController {
    /**
     * 参数校验
     */
    @Before(priority = 40)
    static void paramValidate() {
        if (validation.hasErrors()) {
            renderJSON(GsonUtil.validationErrorToJson(validation.errors()));
        }
    }

    /**
     * 实体不存在. 一般来说是根据id去查找的时候
     */
    @Catch(priority = 30, value = EntityNotFoundException.class)
    static void entityNotFoundException(EntityNotFoundException e) {
        if(Play.mode.isProd()){
            entityNotFound();
        }else{
            wrapException(e);
        }

    }

    /**
     * jpa查询错误. (一般来说是sql的语法错误)
     */
    @Catch(priority = 40, value = JPABase.JPAQueryException.class)
    static void jpaQueryException(JPABase.JPAQueryException e) {
        if(Play.mode.isProd()){
            jpaQueryError(e);
        }else{
            wrapException(e);
        }
    }

    /**
     * jpa查询超时
     */
    @Catch(priority = 50, value = QueryTimeoutException.class)
    public static void queryTimeoutException(QueryTimeoutException e) {
        if(Play.mode.isProd()){
            jpaQueryTimeout(e);
        }else{
            wrapException(e);
        }
    }

    /**
     * 一般jpa错误
     */
    @Catch(priority = 60, value = PersistenceException.class)
    public static void persistenceException(PersistenceException e) {
        if(Play.mode.isProd()){
            persistenceError(e);
        }else{
            wrapException(e);
        }
    }

    /**
     * sql错误
     */
    @Catch(priority = 70, value = SQLException.class)
    public static void sqlException(SQLException e){
        if(Play.mode.isProd()){
            sqlExceptionError(e);
        }else{
            wrapException(e);
        }
    }

    @Catch(priority = 80, value = {JsonParseException.class, MalformedJsonException.class})
    public static void jsonSyntaxException(Exception e) {
        if(Play.mode.isProd()){
            jsonSyntaxExceptionError(e);
        }else{
            wrapException(e);
        }
    }

    //最后一道网
    @Catch(priority = 65535, value = Exception.class)
    public static void finallyException(Exception e) {
        if(Play.mode.isProd()){
            finallyExceptionError(e);
        }else{
            wrapException(e);
        }
    }

    /**
     * 下面的代码来自于 play.mvc.ActionInvoker.invoke方法,对异常的包装处理
     * @param ex 原始异常
     */
    @Util
    static void wrapException(Exception ex) {
        // Re-throw the enclosed exception
        ex.printStackTrace();
        if (ex instanceof PlayException) {
            throw (PlayException) ex;
        }
        StackTraceElement element = PlayException.getInterestingStrackTraceElement(ex);
        if (element != null) {
            throw new JavaExecutionException(Play.classes.getApplicationClass(element.getClassName()), element.getLineNumber(), ex);
        }
        throw new JavaExecutionException(Http.Request.current().action, ex);
    }

}
