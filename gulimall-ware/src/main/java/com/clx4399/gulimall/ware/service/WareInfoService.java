package com.clx4399.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.clx4399.common.utils.PageUtils;
import com.clx4399.gulimall.ware.entity.WareInfoEntity;
import com.clx4399.gulimall.ware.feign.MemberFeignService;
import com.clx4399.gulimall.ware.vo.FareVo;
import org.springframework.beans.factory.annotation.Autowired;

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



    /**
     * @param addrId
     * @return com.clx4399.gulimall.ware.vo.FareVo
     * @author CLX
     * @describe: 根据地质计算运费
     * @date 2021/8/24 21:10
     */
    FareVo getfare(Long addrId);
}

