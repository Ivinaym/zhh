package org.com.yzh.framework.springmvc.service;

/**
 * @ClassName: org.com.yzh.framework.springmvc.service
 * @Description:
 * @Author: yin.zhh
 * @Date 2018/1/23 10:42
 * @Version v.1.0.0
 */

public interface InventroryService {

    /**
     * fetch data by rule name
     *
     * @param name rule name
     * @return String
     */
    String quaryName(String name);


    void doRequest();
}
