package util;

import org.apache.commons.lang.reflect.FieldUtils;
import org.hibernate.*;
import org.hibernate.ejb.HibernateEntityManagerFactory;
import org.hibernate.internal.AbstractQueryImpl;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.hibernate.type.Type;
import play.Play;
import play.db.jpa.JPA;
import play.db.jpa.JPABase;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;


/**
 * 这个是专用于hibernate jpa的工具类
 * 注意:只有在jpa是hibernate实现时, 才可以用这个类
 * User: wenzhihong
 * Date: 12-5-2
 * Time: 下午4:28
 */
public abstract class JPAHibernateUtil {

    private static final boolean isAddSqlDebugInfo = Boolean.parseBoolean(Play.configuration.getProperty("isAddSqlDebugInfo", "false"));

    public static final String QUERY_HINT_DEBUG_SPLIT = "#3#";

    //region 方便的工具方法

    /**
     * 获取类 clazz 所在的Session. 在多数据源的时候很有用
     */
    public static Session getSession(Class clazz){
        return (Session) JPABase.getJPAConfig(clazz).getJPAContext().em().getDelegate();
    }

    public static EntityManager em(Class clazz){
        return JPABase.getJPAConfig(clazz).getJPAContext().em();
    }

    public static EntityManager em(){
        return JPA.em();
    }

    /**
     * 只返回代理对象
     */
    public static <T> T getReference(Class<T> entityClass, Object primaryKey){
        return JPABase.getJPAConfig(entityClass).getJPAContext().em().getReference(entityClass, primaryKey);
    }

    /**
     * 从数据库加载对象
     */
    public static <T> T findEntityById(Class<T> entityClass, Object primaryKey){
        return JPABase.getJPAConfig(entityClass).getJPAContext().em().find(entityClass, primaryKey);
    }

    /**
     * 获取org.hibernate.Session. 默认数据源的
     */
    public static Session getSession(){
        return (Session) JPA.em().getDelegate();
    }

    public static SessionFactory getSessionFactory(Class clazz){
        return ((HibernateEntityManagerFactory)JPABase.getJPAConfig(clazz).getJPAContext().em().getEntityManagerFactory()).getSessionFactory();
    }

    /**
     * 获取org.hibernate.SessionFactory. 默认数据源的
     */
    public static SessionFactory getSessionFactory(){
        return ((HibernateEntityManagerFactory)JPA.em().getEntityManagerFactory()).getSessionFactory();
    }

    /**
     * 创建一个hibernate原生的query
     */
    public static Query createQuery(String ql){
        return getSession().createQuery(ql);
    }

    /**
     * 创建一个HibQuery
     */
    public static HibQuery createHibQuery(String ql){
        return new HibQuery(getSession().createQuery(ql)) ;
    }

    /**
     * 创建一个Hibquery, 设置ResultTransformer为转化成bean.
     */
    public static HibQuery createHibQueryWithTransformerAliasToBean(String ql, Class taragetBean){
        return createHibQuery(ql).setResultTransformer(Transformers.aliasToBean(taragetBean));
    }

