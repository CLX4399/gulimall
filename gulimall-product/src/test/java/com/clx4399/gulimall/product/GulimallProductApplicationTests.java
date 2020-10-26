package com.clx4399.gulimall.product;


import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.GetObjectRequest;
import com.clx4399.gulimall.product.service.BrandService;
import com.clx4399.gulimall.product.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@SpringBootTest
class GulimallProductApplicationTests {

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private OSSClient ossClient;

    @Test
    void  testUplaod() throws FileNotFoundException {

        InputStream inputStream = new FileInputStream("C:\\Users\\WhtCl\\Downloads\\三体智子高清4k动漫壁纸_彼岸图网.jpg");
            // download file to local
            ossClient.putObject("gulimal-clx4399", "三体智子高清4k动漫壁纸_彼岸图网.jpg", new File("C:\\Users\\WhtCl\\Downloads\\三体智子高清4k动漫壁纸_彼岸图网.jpg"));
    }

    @Test
    void contextLoads() {

       /* BrandEntity brandEntity = new BrandEntity();
        brandEntity.setBrandId(1L);
        brandEntity.setDescript("联想笔记本");
        brandService.updateById(brandEntity);*/

        /*List list = brandService.list(new QueryWrapper<BrandEntity>().eq("brand_id",1L));
        list.forEach((item)->{
            System.out.println(item);
        });*/

        categoryService.listWithTree();

    }

}
