package com.clx4399.gulimall.cart.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.clx4399.common.utils.R;
import com.clx4399.gulimall.cart.feign.ProductServiceFeign;
import com.clx4399.gulimall.cart.interceptor.CartInterceptor;
import com.clx4399.gulimall.cart.service.CartService;
import com.clx4399.gulimall.cart.to.UserInfoTo;
import com.clx4399.gulimall.cart.vo.CartItemVo;
import com.clx4399.gulimall.cart.vo.CartVo;
import com.clx4399.gulimall.cart.vo.SkuInfoVo;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

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

    @Override
    public CartVo getCart() throws ExecutionException, InterruptedException {
        CartVo cartVo = new CartVo();
        UserInfoTo userInfoTo = CartInterceptor.toThreadLocal.get();
        if (userInfoTo.getUserId() != null){
            String cartKey = CART_PREFIX + userInfoTo.getUserId();
            String tempCartKey = CART_PREFIX + userInfoTo.getUserKey();
            List<CartItemVo> tempCartItem = getCartItem(tempCartKey);
            //临时购物车中有商品，需纳入当前用户购物车中，并清空临时购物车
            if (tempCartItem != null && tempCartItem.size() > 0){
                for (CartItemVo cartItemVo : tempCartItem) {
                    addItem(cartItemVo.getSkuId(), cartItemVo.getCount());
                }
                //清除临时购物车的数据
                clearCartInfo(tempCartKey);
            }
            List<CartItemVo> cartItem = getCartItem(cartKey);
            cartVo.setItems(cartItem);

        }else {
            String cartKey = CART_PREFIX + userInfoTo.getUserKey();
            List<CartItemVo> cartItem = getCartItem(cartKey);
            cartVo.setItems(cartItem);
        }

        return cartVo;
    }

    @Override
    public void clearCartInfo(String cartKey) {
        redisTemplate.delete(cartKey);
    }

    @Override
    public void checkItem(Long skuId, Integer checked) {
        CartItemVo cartItem = getCartItem(skuId);
        cartItem.setCheck(checked==1?true:false);
        String s = JSON.toJSONString(cartItem);
        BoundHashOperations<String, Object, Object> cartOps =
                getCartOps();
        cartOps.put(skuId.toString(),s);
    }

    @Override
    public void changeItemCount(Long skuId, Integer num) {
        //查询购物车里面的商品
        CartItemVo cartItem = getCartItem(skuId);
        cartItem.setCount(num);

        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        //序列化存入redis中
        String redisValue = JSON.toJSONString(cartItem);
        cartOps.put(skuId.toString(), redisValue);
    }

    @Override
    public void deleteIdCartInfo(Integer skuId) {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        cartOps.delete(skuId.toString());
    }

    @Override
    public List<CartItemVo> getCurrentUserCartItems() {
        UserInfoTo userInfoTo = CartInterceptor.toThreadLocal.get();
        if (userInfoTo.getUserId() == null){
            return null;
        }else {
            String userKey =CART_PREFIX + userInfoTo.getUserId();
            List<CartItemVo> cartItem = getCartItem(userKey);
            List<CartItemVo> collect = cartItem.stream().filter(item -> item.getCheck()).map(item -> {
                //更新商品价格为最新的商品价格
                R skuInfoById = productServiceFeign.getSkuInfoById(item.getSkuId());
                SkuInfoVo data = skuInfoById.getData("skuInfo",new TypeReference<SkuInfoVo>() {
                });
                item.setPrice(data.getPrice());
                return item;
            }).collect(Collectors.toList());
            return collect;
        }
    }

    /**
     * @param cartKey
     * @return void
     * @author CLX
     * @describe: cartkey获取购物车后中商品
     * @date 2021/8/16 11:29
     */
    private List<CartItemVo> getCartItem(String cartKey) {
        BoundHashOperations<String, Object, Object> operations = redisTemplate.boundHashOps(cartKey);
        List<Object> values = operations.values();
        if (values!=null && values.size()>0){
            return values.stream().map(item -> {
                String s = String.valueOf(item);
                return JSON.parseObject(s, CartItemVo.class);
            }).collect(Collectors.toList());
        }
        return null;
    }
}
