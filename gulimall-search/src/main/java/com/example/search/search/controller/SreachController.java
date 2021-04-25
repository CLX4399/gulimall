package com.example.search.search.controller;

import com.example.search.search.service.MallSearchProductService;
import com.example.search.search.vo.SearchParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author WhtCl
 * @program: gulimall
 * @description:
 * @date 2021-04-21 20:07:57
 */
@Controller
public class SreachController {

    @Autowired
    private MallSearchProductService mallSearchProductService;

    @GetMapping({"/search.html","/list.html"})
    public String productStatusUp(Model model, SearchParam searchParam
                                 , HttpServletRequest request){
        mallSearchProductService.getProductResult(searchParam);
        return "search";
    }


}
