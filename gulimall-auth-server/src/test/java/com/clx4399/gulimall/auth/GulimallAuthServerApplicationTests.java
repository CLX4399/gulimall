package com.clx4399.gulimall.auth;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.clx4399.gulimall.auth.vdoq.Data;
import com.clx4399.gulimall.auth.vdoq.JsonRootBean;
import org.apache.commons.lang.StringEscapeUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GulimallAuthServerApplicationTests {

    @Test
    void contextLoads() {

        String message = "{\\\"code\\\":1,\\\"message\\\":\\\"success\\\",\\\"data\\\":{\\\"total\\\":4,\\\"list\\\":[{\\\"id\\\":3,\\\"province\\\":\\\"广东\\\",\\\"adcode\\\":\\\"440100\\\",\\\"city\\\":\\\"广州市\\\",\\\"reporttime\\\":\\\"2021-07-27T00:00:00\\\",\\\"date\\\":\\\"2021-07-28T00:00:00\\\",\\\"week\\\":\\\"3\\\",\\\"dayweather\\\":\\\"多云\\\",\\\"nightweather\\\":\\\"雷阵雨\\\",\\\"daytemp\\\":\\\"36\\\",\\\"nighttemp\\\":\\\"27\\\",\\\"daywind\\\":\\\"北\\\",\\\"nightwind\\\":\\\"北\\\",\\\"daypower\\\":\\\"≤3\\\",\\\"nightpower\\\":\\\"≤3\\\"},{\\\"id\\\":5,\\\"province\\\":\\\"广东\\\",\\\"adcode\\\":\\\"440100\\\",\\\"city\\\":\\\"广州市\\\",\\\"reporttime\\\":\\\"2021-07-27T00:00:00\\\",\\\"date\\\":\\\"2021-07-29T00:00:00\\\",\\\"week\\\":\\\"4\\\",\\\"dayweather\\\":\\\"雷阵雨\\\",\\\"nightweather\\\":\\\"中雨\\\",\\\"daytemp\\\":\\\"34\\\",\\\"nighttemp\\\":\\\"26\\\",\\\"daywind\\\":\\\"北\\\",\\\"nightwind\\\":\\\"北\\\",\\\"daypower\\\":\\\"≤3\\\",\\\"nightpower\\\":\\\"≤3\\\"},{\\\"id\\\":7,\\\"province\\\":\\\"广东\\\",\\\"adcode\\\":\\\"440100\\\",\\\"city\\\":\\\"广州市\\\",\\\"reporttime\\\":\\\"2021-07-27T00:00:00\\\",\\\"date\\\":\\\"2021-07-30T00:00:00\\\",\\\"week\\\":\\\"5\\\",\\\"dayweather\\\":\\\"中雨\\\",\\\"nightweather\\\":\\\"中雨\\\",\\\"daytemp\\\":\\\"32\\\",\\\"nighttemp\\\":\\\"25\\\",\\\"daywind\\\":\\\"北\\\",\\\"nightwind\\\":\\\"北\\\",\\\"daypower\\\":\\\"≤3\\\",\\\"nightpower\\\":\\\"≤3\\\"},{\\\"id\\\":343,\\\"province\\\":\\\"广东\\\",\\\"adcode\\\":\\\"440100\\\",\\\"city\\\":\\\"广州市\\\",\\\"reporttime\\\":\\\"2021-07-28T00:00:00\\\",\\\"date\\\":\\\"2021-07-31T00:00:00\\\",\\\"week\\\":\\\"6\\\",\\\"dayweather\\\":\\\"中雨\\\",\\\"nightweather\\\":\\\"雷阵雨\\\",\\\"daytemp\\\":\\\"32\\\",\\\"nighttemp\\\":\\\"25\\\",\\\"daywind\\\":\\\"北\\\",\\\"nightwind\\\":\\\"北\\\",\\\"daypower\\\":\\\"≤3\\\",\\\"nightpower\\\":\\\"≤3\\\"}]},\\\"timestamp\\\":1627441431707,\\\"executeTime\\\":8}";

        String s = StringEscapeUtils.unescapeJava(message);
        JsonRootBean data = JSONObject.parseObject(s, JsonRootBean.class);

    }

}
