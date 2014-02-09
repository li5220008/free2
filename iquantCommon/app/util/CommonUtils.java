package util;

import com.tom.springutil.NumberUtils;
import models.iquantCommon.SendMailDto;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.mail.HtmlEmail;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import play.Logger;
import play.classloading.enhancers.LVEnhancer;
import play.exceptions.UnexpectedException;
import play.libs.F;
import play.libs.Mail;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 常用工具类
 * User: wenzhihong
 * Date: 12-11-10
 * Time: 下午1:26
 */
public abstract class CommonUtils {
    public static final long HAS_SAME_NAME_ERROR = -1111111111;

    //-999 表示全部. 定义在 keyValueOptions.js 里. 所有的都用这个值
    public static final int SELECT_ALL_OPTION_VALUE = -999;

    public static final String[] DATE_FORMAT_STR_ARR = {"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss"};
    public static final SimpleDateFormat yyyyMMdd = new SimpleDateFormat(DATE_FORMAT_STR_ARR[0]);
    public static final SimpleDateFormat yyyyMMddhhMMss = new SimpleDateFormat(DATE_FORMAT_STR_ARR[1]);

    /**
     * 把字符串解析成Date. 支持的字符串格式(yyyy-MM-dd)跟(yyyy-MM-dd HH:mm:ss)
     */
    public static Date parseDate(String d) {
        try {
            return DateUtils.parseDate(d, DATE_FORMAT_STR_ARR);
        } catch (ParseException e) {
            throw new NestableRuntimeException(e);
        }
    }

    public static <T extends Number> T  parseNumber(String text, Class<T> targetClass, T defaultVal) {
        if (StringUtils.isBlank(text) || targetClass == null) {
            return defaultVal;
        }
        return NumberUtils.parseNumber(text, targetClass);
    }

