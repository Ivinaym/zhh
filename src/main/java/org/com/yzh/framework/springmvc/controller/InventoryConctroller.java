package org.com.yzh.framework.springmvc.controller;

import org.com.yzh.framework.springmvc.annotation.*;
import org.com.yzh.framework.springmvc.service.InventroryService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName: InventoryConctroller
 * @Description: That's the purpose of the class
 * @Author: yin.zhh
 * @Date 2018-01-23 10:59
 * @Version v.1.0.0
 */
@Controller
@Path("/invt")
public class InventoryConctroller {

    @Autowired
    private InventroryService inventroryService;

    @Method("/quary/name.json")
    private void quaryName(HttpServletRequest req, HttpServletResponse rep, @Param("name") String name) {
        String quaryName = inventroryService.quaryName(name);
        try {
            rep.getWriter().write(quaryName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
