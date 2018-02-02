package org.com.yzh.framework.springmvc.util;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;


/**
 * @Description: That's the purpose of the class
 * @Author: yin.zhh
 * @Date 2018/2/2 10:31
 * @Version v.1.0.0
 */
public class BeanUtil {


    public static boolean invoke(HttpServletRequest req, HttpServletResponse resp, List<Handler> handlerMapping) throws Exception {

        //throw new Exception("这是一个假象，是我自己定义异常，弄着玩的");
        if (handlerMapping.isEmpty()) {
            return false;
        }

        String url = req.getRequestURI();
        String contextPath = req.getContextPath();
        url = url.replace(contextPath, "").replaceAll("/+", "/");


        for (Handler handler : handlerMapping) {
            Matcher matcher = handler.getPattern().matcher(url);

            if (!matcher.matches()) {
                continue;
            }

            Class<?>[] paramTypes = handler.getMethod().getParameterTypes();

            Object[] paramValues = new Object[paramTypes.length];

            Map params = req.getParameterMap();

            for (Object param : params.entrySet()) {

                Map.Entry<String, String[]> entry = (Map.Entry<String, String[]>)param;

                String value = Arrays.toString(entry.getValue()).replaceAll("[]\\[]", "").replaceAll(",\\s", ",");

                if (handler.getParamIndexMaping().containsKey(entry.getKey())) {

                    int index = handler.getParamIndexMaping().get(entry.getKey());
                    //涉及到类型转换
                    paramValues[index] = castStringValue(value, paramTypes[index]);
                }
            }
            int reqIndex = handler.getParamIndexMaping().get(HttpServletRequest.class.getName());
            paramValues[reqIndex] = req;

            int repIndex = handler.getParamIndexMaping().get(HttpServletResponse.class.getName());
            paramValues[repIndex] = resp;

            //需要对象.方法
            handler.getMethod().invoke(handler.getContorller(), paramValues);
            return true;
        }
        return false;

    }


    private static Object castStringValue(String value, Class<?> clazz) {

        if (clazz == String.class) {
            return value;
        } else if (clazz == Integer.class) {
            return Integer.valueOf(value);
        } else if (clazz == int.class) {
            return Integer.valueOf(value);
        } else {
            return null;
        }

    }

    public static String toLowerFirst(String str) {
        if (str == null) {
            throw new NullPointerException("传入的字符串不能为空!");
        }
        char[] chars = str.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);

    }
}
