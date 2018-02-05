package org.com.yzh.framework.springmvc.test;

import org.com.yzh.framework.springmvc.service.InventroryService;
import org.com.yzh.framework.springmvc.service.impl.InventroryServiceImpl;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * @ClassName: RaterLimiterDemo
 * @Description: That's the purpose of the class
 * @Author: yin.zhh
 * @Date 2018-02-05 13:59
 * @Version v.1.0.0
 */
public class RaterLimiterDemo {
    public static void main(String[] args) {
        InventroryService inventroryService = new InventroryServiceImpl();

        CountDownLatch countDownLatch = new CountDownLatch(1);
        Random r = new Random(10);
        for (int i = 0; i < 20; i++) {
            new Thread(() -> {
                try {

                    countDownLatch.await();

                    Thread.sleep(r.nextInt(950));

                    inventroryService.doRequest();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        countDownLatch.countDown();
    }
}
