package com.clx4399.gulimall.gulimallthridparty;

import com.aliyun.oss.OSSClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@SpringBootTest
class GulimallThridPartyApplicationTests {

    @Autowired
    private OSSClient ossClient;

    @Test
    void  testUplaod() throws FileNotFoundException {

    }

}
