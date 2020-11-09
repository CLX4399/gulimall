package com.clx4399.gulimall.product.service.impl;

import com.clx4399.gulimall.product.entity.AttrGroupEntity;
import com.clx4399.gulimall.product.service.CategoryBrandRelationService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
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

    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

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
        }).sorted((enum1,enum2) -> {
            return ((enum1.getSort() == null ? 0 : enum1.getSort()) - (enum2.getSort() ==null ? 0 : enum2.getSort()));
        }).collect(Collectors.toList());

        return treeForList;
    }

    @Override
    public Long[] getAllPath(Long attrGroupId) {
        List<Long> longs = new ArrayList<>();
        CategoryEntity categoryEntity = this.getById(attrGroupId);
        this.recursionAllPath(categoryEntity,longs);

        Collections.reverse(longs);

        return longs.toArray(new Long[longs.size()-1]);
    }

    @Override
    public void updateDetail(CategoryEntity category) {
        this.updateById(category);
        if (!StringUtils.isEmpty(category.getName())){
            categoryBrandRelationService.updateCategory(category.getCatId(),category.getName());
        }
    }

    private void recursionAllPath(CategoryEntity categoryEntity, List<Long> longs) {
        longs.add(categoryEntity.getCatId());
        if (categoryEntity.getParentCid()!=0){
            CategoryEntity category = this.getById(categoryEntity.getParentCid());
            recursionAllPath(category,longs);
        }
    }


    @Override
    public void removeMenuByIds(List<Long> asList) {

        //TODO 判断是否符合删除条件
        baseMapper.deleteBatchIds(asList);

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
        }).sorted((enum1,enum2) -> {
            return ((enum1.getSort() == null ? 0 : enum1.getSort()) - (enum2.getSort() ==null ? 0 : enum2.getSort()));
        }).collect(Collectors.toList());
        return childrenList;
    }


}