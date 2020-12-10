package com.clx4399.gulimall.ware.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clx4399.common.utils.PageUtils;
import com.clx4399.common.utils.Query;

import com.clx4399.gulimall.ware.dao.PurchaseDetailDao;
import com.clx4399.gulimall.ware.entity.PurchaseDetailEntity;
import com.clx4399.gulimall.ware.service.PurchaseDetailService;


@Service("purchaseDetailService")
public class PurchaseDetailServiceImpl extends ServiceImpl<PurchaseDetailDao, PurchaseDetailEntity> implements PurchaseDetailService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<PurchaseDetailEntity> queryWrapper = new QueryWrapper<>();

        /**
         * key:
         * status:
         * wareId:
         */
        String key = (String) params.get("key");
        String status = (String) params.get("status");
        String wareId = (String) params.get("wareId");

        if (StringUtils.isNotBlank(key)) {
            queryWrapper.and(w -> {
                w.eq("purchase_id", key)
                        .or().eq("sku_id", key);
            });
        }

        if (StringUtils.isNotBlank(status)) {
            queryWrapper.eq("status", status);
        }

        if (StringUtils.isNotBlank(wareId)) {
            queryWrapper.eq("ware_id", wareId);
        }

        IPage<PurchaseDetailEntity> page = this.page(
                new Query<PurchaseDetailEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public List<PurchaseDetailEntity> listDeatilByPurchaseId(Long id) {
        List<PurchaseDetailEntity> detailEntityList = this.baseMapper.selectList(new QueryWrapper<PurchaseDetailEntity>()
                .eq("purchase_id", id));
        return detailEntityList;
    }

}