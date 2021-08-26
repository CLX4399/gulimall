package com.clx4399.gulimall.ware.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clx4399.gulimall.ware.entity.WareSkuEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

    /**
     * @param item
     * @return java.lang.Long
     * @author CLX
     * @describe: 获取库存
     * @date 2021/4/13 20:12
     */
    Long getStock(@Param("item") Long item);

    /**
     * @param skuId
     * @return java.util.List<java.lang.Long>
     * @author CLX
     * @describe: 获取商品库存信息
     * @date 2021/8/26 15:25
     */
    List<Long> listWareIdHasSkuStock(@Param("skuId") Long skuId);

    /**
     * @param skuId
	 * @param wareId
	 * @param num 锁定确定订单中的商品
     * @return java.lang.Long
     * @author CLX
     * @describe:
     * @date 2021/8/26 15:28
     */
    Long lockSkuStock(@Param("skuId") Long skuId, @Param("wareId") Long wareId, @Param("num") Integer num);
}
