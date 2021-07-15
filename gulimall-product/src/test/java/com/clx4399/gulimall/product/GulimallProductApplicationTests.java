package com.clx4399.gulimall.product;


import com.clx4399.gulimall.product.service.AttrGroupService;
import com.clx4399.gulimall.product.service.BrandService;
import com.clx4399.gulimall.product.service.CategoryService;
import com.clx4399.gulimall.product.vo.SpuItemAttrGroupVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

@Slf4j
@SpringBootTest
class   GulimallProductApplicationTests {

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AttrGroupService attrGroupService;

    @Test
    void contextLoads() {
        List<SpuItemAttrGroupVo> attrGroupVos = attrGroupService.getAttrGroupWithAttrs(13l,225l);
        log.info(attrGroupVos.toString());
    }

}
