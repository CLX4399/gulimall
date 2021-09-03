package com.clx4399.gulimall.order.dao;

import com.clx4399.gulimall.order.entity.OmsOrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 订单
 *
 * @author clx
 * @email whtclx@gmail.com
 * @date 2020-08-02 21:58:48
 */
@Mapper
public interface OmsOrderDao extends BaseMapper<OmsOrderEntity> {

    /**
     * @param outTradeNo
	 * @param code
     * @return void
     * @author CLX
     * @describe: 更新订单状态
     * @date 2021/9/2 22:15
     */
    void updateOrderStatus(@Param("outTradeNo") String outTradeNo, @Param("code") int code);
}
