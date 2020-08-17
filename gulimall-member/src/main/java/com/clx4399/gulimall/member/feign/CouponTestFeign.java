package com.clx4399.gulimall.member.feign;

import com.clx4399.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("gulimall-coupon")
public interface CouponTestFeign {

    /**
     * @param
     * @return com.clx4399.common.utils.R
     * @author CLX
     * @describe:
     * @date 2020/8/4 17:16
     */
    @GetMapping("/coupon/coupon/allList")
    R allList();

}
