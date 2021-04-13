package com.clx4399.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.clx4399.common.utils.PageUtils;
import com.clx4399.gulimall.product.entity.AttrEntity;
import com.clx4399.gulimall.product.vo.AttrRespVo;
import com.clx4399.gulimall.product.vo.AttrVo;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author clx
 * @email whtclx@gmail.com
 * @date 2020-07-27 00:49:19
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveAttr(AttrVo attr);

    PageUtils queryAttrBase(Map<String, Object> params, Long catelogId, String type);

    AttrRespVo getAttrInfo(Long attrId);

    void updateAttr(AttrVo attr);

    List<AttrEntity> getAttrByAttrGroup(String attrgroupId);

    /**
     * @param attrgroupId
     * @param params
     * @return com.clx4399.common.utils.PageUtils
     * @author CLX
     * @describe:  获取属性分组没有关联的其他属性
     * @date 2020/11/15 22:37
     */
    PageUtils getNoAttrByAttrGroup(String attrgroupId, Map<String, Object> params);

    /**
     * @param productAttrIdLists
     * @return java.util.List<java.lang.Long>
     * @author CLX
     * @describe: 获取
     * @date 2021/4/13 20:55
     */
    List<Long> selectSearchAttrIds(List<Long> productAttrIdLists);
}

