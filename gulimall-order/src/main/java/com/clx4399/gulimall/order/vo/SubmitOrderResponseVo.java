package com.clx4399.gulimall.order.vo;

import com.clx4399.gulimall.order.entity.OmsOrderEntity;
import lombok.Data;

/**
 * @description:
 * @author: wei-xhh
 * @create: 2020-07-27
 */
@Data
public class SubmitOrderResponseVo {
    private OmsOrderEntity order;

    //错误状态码:0成功
    private Integer code;

}
