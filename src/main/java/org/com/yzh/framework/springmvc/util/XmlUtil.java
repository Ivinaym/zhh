/**
 * File Name:XmlUtil.java
 * Package Name:com.suneee.base.util
 * Description: (That's the purpose of the file)
 * Date:2015年5月12日下午6:24:25
 * Copyright (c) 2015, forint.lee@qq.com All Rights Reserved.
 */

package org.com.yzh.framework.springmvc.util;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ClassName:XmlUtil <br/>
 * Description:That's the purpose of the class Date: 2015年5月12日 下午6:24:25 <br/>
 *
 * @author carten
 * @version V1.0
 * @see
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
     * */
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
     * */
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
     * @Title: getFirstNodeName
     * @Description: (That ' s the purpose of the method)
     * @param file
     * @return
     * @throws Exception
     * @throws
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

}
