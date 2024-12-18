package hbb.istudy.com.Spring笔记.手写Spring.V1.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author binbin
 * @Date 2024 12 17 11 21
 **/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HService {
    String value() default "";
}
