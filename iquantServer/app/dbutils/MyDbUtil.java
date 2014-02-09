package dbutils;

import com.google.common.collect.Lists;
import org.apache.commons.dbutils.*;
import org.apache.commons.dbutils.handlers.*;
import play.Logger;
import play.classloading.enhancers.LVEnhancer;
import play.db.DB;
import play.db.jpa.JPA;
import play.exceptions.DatabaseException;
import play.exceptions.UnexpectedException;
import play.libs.F;
import play.templates.BaseTemplate;
import play.templates.TemplateLoader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static dbutils.DBTemplateName.QIC;
import static dbutils.DBTemplateName.QICORE;
import static dbutils.DBTemplateName.QSM;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-6-25
 * Time: 下午4:42
 * 功能描述:  数据库操作方法全写在这里
 */
public class MyDbUtil {
    //把数据库查询的行处理成 Map
    public static final RowProcessor MAP_ROW_PROCESSOR = new MapRowProcessor();

    public static final BeanProcessorWithModelId PLAY_BEAN_PROCESSOR = new BeanProcessorWithModelId();

    //处理一行. 加入处理playframework的model
    public static final RowProcessor ROW_PROCESSOR = new BasicRowProcessor(PLAY_BEAN_PROCESSOR);

    //默认数据库配制名, 这是一个常量, 不能变
    public final String dbConfName;
    public final QueryRunner queryRunner;
    public final DBTemplateName dbTemplateName;
    public final NameParameterFacade nameParameterFacade;

    public MyDbUtil() {
        this(DBTemplateName.QIC);
    }

    public MyDbUtil(DBTemplateName dbTemplateName) {
        this.dbTemplateName = dbTemplateName;
        this.dbConfName = dbTemplateName.dbConfigName;
        this.nameParameterFacade = new NameParameterFacade(dbConfName);
        this.queryRunner = new QueryRunner();
    }

    /**
     * 返回提取数据的数据库连接
     */
    public  Connection getDBConnection() {
        return DB.getDBConfig(dbConfName, false).getConnection();
    }

    public  Connection getConnection() {
        return DB.getDBConfig(dbConfName, false).getConnection();
    }

    /**
     * 在提取数据的数据库上执行sql. (一般是执行对数据库有更新的那种)
     */
    public  boolean execute4DB(String SQL) {
        return DB.getDBConfig(dbConfName, false).execute(SQL);
    }

    /**
     * 在提取数据的数据库上执行sql语句(查询类)
     */
    public  ResultSet executeQuery4DB(String SQL) {
        return DB.getDBConfig(dbConfName, false).executeQuery(SQL);
    }

