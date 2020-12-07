package com.clx4399.gulimall.product.controller;

import java.util.Arrays;
import java.util.Map;

import com.clx4399.common.to.SpuBoundTo;
import com.clx4399.gulimall.product.feign.CouponFeignServices;
import com.clx4399.gulimall.product.vo.AttrRespVo;
import com.clx4399.gulimall.product.vo.AttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clx4399.gulimall.product.entity.AttrEntity;
import com.clx4399.gulimall.product.service.AttrService;
import com.clx4399.common.utils.PageUtils;
import com.clx4399.common.utils.R;



/**
 * 商品属性
 *
 * @author clx
 * @email whtclx@gmail.com
 * @date 2020-08-01 18:24:57
 */
@RestController
@RequestMapping("product/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;

    @Autowired
    CouponFeignServices services;


    @RequestMapping("/test")
    R test(){
        R r = services.saveSpuBounds(new SpuBoundTo());
        return R.ok().put("message",r.getCode());
    };

    /**
     * /product/attr/base/list/{catelogId}
     * @param params
     * @return com.clx4399.common.utils.R
     * @author CLX
     * @describe:
     * @date 2020/11/9 23:18
     */
    @RequestMapping("/{type}/list/{catelogId}")
    public R attrBaseList(@RequestParam Map<String, Object> params,
                          @PathVariable("catelogId") Long catelogId,
                          @PathVariable("type") String type){
        PageUtils page = attrService.queryAttrBase(params,catelogId,type);

        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:attr:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    //@RequiresPermissions("product:attr:info")
    public R info(@PathVariable("attrId") Long attrId){
		AttrRespVo attr = attrService.getAttrInfo(attrId);

        return R.ok().put("attr", attr);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:attr:save")
    public R save(@RequestBody AttrVo attr){
		attrService.saveAttr(attr);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:attr:update")
    public R update(@RequestBody AttrVo attr){
		attrService.updateAttr(attr);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:attr:delete")
    public R delete(@RequestBody Long[] attrIds){
		attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }

}
