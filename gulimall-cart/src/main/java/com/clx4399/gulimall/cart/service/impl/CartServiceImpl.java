package com.clx4399.gulimall.cart.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.clx4399.common.utils.R;
import com.clx4399.gulimall.cart.feign.ProductServiceFeign;
import com.clx4399.gulimall.cart.interceptor.CartInterceptor;
import com.clx4399.gulimall.cart.service.CartService;
import com.clx4399.gulimall.cart.to.UserInfoTo;
import com.clx4399.gulimall.cart.vo.CartItemVo;
import com.clx4399.gulimall.cart.vo.SkuInfoVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import static com.clx4399.common.constant.CartConstant.CART_PREFIX;

/**
 * @author WhtCl
 * @program: gulimall
 * @description:
 * @date 2021-08-13 11:16:54
 */
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    ProductServiceFeign productServiceFeign;

    @Autowired
    private ThreadPoolExecutor executor;

    @Override
    public CartItemVo addItem(Long skuId, Integer num) throws ExecutionException, InterruptedException {

        BoundHashOperations<String, Object, Object> cartOps = getCartOps();

        String productRedisValue = (String) cartOps.get(skuId.toString());

        //为空代表为新加商品，否则修改其数量即可
        if (StringUtils.isBlank(productRedisValue)){
            CartItemVo cartItemVo = new CartItemVo();

            CompletableFuture<Void> voidCompletableFuture1 = CompletableFuture.runAsync(() -> {
                R r = productServiceFeign.getSkuInfoById(skuId);
                SkuInfoVo skuInfo = r.getData("skuInfo", new TypeReference<SkuInfoVo>() {
                });
                cartItemVo.setSkuId(skuId);
                cartItemVo.setImage(skuInfo.getSkuDefaultImg());
                cartItemVo.setPrice(skuInfo.getPrice());
                cartItemVo.setTitle(skuInfo.getSkuTitle());
                cartItemVo.setCount(Math.toIntExact(num));
            }, executor);

            CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(() -> {
                List<String> skuSaleAttrValues =
                        productServiceFeign.getSkuSaleAttrValues(skuId);
                cartItemVo.setSkuAttrValues(skuSaleAttrValues);
            }, executor);

            CompletableFuture.allOf(voidCompletableFuture,voidCompletableFuture1).get();
            cartOps.put(skuId.toString(), JSON.toJSONString(cartItemVo));

            return cartItemVo;
        }else {
            //购物车有此商品，修改数量即可
            CartItemVo cartItemVo = JSON.parseObject(productRedisValue, CartItemVo.class);
            cartItemVo.setCount(cartItemVo.getCount() + num);
            //修改redis的数据
            String cartItemJson = JSON.toJSONString(cartItemVo);
            cartOps.put(skuId.toString(), cartItemJson);

            return cartItemVo;
        }
    }

    /**
     * @param
     * @return org.springframework.data.redis.core.BoundHashOperations<java.lang.String,java.lang.Object,java.lang.Object>
     * @author CLX
     * @describe: 获取购物车redis操作对象
     * @date 2021/8/13 14:57
     */
    private BoundHashOperations<String, Object, Object> getCartOps() {
        UserInfoTo userInfoTo = CartInterceptor.toThreadLocal.get();
        String cartKey = "";
        if (userInfoTo.getUserId() != null){
            cartKey = CART_PREFIX + userInfoTo.getUserId();
        }else {
            cartKey = CART_PREFIX + userInfoTo.getUserKey();
        }
        BoundHashOperations<String, Object, Object> operations = redisTemplate.boundHashOps(cartKey);
        return operations;
    }

    @Override
    public CartItemVo getCartItem(Long skuId) {
        //拿到要操作的购物车信息
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();

        String redisValue = (String) cartOps.get(skuId.toString());

        return JSON.parseObject(redisValue, CartItemVo.class);
    }
}
