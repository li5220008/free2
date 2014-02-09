package dbutils;

import com.google.common.collect.Lists;
import dbutils.spring.*;
import play.Play;
import play.libs.F;

import java.util.*;

/**
 * 占位符格式的sql的统一对外接口工具. 把 dbutils.spring 包下面的类屏蔽掉
 * User: wenzhihong
 * Date: 13-4-28
 * Time: 下午4:37
 */
public class NameParameterFacade {
    /**
     * Default maximum number of entries for this template's SQL cache: 256
     */
    public static final int DEFAULT_CACHE_LIMIT = 256;

    private volatile int cacheLimit = DEFAULT_CACHE_LIMIT;

    /**
     * Cache of original SQL String to ParsedSql representation
     */
    @SuppressWarnings("serial")
    private final Map<String, ParsedSql> parsedSqlCache =
            new LinkedHashMap<String, ParsedSql>(DEFAULT_CACHE_LIMIT, 0.75f, true) {
                @Override
                protected boolean removeEldestEntry(Map.Entry<String, ParsedSql> eldest) {
                    return size() > getCacheLimit();
                }
            };

    private String name;

    public NameParameterFacade(String name){
        this.name = name;
    }

    /**
     * Specify the maximum number of entries for this template's SQL cache.
     * Default is 256.
     */
    public void setCacheLimit(int cacheLimit) {
        this.cacheLimit = cacheLimit;
    }

    /**
     * Return the maximum number of entries for this template's SQL cache.
     */
    public int getCacheLimit() {
        return this.cacheLimit;
    }

    /**
     * Obtain a parsed representation of the given SQL statement.
     * <p>The default implementation uses an LRU cache with an upper limit
     * of 256 entries.
     *
     * @param sql the original SQL
     * @return a representation of the parsed SQL statement
     */
    protected ParsedSql getParsedSql(String sql) {
        if (Play.mode == Play.mode.DEV) {
            return NamedParameterUtils.parseSqlStatement(sql);
        }

        if (getCacheLimit() <= 0) {
            return NamedParameterUtils.parseSqlStatement(sql);
        }
        synchronized (this.parsedSqlCache) {
            ParsedSql parsedSql = this.parsedSqlCache.get(sql);
            if (parsedSql == null) {
                parsedSql = NamedParameterUtils.parseSqlStatement(sql);
                this.parsedSqlCache.put(sql, parsedSql);
            }
            return parsedSql;
        }
    }

    /**
     * 获取 sql语句, 及展开的参数数组
     *
     * @return _1是sql语句, _2是参数数组
     */
    public F.T2<String, Object[]> getSqlAndParameters(String sql, SqlParameterSource paramSource) {
        ParsedSql parsedSql = getParsedSql(sql);
        String sqlToUse = NamedParameterUtils.substituteNamedParameters(parsedSql, paramSource);
        Object[] params = NamedParameterUtils.buildValueArray(parsedSql, paramSource, null);

        List<Object> paramList = Lists.newLinkedList();
        for (Object param : params) {
            if (param instanceof Collection) {
                Iterator entryIter = ((Collection) param).iterator();
                while (entryIter.hasNext()) {
                    Object entryItem = entryIter.next();
                    if (entryItem instanceof Object[]) {
                        Object[] expressionList = (Object[]) entryItem;
                        for (Object it : expressionList) {
                            paramList.add(it);
                        }
                    }else {
                        paramList.add(entryItem);
                    }
                }

            }else {
                paramList.add(param);
            }
        }
        return F.T2(sqlToUse, paramList.toArray());
    }

    public F.T2<String, Object[]> getSqlAndParameters(String sql, Object obj) {
            BeanPropertySqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(obj);
            return getSqlAndParameters(sql, sqlParameterSource);
    }

    public F.T2<String, Object[]> getSqlAndParameters(String sql, Map<String, ?> map) {
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource(map);
        return getSqlAndParameters(sql, sqlParameterSource);
    }
}
