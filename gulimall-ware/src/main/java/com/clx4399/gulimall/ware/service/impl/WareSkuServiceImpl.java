package com.clx4399.gulimall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clx4399.common.utils.PageUtils;
import com.clx4399.common.utils.Query;
import com.clx4399.common.utils.R;
import com.clx4399.gulimall.ware.dao.WareSkuDao;
import com.clx4399.gulimall.ware.entity.WareSkuEntity;
import com.clx4399.gulimall.ware.feign.ProductFeign;
import com.clx4399.gulimall.ware.service.WareSkuService;
import com.clx4399.gulimall.ware.vo.SkuHasStockVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

    @Autowired
    ProductFeign productFeign;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<WareSkuEntity> queryWrapper = new QueryWrapper<>();
        String skuId = (String) params.get("skuId");
        String wareId = (String) params.get("wareId");

        if (StringUtils.isNotBlank(skuId)){
            queryWrapper.eq("sku_id",skuId);
        }

        if (StringUtils.isNotBlank(wareId)){
            queryWrapper.eq("ware_id",wareId);
        }

        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public void addStock(Long skuId, Long wareId, Integer skuNum) {
        List<WareSkuEntity> skuEntityList = this.baseMapper.selectList(new QueryWrapper<WareSkuEntity>().eq("sku_id", skuId)
                .eq("ware_id", wareId));
        if (skuEntityList.size()>0){
            //更新库存信息
            this.baseMapper.updateStock(skuId,wareId,skuNum);
        }else {
            //新增库存信息
            WareSkuEntity wareSkuEntity = new WareSkuEntity();
            wareSkuEntity.setSkuId(skuId);
            wareSkuEntity.setWareId(wareId);
            wareSkuEntity.setStock(skuNum);
            wareSkuEntity.setStockLocked(0);
            //TODO 处理调用异常时对方法处理
            try {
                R info = productFeign.getSkuInfo(skuId);
                Map<String, Object> skuInfo = (Map<String, Object>) info.get("skuInfo");
                wareSkuEntity.setSkuName((String) skuInfo.get("skuName"));
            }catch (Exception e){
                e.printStackTrace();
            }

            this.save(wareSkuEntity);
        }
    }

    @Override
    public List<SkuHasStockVo> getSkuHasStock(List<Long> skuIds) {
        List<SkuHasStockVo> collect = skuIds.stream().map(item -> {
            SkuHasStockVo skuHasStockVo = new SkuHasStockVo();
            Long stock = baseMapper.getStock(item);
            skuHasStockVo.setSkuId(item);
            skuHasStockVo.setStock(stock==null?false:(stock>0?true:false));
            return skuHasStockVo;
        }).collect(Collectors.toList());
        return collect;
    }

}
