package com.clx4399.gulimall.ware.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.clx4399.common.utils.R;
import com.clx4399.gulimall.ware.feign.MemberFeignService;
import com.clx4399.gulimall.ware.vo.FareVo;
import com.clx4399.gulimall.ware.vo.MemberAddressVo;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Random;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clx4399.common.utils.PageUtils;
import com.clx4399.common.utils.Query;

import com.clx4399.gulimall.ware.dao.WareInfoDao;
import com.clx4399.gulimall.ware.entity.WareInfoEntity;
import com.clx4399.gulimall.ware.service.WareInfoService;


@Service("wareInfoService")
public class WareInfoServiceImpl extends ServiceImpl<WareInfoDao, WareInfoEntity> implements WareInfoService {

    @Autowired
    MemberFeignService memberFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<WareInfoEntity> wrapper = new QueryWrapper<>();
        String key = (String) params.get("key");
        if (StringUtils.isNotBlank(key)){
            wrapper.eq("id",key)
                    .or().like("name",key)
                    .or().like("address",key)
                    .or().like("areacode",key);
        }

        IPage<WareInfoEntity> page = this.page(
                new Query<WareInfoEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

    @Override
    public FareVo getfare(Long addrId) {
        FareVo fareVo = new FareVo();
        R info = memberFeignService.info(addrId);
        fareVo.setAddress(info.getData("memberReceiveAddress",new TypeReference<MemberAddressVo>(){}));
        Random random = new Random();
        int i = random.nextInt(50);
        fareVo.setFare(new BigDecimal(i));
        return fareVo;
    }

}
