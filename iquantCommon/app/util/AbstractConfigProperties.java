package util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 12-12-11
 * Time: 下午1:31
 * 功能描述: 一些全局配置加载 key-value,实现类QicConfigPropties
 */
public abstract class AbstractConfigProperties {

    protected static Map<String,Object> map = new ConcurrentHashMap<String, Object>();

    public abstract void load();

    public static <T> T get(String key,Class<T> clazz){
        return (T)map.get(key);
    }
    public static int getInt(String key){
        return get(key,Integer.class);
    }
    public static boolean  getBoolean(String key){
        return get(key,Boolean.class);
    }
    public static long getLong(String key){
        return get(key,Long.class);
    }
    public static String getString(String key){
        return get(key,String.class);
    }
    public static void set(String key ,String value){
        map.put(key,value);
    }


}
