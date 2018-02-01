package org.com.yzh.framework.springmvc.annotation;

import java.lang.annotation.*;

/**
 * @ClassName: org.com.yzh.framework.springmvc.annotation
 * @Description:
 * @Author: yin.zhh
 * @Date 2018/1/23 10:36
 * @Version v.1.0.0
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Param {
    String value() default "";
}
