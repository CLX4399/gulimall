package com.clx4399.gulimall.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clx4399.gulimall.product.entity.AttrEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品属性
 *
 * @author clx
 * @email whtclx@gmail.com
 * @date 2020-07-27 00:49:19
 */
@Mapper
public interface AttrDao extends BaseMapper<AttrEntity> {

    List<Long> selectSearchAttrIds(@Param("productAttrIdLists") List<Long> productAttrIdLists);
}
