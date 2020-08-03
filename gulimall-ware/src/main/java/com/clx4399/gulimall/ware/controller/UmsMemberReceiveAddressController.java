package com.clx4399.gulimall.ware.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clx4399.gulimall.ware.entity.UmsMemberReceiveAddressEntity;
import com.clx4399.gulimall.ware.service.UmsMemberReceiveAddressService;
import com.clx4399.common.utils.PageUtils;
import com.clx4399.common.utils.R;



/**
 * 会员收货地址
 *
 * @author clx
 * @email whtclx@gmail.com
 * @date 2020-08-02 22:07:11
 */
@RestController
@RequestMapping("ware/umsmemberreceiveaddress")
public class UmsMemberReceiveAddressController {
    @Autowired
    private UmsMemberReceiveAddressService umsMemberReceiveAddressService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("ware:umsmemberreceiveaddress:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = umsMemberReceiveAddressService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("ware:umsmemberreceiveaddress:info")
    public R info(@PathVariable("id") Long id){
		UmsMemberReceiveAddressEntity umsMemberReceiveAddress = umsMemberReceiveAddressService.getById(id);

        return R.ok().put("umsMemberReceiveAddress", umsMemberReceiveAddress);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("ware:umsmemberreceiveaddress:save")
    public R save(@RequestBody UmsMemberReceiveAddressEntity umsMemberReceiveAddress){
		umsMemberReceiveAddressService.save(umsMemberReceiveAddress);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("ware:umsmemberreceiveaddress:update")
    public R update(@RequestBody UmsMemberReceiveAddressEntity umsMemberReceiveAddress){
		umsMemberReceiveAddressService.updateById(umsMemberReceiveAddress);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("ware:umsmemberreceiveaddress:delete")
    public R delete(@RequestBody Long[] ids){
		umsMemberReceiveAddressService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
