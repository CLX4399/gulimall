package com.clx4399.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.clx4399.common.utils.PageUtils;
import com.clx4399.gulimall.ware.entity.WareOrderTaskEntity;

import java.util.Map;

/**
 * 库存工作单
 *
 * @author clx
 * @email whtclx@gmail.com
 * @date 2020-12-07 16:10:23
 */
public interface WareOrderTaskService extends IService<WareOrderTaskEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * @param orderSn
     * @return com.clx4399.gulimall.ware.entity.WareOrderTaskEntity
     * @author CLX
     * @describe: 订单编号获取锁库存任务单
     * @date 2021/9/1 20:58
     */
    WareOrderTaskEntity getOrderTaskByOrderSn(String orderSn);
}

