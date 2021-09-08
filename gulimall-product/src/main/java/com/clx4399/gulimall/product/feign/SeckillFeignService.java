package com.clx4399.gulimall.product.feign;

import com.clx4399.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author WhtCl
 * @program: gulimall
 * @description:
 * @date 2021-09-07 10:22:02
 */
@FeignClient("gulimall-seckill")
public interface SeckillFeignService {

    /**
     * @param skuId
     * @return com.clx4399.common.utils.R
     * @author CLX
     * @describe: 获取秒杀商品信息
     * @date 2021/9/7 10:24
     */
    @ResponseBody
    @GetMapping("/sku/seckill/{skuId}")
    R getSkuSeckillInfo(@PathVariable Long skuId);

}
