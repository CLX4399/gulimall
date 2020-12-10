package com.clx4399.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.clx4399.common.utils.PageUtils;
import com.clx4399.gulimall.ware.entity.WareSkuEntity;

import java.util.Map;

/**
 * 商品库存
 *
 * @author clx
 * @email whtclx@gmail.com
 * @date 2020-12-07 16:10:22
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * @param skuId
	 * @param wareId
	 * @param skuNum
     * @return void
     * @author CLX
     * @describe: 保存商品入库信息
     * @date 2020/12/9 19:09
     */
    void addStock(Long skuId, Long wareId, Integer skuNum);
}

