package com.clx4399.gulimall.product.feign;

import com.clx4399.common.to.SpuBoundTo;
import com.clx4399.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("gulimall-coupon")
public interface CouponFeignServices {

    /**
     * @param spuBoundTo
     * @return com.clx4399.common.utils.R
     * @author CLX
     * @describe:
     * @date 2020/11/28 15:42
     */
    @PostMapping("/coupon/spubounds/save")
    R saveSpuBounds(SpuBoundTo spuBoundTo);

}
