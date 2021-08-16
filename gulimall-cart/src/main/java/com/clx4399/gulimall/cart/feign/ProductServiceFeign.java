package com.clx4399.gulimall.cart.feign;

import com.clx4399.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author WhtCl
 * @program: gulimall
 * @description:
 * @date 2021-08-13 15:04:26
 */
@FeignClient("gulimall-product")
public interface ProductServiceFeign {

    /**
     * @param skuId
     * @return com.clx4399.common.utils.R
     * @author CLX
     * @describe: skuid获取sku信息
     * @date 2021/8/13 15:29
     */
    @RequestMapping("/product/skuinfo/info/{skuId}")
    R getSkuInfoById(@PathVariable("skuId") Long skuId);

    /**
     * @param skuId
     * @return java.util.List<java.lang.String>
     * @author CLX
     * @describe: skuid获取销售信息
     * @date 2021/8/13 15:29
     */
    @GetMapping(value = "/product/skusaleattrvalue/stringList/{skuId}")
    List<String> getSkuSaleAttrValues(@PathVariable("skuId") Long skuId);

}
