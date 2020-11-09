package com.clx4399.gulimall.product.dao;

import com.clx4399.gulimall.product.entity.CategoryBrandRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 品牌分类关联
 * 
 * @author clx
 * @email whtclx@gmail.com
 * @date 2020-07-27 00:49:19
 */
@Mapper
public interface CategoryBrandRelationDao extends BaseMapper<CategoryBrandRelationEntity> {

    void updataCategotuBase(@Param("catId")Long catId, @Param("name") String name);
}
