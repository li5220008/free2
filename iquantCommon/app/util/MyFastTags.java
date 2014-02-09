package util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import groovy.lang.Closure;
import models.iquantCommon.KeyValue;
import play.Logger;
import play.Play;
import play.mvc.Http;
import play.templates.FastTags;
import play.templates.GroovyTemplate;
import play.templates.JavaExtensions;
import play.utils.HTML;
import play.vfs.VirtualFile;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: wenzhihong
 * Date: 12-9-14
 * Time: 下午2:47
 */
public class MyFastTags extends FastTags {
    /**
     * 空值
     */
    public static final String EMPTY_VALUE = "--";

    public static final String ZERO_VALUE = "0";

    public static final String FLOAT_ZERO_VALUE = "0.00";

    static Map<String, List<KeyValue>> kvs;

    static {
        readKvConfig();
    }

    //#{kvoption 'yields' /}
    public static void _kvoption(Map<?, ?> args, Closure body, PrintWriter out,
                                 GroovyTemplate.ExecutableTemplate template, int fromLine) {

        String value = (String) args.get("arg");
        List<KeyValue> keyValueList = kvs.get(value);
        for (KeyValue kv : keyValueList) {
            out.print("<option value=\"");
            out.print(kv.k + "\">");
            out.print(kv.v);
            out.println("</option>");
        }
    }

    /**
     * 处理keyvalue的select的.
     * keyname key的名称, 这个要出现在 keyValueOptions.js 里
     * size    select 选择个数, 默认为1.
     *
     * #{kvselect 'yields'}
     * #{kvselect keyname:'yields', size:2}   select为2
     */
    public static void _kvselect(Map<?, ?> args, Closure body, PrintWriter out,
                                 GroovyTemplate.ExecutableTemplate template, int fromLine) {
        String keyname = (String) args.get("arg");
        if (keyname == null) {
            keyname = (String) args.get("keyname");
        }
        int size = (Integer) args.get("size") != null ? (Integer) args.get("size") : 1;
        String serializedAttrs = FastTags.serialize(args, "size", "keyname");

        out.println("<select " + serializedAttrs + " size=\"" + size + "\" " + ">");
        List<KeyValue> keyValueList = kvs.get(keyname);
        if (keyValueList != null) {
            for (KeyValue kv : keyValueList) {
                out.print("<option value=\"");
                out.print(kv.k + "\">");
                out.print(kv.v);
                out.println("</option>");
            }
        }
        out.println("</select>");

    }

    /**
     * 用于排序符号
     * sortName : 排序字段
     * fieldName : 当前字段
     * flag : 排序方向
     */
    public static void _sortTagShow(Map<?, ?> args, Closure body, PrintWriter out, GroovyTemplate.ExecutableTemplate template, int fromLine) {
        String sortName = (String) args.get("sortName");
        String fieldName = (String) args.get("fieldName");
        int flag = ((Integer)args.get("flag")).intValue();

        if(fieldName.equalsIgnoreCase(sortName)){
            if(flag == 0){
                out.print("↑");
            }else{
                out.print("↓");
            }
        }
    }

    /**
     * 用于取字段规定长度
     * v : 排序字段
     * l:长度
     */
    public static void _subString(Map<?, ?> args, Closure body, PrintWriter out, GroovyTemplate.ExecutableTemplate template, int fromLine) {
        String v = (String) args.get("v");
        int  l = ((Integer)args.get("l")).intValue();
        String m="";
        if(v.length()>l){
            m=v.substring(0,l);
            m+="…";
            out.print(m);
        }else{
            out.print(v);
        }

    }

    /**
     * 处理空值,及数字跟日期的格式化
     * 默认做tostring的处理.
     * 包含参数: v   值
     * f  可选 格式化字符串. 注意这里可以是格式化数字或日期类型. 注意相应类型的格式化字符串. 如 数字 #,##0.00
     * d  默认值. 跟v的类型要匹配
     * u  单位(后缀)  字符串
     */
    public static void _emVF(Map<?, ?> args, Closure body, PrintWriter out, GroovyTemplate.ExecutableTemplate template, int fromLine) {
        Object v = args.get("arg");
        if (v == null) {
            v = args.get("v");
        }
        final String f = (String) args.get("f"); //格式化字符串
        final Object d = args.get("d"); //默认值
        final String u = args.get("u") == null ? "" : (String) args.get("u");

        if (v == null) {
            if (d == null) {
                out.print(EMPTY_VALUE);
            } else {
                objFormate(out, d, f, u);
            }
            return;
        }

        objFormate(out, v, f, u);
    }

