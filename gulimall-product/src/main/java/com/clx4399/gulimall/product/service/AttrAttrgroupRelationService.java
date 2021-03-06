package com.clx4399.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.clx4399.common.utils.PageUtils;
import com.clx4399.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.clx4399.gulimall.product.vo.AttrRelationAttrGroupVo;

import java.util.List;
import java.util.Map;

/**
 * 属性&属性分组关联
 *
 * @author clx
 * @email whtclx@gmail.com
 * @date 2020-07-27 00:49:20
 */
public interface AttrAttrgroupRelationService extends IService<AttrAttrgroupRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void deleteAttrRelation(AttrRelationAttrGroupVo[] attrGroupVos);

    void save(List<AttrRelationAttrGroupVo> attrGroupVos);
}

