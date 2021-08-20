package com.clx4399.gulimall.order.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author WhtCl
 * @program: gulimall
 * @description:
 * @date 2021-08-19 15:37:58
 */
@Controller
public class HelloController {

    @GetMapping("/{page}.html")
    public String listPage(@PathVariable String page){
        return page;
    }

}
