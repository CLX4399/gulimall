package com.clx4399.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.clx4399.common.utils.PageUtils;
import com.clx4399.gulimall.product.entity.SkuImagesEntity;
import com.clx4399.gulimall.product.entity.SpuImagesEntity;

import java.util.List;
import java.util.Map;

/**
 * spu图片
 *
 * @author clx
 * @email whtclx@gmail.com
 * @date 2020-07-27 00:49:19
 */
public interface SpuImagesService extends IService<SpuImagesEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveImagesInfo(Long id, List<String> images);


}

