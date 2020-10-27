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

        InputStream inputStream = new FileInputStream("C:\\Users\\WhtCl\\Downloads\\三体智子高清4k动漫壁纸_彼岸图网.jpg");
        // download file to local
        ossClient.putObject("gulimal-clx4399", "111.jpg", new File("D:\\111.jpg"));
    }

}
