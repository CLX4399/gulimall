package com.clx4399.gulimall.product.dao;

import com.clx4399.gulimall.product.entity.AttrGroupEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clx4399.gulimall.product.vo.SpuItemAttrGroupVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 属性分组
 *
 * @author clx
 * @email whtclx@gmail.com
 * @date 2020-07-27 00:49:20
 */
@Mapper
public interface AttrGroupDao extends BaseMapper<AttrGroupEntity> {

    /**
     * @param spuId
	 * @param catalogId
     * @return java.util.List<com.clx4399.gulimall.product.vo.SpuItemAttrGroupVo>
     * @author CLX
     * @describe: 获取spu规格参数
     * @date 2021/7/15 18:34
     */
    List<SpuItemAttrGroupVo> getAttrGroupWithAttrs(@Param("spuId") Long spuId, @Param("catalogId") Long catalogId);
}
