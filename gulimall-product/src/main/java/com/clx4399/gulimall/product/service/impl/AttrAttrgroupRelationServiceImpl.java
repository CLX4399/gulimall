package com.clx4399.gulimall.product.service.impl;

import com.clx4399.gulimall.product.vo.AttrRelationAttrGroupVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clx4399.common.utils.PageUtils;
import com.clx4399.common.utils.Query;

import com.clx4399.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.clx4399.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.clx4399.gulimall.product.service.AttrAttrgroupRelationService;


@Service("attrAttrgroupRelationService")
public class AttrAttrgroupRelationServiceImpl extends ServiceImpl<AttrAttrgroupRelationDao, AttrAttrgroupRelationEntity> implements AttrAttrgroupRelationService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrAttrgroupRelationEntity> page = this.page(
                new Query<AttrAttrgroupRelationEntity>().getPage(params),
                new QueryWrapper<AttrAttrgroupRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void deleteAttrRelation(AttrRelationAttrGroupVo[] attrGroupVos) {
        List<AttrAttrgroupRelationEntity> collect = Arrays.stream(attrGroupVos).map((attrGroupVo) -> {
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
            attrAttrgroupRelationEntity.setAttrGroupId(attrGroupVo.getAttrGroupId());
            attrAttrgroupRelationEntity.setAttrId(attrGroupVo.getAttrId());
            return attrAttrgroupRelationEntity;
        }).collect(Collectors.toList());
        this.baseMapper.batchDeleteAttrRelation(collect);
    }

    @Override
    public void save(List<AttrRelationAttrGroupVo> attrGroupVos) {
        List<AttrAttrgroupRelationEntity> relationEntities = attrGroupVos.stream().map(item -> {
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(item, attrAttrgroupRelationEntity);
            return attrAttrgroupRelationEntity;
        }).collect(Collectors.toList());
        this.saveBatch(relationEntities);
    }

}