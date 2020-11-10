package com.clx4399.gulimall.product.vo;

import lombok.Data;

/**
 * @author WhtCl
 * @program: gulimall
 * @description:
 * @date 2020-11-10 00:39:01
 */
@Data
public class AttrRespVo extends AttrVo {

    /**
     * "catelogName": "手机/数码/手机", //所属分类名字
     * */
    private String catelogName;


    /**
     *"groupName": "主体", //所属分组名字
     */
    private String groupName;

    /**
     * 分类完整路径
     * */
    private Long[] catelogPath;

}
