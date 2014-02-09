package util;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Date;

/**
 * HighChart 的数据类型,因为highChart里的数据跟标准里的json日期类不一样. 为了更好利用gson,所以提出这个类
 * User: wenzhihong
 * Date: 12-11-19
 * Time: 上午8:31
 */
public class HighChartDataType {
    /**
     * 按 HighChart 的数据类型转化成 json. 主要是用于画图的
     */
    public static String toJsonWithHighChartDataType(Object o) {
        Gson gson = new GsonBuilder().registerTypeAdapter(HCDate.class, new JsonSerializer<HCDate>() {
            public JsonElement serialize(HCDate src, Type typeOfSrc, JsonSerializationContext context) {
                return new JsonPrimitive(new HCDateNumber(src.getAsString()));
            }
        }).create();

        return gson.toJson(o);
    }

    /**
     * HighChart 的日期类型
     */
    public static class HCDate {
        String str;

        public HCDate(Date d) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(d);
            StringBuilder sb = new StringBuilder("Date.UTC(");
            sb.append(calendar.get(Calendar.YEAR)).append(",")
                    .append(calendar.get(Calendar.MONTH)).append(",")
                    .append(calendar.get(Calendar.DATE)).append(")");
            str = sb.toString();
        }

        public String getAsString() {
            return str;
        }
    }

    /**
     * 为了gson输出 HighChart 的日期类型而设置的 number 类. 因为只有 number 输出才没有引号
     */
    public static class HCDateNumber extends Number {
        private static final RuntimeException e = new RuntimeException("不支持");
        String asStr;

        public HCDateNumber(String str) {
            asStr = str;
        }

        @Override
        public int intValue() {
            throw e;
        }

        @Override
        public long longValue() {
            throw e;
        }

        @Override
        public float floatValue() {
            throw e;
        }

        @Override
        public double doubleValue() {
            throw e;
        }

        @Override
        public String toString() {
            return asStr;
        }
    }
}
