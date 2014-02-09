package util;

import play.Logger;
import play.libs.F;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: 刘志
 * Date: 12-12-14
 * Time: 下午2:57
 */
public class MessageBuilder {
    private final String KEY_REGEX = "\\[\\$(\\w+)\\.(\\w+)]";
    private final String KEY_FORMAT = "[$%s.%s]";

    private String messageTemplate;

    private Map<String, Object> parameters = new HashMap<String, Object>();

    private HashSet<F.T2<String, String>> templateKeySet = new HashSet<F.T2<String, String>>();

    public MessageBuilder(String message) {
        messageTemplate = message;
    }

    public void addParameter(String name, Object value) throws Exception {
        if(parameters.containsKey(name))
            throw new Exception("name is in parameter map.");
        parameters.put(name, value);
    }

    public void updateParameter(String name, Object value) throws Exception {
        if(!parameters.containsKey(name))
            throw new Exception("name not found in parameter map.");
        parameters.put(name, value);
    }

    public String execute() {
        Pattern pattern = Pattern.compile(KEY_REGEX);
        Matcher matcher = pattern.matcher(messageTemplate);
        //提取参数
        while(matcher.find()) {
            //System.out.printf("%s matched %d, first is '%s'.\n", KEY_REGEX, matcher.groupCount(), matcher.group(2));
            int groupCount = matcher.groupCount();
            if(groupCount != 2)
                continue;
            templateKeySet.add(F.T2(matcher.group(1), matcher.group(2)));
        }
        String result = messageTemplate;
        //处理参数并替换
        for(F.T2<String, String> item : templateKeySet) {
            Object parameterInstanse = parameters.get(item._1);
            if(parameterInstanse == null)
                continue;
            Object parameterValue;
            try{
               // Method[] methods = parameterInstanse.getClass().getMethods();
               // for(Method method : methods) {
               //     System.out.printf("method:%s\n", method.getName());
               //   }
                //Field parameterGetField = parameterInstanse.getClass().getField(item._2);
                //parameterValue = (String)parameterGetField.get(parameterInstanse);
                Method method = parameterInstanse.getClass().getMethod("get" + item._2);
                parameterValue = (Object)method.invoke(parameterInstanse);
            } catch (Exception e){
                Logger.warn(e, "try get message field error.");
                continue;
            }
            if(parameterValue != null)
                result = result.replace(String.format(KEY_FORMAT, item._1, item._2), parameterValue.toString());
        }
        return result;
    }
}
