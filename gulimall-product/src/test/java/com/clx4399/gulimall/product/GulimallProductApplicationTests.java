package com.clx4399.gulimall.product;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.clx4399.gulimall.product.entity.BrandEntity;
import com.clx4399.gulimall.product.service.BrandService;
import com.clx4399.gulimall.product.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class GulimallProductApplicationTests {

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

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
