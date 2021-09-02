package com.clx4399.gulimall.order.config;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.clx4399.gulimall.order.vo.PayVo;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "alipay")
@Component
@Data
public class AlipayTemplate {

    //在支付宝创建的应用的id
    private String app_id = "2021000118611819";

    // 商户私钥，您的PKCS8格式RSA2私钥
    private String merchant_private_key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCFI9OqpNXHJhtADpKFBxFtMr3EtrCz4d9eJGndTpbqRKAaVY5j6lxC22tD2i2HFQyk1VO2TfFE1NGRZRVBC8ec6wEWcFYDd6dB2iUdEemCYroGwrNxQ0f+bpcavNbm6IhhCzS6b6iRmI99RLHvgwEvNF0sQUgR9C+bdc66Q6meh74dbuzVVfyfnZF87YEDfF7BDNgXHmM64tolAUeW/lnQcSi8aS2edOoI++tc+bF4ccKiHM0Gkhc4clmqBCm2rjpqTUbVsPRVPNjd2PmJYfYap9atgguCuNcT8d13ZbQXe9SAqKJkcq6eHYnGG9ozvG+ZGJYg7chg19TnIJ4VKnkjAgMBAAECggEATXZExnnU72NaY3CXCtLL7s6g3ny0XlRCU1G4TkxVPcPjqq6wg61tNHDY/cdIydkTXCvJ4r2CbFut+nvnW8rwvnl0TovZX3cUVxoGkd1EENpEBX7uTC2rC3toauQH3JzaDCwq53Xji6JJicaYCBQeckpzEzaAKgH6WOa3+mM3osXWq244kl5NoMJg10hWILH20Lhvy4uIpRblu+UcQDFOGfOnivkSw0Swnaj0cdN+0Yu5D/Vg5tpboBmd6AAVME2kua+URCMYvRxDPfNUS+ah2bM5dQ16IgasAFTY0FlE7ev2+aG6lfTsM8i11klPYnyStkqTOV1lag2PGIERiUEFAQKBgQDphe9UszEFrd8u5V/UCmvoXDKBJ+LNYbpS4GA1g0Xy/Cj8akPfAtGQftci9A680N3zaToOLHAHzRUHYYb7aH9QFgszH5HTm04BWnNdKUFTawjmZdIMkCKp8kyubCGQDN/Fsn4D4LdtrVVjFdhLnxxhs8qXqImt3H6O4+SVhWSDYwKBgQCR9G0GD/iE0JO9xMiZwDlabstgq3Q46SCBqdQU5stHdB0xvdGWOrGETf2BiG7hBCh2xplluwajNy0McrFFxkaDsSLDiUb2SxEX+nvYdNcbWHRB58st2qO5hnSAIEsgyK/RRMhQLeCz3Shj7QDZoS4xPsPdtPx9ZxHX9Vlfb91/QQKBgB5/Zm1KZbmXJpGqVSSNI+CZweLKQb2O6NWQBiCW/1ing/bLLgf/Bz12w0QGWbJhGMuET5ewSAVMzFVFcGp/EDl2V4oasmHPY5WbqXteOvDZBrWIULFhVZWaVldlOdDlTYg1Mm3xOCNzwrYJEVF2+y7rWycJVMnHBSRGMQEyBZl/AoGBAIoD7CRTbdmJsms8bcSV9KYDJuYeuyQlrstt4FJze73xmrmymUHJ9pr5dMLMVK0K1Yibe+qq1Df6sOqYefFU8NEO66SjZyr3CQ73mayxr8b3+ddobMTLfw6sifWuk9xIaZklxZWsDFOIyezT7H3e4GNIysYXorqigEkKhJyul3EBAoGAKT5ZeNYbjRqKotQ982UWg2TrmOoWvcJf+SJb+CnG3CJgXfwxMXuDPv41qTPZE7a18M7jvF6L1fbPOnOG2tfKxM7YonKbAninLBMVZ3mcHFg1yxZqiQ2c+EvXTMPPOq/J3FlFxkDRFTzYoVasQoDb3n6J7mqPdxeYq4Jm1RwpyuI=";
    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    private String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAq/5eSRoCZhkKxBeRUMqMd4GEE9sBlDXoeOfNn6hdHWYWjYIzZ3BmygLtXhVYfiQeG1yGV08n87ogx2I+GRal5rFmIlF+S/9PEAV9nJjwoeSZZdEBdAqvv2kzZnLkB/7PIPW1JjYCmgETifUhLL6tFCe8O40iuyklfNtxKuBeilm21ANg7O3QwRggcoB1zja7WiF2cYH68ZHouLuPohia3ymYOy6bygLtB2fURZv7JDU7qAYmv2gzNsUF7GuaU0Yi6awJbj2RjMzy0nF8Lkg1yeI0lMHVl/BZUX7U3QTNLY1gARTN8L19dlrmAENcM8NQ4JHWQCxJIe0iq9nH+FpS2QIDAQAB";    // 服务器[异步通知]页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    // 支付宝会悄悄的给我们发送一个请求，告诉我们支付成功的信息
    private String notify_url = "http://weixhh.utools.club/payed/notify";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    //同步通知，支付成功，一般跳转到成功页
    private String return_url = "http://member.greymall.com/memberOrder.html";

    // 签名方式
    private String sign_type = "RSA2";

    // 字符编码格式
    private String charset = "utf-8";

    private String timeout = "30m";

    // 支付宝网关； https://openapi.alipaydev.com/gateway.do
    private String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    public String pay(PayVo vo) throws AlipayApiException {

        //AlipayClient alipayClient = new DefaultAlipayClient(AlipayTemplate.gatewayUrl, AlipayTemplate.app_id, AlipayTemplate.merchant_private_key, "json", AlipayTemplate.charset, AlipayTemplate.alipay_public_key, AlipayTemplate.sign_type);
        //1、根据支付宝的配置生成一个支付客户端
        AlipayClient alipayClient = new DefaultAlipayClient(gatewayUrl,
                app_id, merchant_private_key, "json",
                charset, alipay_public_key, sign_type);

        //2、创建一个支付请求 //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(return_url);
        alipayRequest.setNotifyUrl(notify_url);

        //商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = vo.getOut_trade_no();
        //付款金额，必填
        String total_amount = vo.getTotal_amount();
        //订单名称，必填
        String subject = vo.getSubject();
        //商品描述，可空
        String body = vo.getBody();

        alipayRequest.setBizContent("{\"out_trade_no\":\"" + out_trade_no + "\","
                + "\"total_amount\":\"" + total_amount + "\","
                + "\"subject\":\"" + subject + "\","
                + "\"body\":\"" + body + "\","
                + "\"timeout_express\":\""+ timeout +"\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        String result = alipayClient.pageExecute(alipayRequest).getBody();

        //会收到支付宝的响应，响应的是一个页面，只要浏览器显示这个页面，就会自动来到支付宝的收银台页面
        System.out.println("支付宝的响应：" + result);

        return result;

    }
}
