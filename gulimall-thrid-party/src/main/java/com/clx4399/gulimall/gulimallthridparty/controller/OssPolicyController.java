package com.clx4399.gulimall.gulimallthridparty.controller;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSS;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.clx4399.common.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author WhtCl
 * @program: gulimall
 * @description: oss对象签名控制器
 * @date 2020-10-29 11:40:25
 */
@RestController
public class OssPolicyController {

    @Autowired
    private OSS ossClient;

    @Value("${spring.cloud.alicloud.oss.access-key}")
    private String accessId ;

    @Value("${spring.cloud.alicloud.oss.secret-key}")
    private String accessKey;

    @Value("${spring.cloud.alicloud.oss.endpoint}")
    private String endpoint;

    /**
     * @param
     * @return com.clx4399.common.utils.R
     * @author CLX
     * @describe:  oss对象上传签名申请
     * @date 2020/10/30 15:25
     */
    @RequestMapping("/oss/policy")
    public R policyMessage(){

        // 请填写您的 bucketname 。
        String bucket = "gulimal-clx4399";

        // host的格式为 bucketname.endpoint
        String host = "http://" + bucket + "." + endpoint;

        // callbackUrl为 上传回调服务器的URL，请将下面的IP和Port配置为您自己的真实信息。
        String callbackUrl = "http://88.88.88.88.:8888";

        // 用户上传文件时指定的前缀。
        String dir = "gulimall-test";

        Map<String, String> respMap = new LinkedHashMap<String, String>();
        try {
            long expireTime = 30;
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            Date expiration = new Date(expireEndTime);
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

            String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes("utf-8");
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = ossClient.calculatePostSignature(postPolicy);

            respMap.put("accessid", accessId);
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            respMap.put("dir", dir);
            respMap.put("host", host);
            respMap.put("expire", String.valueOf(expireEndTime / 1000));
            // respMap.put("expire", formatISO8601Date(expiration));

            JSONObject jasonCallback = new JSONObject();
            jasonCallback.put("callbackUrl", callbackUrl);
            jasonCallback.put("callbackBody",
                    "filename=${object}&size=${size}&mimeType=${mimeType}&height=${imageInfo.height}&width=${imageInfo.width}");
            jasonCallback.put("callbackBodyType", "application/x-www-form-urlencoded");
            String base64CallbackBody = BinaryUtil.toBase64String(jasonCallback.toString().getBytes());
            respMap.put("callback", base64CallbackBody);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return R.ok().put("data",respMap);

    }

}
