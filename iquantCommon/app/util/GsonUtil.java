package util;

import com.google.gson.*;
import org.hibernate.proxy.HibernateProxy;
import play.data.validation.Error;

import java.util.HashSet;
import java.util.List;

/**
 * 这里放一下Gson常用的方法. 如关于gson的一些定制方法
 * User: wenzhihong
 * Date: 12-11-11
 * Time: 下午1:24
 */
public abstract class GsonUtil {
    public static Gson createGson() {
        return new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
    }

    //返回一个gson, 指定日期的格式
    public static Gson createGson(String dateFormate) {
        return new GsonBuilder().setDateFormat(dateFormate).create();
    }

    public static Gson createWithoutNulls(){
        return new GsonBuilder().setDateFormat("yyyy-MM-dd").excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
    }

    public static Gson createWithoutNullsDisableHtmlEscaping(){
        return new GsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
    }

    /**
     * 转化成gson. 不包含hibernate的代理对象
     *
     * @return
     */
    public static String toJsonWithOutHibernateProxy(Object o) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setExclusionStrategies(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                return false;
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                if (HibernateProxy.class.isAssignableFrom(clazz)) {
                    return true;
                }
                return false;
            }
        });
        return gsonBuilder.create().toJson(o);
    }


    /**
     * 把验证的Error转化成json 字符串
     *
     * @param errors
     * @return
     */
    public static String validationErrorToJson(List<Error> errors) {
        JsonObject root = new JsonObject();
        JsonArray errorArr = new JsonArray();
        for (Error err : errors) {
            JsonObject item = new JsonObject();
            item.addProperty("key", err.getKey());
            item.addProperty("msg", err.message());
            errorArr.add(item);
        }
        root.add("validateErrors", errorArr);

        return createGson().toJson(root);
    }

    /**
     * 把列出的属性加入到生成的json中
     *
     * @param o
     * @param properties
     * @return
     */
    public static String toJsonIncludeProperties(Object o, String... properties) {
        Gson gson = new GsonBuilder().setExclusionStrategies(new PropertiesInclude(properties)).create();
        return gson.toJson(o);
    }

    static class PropertiesInclude implements ExclusionStrategy {
        HashSet<String> includeProSet = null;

        public PropertiesInclude(String[] pros) {
            includeProSet = new HashSet<String>(pros.length);
            for (String s : pros) {
                includeProSet.add(s);
            }
        }

        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            String name = f.getName();
            return !includeProSet.contains(name);
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }
}
