package com.example.search.search.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author WhtCl
 * @program: gulimall
 * @description:
 * @date 2021-04-21 20:07:57
 */
@Controller
public class SreachController {

    @GetMapping({"/search.html","/list.html"}) // ElasticSaveController
    public String productStatusUp(){
        return "search";
    }


}
