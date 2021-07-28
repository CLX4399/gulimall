package com.clx4399.gulimall.gulimallthridparty.service;


import com.clx4399.gulimall.gulimallthridparty.util.HttpUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author WhtCl
 * @program: gulimall
 * @description:
 * @date 2021-07-23 15:06:29
 */
@ConfigurationProperties(prefix = "spring.cloud.alicloud.sms")
@Data
@Component
@Slf4j
public class SmsCompent {

    /**
     * 请求地址
     */
    String host;
    /**
     * 请求路径
     */
    String path;
    /**
     * 请求方法
     */
    String method;
    /**
     * app编号
     */
    String appcode;

    /**
     * @param phone
	 * @param code
     * @return void
     * @author CLX
     * @describe: 发送验证码
     * @date 2021/7/23 15:23
     */
    public void sendSms(String phone, String code) {
        Map<String, String> headers = new HashMap<>(1);
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<>(5);
        querys.put("mobile", phone);
        querys.put("param", "**code**:"+code+",**minute**:5");
        querys.put("smsSignId", "2e65b1bb3d054466b82f0c9d125465e2");
        querys.put("templateId", "908e94ccf08b4476ba6c876d13f084ad");
        Map<String, String> bodys = new HashMap<>(1);


        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            log.info(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
