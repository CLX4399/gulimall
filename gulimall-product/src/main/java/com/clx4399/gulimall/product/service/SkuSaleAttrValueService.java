package com.clx4399.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.clx4399.common.utils.PageUtils;
import com.clx4399.gulimall.product.entity.SkuSaleAttrValueEntity;
import com.clx4399.gulimall.product.vo.SkuItemSaleAttrVo;

import java.util.List;
import java.util.Map;

/**
 * sku销售属性&值
 *
 * @author clx
 * @email whtclx@gmail.com
 * @date 2020-07-27 00:49:18
 */
public interface SkuSaleAttrValueService extends IService<SkuSaleAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * @param spuId
     * @return java.util.List<com.clx4399.gulimall.product.vo.SkuItemSaleAttrVo>
     * @author CLX
     * @describe: 根据spuid销售属性信息
     * @date 2021/7/15 19:33
     */
    List<SkuItemSaleAttrVo> getSkuSaleAttrsBy(Long spuId);

    /**
     * @param skuId
     * @return java.util.List<java.lang.String>
     * @author CLX
     * @describe: 通过skuid获取销售属性组合
     * @date 2021/8/13 15:24
     */
    List<String> getSkuSaleAttrValuesAsStringList(Long skuId);

}

