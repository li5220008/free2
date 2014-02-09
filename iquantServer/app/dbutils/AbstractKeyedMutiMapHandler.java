package dbutils;

import com.google.common.collect.ListMultimap;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.RowProcessor;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 把记录集映射成使用 ListMultimap 做为结果集的
 * User: wenzhihong
 * Date: 12-11-24
 * Time: 下午12:54
 */
public abstract class AbstractKeyedMutiMapHandler<K, V> implements ResultSetHandler<ListMultimap<K, V>> {
    protected static final RowProcessor ROW_PROCESSOR = new BasicRowProcessor();

    @Override
    public ListMultimap<K, V> handle(ResultSet rs) throws SQLException {
        ListMultimap<K, V> result = createListMultimap();
        while (rs.next()){
            K key = createKey(rs);
            V value = createRow(rs);
            if(key != null){ //如果key为空值, 则不放入
                result.put(key, value);
            }

        }
        return result;
    }

    protected abstract V createRow(ResultSet rs)  throws SQLException;

    protected abstract K createKey(ResultSet rs)  throws SQLException;

    protected abstract ListMultimap<K, V> createListMultimap();
}
