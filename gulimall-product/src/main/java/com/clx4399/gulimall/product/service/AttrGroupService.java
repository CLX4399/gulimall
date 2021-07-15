package com.clx4399.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.clx4399.common.utils.PageUtils;
import com.clx4399.gulimall.product.entity.AttrEntity;
import com.clx4399.gulimall.product.entity.AttrGroupEntity;
import com.clx4399.gulimall.product.vo.AttrGroupWithAttrVo;
import com.clx4399.gulimall.product.vo.SpuItemAttrGroupVo;

import java.util.List;
import java.util.Map;

/**
 * 属性分组
 *
 * @author clx
 * @email whtclx@gmail.com
 * @date 2020-07-27 00:49:20
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPage(Map<String, Object> params, Long attrgroupId);

    List<AttrGroupWithAttrVo> getAttrWithGroupByCatelogId(String catelogId);

    /**
     * @param spuId
	 * @param catalogId
     * @return java.util.List<com.clx4399.gulimall.product.vo.SpuItemAttrGroupVo>
     * @author CLX
     * @describe: 获取spu规格参数
     * @date 2021/7/15 18:30
     */
    List<SpuItemAttrGroupVo> getAttrGroupWithAttrs(Long spuId, Long catalogId);
}

