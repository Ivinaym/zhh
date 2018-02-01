package org.com.yzh.framework.springmvc.service.impl;

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

    @Override
    public String quaryName(String name) {
        return "My name is " + name;
    }
}
