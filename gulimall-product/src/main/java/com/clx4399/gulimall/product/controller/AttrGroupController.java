package com.clx4399.gulimall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.clx4399.gulimall.product.entity.AttrEntity;
import com.clx4399.gulimall.product.service.AttrService;
import com.clx4399.gulimall.product.service.CategoryService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.clx4399.gulimall.product.entity.AttrGroupEntity;
import com.clx4399.gulimall.product.service.AttrGroupService;
import com.clx4399.common.utils.PageUtils;
import com.clx4399.common.utils.R;



/**
 * 属性分组
 *
 * @author clx
 * @email whtclx@gmail.com
 * @date 2020-08-01 18:24:57
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AttrService attrService;

    /**
     * @param
     * @return com.clx4399.common.utils.R
     * @author CLX
     * @describe:
     * @date 2020/11/11 20:50
     */
    @GetMapping("/{attrgroupId}/attr/relation")
    public R attrGroupRelation(@PathVariable String attrgroupId){
        List<AttrEntity> list = attrService.getAttrByAttrGroup(attrgroupId);
        return R.ok().put("data",list);
    }

    /**
     * 列表
     */
    @RequestMapping("/list/{attrgroupId}")
    //@RequiresPermissions("product:attrgroup:list")
    public R list(@RequestParam Map<String, Object> params, @PathVariable Long attrgroupId){
        PageUtils page = attrGroupService.queryPage(params,attrgroupId);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    //@RequiresPermissions("product:attrgroup:info")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
		AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);

        Long[] longs = categoryService.getAllPath(attrGroup.getCatelogId());

        attrGroup.setCatelogPath(longs);

        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:attrgroup:save")
    public R save(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:attrgroup:update")
    public R update(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:attrgroup:delete")
    public R delete(@RequestBody Long[] attrGroupIds){
		attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

}
