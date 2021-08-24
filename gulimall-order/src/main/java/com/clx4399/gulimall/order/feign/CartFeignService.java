package com.clx4399.gulimall.order.feign;

import com.clx4399.gulimall.order.vo.OrderItemVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author WhtCl
 * @program: gulimall
 * @description: 购物车商品服务
 * @date 2021-08-23 16:42:55
 */
@FeignClient("gulimall-cart")
public interface CartFeignService {
    /**
     * @param
     * @return java.util.List<com.clx4399.gulimall.order.vo.OrderItemVo>
     * @author CLX
     * @describe: 获取当前用户购物车商品信息
     * @date 2021/8/23 16:46
     */
    @GetMapping("/currentUserCartItems")
    List<OrderItemVo> getCurrentUserCartItems();
}
