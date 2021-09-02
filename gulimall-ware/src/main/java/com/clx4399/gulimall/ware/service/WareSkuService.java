package com.clx4399.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.clx4399.common.to.mq.OrderTo;
import com.clx4399.common.to.mq.StockLockedTo;
import com.clx4399.common.utils.PageUtils;
import com.clx4399.gulimall.ware.entity.WareSkuEntity;
import com.clx4399.gulimall.ware.vo.SkuHasStockVo;
import com.clx4399.gulimall.ware.vo.WareSkuLockVo;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author clx
 * @email whtclx@gmail.com
 * @date 2020-12-07 16:10:22
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * @param skuId
	 * @param wareId
	 * @param skuNum
     * @return void
     * @author CLX
     * @describe: 保存商品入库信息
     * @date 2020/12/9 19:09
     */
    void addStock(Long skuId, Long wareId, Integer skuNum);

    /**
     * @param skuIds
     * @return java.util.List<com.clx4399.gulimall.ware.vo.SkuHasStockVo>
     * @author CLX
     * @describe: 通过id判断是否有库存
     * @date 2021/4/13 19:42
     */
    List<SkuHasStockVo> getSkuHasStock(List<Long> skuIds);

    /**
     * @param lockVo
     * @return boolean
     * @author CLX
     * @describe: 锁定商品库存
     * @date 2021/8/26 14:46
     */
    boolean orderLockStock(WareSkuLockVo lockVo);

    /**
     * @param to
     * @return void
     * @author CLX
     * @describe: 解锁库存
     * @date 2021/8/31 20:08
     */
    void unLockStock(StockLockedTo to);

    /**
     * @param to
     * @return
     * @author CLX
     * @describe: 订单关闭库存解放
     * @date 2021/9/1 20:42
     */
    void unLockStock(OrderTo to);

}

