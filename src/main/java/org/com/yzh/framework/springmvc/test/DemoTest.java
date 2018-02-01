package org.com.yzh.framework.springmvc.test;


import org.com.yzh.framework.springmvc.annotation.Controller;
import org.testng.annotations.Test;

/**
 * @ClassName: DemoTest
 * @Description: That's the purpose of the class
 * @Author: yin.zhh
 * @Date 2018-01-31 16:05
 * @Version v.1.0.0
 */
public class DemoTest {

    @Test
    public void demo01() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class<?> clazz = Class.forName("org.com.yzh.framework.springmvc.controller.InventoryConctroller");

        boolean annotation = clazz.isAnnotation();
        System.out.println(annotation);

        if (clazz.isAnnotationPresent(Controller.class)) {
            System.out.println("---------------zz".toUpperCase());

        } else {
            System.out.println("666666666666");
        }
    }
}
