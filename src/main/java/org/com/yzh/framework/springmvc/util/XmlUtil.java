package org.com.yzh.framework.springmvc.util;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import static jdk.nashorn.api.scripting.ScriptUtils.convert;


/**
 * @Description: That's the purpose of the class
 * @Author: yin.zhh
 * @Date 2018/2/2 10:31
 * @Version v.1.0.0
 */
public class XmlUtil {

    public static void copyFile(File oldfile, String newPath, String newname) {
        try {
            int byteread;
            if (newname != null && !"".equals(newname)) {
                newPath = newPath + File.separator + newname;
            }
            if (oldfile.exists()) {
                InputStream inStream = new FileInputStream(oldfile);
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                while ((byteread = inStream.read(buffer)) != -1) {
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
                fs.flush();
                fs.close();
            }

        } catch (Exception e) {
            System.out.println("error  ");
            e.printStackTrace();
        }
    }


    private static boolean isObject(Object obj) {
        return obj != null && !(obj instanceof String) && !(obj instanceof Integer) && !(obj instanceof
                BigDecimal) && !(obj instanceof Double) && !(obj instanceof Float) && !(obj instanceof Byte)
                && !(obj instanceof Long) && !(obj instanceof Date) && !(obj instanceof Character)
                && !(obj instanceof Short) && !(obj instanceof Boolean);
    }


    /**
     * 根据属性名获取属性值
     */
    private static Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter);
            Object value = method.invoke(o);
            if (value != null && (value instanceof Date)) {
                String formatStr = "yyyyMMddHHmmss";
                if ("ieDate".equalsIgnoreCase(fieldName)) {
                    formatStr = "yyyyMMdd";
                } else if ("appTime".equalsIgnoreCase(fieldName)) {
                    value = new Date();
                }
                DateFormat format = new SimpleDateFormat(formatStr);
                value = format.format(value);
            }
            // unit uni1 unit2 都是字符串 不足三位的前面补0
            if (fieldName.startsWith("unit") && value != null) {
                StringBuilder strval = new StringBuilder((String) value);
                if (strval.length() != 0 && strval.length() < 3) {
                    while (strval.length() < 3) {
                        strval.insert(0, "0");
                    }
                }
                value = strval.toString();
            }
            return value;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 根据属性名获取属性值
     */
    public static Object setFieldValue(String fieldName, String value, Object o) {
        try {
            Method method = getSetMethod(o.getClass(), fieldName);
            assert method != null;
            Class<?>[] T = method.getParameterTypes();
            Object obj = Class.forName(T[0].getName()).newInstance();
            if (obj instanceof Date) {
                DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                Date date = format.parse(value);
                method.invoke(o, date);
            } else {
                method.invoke(o, value);
            }
            return o;
        } catch (Exception e) {
            return null;
        }
    }

    private static Method getSetMethod(Class<?> objectClass, String fieldName) {
        try {
            Class<?>[] parameterTypes = new Class[1];
            Field field = objectClass.getDeclaredField(fieldName);
            parameterTypes[0] = field.getType();
            String sb = "set" +
                    fieldName.substring(0, 1).toUpperCase() +
                    fieldName.substring(1);

            return objectClass.getMethod(sb, parameterTypes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取xml文件的根节点名字
     *
     * @param file
     * @return
     * @throws Exception
     * @throws
     * @Title: getFirstNodeName
     * @Description: (That ' s the purpose of the method)
     */
    public static String getFirstNodeName(File file) throws Exception {
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            org.w3c.dom.Document doc = builder.parse(file);
            doc.getDocumentElement().normalize();
            return doc.getDocumentElement().getNodeName();
        } catch (Exception e) {
            e.printStackTrace();
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


    public static Handler getHandler(HttpServletRequest req, List<Handler> handlerMaping) {

        if (handlerMaping.isEmpty()) {
            return null;
        }

        String uri = req.getRequestURI();
        String contextPath = req.getContextPath();
        uri = uri.replace(contextPath, "").replaceAll("/+", "/");

        for (Handler handler : handlerMaping) {
            Matcher matcher = handler.getPattern().matcher(uri);
            if (matcher.matches()) {
                return handler;
            }
        }
        return null;
    }


    public static void invoke(Handler handler, HttpServletRequest req, HttpServletResponse rep) throws InvocationTargetException, IllegalAccessException {
        //获取方法的参数列表
        Class<?>[] parameterTypes = handler.getMethod().getParameterTypes();

        //保存所有需要赋值的参数值
        Object[] paramValues = new Object[parameterTypes.length];

        Map parameterMap = req.getParameterMap();
        for (Object o : parameterMap.entrySet()) {
            Map.Entry<String, String[]> entry = (Map.Entry<String, String[]>) o;

            String value = Arrays.toString(entry.getValue()).replaceAll("\\[|\\]", "").replaceAll(",\\s", ",");

            //找到匹配对象,则开始填充数值
            if (handler.getParamIndexMaping().containsKey(entry.getKey())) {
                Integer integer = handler.getParamIndexMaping().get(entry.getKey());
                paramValues[integer] = convert(parameterTypes[integer], value);
            }

        }

        //设置方法中的request对象和response对象

        Integer intRequest = handler.getParamIndexMaping().get(HttpServletRequest.class.getName());
        paramValues[intRequest] = req;

        Integer intResponse = handler.getParamIndexMaping().get(HttpServletResponse.class.getName());
        paramValues[intResponse] = rep;

        handler.getMethod().invoke(handler.getContorller(), paramValues);
    }

    public static boolean pattern(HttpServletRequest req, HttpServletResponse resp, List<Handler> handlerMapping) throws Exception {

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

            Map<String, String[]> params = req.getParameterMap();

            for (Map.Entry<String, String[]> param : params.entrySet()) {

                String value = Arrays.toString(param.getValue()).replaceAll("\\]|\\[", "").replaceAll(",\\s", ",");

                if (!handler.getParamIndexMaping().containsKey(param.getKey())) {
                    continue;
                }

                int index = handler.getParamIndexMaping().get(param.getKey());

                //涉及到类型转换
                paramValues[index] = castStringValue(value, paramTypes[index]);

            }


            //
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


    public String lowerFirstChar(String str) {

        char[] chars = str.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);

    }

}
