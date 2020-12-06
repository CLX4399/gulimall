package com.clx4399.gulimall.coupon.service.impl;

import com.clx4399.common.to.SkuRedution;
import com.clx4399.gulimall.coupon.entity.SkuLadderEntity;
import com.clx4399.gulimall.coupon.service.SkuLadderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clx4399.common.utils.PageUtils;
import com.clx4399.common.utils.Query;

import com.clx4399.gulimall.coupon.dao.SeckillSkuRelationDao;
import com.clx4399.gulimall.coupon.entity.SeckillSkuRelationEntity;
import com.clx4399.gulimall.coupon.service.SeckillSkuRelationService;


@Service("seckillSkuRelationService")
public class SeckillSkuRelationServiceImpl extends ServiceImpl<SeckillSkuRelationDao, SeckillSkuRelationEntity> implements SeckillSkuRelationService {


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SeckillSkuRelationEntity> page = this.page(
                new Query<SeckillSkuRelationEntity>().getPage(params),
                new QueryWrapper<SeckillSkuRelationEntity>()
        );

        return new PageUtils(page);
    }


}