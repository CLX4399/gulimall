package com.clx4399.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.clx4399.common.utils.PageUtils;
import com.clx4399.gulimall.product.entity.SkuInfoEntity;
import com.clx4399.gulimall.product.vo.SkuItemVo;

import java.util.List;
import java.util.Map;

/**
 * sku信息
 *
 * @author clx
 * @email whtclx@gmail.com
 * @date 2020-07-27 00:49:18
 */
public interface SkuInfoService extends IService<SkuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSkuInfo(SkuInfoEntity skuInfoEntity);

    /**
     * @param params
     * @return com.clx4399.common.utils.PageUtils
     * @author CLX
     * @describe: 根据查询条件检索结果
     * @date 2020/12/7 11:40
     */
    PageUtils queryPageByCondition(Map<String, Object> params);

    /**
     * @param skuId
     * @return java.util.List<com.clx4399.gulimall.product.entity.SkuInfoEntity>
     * @author CLX
     * @describe: 通过spuid获取sku信息
     * @date 2021/4/13 20:33
     */
    List<SkuInfoEntity> getSkuBySpuId(Long skuId);

    /**
     * @param skuId
     * @return com.clx4399.gulimall.product.vo.SkuItemVo
     * @author CLX
     * @describe: 通过skuid获取基本商品信息
     * @date 2021/7/14 20:08
     */
    SkuItemVo item(String skuId);
}

