package com.clx4399.gulimall.ware.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.clx4399.gulimall.ware.vo.PurchaseDoneVo;
import com.clx4399.gulimall.ware.vo.PurchaseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.clx4399.gulimall.ware.entity.PurchaseEntity;
import com.clx4399.gulimall.ware.service.PurchaseService;
import com.clx4399.common.utils.PageUtils;
import com.clx4399.common.utils.R;



/**
 * 采购信息
 *
 * @author clx
 * @email whtclx@gmail.com
 * @date 2020-12-07 16:10:23
 */
@RestController
@RequestMapping("ware/purchase")
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;

    /**
     * 合并采购需求单到采购单中
     */
    @PostMapping("/done")
    //@RequiresPermissions("ware:purchase:list")
    public R done(@RequestBody PurchaseDoneVo purchaseDoneVo){
        purchaseService.done(purchaseDoneVo);

        return R.ok();
    }

    /**
     * 合并采购需求单到采购单中
     */
    @PostMapping("/received")
    //@RequiresPermissions("ware:purchase:list")
    public R receivedPurchase(@RequestBody List<Long> longs){
        purchaseService.receivedPurchase(longs);

        return R.ok();
    }

    /**
     * 合并采购需求单到采购单中
     */
    @RequestMapping("/merge")
    //@RequiresPermissions("ware:purchase:list")
    public R mergePurchase(@RequestBody PurchaseVo purchaseVo){
       purchaseService.mergePurchase(purchaseVo);

        return R.ok();
    }

    /**
     * 列表
     */
    @RequestMapping("/unreceive/list")
    //@RequiresPermissions("ware:purchase:list")
    public R unreceiveList(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseService.queryPageUnreceive(params);

        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("ware:purchase:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("ware:purchase:info")
    public R info(@PathVariable("id") Long id){
		PurchaseEntity purchase = purchaseService.getById(id);

        return R.ok().put("purchase", purchase);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("ware:purchase:save")
    public R save(@RequestBody PurchaseEntity purchase){
        purchase.setCreateTime(new Date());
        purchase.setUpdateTime(new Date());
		purchaseService.save(purchase);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("ware:purchase:update")
    public R update(@RequestBody PurchaseEntity purchase){
		purchaseService.updateById(purchase);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("ware:purchase:delete")
    public R delete(@RequestBody Long[] ids){
		purchaseService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
