package com.clx4399.gulimall.cart.service;

import com.clx4399.gulimall.cart.vo.CartItemVo;

import java.util.concurrent.ExecutionException;

/**
 * @author WhtCl
 * @program: gulimall
 * @description: 购物车接口
 * @date 2021-08-13 11:16:05
 */
public interface CartService {

    /**
     * @param skuId
	 * @param num
     * @return void
     * @author CLX
     * @describe:  添加商品到购物车
     * @date 2021/8/13 11:20
     */
    CartItemVo addItem(Long skuId, Integer num) throws ExecutionException, InterruptedException;

    /**
     * @param skuId
     * @return com.clx4399.gulimall.cart.vo.CartItemVo
     * @author CLX
     * @describe: 通过skuid获取添加购物车的商品信息
     * @date 2021/8/13 11:20
     */
    CartItemVo getCartItem(Long skuId);
}
