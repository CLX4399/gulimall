package com.clx4399.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.clx4399.common.utils.PageUtils;
import com.clx4399.gulimall.product.entity.CategoryEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author clx
 * @email whtclx@gmail.com
 * @date 2020-07-27 00:49:19
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * @author CLX
     * @describe: 查找所有菜单栏目
     * @date 2020/8/18 9:44
     * @param
     * @return java.util.List<com.clx4399.gulimall.product.entity.CategoryEntity>
     */
    List<CategoryEntity> listWithTree();

    /**
     * @param asList
     * @return void
     * @author CLX
     * @describe: 删除菜单（判断是否符合删除条件）
     * @date 2020/8/30 16:18
     */
    void removeMenuByIds(List<Long> asList);
}

