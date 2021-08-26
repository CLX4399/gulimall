package com.clx4399.gulimall.order.feign;

import com.clx4399.common.utils.R;
import com.clx4399.gulimall.order.vo.WareSkuLockVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

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

    /**
     * @param addrId
     * @return com.clx4399.common.utils.R
     * @author CLX
     * @describe:
     * @date 2021/8/25 20:34
     */
    @GetMapping("/ware/wareinfo/fare")
    R getFare(@RequestParam Long addrId);

    /**
     * @param lockVo
     * @return com.clx4399.common.utils.R
     * @author CLX
     * @describe: 确定订单锁定商品库存
     * @date 2021/8/26 14:43
     */
    @PostMapping("/ware/waresku/lock/order")
    R orderLockStock(WareSkuLockVo lockVo);
}
