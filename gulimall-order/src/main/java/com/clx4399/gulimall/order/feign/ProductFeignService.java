package com.clx4399.gulimall.order.feign;

import com.clx4399.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author WhtCl
 * @program: gulimall
 * @description:
 * @date 2021-08-26 09:26:57
 */
@FeignClient("gulimall-product")
public interface ProductFeignService {

    /**
     * @param skuId
     * @return com.clx4399.common.utils.R
     * @author CLX
     * @describe: 获取sku信息
     * @date 2021/8/26 9:36
     */
    @GetMapping("/product/spuinfo/info/{skuId}")
    R getSpuInfoBySkuId(@PathVariable("skuId") Long skuId);


}