    /**
     * 重写emVF方法,如果为空,用0表示
     * @param args
     * @param body
     * @param out
     * @param template
     * @param fromLine
     */
    public static void _emFM(Map<?, ?> args, Closure body, PrintWriter out, GroovyTemplate.ExecutableTemplate template, int fromLine) {
        Object v = args.get("arg");
        if (v == null) {
            v = args.get("v");
        }
        final String f = (String) args.get("f"); //格式化字符串
        final Object d = args.get("d"); //默认值
        final String u = args.get("u") == null ? "" : (String) args.get("u");

        if (v == null) {
            if (d == null) {
                out.print(ZERO_VALUE);
            } else {
                objZeroF2(out, d, f, u);
            }
            return;
        }

        objZeroF2(out, v, f, u);
    }


    public static void _hasAuth(Map<?, ?> args, Closure body, PrintWriter out, GroovyTemplate.ExecutableTemplate template, int fromLine) {
        long uid = Http.Request.current().params.get("uid",Long.class);
        long id = Long.valueOf((String)args.get("arg"));
        body.call();
     /*   if(FunctionAuthManager.auth(id,uid)){
            body.call();
        }else{
            // do  nothing
        }*/
    }

    private static void objFormate(PrintWriter out, Object arg, String f, String u) {
        if (arg instanceof Number) { //处理数字
            Number val = (Number) arg;

            if (f == null) {
                out.print(val.toString());
                out.print(u);
            } else if ((val instanceof Float
                    || val instanceof Double
                    || val instanceof BigDecimal)
                    && Math.abs(val.doubleValue()) < 1e-10) {

                out.print(EMPTY_VALUE);
            } else if ((val instanceof Long
                    || val instanceof Integer
                    || val instanceof Short
                    || val instanceof Byte
                    || val instanceof BigInteger)
                    && val.longValue() == 0) {

                out.print(EMPTY_VALUE);
            } else {
                String result = "";
                DecimalFormat df = new DecimalFormat(f);
                df.setRoundingMode(RoundingMode.HALF_UP); //四舍五入
                result = df.format(val);
                out.print(result);
                out.print(u);
            }
            return;
        }

        if (arg instanceof Date) {
            Date val = (Date) arg;
            if (f == null) {
                out.print(val.toString());
            } else {
                out.print(JavaExtensions.format(val, f));
            }
            out.print(u);
            return;
        }

        out.print(HTML.htmlEscape(arg.toString()));
        out.print(u);
    }


    private static void objZeroF(PrintWriter out, Object arg, String f, String u) {
        if (arg instanceof Number) { //处理数字
            Number val = (Number) arg;
            if (f == null) {
                out.print(val.toString());
                out.print(u);
            }else {
                out.print(JavaExtensions.format(val, f));
                out.print(u);
            }
            return;
        }

        if (arg instanceof Date) {
            Date val = (Date) arg;
            if (f == null) {
                out.print(val.toString());
            } else {
                out.print(JavaExtensions.format(val, f));
            }
            out.print(u);
            return;
        }

        out.print(HTML.htmlEscape(arg.toString()));
        out.print(u);
    }

    // objZeroF() 第二代 加入页面样式控制
    private static void objZeroF2(PrintWriter out, Object arg, String f, String u) {
        String startLabel = "<strong>";
        String endLabel = "</strong>";
        if (arg instanceof Number) { //处理数字
            Number val = (Number) arg;
            if (f == null) {
                out.print(startLabel+val.toString());
                out.print(u);
            }else {
                out.print(startLabel+JavaExtensions.format(val, f));
                out.print(u+endLabel);
            }
            return;
        }

        if (arg instanceof Date) {
            Date val = (Date) arg;
            if (f == null) {
                out.print(startLabel+val.toString());
            } else {
                out.print(startLabel+JavaExtensions.format(val, f));
            }
            out.print(u+endLabel);
            return;
        }

        out.print(startLabel+HTML.htmlEscape(arg.toString()));
        out.print(u+endLabel);
    }

    private static Map<String, List<KeyValue>> readKvConfig() {
        VirtualFile virtualFile = Play.getVirtualFile("conf/keyValueOptions.js");
        if(virtualFile == null){ //如果不存在
            Logger.warn("不存在key/value conf/keyValueOptions.js 配置文件, 返回空map. please check if you don't having conf/keyValueOptions.js file");
            Map<String, List<KeyValue>> map = new HashMap<String, List<KeyValue>>();
            kvs = map;
            return map;
        }
        String json =  CommonUtils.readJsonConfigFile2String(virtualFile.inputstream());
        Gson gson = new Gson();
        Map<String, List<KeyValue>> map = gson.fromJson(json, new TypeToken<Map<String, List<KeyValue>>>() {
        }.getType());
        kvs = map;
        return map;
    }


