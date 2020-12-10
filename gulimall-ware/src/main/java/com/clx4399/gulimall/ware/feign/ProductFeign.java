package com.clx4399.gulimall.ware.feign;

import com.clx4399.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author WhtCl
 */
@FeignClient("gulimall-product")
public interface ProductFeign {

    /**
     * 信息
     */
    @RequestMapping("/product/skuinfo/info/{skuId}")
    //@RequiresPermissions("product:skuinfo:info")
    R getSkuInfo(@PathVariable("skuId") Long skuId);

}
