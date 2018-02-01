package org.com.yzh.framework.springmvc.util;

import org.com.yzh.framework.springmvc.annotation.Param;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @ClassName: Handler
 * @Description: That's the purpose of the class
 * @Author: yin.zhh
 * @Date 2018-02-01 17:52
 * @Version v.1.0.0
 */
public class Handler {
    /**
     * 保存方法对应的实例
     */
    private Object contorller;
    /**
     * 保存映射的方法
     */
    private Method method;

    private Pattern pattern;
    /**
     * 参数顺序
     */
    private Map<String, Integer> paramIndexMaping;


    public Handler(Object contorller, Method method, Pattern pattern) {
        this.contorller = contorller;
        this.method = method;
        this.pattern = pattern;

        paramIndexMaping = new HashMap<>();

        putParamIndexMaping(method);
    }

    private void putParamIndexMaping(Method method) {
        /**
         * 提取方法加了注解的参数
         */
        Annotation[][] annotations = method.getParameterAnnotations();
        for (int i = 0; i < annotations.length; i++) {
            for (Annotation annotation : annotations[i]){
                if ( annotation instanceof Param) {

                }
            }
        }

    }
}
