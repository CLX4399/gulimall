package com.clx4399.gulimall.product.controller;

import com.clx4399.common.utils.PageUtils;
import com.clx4399.common.utils.R;
import com.clx4399.gulimall.product.entity.SkuInfoEntity;
import com.clx4399.gulimall.product.entity.SpuInfoEntity;
import com.clx4399.gulimall.product.service.SkuInfoService;
import com.clx4399.gulimall.product.service.SpuInfoService;
import com.clx4399.gulimall.product.vo.spusavevo.SpuSaveVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;



/**
 * spu信息
 *
 * @author clx
 * @email whtclx@gmail.com
 * @date 2020-08-01 18:24:56
 */
@RestController
@RequestMapping("product/spuinfo")
public class SpuInfoController {
    @Autowired
    private SpuInfoService spuInfoService;

    @Autowired
    private SkuInfoService skuInfoService;

    @GetMapping("/info/{skuId}")
    //@RequiresPermissions("product:skuinfo:info")
    public R getSkuInfo(@PathVariable("skuId") Long skuId){
        SkuInfoEntity skuInfo = skuInfoService.getById(skuId);

        return R.ok().put("skuInfo", skuInfo);
    }


    /**
     * @param spuId
     * @return com.clx4399.common.utils.R
     * @author CLX
     * @describe: 商品上架
     * @date 2021/5/12 16:36
     */
    @PostMapping("/{spuid}/up")
    public R getSkuInfoBySkuId(@PathVariable("spuid") Long spuId){

        spuInfoService.getSpuInfoBySkuId(spuId);
        return R.ok();
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:spuinfo:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = spuInfoService.queryPageBySpu(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("product:spuinfo:info")
    public R info(@PathVariable("id") Long id){
		SpuInfoEntity spuInfo = spuInfoService.getById(id);
        return R.ok().put("spuInfo", spuInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:spuinfo:save")
    public R save(@RequestBody SpuSaveVo spuInfo){
		spuInfoService.saveForSpuSaveVo(spuInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:spuinfo:update")
    public R update(@RequestBody SpuInfoEntity spuInfo){
		spuInfoService.updateById(spuInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:spuinfo:delete")
    public R delete(@RequestBody Long[] ids){
		spuInfoService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
