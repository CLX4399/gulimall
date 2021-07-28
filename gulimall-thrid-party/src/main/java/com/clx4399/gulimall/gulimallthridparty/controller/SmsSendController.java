package com.clx4399.gulimall.gulimallthridparty.controller;

import com.clx4399.common.utils.R;
import com.clx4399.gulimall.gulimallthridparty.service.SmsCompent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WhtCl
 * @program: gulimall
 * @description:
 * @date 2021-07-23 16:11:57
 */
@RestController
@RequestMapping("/sms")
public class SmsSendController {

    @Autowired
    private SmsCompent smsCompent;

    /**
     * @param phone
	 * @param code
     * @return com.clx4399.common.utils.R
     * @author CLX
     * @describe: 发送验证码
     * @date 2021/7/23 16:31
     */
    @GetMapping("/sendSms")
    public R sendCode(@RequestParam("phone") String phone, @RequestParam("code")String code){
        smsCompent.sendSms(phone,code);
        return R.ok();
    }

}
