package com.clx4399.gulimall.member.feign;

import com.clx4399.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * @author WhtCl
 * @program: gulimall
 * @description: 订单服务feign
 * @date 2021-09-02 20:36:29
 */
@FeignClient("gulimall-order")
public interface OrderFeignService {

    @PostMapping("/order/omsorder/listWithItem")
    R listWithItem(@RequestBody Map<String, Object> params);

}