    /**
     * 处理空值,及数字跟日期的格式化
     * 默认做tostring的处理.
     * 包含参数: v   值
     * f  可选 格式化字符串. 注意这里可以是格式化数字或日期类型. 注意相应类型的格式化字符串. 如 数字 #,##0.00
     * d  默认值. 跟v的类型要匹配
     * u  单位(后缀)  字符串
     * c  Boolean 可选 默认false：粗体，true：粗体，红涨绿跌
     * l  Boolean 是否限制小数点 可选 默认false：true则进行限制规则：(当传入的数值的绝对值在 100~1000位时只保留一位小数点 当大于等于1000时不保留小数点)
     * s  Boolean 可选 默认true: 粗体显示，false:不用加粗 (是否粗体显示字体？)
     *
     */
    public static void _emVFC(Map<?, ?> args, Closure body, PrintWriter out, GroovyTemplate.ExecutableTemplate template, int fromLine ) {
        Object v = args.get("arg");
        if (v == null) {
            v = args.get("v");
        }
        final String f = (String) args.get("f"); //格式化字符串
        final Object d = args.get("d"); //默认值
        final String u = args.get("u") == null ? "" : (String) args.get("u");
        final Boolean c = (Boolean)args.get("c")== null ? false : true;
        final Boolean l = (Boolean)args.get("l")== null ? false : true;
        final Boolean s = (Boolean)args.get("s")== null ? true : false;

        if (v == null) {
            if (d == null) {
                out.print(EMPTY_VALUE);
            } else {
                objFormateC(out, d, f, u, c,l,s);
            }
            return;
        }
        objFormateC(out, v, f, u, c,l,s);
    }

    //加入了样式控制
    private static void objFormateC(PrintWriter out, Object arg, String f, String u,Boolean c,Boolean l,Boolean s) {
        String startlabel = "<Strong>";
        String startLabel1 = "<Strong style='color:red'>";
        String startLabel2 = "<Strong style='color:green'>";
        String endLabel ="</Strong>";
        if(!s){//如果不需要以粗体显示则用<span>标签取代<strong> (应用场景：后台策略详情中的绩效指标)
            startlabel = "<span>";
            startLabel1 = "<span style='color:red'>";
            startLabel2 = "<span style='color:green'>";
            endLabel = "</span>";
        }
        if (arg instanceof Number) { //处理数字
            Number val = (Number) arg;
            if(((Number) val).floatValue()>=0){
                if(c){
                    startlabel = startLabel1;
                }
            }else {
                if(c){
                    startlabel = startLabel2;
                }
            }
            if (f == null) {
                out.print(startlabel+val.toString());
                out.print(u+endLabel);
            } else if ((val instanceof Float
                    || val instanceof Double
                    || val instanceof BigDecimal)
                    && Math.abs(val.doubleValue()) < 1e-10) {

                out.print(startlabel+FLOAT_ZERO_VALUE);
                out.print(u+endLabel);
            } else if ((val instanceof Long
                    || val instanceof Integer
                    || val instanceof Short
                    || val instanceof Byte
                    || val instanceof BigInteger)
                    && val.longValue() == 0) {

                out.print(startlabel+FLOAT_ZERO_VALUE);
                out.print(u+endLabel);
            } else {
                //当传入的数值的绝对值在 100~1000位时只保留一位小数点 当大于等于1000时不保留小数点
                if(l){
                    double dV = Math.abs(val.doubleValue());
                    if(dV >=100 && dV<1000){
                        f = f.substring(0,f.indexOf(".")+2);
                    }else if(dV >=1000){
                        f = f.substring(0,f.indexOf("."));
                    }
                }

                out.print(startlabel+JavaExtensions.format(val, f));
                out.print(u+endLabel);
            }
            return;
        }

        if (arg instanceof Date) {
            Date val = (Date) arg;
            if (f == null) {
                out.print(startlabel+val.toString());
            } else {
                out.print(startlabel+JavaExtensions.format(val, f));
            }
            out.print(u+endLabel);
            return;
        }
        if (arg instanceof String) {
            String val = (String) arg;
            if("多开".equals(val) || "多平".equals(val)){
                if(c){
                    startlabel = startLabel1;
                }
            }else {
                if(c){
                    startlabel = startLabel2;
                }
            }
            if (f == null) {
                out.print(startlabel+val.toString());
            } else {
                out.print(startlabel+val);
            }
            out.print(u+endLabel);
            return;
        }

        out.print(startlabel+HTML.htmlEscape(arg.toString()));
        out.print(u+endLabel);
    }


}
