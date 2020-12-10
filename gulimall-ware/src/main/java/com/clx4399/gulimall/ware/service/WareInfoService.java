package com.clx4399.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.clx4399.common.utils.PageUtils;
import com.clx4399.gulimall.ware.entity.WareInfoEntity;

import java.util.Map;

/**
 * 仓库信息
 *
 * @author clx
 * @email whtclx@gmail.com
 * @date 2020-12-07 16:10:23
 */
public interface WareInfoService extends IService<WareInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

