package org.com.yzh.framework.springmvc.util;

import java.lang.reflect.Method;
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


    public Handler(Object contorller, Method method, Pattern pattern, Map<String, Integer> paramIndexMaping) {
        this.contorller = contorller;
        this.method = method;
        this.pattern = pattern;
        this.paramIndexMaping = paramIndexMaping;
    }

    public Object getContorller() {
        return contorller;
    }

    public Method getMethod() {
        return method;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public Map<String, Integer> getParamIndexMaping() {
        return paramIndexMaping;
    }

}
