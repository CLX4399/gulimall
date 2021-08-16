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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

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
     * @param skuId
     * @return java.lang.String
     * @author CLX
     * @describe: 删除购物项
     * @date 2021/8/16 14:55
     */
    @GetMapping(value = "/deleteItem")
    public String deleteItem(@RequestParam("skuId") Integer skuId) {
        cartService.deleteIdCartInfo(skuId);
        return "redirect:http://cart.gulimall.com/cart.html";

    }

    /**
     * @param skuId
	 * @param num
     * @return java.lang.String
     * @author CLX
     * @describe: 修改购物项数量
     * @date 2021/8/16 14:53
     */
    @GetMapping(value = "/countItem")
    public String countItem(@RequestParam(value = "skuId") Long skuId,
                            @RequestParam(value = "num") Integer num) {
        cartService.changeItemCount(skuId, num);
        return "redirect:http://cart.gulimall.com/cart.html";
    }

    /**
     * @param skuId
	 * @param checked
     * @return java.lang.String
     * @author CLX
     * @describe: 勾选购物项
     * @date 2021/8/16 14:49
     */
    @GetMapping(value = "/checkItem")
    public String checkItem(@RequestParam(value = "skuId") Long skuId,
                            @RequestParam(value = "checked") Integer checked) {
        cartService.checkItem(skuId, checked);
        return "redirect:http://cart.gulimall.com/cart.html";

    }

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
        CartVo cartVo = cartService.getCart();
        model.addAttribute("cart", cartVo);
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
     * @return java.lang.String
     * @author CLX
     * @describe: 商品添加购物车,携带skuid跳转到添加成功页面
     * @date 2021/8/13 11:13
     */
    @GetMapping(value = "/addToCart")
    public String addToCart(@RequestParam("skuId") Long skuId,
                            @RequestParam("num") Integer num
                            ) throws ExecutionException, InterruptedException {
        //重定向到成功页面。再次查询购物车数据即可
        cartService.addItem(skuId,num);
        //model.addAttribute("skuId", skuId);
        return "redirect:http://cart.gulimall.com/addToCartSuccessPage.html?skuId="+skuId;
    }
}
