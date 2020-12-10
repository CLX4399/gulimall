package com.clx4399.gulimall.ware.dao;

import com.clx4399.gulimall.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 商品库存
 * 
 * @author clx
 * @email whtclx@gmail.com
 * @date 2020-12-07 16:10:22
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {

    /**
     * @param skuId
	 * @param wareId
	 * @param skuNum
     * @return void
     * @author CLX
     * @describe:  更新库存信息
     * @date 2020/12/9 19:18
     */
    void updateStock(@Param("skuId") Long skuId, @Param("wareId") Long wareId, @Param("skuNum") Integer skuNum);
}