    /**
     * 创建一个Hibquery, 在select 子句里要用 as mapkey, 返回一个map, 且key已定义好
     */
    public static HibQuery createHibQueryWithTransformerAliasToMap(String ql){
        return createHibQuery(ql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
    }

    /**
     * 创建一个hibernate原生的org.hibernate.SqlQuery
     */
    public static SQLQuery createSqlQuery(String sql){
        return getSession().createSQLQuery(sql);
    }

    /**
     * 创建一个HibSqlQuery
     */
    public static HibSqlQuery createHibSqlQuery(String sql){
        return new HibSqlQuery(getSession().createSQLQuery(sql));
    }

    /**
     * 创建一个HibSqlQuery, 设置ResultTransformer为转化成bean.
     * @return
     */
    public static HibQuery createHibSqlQueryWithTransformerAliasToBean(String ql, Class taragetBean){
        return createHibSqlQuery(ql).setResultTransformer(Transformers.aliasToBean(taragetBean));
    }

    /**
     * 创建一个Hibquery, 在select 子句里要用 as mapkey, 返回一个map, 且key已定义好
     */
    public static HibQuery createHibSqlQueryWithTransformerAliasToMap(String ql){
        return createHibSqlQuery(ql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
    }
    //endregion

    /**
     * 给查询设置查询提示, 加入debug信息
     */
    public static void setQueryHintWithDebugInfo(Query query){
        String debugInfo = "";
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        debugInfo = elements[2].getClassName() + "." + elements[2].getMethodName() + " at " + elements[2].getLineNumber();
        referenceQueryHint(query, debugInfo);
    }

    /**
     * 给jpa的query设置查询提示, 加入debug信息
     */
    public static void setQueryHintWithDebugInfo(javax.persistence.Query query){
        String oldComment = ""; //原先的提示信息
        String debugInfo = "";
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        debugInfo = elements[2].getClassName() + "." + elements[2].getMethodName() + " at " + elements[2].getLineNumber();
        oldComment = query.getHints().get(org.hibernate.ejb.QueryHints.HINT_COMMENT) == null ? "" : (String)query.getHints().get(org.hibernate.ejb.QueryHints.HINT_COMMENT);
        query.setHint(org.hibernate.ejb.QueryHints.HINT_COMMENT, oldComment + QUERY_HINT_DEBUG_SPLIT + debugInfo);
    }

    private static void referenceQueryHint(Query query, String debugInfo) {
        String oldComment = ""; //原先的提示信息
        if (query instanceof AbstractQueryImpl) { //暴力破解
            AbstractQueryImpl queryImp = (AbstractQueryImpl) query;
            Object commentValue = "";
            try {
                commentValue = FieldUtils.readField(queryImp, "comment", true);
                oldComment = commentValue == null ? "" : (String)commentValue;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            query.setComment(oldComment + QUERY_HINT_DEBUG_SPLIT + debugInfo);
        }
    }



    //region HibQuery对org.hibernate.query的代理实现
    /**
     * 这里只是简单的org.hibernate.Query. 代理实现 因为要实现对list的调用修改. 主要是加入sql生成的调试信息. 及方法的链式调用
     */
    public static class HibQuery{
        public Query query;

        public HibQuery(Query query){
            this.query = query;
        }

        private static void queryDebugHint(Query query){
            if (isAddSqlDebugInfo) {
                String debugInfo = "";
                StackTraceElement[] elements = Thread.currentThread().getStackTrace();
                debugInfo = elements[3].getClassName() + "." + elements[3].getMethodName() + " at " + elements[3].getLineNumber();
                referenceQueryHint(query, debugInfo);
            }
        }

        //region 增加额外的方便方法
        public <T> T first() {
            queryDebugHint(query);
            try {
                List<T> results = query.setMaxResults(1).list();
                if (results.isEmpty()) {
                    return null;
                }
                return results.get(0);
            } catch (HibernateException e) {
                throw new JPABase.JPAQueryException("Error while executing query <strong>" + query.getQueryString() + "</strong>", JPABase.JPAQueryException.findBestCause(e));
            }
        }

        public <T> List<T> fetch() {
            queryDebugHint(query);
            try {
                return query.list();
            } catch (HibernateException e) {
                throw new JPABase.JPAQueryException("Error while executing query <strong>" + query.getQueryString() + "</strong>", JPABase.JPAQueryException.findBestCause(e));
            }
        }

        public <T> List<T> fetch(int max) {
            queryDebugHint(query);
            try {
                query.setMaxResults(max);
                return query.list();
            } catch (HibernateException e) {
                throw new JPABase.JPAQueryException("Error while executing query <strong>" + query.getQueryString() + "</strong>", JPABase.JPAQueryException.findBestCause(e));
            }
        }

        //翻页方法
        public <T> List<T> fetch(int page, int length) {
            queryDebugHint(query);
            if (page < 1) {
                page = 1;
            }
            query.setFirstResult((page - 1) * length);
            query.setMaxResults(length);
            try {
                return query.list();
            } catch (HibernateException e) {
                throw new JPABase.JPAQueryException("Error while executing query <strong>" + query.getQueryString() + "</strong>", JPABase.JPAQueryException.findBestCause(e));
            }
        }
        //endregion

        //region 加入了调试信息的功能
        public List list() {
            queryDebugHint(query);
            try{
                return query.list();
            } catch (HibernateException e) {
                throw new JPABase.JPAQueryException("Error while executing query <strong>" + query.getQueryString() + "</strong>", JPABase.JPAQueryException.findBestCause(e));
            }
        }

        public Object uniqueResult() {
            queryDebugHint(query);
            try{
                return query.uniqueResult();
            } catch (HibernateException e) {
                throw new JPABase.JPAQueryException("Error while executing query <strong>" + query.getQueryString() + "</strong>", JPABase.JPAQueryException.findBestCause(e));
            }
        }

        public int executeUpdate() {
            queryDebugHint(query);
            return query.executeUpdate();
        }
        //endregion

        public String getQueryString(){
            return query.getQueryString();
        }

        public Type[] getReturnTypes() throws HibernateException{
            return query.getReturnTypes();
        }

        public String[] getReturnAliases() throws HibernateException{
            return query.getReturnAliases();
        }

        public String[] getNamedParameters() throws HibernateException{
            return query.getNamedParameters();
        }

        public Iterator iterate() throws HibernateException{
            return query.iterate();
        }

        public ScrollableResults scroll() throws HibernateException{
            return  query.scroll();
        }

        public ScrollableResults scroll(ScrollMode scrollMode) throws HibernateException{
            return query.scroll(scrollMode);
        }

        public HibQuery setMaxResults(int maxResults){
            query.setMaxResults(maxResults);
            return this;
        }

        public HibQuery setFirstResult(int firstResult){
            query.setFirstResult(firstResult);
            return this;
        }

        public boolean isReadOnly(){
            return query.isReadOnly();
        }

        public HibQuery setReadOnly(boolean readOnly){
            query.setReadOnly(readOnly);
            return this;
        }

        public HibQuery setCacheable(boolean cacheable){
            query.setCacheable(cacheable);
            return this;
        }

        public HibQuery setCacheRegion(String cacheRegion){
            query.setCacheRegion(cacheRegion);
            return this;
        }

        public HibQuery setTimeout(int timeout){
            query.setTimeout(timeout);
            return this;
        }

        public HibQuery setFetchSize(int fetchSize){
            query.setFetchSize(fetchSize);
            return this;
        }

        public HibQuery setLockOptions(LockOptions lockOptions){
            query.setLockOptions(lockOptions);
            return this;
        }

        public HibQuery setLockMode(String alias, LockMode lockMode){
            query.setLockMode(alias, lockMode);
            return this;
        }

        public HibQuery setComment(String comment){
            query.setComment(comment);
            return this;
        }

        public HibQuery setFlushMode(FlushMode flushMode){
            query.setFlushMode(flushMode);
            return this;
        }

        public HibQuery setCacheMode(CacheMode cacheMode){
            query.setCacheMode(cacheMode);
            return this;
        }

        public HibQuery setParameter(int position, Object val, Type type){
            query.setParameter(position, val, type);
            return this;
        }

        public HibQuery setParameter(String name, Object val, Type type){
            query.setParameter(name, val, type);
            return this;
        }

        public HibQuery setParameter(int position, Object val) throws HibernateException{
            query.setParameter(position, val);
            return this;
        }

        public HibQuery setParameter(String name, Object val) throws HibernateException{
            query.setParameter(name, val);
            return this;
        }

        public HibQuery setParameters(Object[] values, Type[] types) throws HibernateException{
            query.setParameters(values, types);
            return this;
        }

        public HibQuery setParameterList(String name, Collection vals, Type type) throws HibernateException{
            query.setParameterList(name, vals, type);
            return this;
        }

        public HibQuery setParameterList(String name, Collection vals) throws HibernateException{
            query.setParameterList(name, vals);
            return this;
        }

        public HibQuery setParameterList(String name, Object[] vals, Type type) throws HibernateException{
            query.setParameterList(name, vals, type);
            return this;
        }

        public HibQuery setParameterList(String name, Object[] vals) throws HibernateException{
            query.setParameterList(name, vals);
            return this;
        }

        public HibQuery setProperties(Object bean) throws HibernateException{
            query.setProperties(bean);
            return this;
        }

        public HibQuery setProperties(Map bean) throws HibernateException{
            query.setProperties(bean);
            return this;
        }

        public HibQuery setString(int position, String val){
            query.setString(position, val);
            return this;
        }

        public HibQuery setCharacter(int position, char val){
            query.setCharacter(position, val);
            return this;
        }

        public HibQuery setBoolean(int position, boolean val){
            query.setBoolean(position, val);
            return this;
        }

        public HibQuery setByte(int position, byte val){
            query.setByte(position, val);
            return this;
        }

        public HibQuery setShort(int position, short val){
            query.setShort(position, val);
            return this;
        }

        public HibQuery setInteger(int position, int val){
            query.setInteger(position, val);
            return this;
        }

        public HibQuery setLong(int position, long val){
            query.setLong(position, val);
            return this;
        }

        public HibQuery setFloat(int position, float val){
            query.setFloat(position, val);
            return this;
        }

        public HibQuery setDouble(int position, double val){
            query.setDouble(position, val);
            return this;
        }

        public HibQuery setBinary(int position, byte[] val){
            query.setBinary(position, val);
            return this;
        }

        public HibQuery setText(int position, String val){
            query.setText(position, val);
            return this;
        }

        public HibQuery setSerializable(int position, Serializable val){
            query.setSerializable(position, val);
            return this;
        }

        public HibQuery setLocale(int position, Locale locale){
            query.setLocale(position, locale);
            return this;
        }

        public HibQuery setBigDecimal(int position, BigDecimal number){
            query.setBigDecimal(position, number);
            return this;
        }

        public HibQuery setBigInteger(int position, BigInteger number){
            query.setBigInteger(position, number);
            return this;
        }

        public HibQuery setDate(int position, Date date){
            query.setDate(position, date);
            return this;
        }

        public HibQuery setTime(int position, Date date){
            query.setTime(position, date);
            return this;
        }

        public HibQuery setTimestamp(int position, Date date){
            query.setTimestamp(position, date);
            return this;
        }

        public HibQuery setCalendar(int position, Calendar calendar){
            query.setCalendar(position, calendar);
            return this;
        }

        public HibQuery setCalendarDate(int position, Calendar calendar){
            query.setCalendarDate(position, calendar);
            return this;
        }

        public HibQuery setString(String name, String val){
            query.setString(name, val);
            return this;
        }

        public HibQuery setCharacter(String name, char val){
            query.setCharacter(name, val);
            return this;
        }

        public HibQuery setBoolean(String name, boolean val){
            query.setBoolean(name, val);
            return this;
        }

        public HibQuery setByte(String name, byte val){
            query.setByte(name, val);
            return this;
        }

        public HibQuery setShort(String name, short val){
            query.setShort(name, val);
            return this;
        }

        public HibQuery setInteger(String name, int val){
            query.setInteger(name, val);
            return this;
        }

        public HibQuery setLong(String name, long val){
            query.setLong(name, val);
            return this;
        }

        public HibQuery setFloat(String name, float val){
            query.setFloat(name, val);
            return this;
        }

        public HibQuery setDouble(String name, double val){
            query.setDouble(name, val);
            return this;
        }

        public HibQuery setBinary(String name, byte[] val){
            query.setBinary(name, val);
            return this;
        }

        public HibQuery setText(String name, String val){
            query.setText(name, val);
            return this;
        }

        public HibQuery setSerializable(String name, Serializable val){
            query.setSerializable(name, val);
            return this;
        }

        public HibQuery setLocale(String name, Locale locale){
            query.setLocale(name, locale);
            return this;
        }

        public HibQuery setBigDecimal(String name, BigDecimal number){
            query.setBigDecimal(name, number);
            return this;
        }

        public HibQuery setBigInteger(String name, BigInteger number){
            query.setBigInteger(name, number);
            return this;
        }

        public HibQuery setDate(String name, Date date){
            query.setDate(name, date);
            return this;
        }

        public HibQuery setTime(String name, Date date){
            query.setTime(name, date);
            return this;
        }

        public HibQuery setTimestamp(String name, Date date){
            query.setTimestamp(name, date);
            return this;
        }

        public HibQuery setCalendar(String name, Calendar calendar){
            query.setCalendar(name, calendar);
            return this;
        }

        public HibQuery setCalendarDate(String name, Calendar calendar){
            query.setCalendarDate(name, calendar);
            return this;
        }

        public HibQuery setEntity(int position, Object val){
            query.setEntity(position, val);
            return this;
        }

        public HibQuery setEntity(String name, Object val){
            query.setEntity(name, val);
            return this;
        }

        public HibQuery setResultTransformer(ResultTransformer transformer){
            query.setResultTransformer(transformer);
            return this;
        }
    }
    //endregion


    //region HibSqlQuery 做为 org.hibernate.SqlQuery的代理
    /**
     * 做为org.hibernate.SqlQuery的代理
     */
    public static class HibSqlQuery extends HibQuery{

        public HibSqlQuery(SQLQuery query) {
            super(query);
        }

        public SQLQuery getSqlQuery(){
            return (SQLQuery)query;
        }

        public HibSqlQuery addSynchronizedQuerySpace(String querySpace){
            ((SQLQuery)query).addSynchronizedQuerySpace(querySpace);
            return this;
        }

        public HibSqlQuery addSynchronizedEntityName(String entityName) throws MappingException{
            ((SQLQuery)query).addSynchronizedEntityName(entityName);
            return this;
        }

        public HibSqlQuery addSynchronizedEntityClass(Class entityClass) throws MappingException{
            ((SQLQuery)query).addSynchronizedEntityClass(entityClass);
            return this;
        }


        public HibSqlQuery setResultSetMapping(String name){
            ((SQLQuery)query).setResultSetMapping(name);
            return this;
        }

        public HibSqlQuery addScalar(String columnAlias){
            ((SQLQuery)query).addScalar(columnAlias);
            return this;
        }

        public HibSqlQuery addScalar(String columnAlias, Type type){
            ((SQLQuery)query).addScalar(columnAlias, type);
            return this;
        }

        public SQLQuery.RootReturn addRoot(String tableAlias, String entityName){
            return ((SQLQuery)query).addRoot(tableAlias, entityName);
        }

        public SQLQuery.RootReturn addRoot(String tableAlias, Class entityType){
            return ((SQLQuery)query).addRoot(tableAlias, entityType);
        }

        public HibSqlQuery addEntity(String entityName){
            ((SQLQuery)query).addEntity(entityName);
            return this;
        }

        public HibSqlQuery addEntity(String tableAlias, String entityName){
            ((SQLQuery)query).addEntity(tableAlias, entityName);
            return this;
        }

        public HibSqlQuery addEntity(String tableAlias, String entityName, LockMode lockMode){
            ((SQLQuery)query).addEntity(tableAlias, entityName, lockMode);
            return this;
        }

        public HibSqlQuery addEntity(Class entityType){
            ((SQLQuery)query).addEntity(entityType);
            return this;
        }

        public HibSqlQuery addEntity(String tableAlias, Class entityType){
            ((SQLQuery)query).addEntity(tableAlias, entityType);
            return this;
        }

        public HibSqlQuery addEntity(String tableAlias, Class entityName, LockMode lockMode){
            ((SQLQuery)query).addEntity(tableAlias, entityName, lockMode);
            return this;
        }

        public SQLQuery.FetchReturn addFetch(String tableAlias, String ownerTableAlias, String joinPropertyName){
            return addFetch(tableAlias, ownerTableAlias, joinPropertyName);
        }

        public HibSqlQuery addJoin(String tableAlias, String path){
            ((SQLQuery)query).addJoin(tableAlias, path);
            return this;
        }

        public HibSqlQuery addJoin(String tableAlias, String ownerTableAlias, String joinPropertyName){
            ((SQLQuery)query).addJoin(tableAlias, ownerTableAlias, joinPropertyName);
            return this;
        }

        public HibSqlQuery addJoin(String tableAlias, String path, LockMode lockMode){
            ((SQLQuery)query).addJoin(tableAlias, path, lockMode);
            return this;
        }
    }
    //endregion

}
