package com.clx4399.gulimall.product.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clx4399.common.constant.ProductConstant;
import com.clx4399.common.to.SkuEsModel;
import com.clx4399.common.to.SkuRedution;
import com.clx4399.common.to.SpuBoundTo;
import com.clx4399.common.utils.PageUtils;
import com.clx4399.common.utils.Query;
import com.clx4399.common.utils.R;
import com.clx4399.gulimall.product.dao.SpuInfoDao;
import com.clx4399.gulimall.product.entity.*;
import com.clx4399.gulimall.product.feign.CouponFeignServices;
import com.clx4399.gulimall.product.feign.SearchFeignServices;
import com.clx4399.gulimall.product.feign.WareFeignServices;
import com.clx4399.gulimall.product.service.*;
import com.clx4399.gulimall.product.vo.SkuHasStockVo;
import com.clx4399.gulimall.product.vo.spusavevo.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Autowired
    private SpuInfoDescService spuInfoDescService;

    @Autowired
    private SpuImagesService spuImagesService;

    @Autowired
    private AttrService attrService;

    @Autowired
    private ProductAttrValueService productAttrValueService;

    @Autowired
    private SkuInfoService skuInfoService;

    @Autowired
    private SkuImagesService skuImagesService;

    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    private CouponFeignServices couponFeignServices;

    @Autowired
    private WareFeignServices wareFeignServices;

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SearchFeignServices searchFeignServices;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void  saveForSpuSaveVo(SpuSaveVo spuInfo) {

        //保存spu基本信息
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(spuInfo,spuInfoEntity);
        spuInfoEntity.setUpdateTime(new Date());
        spuInfoEntity.setCreateTime(new Date());
        this.saveSpuInfo(spuInfoEntity);

        //保存spu的描述图片信息 pms_spu_desc
        List<String> decript = spuInfo.getDecript();
        SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
        spuInfoDescEntity.setSpuId(spuInfoEntity.getId());
        spuInfoDescEntity.setDecript(String.join(",",decript));
        spuInfoDescService.saveSpuDescInfo(spuInfoDescEntity);

        //保存spu图片集 pms_sku_images
        List<String> images = spuInfo.getImages();
        spuImagesService.saveImagesInfo(spuInfoEntity.getId(),images);

        //保存spu的规格参数 pms_spu_attr_value
        List<BaseAttrs> baseAttrs = spuInfo.getBaseAttrs();
        List<ProductAttrValueEntity> collect = baseAttrs.stream().map(item -> {
            ProductAttrValueEntity productAttrValueEntity = new ProductAttrValueEntity();
            productAttrValueEntity.setSpuId(spuInfoEntity.getId());
            productAttrValueEntity.setAttrId(item.getAttrId());
            AttrEntity attrEntity = attrService.getById(item.getAttrId());
            productAttrValueEntity.setAttrName(attrEntity.getAttrName());
            productAttrValueEntity.setAttrValue(item.getAttrValues());
            productAttrValueEntity.setQuickShow(item.getShowDesc());
            return productAttrValueEntity;
        }).collect(Collectors.toList());
        productAttrValueService.saveAttrValueInfos(collect);

        //保存spu积分信息
        Bounds bounds = spuInfo.getBounds();
        SpuBoundTo spuBoundTo = new SpuBoundTo();
        BeanUtils.copyProperties(bounds,spuBoundTo);
        spuBoundTo.setSpuId(spuInfoEntity.getId());
        R r = couponFeignServices.saveSpuBounds(spuBoundTo);
        if (r.getCode()!=0){
            log.error("远程调用保存spu积分信息服务失败！");
        }

        //保存spu对应的sku信息
        List<Skus> skus = spuInfo.getSkus();
        if (skus!=null && skus.size()>0){
            skus.forEach(item->{
                String defaultImg = "";
                for (Images image : item.getImages()) {
                    if (image.getDefaultImg()==1){
                        defaultImg = image.getImgUrl();
                    }
                }
                //保存sku的基本信息
                SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
                BeanUtils.copyProperties(item,skuInfoEntity);
                skuInfoEntity.setBrandId(spuInfoEntity.getBrandId());
                skuInfoEntity.setCatalogId(spuInfoEntity.getCatalogId());
                skuInfoEntity.setSaleCount(0L);
                skuInfoEntity.setSpuId(spuInfoEntity.getId());
                skuInfoEntity.setSkuDefaultImg(defaultImg);
                skuInfoService.saveSkuInfo(skuInfoEntity);

                //保存sku的图片信息
                Long skuId = skuInfoEntity.getSkuId();
                List<SkuImagesEntity> collect1 = item.getImages().stream().map(img -> {
                    SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                    skuImagesEntity.setDefaultImg(img.getDefaultImg());
                    skuImagesEntity.setImgUrl(img.getImgUrl());
                    skuImagesEntity.setSkuId(skuId);
                    return skuImagesEntity;
                }).filter(entity-> StringUtils.isEmpty(entity.getImgUrl()))
                        .collect(Collectors.toList());
                skuImagesService.saveBatch(collect1);
                //保存sku的销售属性信息
                List<SkuSaleAttrValueEntity> collect2 = item.getAttr().stream().map(saleAttr -> {
                    SkuSaleAttrValueEntity skuSaleAttrValueEntity = new SkuSaleAttrValueEntity();
                    BeanUtils.copyProperties(saleAttr, skuSaleAttrValueEntity);
                    skuSaleAttrValueEntity.setSkuId(skuId);
                    return skuSaleAttrValueEntity;
                 }).collect(Collectors.toList());
                skuSaleAttrValueService.saveBatch(collect2);

                //sku的销售属性，满减信息
                SkuRedution skuRedution = new SkuRedution();
                BeanUtils.copyProperties(item,skuRedution);
                skuRedution.setSkuId(skuId);
                if (skuRedution.getFullCount()>0||skuRedution.getFullPrice().compareTo(new BigDecimal("0"))==1) {
                    R r1 = couponFeignServices.saveSkuRedution(skuRedution);
                    if (r1.getCode() != 0) {
                        log.error("远程调用保存sku的销售属性，满减信息服务失败！");
                    }
                }

            });
        }
    }

    @Override
    public void saveSpuInfo(SpuInfoEntity spuInfoEntity) {
        this.baseMapper.insert(spuInfoEntity);
    }

    @Override
    public PageUtils queryPageBySpu(Map<String, Object> params) {
        QueryWrapper<SpuInfoEntity> spuInfoEntityQueryWrapper = new QueryWrapper<>();

        String status = (String) params.get("status");
        String key = (String) params.get("key");
        String brandId = (String) params.get("brandId");
        String catelogId = (String) params.get("catelogId");

        if (StringUtils.isNotBlank(key)){
            spuInfoEntityQueryWrapper
                    .and(w-> w.eq("id",key).or().like("spu_name",key));
        }
        if (StringUtils.isNotBlank(status)){
            spuInfoEntityQueryWrapper.eq("publish_status",status);
        }
        if (StringUtils.isNotBlank(brandId)){
            spuInfoEntityQueryWrapper.eq("brand_id",brandId);
        }
        if (StringUtils.isNotBlank(catelogId)){
            spuInfoEntityQueryWrapper.eq("catalog_id",catelogId);
        }

        IPage<SpuInfoEntity> page = this.page(new Query<SpuInfoEntity>().getPage(params),
                spuInfoEntityQueryWrapper);
        return new PageUtils(page);
    }

    @Override
    public void getSpuInfoBySkuId(Long spuId) {

        /*1获取相对应sku信息*/
        List<SkuInfoEntity> infoEntityList = skuInfoService.getSkuBySpuId(spuId);

        /*获取相对应商品属性信息*/
        List<ProductAttrValueEntity> attrValueEntities = productAttrValueService.list(new QueryWrapper<ProductAttrValueEntity>().eq("spu_id", spuId));
        List<Long> ProductAttrIdLists = attrValueEntities.stream().map(item -> item.getAttrId()).collect(Collectors.toList());
        List<Long> attrIds = attrService.selectSearchAttrIds(ProductAttrIdLists);
        Set attrIdsSet = new HashSet(attrIds);
        List<SkuEsModel.Attr> attrList = attrValueEntities.stream()
                .filter(i -> attrIdsSet.contains(i.getAttrId()))
                .map(item -> {
            SkuEsModel.Attr attr = new SkuEsModel.Attr();
            BeanUtils.copyProperties(item, attr);
            return attr;
        }).collect(Collectors.toList());

        /*校验商品是否有库存*/
        Map<Long, Boolean> stockMap = null;
        try {
            List<Long> collectId = infoEntityList.stream().map(SkuInfoEntity::getSkuId).collect(Collectors.toList());
            R<List<SkuHasStockVo>> skuHasStock = wareFeignServices.getSkuHasStock(collectId);
            List<SkuHasStockVo> skuHasStockData = skuHasStock.getData(new TypeReference<List<SkuHasStockVo>>() {
            });
            stockMap = skuHasStockData.stream().collect(Collectors.toMap(SkuHasStockVo::getSkuId, SkuHasStockVo::getStock));
        }catch (Exception e){
            log.error("远程调用库存接口异常:{}",e);
        }

        /*2封装每个sku的信息*/
        Map<Long, Boolean> finalStockMap = stockMap;
        List<SkuEsModel> skuEsModels = infoEntityList.stream().map(sku -> {
            SkuEsModel skuEsModel = new SkuEsModel();
            BeanUtils.copyProperties(sku, skuEsModel);
            skuEsModel.setSkuPrice(sku.getPrice());
            skuEsModel.setSkuImg(sku.getSkuDefaultImg());
            //TODO 2、热度评分。0
            skuEsModel.setHotScore(0L);
            //TODO 3、查询品牌和分类的名字信息
            BrandEntity brandEntity = brandService.getById(sku.getBrandId());
            skuEsModel.setBrandName(brandEntity.getName());
            skuEsModel.setBrandImg(brandEntity.getLogo());
            CategoryEntity categoryEntity = categoryService.getById(sku.getCatalogId());
            skuEsModel.setCatalogName(categoryEntity.getName());
            //设置可搜索属性
            skuEsModel.setAttrs(attrList);
            //设置是否有库存
            skuEsModel.setHasStock(finalStockMap==null?false:finalStockMap.get(sku.getSkuId()));
            return skuEsModel;
        }).collect(Collectors.toList());

        /*3调用search将数据给es保存*/
        R r = searchFeignServices.productStatusUp(skuEsModels);
        if (r.getCode()==0){
            this.baseMapper.upSpuStatus(spuId,ProductConstant.ProductStatusEnum.SPU_UP.getCode());
        }else {
            log.error("商品检索es保存远程接口调用失败!");
        }
    }

}
