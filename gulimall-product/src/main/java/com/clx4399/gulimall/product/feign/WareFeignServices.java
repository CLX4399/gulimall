package com.clx4399.gulimall.product.feign;

import com.clx4399.common.utils.R;
import com.clx4399.gulimall.product.vo.SkuHasStockVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author WhtCl
 * @program: gulimall
 * @description: feign调用库存接口
 * @date 2021-04-13 21:08:16
 */
@FeignClient("gulimall-ware")
public interface WareFeignServices {

    /**
     * 查询sku是否有库存
     * 返回skuId 和 stock库存量
     */
    @PostMapping("/ware/waresku/hasStock")
    R<List<SkuHasStockVo>> getSkuHasStock(@RequestBody List<Long> SkuIds);

}
