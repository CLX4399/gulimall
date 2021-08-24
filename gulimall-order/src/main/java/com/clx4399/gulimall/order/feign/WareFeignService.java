package com.clx4399.gulimall.order.feign;

import com.clx4399.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author WhtCl
 * @program: gulimall
 * @description: 库存服务feign调用
 * @date 2021-08-24 19:57:19
 */
@FeignClient("gulimall-ware")
public interface WareFeignService {

    /**
     * @param SkuIds
     * @return com.clx4399.common.utils.R
     * @author CLX
     * @describe: 调用是否有库存
     * @date 2021/8/24 20:05
     */
    @PostMapping("/ware/waresku/hasStock")
    R getSkuHasStock(@RequestBody List<Long> SkuIds);

}
