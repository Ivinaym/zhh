package org.com.yzh.framework.springmvc.test;


import org.com.yzh.framework.springmvc.annotation.Controller;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName: DemoTest
 * @Description: That's the purpose of the class
 * @Author: yin.zhh
 * @Date 2018-01-31 16:05
 * @Version v.1.0.0
 */
public class DemoTest {

    @Test
    public void demo01() throws ClassNotFoundException {
        Class<?> clazz = Class.forName("org.com.yzh.framework.springmvc.controller.InventoryConctroller");

        boolean annotation = clazz.isAnnotation();
        System.out.println(annotation);

        if (clazz.isAnnotationPresent(Controller.class)) {
            System.out.println("---------------zz".toUpperCase());

        } else {
            System.out.println("666666666666");
        }


        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            System.out.println(method.getName());
        }
    }

    @Test
    public void demo02() {
        String str = "[王五]";

        Pattern pattern = Pattern.compile("[]\\[]");
        Matcher matcher = pattern.matcher(str);
        int i = 0;
        while (matcher.find()) {
            String group = matcher.group(i);
            System.out.println(group);
        }
    }
}
