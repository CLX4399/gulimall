package com.clx4399.gulimall.cart.service;

import com.clx4399.gulimall.cart.vo.CartItemVo;
import com.clx4399.gulimall.cart.vo.CartVo;

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

    /**
     * @param
     * @return com.clx4399.gulimall.cart.vo.CartVo
     * @author CLX
     * @describe: 获取购物车
     * @date 2021/8/16 11:13
     */
    CartVo getCart() throws ExecutionException, InterruptedException;

    /**
     * @param cartKey
     * @return void
     * @author CLX
     * @describe: 清空购物车
     * @date 2021/8/16 11:41
     */
    void clearCartInfo(String cartKey);

    /**
     * @param skuId
	 * @param checked
     * @return void
     * @author CLX
     * @describe: 勾选购物项
     * @date 2021/8/16 14:49
     */
    void checkItem(Long skuId, Integer checked);

    /**
     * @param skuId
	 * @param num
     * @return void
     * @author CLX
     * @describe: 修改商品项数量
     * @date 2021/8/16 14:54
     */
    void changeItemCount(Long skuId, Integer num);

    /**
     * @param skuId
     * @return void
     * @author CLX
     * @describe:  删除商品项
     * @date 2021/8/16 14:55
     */
    void deleteIdCartInfo(Integer skuId);
}