    /**
     * 返回int数组里的最小值, 最大值.
     * _1 最小值
     * _2 最大值
     */
    public static F.T2<Number, Number> minMax(int[] arr) {
        if (arr.length == 1) {
            new F.T2(arr[0], arr[0]);
        }

        int minVal = Integer.MAX_VALUE;
        int maxVal = Integer.MIN_VALUE;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] < minVal) {
                minVal = arr[i];
            }
            if (arr[i] > maxVal) {
                maxVal = arr[i];
            }
        }

        return new F.T2(minVal, maxVal);
    }

    /**
     * 返回long数组里的最小值, 最大值.
     * _1 最小值
     * _2 最大值
     */
    public static F.T2<Number, Number> minMax(long[] arr) {
        if (arr.length == 1) {
            new F.T2(arr[0], arr[0]);
        }

        long minVal = Long.MAX_VALUE;
        long maxVal = Long.MIN_VALUE;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] < minVal) {
                minVal = arr[i];
            }
            if (arr[i] > maxVal) {
                maxVal = arr[i];
            }
        }

        return new F.T2(minVal, maxVal);
    }

    /**
     * 返回float数组里的最小值, 最大值.
     * _1 最小值
     * _2 最大值
     */
    public static F.T2<Number, Number> minMax(float[] arr) {
        if (arr.length == 1) {
            new F.T2(arr[0], arr[0]);
        }

        float minVal = Float.MAX_VALUE;
        float maxVal = Float.MIN_VALUE;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] < minVal) {
                minVal = arr[i];
            }
            if (arr[i] > maxVal) {
                maxVal = arr[i];
            }
        }

        return new F.T2(minVal, maxVal);
    }

    /**
     * 返回float数组里的最小值, 最大值.
     * _1 最小值
     * _2 最大值
     */
    public static F.T2<Number, Number> minMax(double[] arr) {
        if (arr.length == 1) {
            new F.T2(arr[0], arr[0]);
        }

        double minVal = Double.MAX_VALUE;
        double maxVal = Double.MIN_VALUE;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] < minVal) {
                minVal = arr[i];
            }
            if (arr[i] > maxVal) {
                maxVal = arr[i];
            }
        }

        return new F.T2(minVal, maxVal);
    }

    /**
     * 读取json配制文件,去掉注释. 以 //, /*, #, var  开头的, 都认为是注释. 注意,这里的注释是行注释
     * @param input
     * @return
     */
    public static String readJsonConfigFile2String(InputStream input){
        StringWriter writer = new StringWriter();
        try {
            LineIterator it = IOUtils.lineIterator(input, "UTF-8");
            while (it.hasNext()){
                String line = it.nextLine();
                String linePack = line.trim();
                if(linePack.startsWith("//") || linePack.startsWith("/*") || linePack.startsWith("#")
                        || linePack.startsWith("var ")){ //认为是注释,跳过
                    continue;
                }else{
                    writer.write(line);
                    writer.write(IOUtils.LINE_SEPARATOR);
                }
            }
        } catch (IOException e) {}
        finally {
            IOUtils.closeQuietly(input);
        }

        return writer.toString();
    }

    /**
     * 按 HighChart 的数据类型转化成 json. 主要是用于画图的
     */
    public static String toJsonWithHighChartDataType(Object o) {
        return HighChartDataType.toJsonWithHighChartDataType(o);
    }

    /**
     * 根据指定格式获取时间
     * @param     format 格式
     * @param     date 给定时间
     * @return     指定格式的时间
     */
    public static  String getFormatDate(String format,Date date){
        if(date==null){
            return "";

        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * Parse an inputstream to DOM
     * @return null if an error occurs during parsing.
     */
    public static Document getDocument(InputStream is) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            return dbf.newDocumentBuilder().parse(is);
        } catch (SAXException e) {
            Logger.warn("Parsing error when building Document object from inputstream'", e);
            e.printStackTrace();
        } catch (IOException e) {
            Logger.warn("Reading error when building Document object from inputstream '", e);
        } catch (ParserConfigurationException e) {
            Logger.warn("Parsing error when building Document object from inputstream '", e);
        }

        return null;
    }

    /**
     * 用一个字符串合并一个字符串数组成一个字符串
     * @param strArray 要合并的数组
     * @param splitStr 分割该字符串的字符串 默认是,
     * @return
     */
    public static String combineStrArrayToString(String strArray[], String splitStr){
        if(StringUtils.isBlank(splitStr)) splitStr = ",";
        StringBuilder stringBuilder = new StringBuilder();
        if(strArray.length >0){
            for(int i=0; i<strArray.length; i++){
                stringBuilder.append(strArray[i]);
                if(i<strArray.length-1) stringBuilder.append(",");
            }
        }
        return stringBuilder.toString();
    }
    public static void  test(Object...  args){
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
    }

    /**
     * 判断集合是否不为空. 空返回false, 不空返回true
     * @param c
     * @return
     */
    public static boolean isNotEmptyCollection(Collection c){
        return c != null && c.size() > 0;
    }

    /**
     * 判断map是否不为空. 空返回false, 不空返回true
     * @param m
     * @return
     */
    public static boolean isNotEmptyMap(Map m){
        return m != null && m.size() > 0;
    }


    //把List格式化为sql能识别的String
    public static String formatStr(List list){
        StringBuilder sb = new StringBuilder();
        if(list!=null&& list.size()!=0){
            for (Object s :list ) {
                sb.append(",'" + s + "'");
            }
            return   sb.substring(1);
        }
        return "";
    }

    /**
     *发送email
     */
    public static boolean sendMail(SendMailDto sendMailDto){
        boolean flag = true;
        if(sendMailDto!=null){
            HtmlEmail email = new HtmlEmail();
            email.setCharset("UTF-8");// 编码格式
            try {
                email.addTo(sendMailDto.accepterEmail);// 接收者
                email.setFrom(sendMailDto.sender, sendMailDto.name);// 发送者，姓名
                email.setSubject(sendMailDto.title);// 邮件标题
                email.setMsg(sendMailDto.content);// 发送内容
                Mail.send(email);
                Logger.info("接收邮件: "+sendMailDto.accepterEmail+" 发送成功!");
                Logger.info("发送邮件服务器: "+sendMailDto.sender);
                Logger.info("发送邮件名: "+sendMailDto.name);
            }catch (Exception e) {
                Logger.info("邮件: "+sendMailDto.accepterEmail+" 发送失败!");
                flag = false;
                e.printStackTrace();
            }
        }
        return flag;
    }
    /**
     *发送html email
     */
    public static boolean sendFAQMail(SendMailDto sendMailDto){
        boolean flag = true;
        if(sendMailDto!=null){
            HtmlEmail email = new HtmlEmail();
            email.setCharset("UTF-8");// 编码格式
            try {
                email.addTo(sendMailDto.accepterEmail);// 接收者
                email.setFrom(sendMailDto.sender, sendMailDto.name);// 发送者，姓名
                email.setSubject(sendMailDto.title);// 邮件标题
                email.setMsg(sendMailDto.content+
                        "\r\n"+
                        "------------------------------------------------------------------------------"+
                        "\r\n"+
                        "用户邮箱："+
                        sendMailDto.userEmail+
                        "\r\n"+
                        "------------------------------------------------------------------------------");
                Mail.send(email);
            }catch (Exception e) {
                flag = false;
                e.printStackTrace();
            }
        }
        return flag;
    }

}
