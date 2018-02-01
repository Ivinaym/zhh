package org.com.yzh.framework.springmvc.annotation;

import java.lang.annotation.*;

/**
 * @ClassName: org.com.yzh.framework.springmvc.annotation
 * @Description:
 * @Author: yin.zhh
 * @Date 2018/1/29 10:47
 * @Version v.1.0.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Method {
    String value() default "";
}
