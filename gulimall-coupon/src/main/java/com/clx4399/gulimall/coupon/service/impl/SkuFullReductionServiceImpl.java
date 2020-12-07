package com.clx4399.gulimall.coupon.service.impl;

import com.clx4399.common.to.MemberPrice;
import com.clx4399.common.to.SkuRedution;
import com.clx4399.gulimall.coupon.entity.MemberPriceEntity;
import com.clx4399.gulimall.coupon.entity.SkuLadderEntity;
import com.clx4399.gulimall.coupon.service.MemberPriceService;
import com.clx4399.gulimall.coupon.service.SkuLadderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clx4399.common.utils.PageUtils;
import com.clx4399.common.utils.Query;

import com.clx4399.gulimall.coupon.dao.SkuFullReductionDao;
import com.clx4399.gulimall.coupon.entity.SkuFullReductionEntity;
import com.clx4399.gulimall.coupon.service.SkuFullReductionService;


@Service("skuFullReductionService")
public class SkuFullReductionServiceImpl extends ServiceImpl<SkuFullReductionDao, SkuFullReductionEntity> implements SkuFullReductionService {

    @Autowired
    private SkuLadderService skuLadderService;

    @Autowired
    private MemberPriceService  memberPriceService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuFullReductionEntity> page = this.page(
                new Query<SkuFullReductionEntity>().getPage(params),
                new QueryWrapper<SkuFullReductionEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveRedution(SkuRedution skuRedution) {
        //保存优惠满减打折
        //保存产品阶梯价格
        SkuLadderEntity skuLadderEntity = new SkuLadderEntity();
        skuLadderEntity.setSkuId(skuRedution.getSkuId());
        skuLadderEntity.setDiscount(skuRedution.getDiscount());
        skuLadderEntity.setFullCount(skuRedution.getFullCount());
        skuLadderEntity.setAddOther(skuRedution.getCountStatus());
        if (skuLadderEntity.getFullCount()>0) {
            skuLadderService.save(skuLadderEntity);
        }

        //保存满减信息
        SkuFullReductionEntity skuFullReductionEntity = new SkuFullReductionEntity();
        BeanUtils.copyProperties(skuRedution,skuFullReductionEntity);
        if (skuFullReductionEntity.getFullPrice().compareTo(new BigDecimal("0"))==1) {
            this.save(skuFullReductionEntity);
        }

        List<MemberPrice> memberPrice = skuRedution.getMemberPrice();
        List<MemberPriceEntity> collect = memberPrice.stream().map(item -> {
            MemberPriceEntity memberPriceEntity = new MemberPriceEntity();
            memberPriceEntity.setAddOther(1);
            memberPriceEntity.setId(skuRedution.getSkuId());
            memberPriceEntity.setMemberLevelId(item.getId());
            memberPriceEntity.setMemberPrice(item.getPrice());
            memberPriceEntity.setMemberLevelName(item.getName());
            return memberPriceEntity;
        }).filter(item->item.getMemberPrice().compareTo(new BigDecimal("0"))==1)
                .collect(Collectors.toList());
        memberPriceService.saveBatch(collect);
    }

}