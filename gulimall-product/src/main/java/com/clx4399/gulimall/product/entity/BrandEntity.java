package com.clx4399.gulimall.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import com.clx4399.common.vaild.AddGroup;
import com.clx4399.common.vaild.UpdateGroup;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;

/**
 * 品牌
 * 
 * @author clx
 * @email whtclx@gmail.com
 * @date 2020-07-27 00:49:19
 */
@Data
@TableName("pms_brand")
public class BrandEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 品牌id
	 */
	@TableId
    @Null(message = "新增时品牌id必须为空",groups = AddGroup.class)
	@NotNull(message = "更新时品牌id不能为空",groups = UpdateGroup.class)
	private Long brandId;
	/**
	 * 品牌名
	 */
	@NotBlank(message = "品牌名不能为空",groups = AddGroup.class)
	private String name;
	/**
	 * 品牌logo地址
	 */
	@NotBlank(message = "logo地址不能为空",groups = {AddGroup.class})
	@URL(message = "logo地址必须为连接地址",groups = {UpdateGroup.class,AddGroup.class})
	private String logo;
	/**
	 * 介绍
	 */
	private String descript;
	/**
	 * 显示状态[0-不显示；1-显示]
	 */
	@NotNull(message = "显示状态新增时不能为空",groups = {AddGroup.class})
	@PositiveOrZero(message = "显示状态更新时必须为0或1",groups = {UpdateGroup.class,AddGroup.class})
	private Integer showStatus;
	/**
	 * 检索首字母
	 */
	@NotBlank(message = "新增时检索首字母不能为空",groups = AddGroup.class)
	//@Pattern(regexp = "/^[a-zA-Z]$/",message = "检索首字母必须为大小写字母开头",groups = {UpdateGroup.class,AddGroup.class })
	private String firstLetter;
	/**
	 * 排序
	 */
	@NotNull(message = "新增时排序不能为空",groups = AddGroup.class)
	private Integer sort;

}
