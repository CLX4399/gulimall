package com.clx4399.gulimall.auth.feign;

import com.clx4399.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author WhtCl
 * @program: gulimall
 * @description:
 * @date 2021-07-23 16:30:28
 */
@Component
@FeignClient("gulimall-thrid-party")
public interface ThridPartyFeignService {

    @GetMapping("/sms/sendSms")
    R sendCode(@RequestParam("phone") String phone, @RequestParam("code")String code);

}
