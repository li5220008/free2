package dbutils;

import com.google.common.collect.Lists;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.RowProcessor;
import org.apache.commons.dbutils.handlers.BeanMapHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-8-19
 * Time: 下午3:31
 * 功能描述:
 */
public class MutilMapHandler<K,V> extends BeanMapHandler<K, List<V>> {
    private RowProcessor rowProcessor = new BasicRowProcessor();
    private  Class<V> type = null;
    public MutilMapHandler(Class<V> type){
        super(null);
        this.type = type;
    }
    public MutilMapHandler(Class<V> type, RowProcessor convert){
                super(null,convert);
                this.type = type;
    }
    public MutilMapHandler(Class<V> type, int columnIndex){
        super(null,columnIndex);
        this.type = type;
    }
    public MutilMapHandler(Class<V> type, String columnName){
        super(null,columnName);
        this.type = type;
    }
    @Override
    public Map<K, List<V>> handle(ResultSet rs) throws SQLException {
        Map<K, List<V>> result = new HashMap<K, List<V>>();

        while (rs.next()) {
            V value = createValue(rs);
            K key = createKey(rs);
            if(result.containsKey(key)){
                result.get(key).add(createValue(rs));
            }else{
                List<V> list = Lists.newArrayList();
                list.add(value);
            }
            result.put(createKey(rs), createRow(rs));
        }
        return result;
    }
    public V createValue(ResultSet rs) throws SQLException {
        return rowProcessor.toBean(rs,type);
    }



}
