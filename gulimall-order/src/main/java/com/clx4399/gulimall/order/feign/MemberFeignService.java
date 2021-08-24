package com.clx4399.gulimall.order.feign;

import com.clx4399.gulimall.order.vo.MemberAddressVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author WhtCl
 * @program: gulimall
 * @description: 会员服务
 * @date 2021-08-23 16:40:13
 */
@FeignClient("gulimall-member")
public interface MemberFeignService {

    /**
     * @param memberId
     * @return java.util.List<com.clx4399.gulimall.order.vo.MemberAddressVo>
     * @author CLX
     * @describe: 获取结算用户地址信息
     * @date 2021/8/23 16:43
     */
    @GetMapping("/member/memberreceiveaddress/{memberId}/address")
    List<MemberAddressVo> getAddress(@PathVariable Long memberId);
}
