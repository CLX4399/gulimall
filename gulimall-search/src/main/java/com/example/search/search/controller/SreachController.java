package com.example.search.search.controller;

import com.example.search.search.service.MallSearchProductService;
import com.example.search.search.vo.SearchParam;
import com.example.search.search.vo.SearchResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.CompletableFuture;

/**
 * @author WhtCl
 * @program: gulimall
 * @description:
 * @date 2021-04-21 20:07:57
 */
@Slf4j
@Controller
public class SreachController {

    @Autowired
    private MallSearchProductService mallSearchProductService;

    /**
     * @param model
	 * @param searchParam
	 * @param request
     * @return java.lang.String
     * @author CLX
     * @describe: 商品搜索
     * @date 2021/4/25 15:06
     */
    @GetMapping({"/search.html","/list.html"})
    public String productStatusUp(Model model, SearchParam searchParam
                                 , HttpServletRequest request){
        SearchResult productResult = mallSearchProductService.getProductResult(searchParam);
        model.addAttribute("result",productResult);
        return "search";
    }


}
