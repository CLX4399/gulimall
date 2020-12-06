package com.clx4399.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.clx4399.common.to.SkuRedution;
import com.clx4399.common.utils.PageUtils;
import com.clx4399.gulimall.coupon.entity.SkuFullReductionEntity;

import java.util.Map;

/**
 * 商品满减信息
 *
 * @author clx
 * @email whtclx@gmail.com
 * @date 2020-08-02 21:24:05
 */
public interface SkuFullReductionService extends IService<SkuFullReductionEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveRedution(SkuRedution skuRedution);
}

