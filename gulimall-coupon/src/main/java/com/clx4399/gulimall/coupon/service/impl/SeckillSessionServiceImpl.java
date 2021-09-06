package com.clx4399.gulimall.coupon.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.clx4399.gulimall.coupon.entity.SeckillSkuRelationEntity;
import com.clx4399.gulimall.coupon.service.SeckillSkuRelationService;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clx4399.common.utils.PageUtils;
import com.clx4399.common.utils.Query;

import com.clx4399.gulimall.coupon.dao.SeckillSessionDao;
import com.clx4399.gulimall.coupon.entity.SeckillSessionEntity;
import com.clx4399.gulimall.coupon.service.SeckillSessionService;


@Service("seckillSessionService")
public class SeckillSessionServiceImpl extends ServiceImpl<SeckillSessionDao, SeckillSessionEntity> implements SeckillSessionService {

    @Autowired
    private SeckillSkuRelationService seckillSkuRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SeckillSessionEntity> page = this.page(
                new Query<SeckillSessionEntity>().getPage(params),
                new QueryWrapper<SeckillSessionEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<SeckillSessionEntity> getLatest3DaysSession() {
        List<SeckillSessionEntity> entityList = this.list(new QueryWrapper<SeckillSessionEntity>()
                .between("start_time", startTime(), endTime()));
        if (entityList!=null){
            List<SeckillSessionEntity> seckillSessionEntities = entityList.stream().map(item -> {
                Long sessionId = item.getId();
                List<SeckillSkuRelationEntity> relationEntities = seckillSkuRelationService.list(new QueryWrapper<SeckillSkuRelationEntity>()
                        .eq("promotion_session_id", sessionId));
                item.setRelationSkus(relationEntities);
                return item;
            }).collect(Collectors.toList());
            return seckillSessionEntities;
        }
        return null;
    }

    private String endTime() {
        LocalDate localDate = LocalDate.now().plusDays(2);
        LocalDateTime localDateTime = LocalDateTime.of(localDate, LocalTime.MAX);
        return localDateTime.format(DatePattern.NORM_DATETIME_FORMATTER);
    }

    private String startTime() {
        LocalDate nowDate = LocalDate.now();
        LocalDateTime localDateTime = LocalDateTime.of(nowDate, LocalTime.MIN);
        return localDateTime.format(DatePattern.NORM_DATETIME_FORMATTER);
    }

}
