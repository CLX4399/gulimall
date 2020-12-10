package com.clx4399.gulimall.ware.service.impl;

import com.clx4399.common.constant.WareConstant;
import com.clx4399.gulimall.ware.entity.PurchaseDetailEntity;
import com.clx4399.gulimall.ware.service.PurchaseDetailService;
import com.clx4399.gulimall.ware.service.WareSkuService;
import com.clx4399.gulimall.ware.vo.PurchaseDoneDetailVo;
import com.clx4399.gulimall.ware.vo.PurchaseDoneVo;
import com.clx4399.gulimall.ware.vo.PurchaseVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clx4399.common.utils.PageUtils;
import com.clx4399.common.utils.Query;

import com.clx4399.gulimall.ware.dao.PurchaseDao;
import com.clx4399.gulimall.ware.entity.PurchaseEntity;
import com.clx4399.gulimall.ware.service.PurchaseService;
import org.springframework.transaction.annotation.Transactional;

import javax.print.attribute.standard.PrinterURI;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {

    @Autowired
    private PurchaseDetailService purchaseDetailService;

    @Autowired
    WareSkuService wareSkuService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageUnreceive(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>().eq("status",0).or().eq("status",1)
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void mergePurchase(PurchaseVo purchaseVo) {
        Long purchaseId = purchaseVo.getPurchaseId();
        if (purchaseId==null){
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            purchaseEntity.setStatus(WareConstant.PurchaseEnum.ALLOCATED.getCode());
            purchaseEntity.setUpdateTime(new Date());
            purchaseEntity.setCreateTime(new Date());
            this.save(purchaseEntity);
            purchaseId =purchaseEntity.getId();
        }

        List<Long> items = purchaseVo.getItems();
        List<PurchaseDetailEntity> purchaseDetailEntities = purchaseDetailService.getBaseMapper().selectBatchIds(items);
        Long finalPurchaseId = purchaseId;
        List<PurchaseDetailEntity> collect = purchaseDetailEntities.stream().map(item -> {
            item.setPurchaseId(finalPurchaseId);
            item.setStatus(WareConstant.PurchaseDetailEnum.ALLOCATED.getCode());
            return item;
        }).collect(Collectors.toList());
        purchaseDetailService.updateBatchById(collect);

        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(purchaseId);
        purchaseEntity.setUpdateTime(new Date());
        this.updateById(purchaseEntity);
    }

    @Override
    public void receivedPurchase(List<Long> longs) {

        //更新采购单
        List<PurchaseEntity> collect = longs.stream().map(item -> {
            PurchaseEntity byId = this.getById(item);
            return byId;
        }).filter(item -> {
            if (item.getStatus().equals(WareConstant.PurchaseEnum.CREATED.getCode()) ||
                    item.getStatus().equals(WareConstant.PurchaseEnum.ALLOCATED.getCode())) {
                return true;
            }
            return false;
        }).map(i -> {
            i.setUpdateTime(new Date());
            i.setStatus(WareConstant.PurchaseEnum.RECEIVED.getCode());
            return i;
        }).collect(Collectors.toList());
        this.updateBatchById(collect);

        //更新需求采购单
        longs.forEach(Iterable-> {
            List<PurchaseDetailEntity> purchaseDetailEntities = purchaseDetailService.listDeatilByPurchaseId(Iterable);
            List<PurchaseDetailEntity> detailEntities = purchaseDetailEntities.stream().map(item -> {
                PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
                purchaseDetailEntity.setId(item.getId());
                purchaseDetailEntity.setStatus(WareConstant.PurchaseDetailEnum.PURSHSAEING.getCode());
                return purchaseDetailEntity;
            }).collect(Collectors.toList());
            purchaseDetailService.updateBatchById(detailEntities);
        });

    }

    @Transactional
    @Override
    public void done(PurchaseDoneVo purchaseDoneVo) {
        Long id = purchaseDoneVo.getId();

        Boolean isFails = false;
        List<PurchaseDetailEntity> list = new ArrayList<>();
        //采购单单项保存
        List<PurchaseDoneDetailVo> items = purchaseDoneVo.getItems();
        for(PurchaseDoneDetailVo purchaseDoneDetailVo : items) {
            if(purchaseDoneDetailVo.getStatus().equals(WareConstant.PurchaseDetailEnum.PURCHASEFAIL.getCode())){
                PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
                purchaseDetailEntity.setStatus(purchaseDoneDetailVo.getStatus());
                purchaseDetailEntity.setId(purchaseDoneDetailVo.getItemId());
                list.add(purchaseDetailEntity);
                isFails = true;
           }else {
                PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
                purchaseDetailEntity.setStatus(purchaseDoneDetailVo.getStatus());
                purchaseDetailEntity.setId(purchaseDoneDetailVo.getItemId());
                list.add(purchaseDetailEntity);

                //商品入库
                PurchaseDetailEntity detailEntity = purchaseDetailService.getById(purchaseDoneDetailVo.getItemId());
                wareSkuService.addStock(detailEntity.getSkuId(),detailEntity.getWareId(),detailEntity.getSkuNum());
            }
        }
        purchaseDetailService.updateBatchById(list);

        //保存采购单
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(id);
        purchaseEntity.setUpdateTime(new Date());
        if (isFails){
            purchaseEntity.setStatus(WareConstant.PurchaseEnum.ABNORMAL.getCode());
        }else{
            purchaseEntity.setStatus(WareConstant.PurchaseEnum.COMPLETED.getCode());
        }
        this.updateById(purchaseEntity);





    }

}