package com.clx4399.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.clx4399.common.utils.PageUtils;
import com.clx4399.gulimall.product.entity.SpuInfoEntity;
import com.clx4399.gulimall.product.vo.spusavevo.SpuSaveVo;

import java.util.Map;

/**
 * spu信息
 *
 * @author clx
 * @email whtclx@gmail.com
 * @date 2020-07-27 00:49:19
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveForSpuSaveVo(SpuSaveVo spuInfo);

    void saveSpuInfo(SpuInfoEntity spuInfoEntity);

    /**
     * @param params
     * @return com.clx4399.common.utils.PageUtils
     * @author CLX
     * @describe:  spu信息检索查询
     * @date 2020/12/7 10:42
     */
    PageUtils queryPageBySpu(Map<String, Object> params);

    /**
     * @param skuId
     * @return com.clx4399.gulimall.product.entity.SpuInfoEntity
     * @author CLX
     * @describe: 通过skuid上架商品
     * @date 2021/4/13 19:36
     */
    void getSpuInfoBySkuId(Long skuId);
}

