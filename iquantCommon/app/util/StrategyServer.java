package util;

import models.iquantCommon.StrategyBaseinfo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-4-2
 * Time: 下午2:13
 * 功能描述:
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface StrategyServer {
    int id() default StrategyBaseinfo.QICORE_ENGINEE_ID;
    int serverType() default 0;//回测服务器
}
