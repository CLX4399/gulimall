package com.clx4399.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.clx4399.common.utils.PageUtils;
import com.clx4399.gulimall.ware.entity.UmsMemberEntity;

import java.util.Map;

/**
 * 会员
 *
 * @author clx
 * @email whtclx@gmail.com
 * @date 2020-08-02 22:07:12
 */
public interface UmsMemberService extends IService<UmsMemberEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

