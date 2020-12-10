package com.clx4399.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.clx4399.common.utils.PageUtils;
import com.clx4399.gulimall.ware.entity.WareOrderTaskDetailEntity;

import java.util.Map;

/**
 * 库存工作单
 *
 * @author clx
 * @email whtclx@gmail.com
 * @date 2020-12-07 16:10:23
 */
public interface WareOrderTaskDetailService extends IService<WareOrderTaskDetailEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

