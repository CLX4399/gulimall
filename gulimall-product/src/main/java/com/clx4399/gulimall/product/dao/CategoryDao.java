package com.clx4399.gulimall.product.dao;

import com.clx4399.gulimall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author clx
 * @email whtclx@gmail.com
 * @date 2020-07-27 00:49:19
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
