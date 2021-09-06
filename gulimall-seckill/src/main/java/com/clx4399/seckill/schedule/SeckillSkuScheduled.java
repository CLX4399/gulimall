package com.clx4399.seckill.schedule;

import com.clx4399.seckill.services.SeckillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author WhtCl
 * @program: gulimall
 * @description:
 * @date 2021-09-06 15:03:42
 */
@Slf4j
@Component
public class SeckillSkuScheduled {

    @Autowired
    SeckillService seckillService;

    @Scheduled(cron = "0 * * * * ?")
    public void uploadSeckillSkuLatest3Days(){
        log.info("秒杀商品上架");
        seckillService.uploadSeckillSkuLatest3Days();
    }

}
