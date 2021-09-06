package com.clx4399.seckill.vo;

import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: wei-xhh
 * @create: 2020-08-01
 */
@ToString
@Data
public class SeckillSessionWithSkus {

    private Long id;
    /**
     * 场次名称
     */
    private String name;
    /**
     * 每日开始时间
     */
    private Date startTime;
    /**
     * 每日结束时间
     */
    private Date endTime;
    /**
     * 启用状态
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date createTime;

    private List<SeckillSkuVo> relationSkus;
}