    /**
     * 批量执行sql语句
     *
     * @param sql
     * @param params 这里是一个二维数组, 第二维记录的是要操作的数据
     * @return 每次受影响的记录条数
     */
    public  int[] batchQicDB(String sql, Object[][] params) {
        Connection conn = getConnection();
        try {
            return queryRunner.batch(conn, sql, params);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    /**
     * 可以执行sql语句. insert, update, delete
     *
     * @return 受sql语句影响的记录条数
     */
    public  int updateDB(String sql, Object... param) {
        Connection conn = getConnection();
        try {
            return queryRunner.update(conn, sql, param);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }


    //-----------------以下为上面方面的签名变化

    /**
     * 在提取数据的数据库上执行sql. (一般是执行对数据库有更新的那种)
     */
    public  boolean execute(String SQL) {
        return DB.getDBConfig(dbConfName, false).execute(SQL);
    }

    /**
     * 在提取数据的数据库上执行sql语句(查询类)
     */
    public  ResultSet executeQuery(String SQL) {
        return DB.getDBConfig(dbConfName, false).executeQuery(SQL);
    }

    /**
     * 查询sql返回单个bean. 如果没有的话, 返回null
     */
    public  <T> T querySingleBean(String sql, Class<T> cl, Object... params) {
        Connection conn = getConnection();
        ResultSetHandler<T> h = new BeanHandler<T>(cl, ROW_PROCESSOR);
        T t = null;
        try {
            t = queryRunner.query(conn, sql, h, params);
            return t;
        } catch (SQLException ex) {
            throw new DatabaseException(ex.getMessage(), ex);
        }
    }

    /**
     * 查询sql返回Bean list. 如果没有的话, 返回的list长度为0
     */
    public  <T> List<T> queryBeanList(String sql, Class<T> cl, Object... params) {
        Connection conn = getConnection();
        ResultSetHandler<List<T>> h = new BeanListHandler<T>(cl, ROW_PROCESSOR);
        try {
            return queryRunner.query(conn, sql, h, params);
        } catch (SQLException ex) {
            throw new DatabaseException(ex.getMessage(), ex);
        }
    }

    /**
     * 查询单条记录, 转成一个map. 注意, 这里的map的key值为小写的
     */
    public  Map<String, Object> querySingleMap(String sql, Object... params) {
        Connection conn = getConnection();
        ResultSetHandler<Map<String, Object>> h = new MapHandler(MAP_ROW_PROCESSOR);
        Map t = null;
        try {
            t = queryRunner.query(conn, sql, h, params);
        } catch (SQLException ex) {
            throw new DatabaseException(ex.getMessage(), ex);
        }
        return t;
    }

    /**
     * 用handler处理查询记录
     */
    public  <T> T queryWithHandler(String sql, ResultSetHandler<T> rsh, Object... params) {
        Connection conn = getConnection();
        try {
            return queryRunner.query(conn, sql, rsh, params);
        } catch (SQLException ex) {
            throw new DatabaseException(ex.getMessage(), ex);
        }
    }

    /**
     * 用于count语句的.只查总数
     */
    public  Long queryCount(String sql, Object... params) {
        Connection conn = getConnection();
        try {
            return queryRunner.query(conn, sql, new ScalarHandler<Long>(), params);
        } catch (SQLException ex) {
            throw new DatabaseException(ex.getMessage(), ex);
        }
    }

    /**
     * 查询多条记录, 转成list<map>. 注意, 这里的map的key值为小写的
     * 如果没有,则返回的list长度为0
     */
    public  List<Map<String, Object>> queryMapList(String sql, Object... params) {
        Connection conn = getConnection();
        ResultSetHandler<List<Map<String, Object>>> h = new MapListHandler(MAP_ROW_PROCESSOR);
        try {
            return queryRunner.query(conn, sql, h, params);
        } catch (SQLException ex) {
            throw new DatabaseException(ex.getMessage(), ex);
        }
    }

    /**
     * 批量执行sql语句
     *
     * @param sql
     * @param params 这里是一个二维数组, 第二维记录的是要操作的数据
     * @return 每次受影响的记录条数
     */
    public  int[] batch(String sql, Object[][] params) {
        Connection conn = getConnection();
        try {
            return queryRunner.batch(conn, sql, params);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    /**
     * 可以执行sql语句. insert, update, delete
     *
     * @param sql   sql语句
     * @param param sql语句里的参数
     * @return 受sql语句影响的记录条数
     */
    public  int update(String sql, Object... param) {
        Connection conn = getConnection();
        try {
            return queryRunner.update(conn, sql, param);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    /**
     * 用于insert 语句. 返回自动增长的id值. 失败则返回 Long.MIN_VALUE
     *
     * @param sql
     * @param param
     * @return
     */
    public  long insert(String sql, Object... param) {
        Connection conn = getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            queryRunner.fillStatement(pstmt, param);
            pstmt.executeUpdate();
            //返回自增加id
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getLong(1);
            }
            return Long.MIN_VALUE;
        } catch (SQLException ex) {
            throw new DatabaseException(ex.getMessage(), ex);
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(pstmt);
        }
    }

    /**
     * 批量插入. 返回每条语句的的自动增长id
     *
     * @param sql
     * @param params
     * @return
     */
    public  List<Long> batchInsert(String sql, Object[][] params) {
        Connection conn = getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            for (int i = 0; i < params.length; i++) {
                queryRunner.fillStatement(pstmt, params[i]);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            rs = pstmt.getGeneratedKeys();
            List<Long> idLists = Lists.newLinkedList();
            while (rs.next()) {
                idLists.add(rs.getLong(1));
            }
            return idLists;
        } catch (SQLException ex) {
            throw new DatabaseException(ex.getMessage(), ex);
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(pstmt);
        }
    }

    //-----------------以下处理含sql占位符的sql

    /**
     * 查询sql返回单个bean. 如果没有的话, 返回null
     */
    public  <T> T querySingleBeanWithNameParam(String id, Class<T> cl, Object paramObj) {
        if (paramObj instanceof Map) {
            return querySingleBeanWithNameParamMap(id, cl, (Map<String,Object>)paramObj);
        }
        String nameParamSql = renderSqlTemplate(id, paramObj);
        F.T2<String, Object[]> t2 = nameParameterFacade.getSqlAndParameters(nameParamSql, paramObj);
        return querySingleBean(t2._1, cl, t2._2);
    }

    public  <T> T querySingleBeanWithNameParamMap(String id, Class<T> cl, Map<String, Object> paramMap) {
        String nameParamSql = renderSqlTemplateFromMap(id, paramMap);
        F.T2<String, Object[]> t2 = nameParameterFacade.getSqlAndParameters(nameParamSql, paramMap);
        return querySingleBean(t2._1, cl, t2._2);
    }

    /**
     * 查询sql返回Bean list. 如果没有的话, 返回的list长度为0
     */
    public  <T> List<T> queryBeanListWithNameParam(String id, Class<T> cl, Object paramObj) {
        if (paramObj instanceof Map) {
            return queryBeanListWithNameParamMap(id, cl, (Map<String,Object>)paramObj);
        }
        String nameParamSql = renderSqlTemplate(id, paramObj);
        F.T2<String, Object[]> t2 = nameParameterFacade.getSqlAndParameters(nameParamSql, paramObj);
        return queryBeanList(t2._1, cl, t2._2);
    }

    /**
     * 查询sql返回Bean list. 如果没有的话, 返回的list长度为0
     */
    public  <T> List<T> queryBeanListWithNameParamMap(String id, Class<T> cl, Map<String, Object> paramMap) {
        String nameParamSql = renderSqlTemplateFromMap(id, paramMap);
        F.T2<String, Object[]> t2 = nameParameterFacade.getSqlAndParameters(nameParamSql, paramMap);
        return queryBeanList(t2._1, cl, t2._2);
    }

    /**
     * 查询单条记录, 转成一个map. 注意, 这里的map的key值为小写的
     */
    public  Map<String, Object> querySingleMapWithNameParam(String id, Object paramObj) {
        if (paramObj instanceof Map) {
            return querySingleMapWithNameParamMap(id, (Map<String, Object>)paramObj);
        }
        String nameParamSql = renderSqlTemplate(id, paramObj);
        F.T2<String, Object[]> t2 = nameParameterFacade.getSqlAndParameters(nameParamSql, paramObj);
        return querySingleMap(t2._1, t2._2);
    }

    /**
     * 查询单条记录, 转成一个map. 注意, 这里的map的key值为小写的
     */
    public  Map<String, Object> querySingleMapWithNameParamMap(String id, Map<String, Object> paramMap) {
        String nameParamSql = renderSqlTemplateFromMap(id, paramMap);
        F.T2<String, Object[]> t2 = nameParameterFacade.getSqlAndParameters(nameParamSql, paramMap);
        return querySingleMap(t2._1, t2._2);
    }

    /**
     * 用handler处理查询记录
     */
    public  <T> T queryWithHandlerWithNameParam(String id, ResultSetHandler<T> rsh, Object paramObj) {
        if (paramObj instanceof Map) {
            return queryWithHandlerWithNameParamMap(id, rsh, (Map<String, Object>)paramObj);
        }
        String nameParamSql = renderSqlTemplate(id, paramObj);
        F.T2<String, Object[]> t2 = nameParameterFacade.getSqlAndParameters(nameParamSql, paramObj);
        return queryWithHandler(t2._1, rsh, t2._2);
    }

    /**
     * 用handler处理查询记录
     */
    public  <T> T queryWithHandlerWithNameParamMap(String id, ResultSetHandler<T> rsh, Map<String, Object> paramMap) {
        String nameParamSql = renderSqlTemplateFromMap(id, paramMap);
        F.T2<String, Object[]> t2 = nameParameterFacade.getSqlAndParameters(nameParamSql, paramMap);
        return queryWithHandler(t2._1, rsh, t2._2);
    }

    /**
     * 用于count语句的.只查总数
     */
    public  Long queryCountWithNameParam(String id, Object paramObj) {
        if (paramObj instanceof Map) {
            return queryCountWithNameParamMap(id, (Map<String,Object>)paramObj);
        }
        String nameParamSql = renderSqlTemplate(id, paramObj);
        F.T2<String, Object[]> t2 = nameParameterFacade.getSqlAndParameters(nameParamSql, paramObj);
        return queryCount(t2._1, t2._2);
    }

    /**
     * 用于count语句的.只查总数
     */
    public  Long queryCountWithNameParamMap(String id, Map<String, Object> paramMap) {
        String nameParamSql = renderSqlTemplateFromMap(id, paramMap);
        F.T2<String, Object[]> t2 = nameParameterFacade.getSqlAndParameters(nameParamSql, paramMap);
        return queryCount(t2._1, t2._2);
    }


    /**
     * 查询多条记录, 转成list<map>. 注意, 这里的map的key值为小写的
     * 如果没有,则返回的list长度为0
     */
    public  List<Map<String, Object>> queryMapListWithNameParam(String id, Object paramObj) {
        if (paramObj instanceof Map) {
            return queryMapListWithNameParamMap(id, (Map<String, Object>)paramObj);
        }
        String nameParamSql = renderSqlTemplate(id, paramObj);
        F.T2<String, Object[]> t2 = nameParameterFacade.getSqlAndParameters(nameParamSql, paramObj);
        return queryMapList(t2._1, t2._2);
    }

    /**
     * 查询多条记录, 转成list<map>. 注意, 这里的map的key值为小写的
     * 如果没有,则返回的list长度为0
     */
    public  List<Map<String, Object>> queryMapListWithNameParamMap(String id, Map<String, Object> paramMap) {
        String nameParamSql = renderSqlTemplateFromMap(id, paramMap);
        F.T2<String, Object[]> t2 = nameParameterFacade.getSqlAndParameters(nameParamSql, paramMap);
        return queryMapList(t2._1, t2._2);
    }


    /**
     * 可以执行sql语句. insert, update, delete
     *
     * @return 受sql语句影响的记录条数
     */
    public int updateWithNameParam(String id, Object paramObj) {
        if (paramObj instanceof Map) {
            return updateWithNameParamMap(id, (Map<String, Object>) paramObj);
        }
        String nameParamSql = renderSqlTemplate(id, paramObj);
        F.T2<String, Object[]> t2 = nameParameterFacade.getSqlAndParameters(nameParamSql, paramObj);
        return update(t2._1, t2._2);
    }

    /**
     * 可以执行sql语句. insert, update, delete
     *
     * @return 受sql语句影响的记录条数
     */
    public  int updateWithNameParamMap(String id, Map<String, Object> paramMap) {
        String nameParamSql = renderSqlTemplateFromMap(id, paramMap);
        F.T2<String, Object[]> t2 = nameParameterFacade.getSqlAndParameters(nameParamSql, paramMap);
        return update(t2._1, t2._2);
    }

    public  int updateWithNameParamMap(String id, Map<String, Object> paramMap, Object sqlValObj) {
        String nameParamSql = renderSqlTemplateFromMap(id, paramMap);
        F.T2<String, Object[]> t2 = nameParameterFacade.getSqlAndParameters(nameParamSql, sqlValObj);
        return update(t2._1, t2._2);
    }

    /**
     * 可以批量执行sql语句. insert, update, delete
     *
     * @return 受sql语句影响的记录条数
     */
    public  int[] updateBatchWithNameParam(String id, Object renderObj, Object[] paramObj) {
        String nameParamSql = renderSqlTemplate(id, renderObj);
        Object[][] param = new Object[paramObj.length][];
        int i=-1,j=0;
        String sql = null;
        for(Object obj : paramObj){
            F.T2<String, Object[]> t2 = nameParameterFacade.getSqlAndParameters(nameParamSql, obj);
            if (sql == null) {
                sql = t2._1;
            }
            j=0;
            ++i;
            param[i] = new Object[t2._2.length];
            for(Object temp : t2._2){
                param[i][j++] = temp;
            }
        }
        return batch(sql, param);
    }


    /**
     * 用于insert 语句. 返回自动增长的id值. 失败则返回 Long.MIN_VALUE
     */
    public  long insertWithNameParam(String id, Object paramObj) {
        if (paramObj instanceof Map) {
            return insertWithNameParamMap(id, (Map<String, Object>) paramObj);
        }
        String nameParamSql = renderSqlTemplate(id, paramObj);
        F.T2<String, Object[]> t2 = nameParameterFacade.getSqlAndParameters(nameParamSql, paramObj);
        return insert(t2._1, t2._2);
    }

    /**
     * 用于insert 语句. 返回自动增长的id值. 失败则返回 Long.MIN_VALUE
     */
    public  long insertWithNameParamMap(String id, Map<String, Object> paramMap) {
        String nameParamSql = renderSqlTemplateFromMap(id, paramMap);
        F.T2<String, Object[]> t2 = nameParameterFacade.getSqlAndParameters(nameParamSql, paramMap);
        return insert(t2._1, t2._2);
    }

    /**
     * 用于insert 语句. 返回自动增长的id值. 失败则返回 Long.MIN_VALUE
     */
    public  List<Long> insertBatchWithNameParam(String id, Object renderObj, Object[] paramObj) {
        String nameParamSql = renderSqlTemplate(id, renderObj);
        Object[][] param = new Object[paramObj.length][];
        int i=-1,j=0;
        String sql = null;
        for(Object obj : paramObj){
            F.T2<String, Object[]> t2 = nameParameterFacade.getSqlAndParameters(nameParamSql, obj);
            if (sql == null) {
                sql = t2._1;
            }
            j=0;
            ++i;
            param[i] = new Object[t2._2.length];
            for(Object temp : t2._2){
                param[i][j++] = temp;
            }
        }

        return batchInsert(sql, param);
    }

    private static String renderSqlTemplateFromMap(String sqlId,Map<String,Object> map){
        String sqlSource = SqlLoader.getSqlById(sqlId);
        BaseTemplate template = TemplateLoader.load("sql_" + sqlId, sqlSource);
        String sql = template.render(map);
        if (Logger.isDebugEnabled()) {
            Logger.debug("从模板生成的sql:\n%s", sql);
        }
        return sql;
    }

    private static String renderSqlTemplate(String sqlId,Object ... args){
        Map<String, Object> templateBinding = new HashMap<String, Object>(4);
        Stack<LVEnhancer.MethodExecution> stack = LVEnhancer.LVEnhancerRuntime.getCurrentMethodParams();
        if (stack.size() > 0) {
            LVEnhancer.MethodExecution me = stack.get(stack.size() - 2).getCurrentNestedMethodCall();
            LVEnhancer.LVEnhancerRuntime.ParamsNames paramsNames = new LVEnhancer.LVEnhancerRuntime.ParamsNames(me.getSubject(), me.getParamsNames(), me.getVarargsNames());
            String[] names = paramsNames.varargs;
            if (args != null && args.length > 0 && names == null) {
                throw new UnexpectedException("no varargs names while args.length > 0 !");
            }
            for (int i = 0; i < args.length; i++) {
                templateBinding.put(names[i], args[i]);
            }
        }
        return renderSqlTemplateFromMap(sqlId,templateBinding);
    }


    //region 这些方法一般只用于编写单元测试时. 因为playframework会默认帮我们提交或回滚事务
    /**
     * 标记指定数据库 回滚事务
     */
    public static void dbRollbackOnly(DBTemplateName dbTemplateName) {
        JPA.getJPAConfig(dbTemplateName.dbConfigName).getJPAContext().em().getTransaction().setRollbackOnly();
    }

    /**
     * 标记qic数据库回滚事务
     */
    public static void qicRollbackOnly() {
        dbRollbackOnly(QIC);
    }

    /**
     * 标记qicore数据库回滚事务
     */
    public static void qicoreRollbackOnly() {
        dbRollbackOnly(QICORE);
    }

    /**
     * 标记qsm数据库回滚事务
     */
    public static void qsmRollbackOnly() {
        dbRollbackOnly(QSM);
    }

    /**
     * 标记所有数据库事务回滚
     */
    public static void allDbRollbackOnly() {
        EnumSet<DBTemplateName> dbTemplateNames = EnumSet.allOf(DBTemplateName.class);
        for (DBTemplateName dbTemplateName : dbTemplateNames) {
            dbRollbackOnly(dbTemplateName);
        }
    }

    /**
     * 指定数据库 回滚事务
     */
    public static void dbRollback(DBTemplateName dbTemplateName) {
        JPA.getJPAConfig(dbTemplateName.dbConfigName).getJPAContext().em().getTransaction().rollback();
    }

    /**
     * qic数据库回滚事务
     */
    public static void qicRollback() {
        dbRollback(QIC);
    }

    /**
     * qicore数据库回滚事务
     */
    public static void qicoreRollback() {
        dbRollback(QICORE);
    }

    /**
     * qsm数据库回滚事务
     */
    public static void qsmRollback() {
        dbRollback(QSM);
    }

    /**
     * 所有数据库事务回滚
     */
    public static void allDbdbRollback() {
        EnumSet<DBTemplateName> dbTemplateNames = EnumSet.allOf(DBTemplateName.class);
        for (DBTemplateName dbTemplateName : dbTemplateNames) {
            dbRollback(dbTemplateName);
        }
    }

    /**
     * 指定数据库提交
     *
     * @param dbTemplateName
     */
    public static void dbCommit(DBTemplateName dbTemplateName) {
        JPA.getJPAConfig(dbTemplateName.dbConfigName).getJPAContext().em().getTransaction().commit();
    }

    /**
     * qic数据库提交
     */
    public static void qicCommit() {
        dbCommit(QIC);
    }

    /**
     * qicore数据库提交
     */
    public static void qicoreCommit() {
        dbCommit(QICORE);
    }

    /**
     * qsm数据库提交
     */
    public static void qsmCommit() {
        dbCommit(QSM);
    }

    /**
     * 全部数据库提交
     */
    public static void allDBCommit() {
        EnumSet<DBTemplateName> dbTemplateNames = EnumSet.allOf(DBTemplateName.class);
        for (DBTemplateName dbTemplateName : dbTemplateNames) {
            dbCommit(dbTemplateName);
        }
    }
    //endregion

}
