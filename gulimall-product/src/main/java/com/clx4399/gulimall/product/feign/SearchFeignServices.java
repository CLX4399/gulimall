package com.clx4399.gulimall.product.feign;

import com.clx4399.common.to.SkuEsModel;
import com.clx4399.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author WhtCl
 * @program: gulimall
 * @description: es检索模块feign接口
 * @date 2021-04-14 14:13:19
 */
@FeignClient("gulimall-search")
public interface SearchFeignServices {

    /*** 上架商品*/
    @PostMapping("search/product/upproduct") // ElasticSaveController
    R productStatusUp(@RequestBody List<SkuEsModel> skuEsModels);

}
