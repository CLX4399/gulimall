package com.clx4399.gulimall.ware.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clx4399.common.utils.PageUtils;
import com.clx4399.common.utils.Query;

import com.clx4399.gulimall.ware.dao.WareOrderTaskDao;
import com.clx4399.gulimall.ware.entity.WareOrderTaskEntity;
import com.clx4399.gulimall.ware.service.WareOrderTaskService;


@Service("wareOrderTaskService")
public class WareOrderTaskServiceImpl extends ServiceImpl<WareOrderTaskDao, WareOrderTaskEntity> implements WareOrderTaskService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WareOrderTaskEntity> page = this.page(
                new Query<WareOrderTaskEntity>().getPage(params),
                new QueryWrapper<WareOrderTaskEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public WareOrderTaskEntity getOrderTaskByOrderSn(String orderSn) {
        WareOrderTaskEntity wareOrderTaskEntity = baseMapper.selectOne(new QueryWrapper<WareOrderTaskEntity>().eq("order_sn", orderSn));
        return wareOrderTaskEntity;
    }

}
