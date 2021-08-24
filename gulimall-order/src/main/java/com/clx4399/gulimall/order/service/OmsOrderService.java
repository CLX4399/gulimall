package com.clx4399.gulimall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.clx4399.common.utils.PageUtils;
import com.clx4399.gulimall.order.entity.OmsOrderEntity;
import com.clx4399.gulimall.order.vo.OrderConfirmVo;

import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * 订单
 *
 * @author clx
 * @email whtclx@gmail.com
 * @date 2020-08-02 21:58:48
 */
public interface OmsOrderService extends IService<OmsOrderEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * @param
     * @return com.clx4399.gulimall.order.vo.OrderConfirmVo
     * @author CLX
     * @describe: 返回购物车结算所需数据
     * @date 2021/8/23 15:34
     */
    OrderConfirmVo confirmOrder() throws ExecutionException, InterruptedException;
}

