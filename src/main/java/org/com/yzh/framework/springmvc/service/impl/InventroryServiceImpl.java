package org.com.yzh.framework.springmvc.service.impl;

import com.google.common.util.concurrent.RateLimiter;
import org.com.yzh.framework.springmvc.annotation.Service;
import org.com.yzh.framework.springmvc.service.InventroryService;

/**
 * @ClassName: InventroryServiceImpl
 * @Description: That's the purpose of the class
 * @Author: yin.zhh
 * @Date 2018-01-23 10:42
 * @Version v.1.0.0
 */

@Service
public class InventroryServiceImpl implements InventroryService {

    private static int i = 1;

    /**
     * 令牌机制 qps = 10
     */
    private RateLimiter rateLimiter = RateLimiter.create(10);

    @Override
    public String quaryName(String name) {
        return "My name is " + name;
    }

    @Override
    public void doRequest() {

        if (rateLimiter.tryAcquire()) {
            System.out.println("请求成功!" + i++);
        } else {
            System.out.println("请求过多,请稍后再试.....");
        }

    }
}
