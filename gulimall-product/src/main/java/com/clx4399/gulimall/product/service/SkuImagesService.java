package com.clx4399.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.clx4399.common.utils.PageUtils;
import com.clx4399.gulimall.product.entity.SkuImagesEntity;

import java.util.List;
import java.util.Map;

/**
 * sku图片
 *
 * @author clx
 * @email whtclx@gmail.com
 * @date 2020-07-27 00:49:18
 */
public interface SkuImagesService extends IService<SkuImagesEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * @param skuId
     * @return java.util.List<com.clx4399.gulimall.product.entity.SpuImagesEntity>
     * @author CLX
     * @describe: 通过skuid获取图片信息
     * @date 2021/7/15 17:50
     */
    List<SkuImagesEntity> getImagesBySkuId(String skuId);
}

