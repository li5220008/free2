package dbutils;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;
import org.apache.commons.dbutils.RowProcessor;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 指定key的列, 及每行映射成的bean类型
 * User: wenzhihong
 * Date: 12-11-24
 * Time: 下午1:00
 */
public class BeanMultiMapHandler<K,V> extends AbstractKeyedMutiMapHandler<K, V>{
    //估算大概有多少个key
    private int expectedKeys = 0;

    private final Class<V> type;

    private final RowProcessor convert;

    private final int columnIndex;

    private final String columnName;

    public BeanMultiMapHandler(Class<V> type) {
        this(type, ROW_PROCESSOR, 1, null, 0);
    }

    public BeanMultiMapHandler(Class<V> type, RowProcessor convert) {
        this(type, convert, 1, null, 0);
    }

    public BeanMultiMapHandler(Class<V> type, int columnIndex) {
        this(type, ROW_PROCESSOR, columnIndex, null, 0);
    }

    public BeanMultiMapHandler(Class<V> type, String columnName) {
        this(type, ROW_PROCESSOR, 1, columnName, 0);
    }

    public BeanMultiMapHandler(Class<V> type, int columnIndex, int expectedKeys) {
            this(type, ROW_PROCESSOR, columnIndex, null, expectedKeys);
    }

    public BeanMultiMapHandler(Class<V> type, String columnName, int expectedKeys) {
        this(type, ROW_PROCESSOR, 1, columnName, expectedKeys);
    }


    private BeanMultiMapHandler(Class<V> type, RowProcessor convert,
            int columnIndex, String columnName, int expectedKeys) {
        super();
        this.type = type;
        this.convert = convert;
        this.columnIndex = columnIndex;
        this.columnName = columnName;
        this.expectedKeys = expectedKeys;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected K createKey(ResultSet rs) throws SQLException {
        return (columnName == null) ?
               (K) rs.getObject(columnIndex) :
               (K) rs.getObject(columnName);
    }

    @Override
    protected V createRow(ResultSet rs) throws SQLException {
        return this.convert.toBean(rs, type);
    }


    @Override
    protected ListMultimap<K, V> createListMultimap() {
        if(expectedKeys <= 8){
            return LinkedListMultimap.create();
        }else{
            return LinkedListMultimap.create(expectedKeys);
        }

    }
}
