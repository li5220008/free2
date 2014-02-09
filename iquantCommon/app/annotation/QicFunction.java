package annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 12-12-25
 * Time: 下午1:34
 * 功能描述: 标识方法的权限
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface QicFunction {
  long id() ;
  long[] dependencies() default {};
}
