package com.clx4399.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.clx4399.common.utils.PageUtils;
import com.clx4399.gulimall.coupon.entity.SeckillSessionEntity;

import java.util.List;
import java.util.Map;

/**
 * 秒杀活动场次
 *
 * @author clx
 * @email whtclx@gmail.com
 * @date 2020-08-02 21:24:06
 */
public interface SeckillSessionService extends IService<SeckillSessionEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * @param
     * @return java.util.List<com.clx4399.gulimall.coupon.entity.SeckillSessionEntity>
     * @author CLX
     * @describe: 获取秒杀商品活动三天内信息
     * @date 2021/9/6 15:20
     */
    List<SeckillSessionEntity> getLatest3DaysSession();
}

