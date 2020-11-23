package com.clx4399.gulimall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.clx4399.gulimall.product.entity.AttrEntity;
import com.clx4399.gulimall.product.service.AttrAttrgroupRelationService;
import com.clx4399.gulimall.product.service.AttrService;
import com.clx4399.gulimall.product.service.CategoryService;
import com.clx4399.gulimall.product.service.impl.AttrGroupServiceImpl;
import com.clx4399.gulimall.product.vo.AttrGroupWithAttrVo;
import com.clx4399.gulimall.product.vo.AttrRelationAttrGroupVo;
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

    @Autowired
    private AttrAttrgroupRelationService attrAttrgroupRelationService;


    /**
     * @param catelogId
     * @return com.clx4399.common.utils.R
     * @author CLX
     * @describe: 通过分类id获取对应的分组及其对应属性
     * @date 2020/11/22 0:29
     */
    @GetMapping("/{catelogId}/withattr")
    public R attrWithGroupByCatelogId(@PathVariable String catelogId){
        List<AttrGroupWithAttrVo> list = attrGroupService.getAttrWithGroupByCatelogId(catelogId);
        return R.ok().put("data",list);
    }

    /**
     * @param attrGroupVos
     * @return com.clx4399.common.utils.R
     * @author CLX
     * @describe:
     * @date 2020/11/17 18:44
     */
    @PostMapping("/attr/relation")
    public R addAttrRelationGroup(@RequestBody List<AttrRelationAttrGroupVo> attrGroupVos){
        attrAttrgroupRelationService.save(attrGroupVos);
        return R.ok();
    }

    /**
     * @param attrGroupVos
     * @return /product/attrgroup/attr/relation/delete
     * @author CLX
     * @describe:
     * @date 2020/11/11 23:51
     */
    @PostMapping("/attr/relation/delete")
    public R deleteAttrGroup(@RequestBody AttrRelationAttrGroupVo[] attrGroupVos){
        attrAttrgroupRelationService.deleteAttrRelation(attrGroupVos);
        return R.ok();
    }

    /**
     * @param
     * @return
     * @author CLX
     * @describe: 获取属性分组没有关联的其他属性
     * /product/attrgroup/{attrgroupId}/noattr/relation
     * @date 2020/11/15 22:35
     */
    @GetMapping("/{attrgroupId}/noattr/relation")
    public R noAttrGroupRelation(@PathVariable String attrgroupId,
                                 @RequestParam Map<String, Object> params){
        PageUtils page = attrService.getNoAttrByAttrGroup(attrgroupId,params);
        return R.ok().put("page",page);
    }

    /**
     * @param
     * @return com.clx4399.common.utils.R
     * @author CLX
     * @describe: 获取分组与属性关联关系
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
