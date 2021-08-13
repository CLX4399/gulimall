package com.clx4399.gulimall.cart.controller;

import com.clx4399.gulimall.cart.interceptor.CartInterceptor;
import com.clx4399.gulimall.cart.service.CartService;
import com.clx4399.gulimall.cart.to.UserInfoTo;
import com.clx4399.gulimall.cart.vo.CartItemVo;
import com.clx4399.gulimall.cart.vo.CartVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * @author WhtCl
 * @program: gulimall
 * @description: 购物车控制类
 * @date 2021-08-12 16:16:08
 */
@Slf4j
@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     * @param model
     * @return java.lang.String
     * @author CLX
     * @describe: 获取
     * @date 2021/8/12 16:51
     */
    @GetMapping(value = "/cart.html")
    public String cartListPage(Model model) throws ExecutionException, InterruptedException {
        //快速得到用户信息：id,user-key
        UserInfoTo userInfoTo = CartInterceptor.toThreadLocal.get();
        log.info(userInfoTo.toString());
        //CartVo cartVo = cartService.getCart();
        model.addAttribute("cart", new CartVo());
        return "cartList";
    }

    /**
     * @param skuId
	 * @param model
     * @return java.lang.String
     * @author CLX
     * @describe: 添加购物车
     * @date 2021/8/12 16:55
     */
    @GetMapping(value = "/addToCartSuccessPage.html")
    public String addToCartSuccessPage(@RequestParam("skuId") Long skuId,
                                       Model model) {
        //重定向到成功页面。再次查询购物车数据即可
        CartItemVo cartItemVo = cartService.getCartItem(skuId);
        model.addAttribute("cartItem", cartItemVo);
        return "success";
    }

    /**
     * @param skuId
	 * @param model
     * @return java.lang.String
     * @author CLX
     * @describe: 商品添加购物车,携带skuid跳转到添加成功页面
     * @date 2021/8/13 11:13
     */
    @GetMapping(value = "/addToCart")
    public String addToCart(@RequestParam("skuId") Long skuId,
                            @RequestParam("num") Integer num,
                                       Model model) throws ExecutionException, InterruptedException {
        //重定向到成功页面。再次查询购物车数据即可
        cartService.addItem(skuId,num);
        model.addAttribute("skuId", skuId);
        return "redirect:http://cart.gulimall.com/addToCartSuccessPage.html";
    }
}
