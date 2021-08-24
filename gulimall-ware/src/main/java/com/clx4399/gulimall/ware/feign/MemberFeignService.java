package com.clx4399.gulimall.ware.feign;

import com.clx4399.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author WhtCl
 * @program: gulimall
 * @description: 用户服务feign调用
 * @date 2021-08-24 21:10:55
 */
@FeignClient("gulimall-member")
public interface MemberFeignService {

    /**
     * @param id
     * @return com.clx4399.common.utils.R
     * @author CLX
     * @describe: 获取用户信息根据id
     * @date 2021/8/24 21:12
     */
    @RequestMapping("/member/member/info/{id}")
    R info(@PathVariable("id") Long id);

}
