package com.clx4399.gulimall.ware.feign;

import com.clx4399.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author WhtCl
 * @program: gulimall
 * @description: 订单服务feign
 * @date 2021-08-31 20:15:50
 */
@FeignClient("gulimall-order")
public interface OrderFeignService {


    /**
     * @param orderSn
     * @return com.clx4399.common.utils.R
     * @author CLX
     * @describe: 根据订单编号获取订单信息
     * @date 2021/8/31 20:23
     */
    @GetMapping("/order/omsorder/status/{orderSn}")
    R getOrderByOrderSn(@PathVariable("orderSn") String orderSn);
}
