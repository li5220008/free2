package util;

import play.classloading.enhancers.LVEnhancer;
import play.exceptions.UnexpectedException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 这里集中存放redis的key值, 强烈避免在代码中直接硬编码的方式.
 * 使用定义常量的方式的引用, 若外部系统要参考本系统定义了哪些redis key值时, 可只参考这个文件
 * User: wenzhihong
 * Date: 12-5-14
 * Time: 上午10:06
 */
public class RedisKeys {

    //region 定义redis的key值常量及说明信息
    /**
     * 存放这个mac地址登陆的 token
     * valueType : String
     */
    public static final String USER_MAC_TOKEN_KEY = "user.{mac}.token";

    //定义方便构造的方法, 这些方法定义只是想为了更好的性能, 如果偷懒的话, 可以使用下面的 caculateKey 方法
    public static String userMacTokenKey(String mac){
        return "user." + mac + ".token";
    }

    /**
     * 存放以这个 userName 做为登陆名的用户的 mac地址, 支持一个userName 可以从不同的机器登陆. 登陆个数的限制存放在 nt_userinfo 表里的 maxLogin 字段
     * valueType : set
     */
    public static final String USER_MAC_SET_KEY = "user.{userName}.macset";

    public static String userMacSetKey(String userName){
        return "user." + userName + ".macset";
    }
    //endregion


    //复杂的key使用下面的方法进行构造

    /**
     * 计算出key值, 模拟play的controller的猜测变量的名字
     * @param templateStr
     * @param args
     * @return
     */
    public static String caculateKey(String templateStr, Object... args){
        Map<String, Object> templateBinding = new HashMap<String, Object>(4);
        String[] names = LVEnhancer.LVEnhancerRuntime.getParamNames().varargs;
        if(args != null && args.length > 0 && names == null) {
            throw new UnexpectedException("no varargs names while args.length > 0 !");
        }
        for(int i = 0; i < args.length; i++) {
            templateBinding.put(names[i], args[i]);
        }

        return caculateKey(templateStr, templateBinding);
    }

    /**
     * 简单实现{}模板功能. 计算出key值
     * 如{aa} cc {bb} 其中 {aa}, {bb} 为占位符. 可用相关变量进行替换
     * @param templateStr 模板字符串
     * @param data     替换的变量值
     * @param defaultNullReplaceVals  默认null值替换字符, 如果不提供, 则为字符串""
     * @return 返回替换后的字符串, 如果模板字符串为null, 则返回null
     */
    @SuppressWarnings("unchecked")
    public static String caculateKey(String templateStr, Map<String, ?> data, String... defaultNullReplaceVals) {
        if(templateStr == null) return null;

        if(data == null) data = Collections.EMPTY_MAP;

        String nullReplaceVal = defaultNullReplaceVals.length > 0 ? defaultNullReplaceVals[0] : "";
        Pattern pattern = Pattern.compile("\\{([^}]+)}");

        StringBuffer newValue = new StringBuffer(templateStr.length());

        Matcher matcher = pattern.matcher(templateStr);

        while (matcher.find()) {
            String key = matcher.group(1);
            String r = data.get(key) != null ? data.get(key).toString() : nullReplaceVal;
            matcher.appendReplacement(newValue, r.replaceAll("\\\\", "\\\\\\\\")); //这个是为了替换windows下的文件目录在java里用\\表示
        }

        matcher.appendTail(newValue);

        return newValue.toString();
    }

    public static class UserFunctionCacheKey{
        public static final String USER_ROLE_PREFIX="ur_";
        public static final String ROLE_FUNCTION_PREFIX="rf_";
    }

    //测试方法
    public static void main(String[] args) {
        String tmpLine = "简历:\n 姓名: {姓} {名} \n 性别: {性别}\n 年龄: {年龄} \n";
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("姓", "wen");
        data.put("名", "66");
        data.put("性别", "man");
        data.put("年龄", "222");

        System.out.println(caculateKey(tmpLine, data, "--"));
    }

}
