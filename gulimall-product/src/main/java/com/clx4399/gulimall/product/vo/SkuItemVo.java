package com.clx4399.gulimall.product.vo;

import com.clx4399.gulimall.product.entity.SkuImagesEntity;
import com.clx4399.gulimall.product.entity.SkuInfoEntity;
import com.clx4399.gulimall.product.entity.SpuInfoDescEntity;
import lombok.Data;

import java.util.List;

/**
 * @author WhtCl
 * @program: gulimall
 * @description: 商品详情vo
 * @date 2021-07-07 10:34:58
 */
@Data
public class SkuItemVo {

    //获取sku基本信息 pms_sku_info
    private SkuInfoEntity info;

    //获取sku图片信息 pms_sku_images
    private List<SkuImagesEntity> images;

    //获取spu的sku组合信息
    private List<SkuItemSaleAttrVo> saleAttrs;

    //获取spu的介绍
    private SpuInfoDescEntity desc;

    //获取spu的参数规格信息
    private List<SpuItemAttrGroupVo> attrGroups;

    //是否有货
    private boolean hasStock = true;


}
