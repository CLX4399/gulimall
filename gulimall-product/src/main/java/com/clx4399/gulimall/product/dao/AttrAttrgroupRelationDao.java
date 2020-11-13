package com.clx4399.gulimall.product.dao;

import com.clx4399.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 属性&属性分组关联
 * 
 * @author clx
 * @email whtclx@gmail.com
 * @date 2020-07-27 00:49:20
 */
@Mapper
public interface AttrAttrgroupRelationDao extends BaseMapper<AttrAttrgroupRelationEntity> {

    void batchDeleteAttrRelation(@Param("collect") List<AttrAttrgroupRelationEntity> collect);
}
