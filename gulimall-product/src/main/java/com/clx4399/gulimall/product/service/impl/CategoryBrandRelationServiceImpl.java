package com.clx4399.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.clx4399.gulimall.product.dao.BrandDao;
import com.clx4399.gulimall.product.dao.CategoryDao;
import com.clx4399.gulimall.product.entity.BrandEntity;
import com.clx4399.gulimall.product.entity.CategoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clx4399.common.utils.PageUtils;
import com.clx4399.common.utils.Query;

import com.clx4399.gulimall.product.dao.CategoryBrandRelationDao;
import com.clx4399.gulimall.product.entity.CategoryBrandRelationEntity;
import com.clx4399.gulimall.product.service.CategoryBrandRelationService;


@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {

    @Autowired
    private BrandDao brandDao;

    @Autowired
    private CategoryDao categoryDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<CategoryBrandRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveDetail(CategoryBrandRelationEntity categoryBrandRelation) {
        Long brandid = categoryBrandRelation.getBrandId();
        Long catelogId = categoryBrandRelation.getCatelogId();

        BrandEntity brandEntity = brandDao.selectById(brandid);
        CategoryEntity categoryEntity = categoryDao.selectById(catelogId);

        categoryBrandRelation.setBrandName(brandEntity.getName());
        categoryBrandRelation.setCatelogName(categoryEntity.getName());
        this.save(categoryBrandRelation);
    }

    @Override
    public void updateBrand(Long brandId, String name) {
        CategoryBrandRelationEntity categoryBrandRelationEntity = new CategoryBrandRelationEntity();
        categoryBrandRelationEntity.setBrandId(brandId);
        categoryBrandRelationEntity.setBrandName(name);
        this.update(categoryBrandRelationEntity,
                new UpdateWrapper<CategoryBrandRelationEntity>().
                        eq("brand_id",brandId));
    }

    @Override
    public void updateCategory(Long catId, String name) {
        CategoryBrandRelationEntity categoryBrandRelationEntity = new CategoryBrandRelationEntity();
        categoryBrandRelationEntity.setCatelogId(catId);
        categoryBrandRelationEntity.setCatelogName(name);
        this.update(categoryBrandRelationEntity,
                new UpdateWrapper<CategoryBrandRelationEntity>().
                        eq("catelog_id",catId));
        baseMapper.updataCategotuBase(catId,name);
    }

    @Override
    public List<BrandEntity> getBrandsByCatId(Long catId) {

        List<CategoryBrandRelationEntity> brandRelationEntities = this.baseMapper.selectList(new QueryWrapper<CategoryBrandRelationEntity>()
                .eq("catelog_id", catId));
        List<Long> collect = brandRelationEntities.stream().map(item -> item.getBrandId()).collect(Collectors.toList());
        List<BrandEntity> brandEntities = brandDao.selectBatchIds(collect);
        return brandEntities;
    }

}