package com.clx4399.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.clx4399.common.utils.PageUtils;
import com.clx4399.gulimall.ware.entity.PurchaseEntity;
import com.clx4399.gulimall.ware.vo.PurchaseDoneVo;
import com.clx4399.gulimall.ware.vo.PurchaseVo;

import java.util.List;
import java.util.Map;

/**
 * 采购信息
 *
 * @author clx
 * @email whtclx@gmail.com
 * @date 2020-12-07 16:10:23
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * @param params
     * @return com.clx4399.common.utils.PageUtils
     * @author CLX
     * @describe: 查询需求和已分配采购需求单
     * @date 2020/12/7 17:18
     */
    PageUtils queryPageUnreceive(Map<String, Object> params);

    /**
     * @param purchaseVo
     * @return com.clx4399.common.utils.PageUtils
     * @author CLX
     * @describe: 合并采购需求单
     * @date 2020/12/7 17:35
     */
    void mergePurchase(PurchaseVo purchaseVo);

    /**
     * @param longs
     * @return void
     * @author CLX
     * @describe: 领取采购单
     * @date 2020/12/7 19:35
     */
    void receivedPurchase(List<Long> longs);

    /**
     * @param purchaseDoneVo
     * @return void
     * @author CLX
     * @describe: 订单已完成
     * @date 2020/12/9 18:39
     */
    void done(PurchaseDoneVo purchaseDoneVo);
}

