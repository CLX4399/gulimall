package com.clx4399.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clx4399.common.utils.PageUtils;
import com.clx4399.common.utils.Query;
import com.clx4399.gulimall.product.dao.SkuInfoDao;
import com.clx4399.gulimall.product.entity.SkuImagesEntity;
import com.clx4399.gulimall.product.entity.SkuInfoEntity;
import com.clx4399.gulimall.product.entity.SpuImagesEntity;
import com.clx4399.gulimall.product.entity.SpuInfoDescEntity;
import com.clx4399.gulimall.product.service.*;
import com.clx4399.gulimall.product.vo.SkuItemSaleAttrVo;
import com.clx4399.gulimall.product.vo.SkuItemVo;
import com.clx4399.gulimall.product.vo.SpuItemAttrGroupVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    @Autowired
    private SkuImagesService imagesService;

    @Autowired
    private SpuInfoDescService descService;

    @Autowired
    private AttrGroupService attrGroupService;

    @Autowired
    private SkuSaleAttrValueService saleAttrValueService;

    @Autowired
    private ThreadPoolExecutor executor;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<SkuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveSkuInfo(SkuInfoEntity skuInfoEntity) {
        this.save(skuInfoEntity);
    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<SkuInfoEntity> skuInfoEntityQueryWrapper = new QueryWrapper<>();

        String key = (String) params.get("key");
        String catelogId = (String) params.get("catelogId");
        String brandId = (String) params.get("brandId");
        String min = (String) params.get("min");
        String max = (String) params.get("max");

        if (StringUtils.isNotBlank(key) && !"0".equalsIgnoreCase(key)){
            skuInfoEntityQueryWrapper.and(w->{
                w.eq("sku_id",key).or().like("sku_name",key);
            });
        }

        if (StringUtils.isNotBlank(catelogId) && !"0".equalsIgnoreCase(catelogId)){
            skuInfoEntityQueryWrapper.eq("catalog_id",catelogId);
        }
        if (StringUtils.isNotBlank(brandId) && !"0".equalsIgnoreCase(brandId)){
            skuInfoEntityQueryWrapper.eq("brand_id",brandId);
        }

        if (StringUtils.isNotBlank(min) && !"0".equalsIgnoreCase(min)){
            skuInfoEntityQueryWrapper.ge("price",min);
        }

        if (StringUtils.isNotBlank(max) && !"0".equalsIgnoreCase(max)){
            skuInfoEntityQueryWrapper.le("price",max);
        }

        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                skuInfoEntityQueryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public List<SkuInfoEntity> getSkuBySpuId(Long skuId) {
        List<SkuInfoEntity> infoEntityList = baseMapper.selectList(new QueryWrapper<SkuInfoEntity>().eq("spu_id", skuId));
        return infoEntityList;
    }

    @Override
    public SkuItemVo item(String skuId) throws ExecutionException, InterruptedException {
        SkuItemVo skuItemVo = new SkuItemVo();


        CompletableFuture<SkuInfoEntity> infoFuture = CompletableFuture.supplyAsync(() -> {
            //获取sku基本信息 pms_sku_info
            SkuInfoEntity skuInfoEntity = getById(skuId);
            skuItemVo.setInfo(skuInfoEntity);
            return skuInfoEntity;
        }, executor);

        CompletableFuture<Void> imagesFuture = CompletableFuture.runAsync(() -> {
            //获取sku图片信息 pms_sku_images
            List<SkuImagesEntity> images = imagesService.getImagesBySkuId(skuId);
            skuItemVo.setImages(images);
        }, executor);


        CompletableFuture<Void> saleAttrsFuture = infoFuture.thenAcceptAsync((res) -> {
            //获取spu的sku组合信息
            List<SkuItemSaleAttrVo> saleAttrVos = saleAttrValueService.getSkuSaleAttrsBy(res.getSpuId());
            skuItemVo.setSaleAttrs(saleAttrVos);
        }, executor);


        CompletableFuture<Void> descFuture = infoFuture.thenAcceptAsync((res) -> {
            //获取spu的介绍
            SpuInfoDescEntity descEntity = descService.getOne(new QueryWrapper<SpuInfoDescEntity>().eq("spu_id", res.getSpuId()));
            skuItemVo.setDesc(descEntity);
        }, executor);


        CompletableFuture<Void> attGroupFuture = infoFuture.thenAcceptAsync((res) -> {
            //获取spu的参数规格信息
            List<SpuItemAttrGroupVo> attrGroupVos = attrGroupService.getAttrGroupWithAttrs(res.getSpuId(), res.getCatalogId());
            skuItemVo.setAttrGroups(attrGroupVos);
        }, executor);

        CompletableFuture.allOf(imagesFuture,saleAttrsFuture,descFuture,attGroupFuture).get();

        return skuItemVo;
    }

}
