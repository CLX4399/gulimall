package com.clx4399.gulimall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clx4399.common.utils.PageUtils;
import com.clx4399.common.utils.Query;
import com.clx4399.gulimall.product.dao.CategoryDao;
import com.clx4399.gulimall.product.entity.CategoryEntity;
import com.clx4399.gulimall.product.service.CategoryBrandRelationService;
import com.clx4399.gulimall.product.service.CategoryService;
import com.clx4399.gulimall.product.vo.Catalog3Vo;
import com.clx4399.gulimall.product.vo.Catelog2Vo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedissonClient redisson;

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

    @CacheEvict(value = {"category"},allEntries = true)
    @Override
    public void updateDetail(CategoryEntity category) {
        this.updateById(category);
        if (!StringUtils.isEmpty(category.getName())){
            categoryBrandRelationService.updateCategory(category.getCatId(),category.getName());
        }
    }

    @Cacheable(cacheNames =  {"categroy"} , key = "#root.methodName")
    @Override
    public List<CategoryEntity> getLevel1Categroies(int level) {
        List<CategoryEntity> list = baseMapper.selectList(new QueryWrapper<CategoryEntity>()
                .eq("cat_level", level).orderByAsc("cat_id"));
        return list;
    }

    public Map<String, List<Catelog2Vo>> getCateLogLevel2FromRedisTemple() {

        String catelogJson = stringRedisTemplate.opsForValue().get("catelogJson");
        if (StringUtils.isBlank(catelogJson)){
                //Map<String, List<Catelog2Vo>> cateLogLevel2FromDB = getCateLogLevel2FromDB();
                Map<String, List<Catelog2Vo>> cateLogLevel2FromDB = null;
                String jsonString = JSON.toJSONString(cateLogLevel2FromDB);
                stringRedisTemplate.opsForValue().set("catelogJson", jsonString);
            return cateLogLevel2FromDB;
        }
        Map<String, List<Catelog2Vo>> stringListMap = JSON.parseObject(catelogJson, new TypeReference<Map<String, List<Catelog2Vo>>>() {});
        return stringListMap;
    }

    @Cacheable(cacheNames =  {"categroy"} , key = "#root.methodName")
    @Override
    public Map<String, List<Catelog2Vo>> getCateLogLevel2() {
            log.info("三级分类数据库查询!");
            List<CategoryEntity> list = baseMapper.selectList(null);

            /*获取一级分类*/
            List<CategoryEntity> level1 = getCategoryByLevel(list, 0L);

            /*获取二级分类*/
            Map<String, List<Catelog2Vo>> listMap = level1.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
                List<CategoryEntity> categoryL2 = getCategoryByLevel(list, v.getCatId());
                if (!categoryL2.isEmpty()) {
                    List<Catelog2Vo> collect = categoryL2.stream().map(item -> {
                        List<CategoryEntity> datalogL3 = getCategoryByLevel(list, item.getCatId());
                        List<Catalog3Vo> catalog3Vos = datalogL3.stream().map(data -> {
                            Catalog3Vo catalog3Vo = new Catalog3Vo(item.getCatId().toString(), data.getCatId().toString(), data.getName());
                            return catalog3Vo;
                        }).collect(Collectors.toList());
                        Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString(), catalog3Vos, item.getCatId().toString(), item.getName());
                        return catelog2Vo;
                    }).collect(Collectors.toList());
                    return collect;
                }
                return null;
            }));

            return listMap;
    }

    public List<CategoryEntity> getCategoryByLevel(List<CategoryEntity> list,long parentId) {
        return list.stream().filter(item-> item.getParentCid()==parentId).collect(Collectors.toList());
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
