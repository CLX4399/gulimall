package com.clx4399.gulimall.product.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clx4399.common.utils.PageUtils;
import com.clx4399.common.utils.Query;

import com.clx4399.gulimall.product.dao.CategoryDao;
import com.clx4399.gulimall.product.entity.CategoryEntity;
import com.clx4399.gulimall.product.service.CategoryService;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        List<CategoryEntity> list = baseMapper.selectList(null);

        List<CategoryEntity> treeForList = list.stream().filter(categoryEntity ->
                categoryEntity.getParentCid() == 0).map(item -> {
            item.setChildren(getChildren(item, list));
            return item;
        }).collect(Collectors.toList());

        return treeForList;
    }

    /**
     * @param entity
	 * @param list
     * @return java.util.List<com.clx4399.gulimall.product.entity.CategoryEntity>
     * @author CLX
     * @describe: 递归查找所有顶级菜单下的各个子菜单，和子菜单下的子菜单
     * @date 2020/8/18 11:38
     */
    private List<CategoryEntity> getChildren(CategoryEntity entity, List<CategoryEntity> list) {
        List<CategoryEntity> childrenList = list.stream().filter(categoryEntity ->
                Objects.equals(categoryEntity.getParentCid(), entity.getCatId())).map(item -> {
            item.setChildren(getChildren(item, list));
            return item;
        }).collect(Collectors.toList());
        return childrenList;
    }


}