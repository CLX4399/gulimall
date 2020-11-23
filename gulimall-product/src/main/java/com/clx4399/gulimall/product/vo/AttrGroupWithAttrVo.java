package com.clx4399.gulimall.product.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.clx4399.gulimall.product.entity.AttrEntity;
import lombok.Data;

import java.util.List;

/**
 * @author WhtCl
 * @program: gulimall
 * @description:
 * @date 2020-11-22 00:32:32
 */
@Data
public class AttrGroupWithAttrVo {

    /**
     * 分组id
     */
    private Long attrGroupId;
    /**
     * 组名
     */
    private String attrGroupName;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 描述
     */
    private String descript;
    /**
     * 组图标
     */
    private String icon;
    /**
     * 所属分类id
     */
    private Long catelogId;

    /**
     *  分组所属属性
     * */
    private List<AttrEntity> attrs;


}
