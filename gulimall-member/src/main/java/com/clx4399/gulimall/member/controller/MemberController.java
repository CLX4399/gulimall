package com.clx4399.gulimall.member.controller;

import java.util.Arrays;
import java.util.Map;

import com.clx4399.gulimall.member.feign.CouponTestFeign;
import com.clx4399.gulimall.member.vo.MemberUserLoginVo;
import com.clx4399.gulimall.member.vo.MemberUserRegisterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.clx4399.gulimall.member.entity.MemberEntity;
import com.clx4399.gulimall.member.service.MemberService;
import com.clx4399.common.utils.PageUtils;
import com.clx4399.common.utils.R;



/**
 * 会员
 *
 * @author clx
 * @email whtclx@gmail.com
 * @date 2020-08-02 21:50:00
 */
@RestController
@RequestMapping("member/member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @Autowired
    private CouponTestFeign couponTestFeign;

    @RequestMapping("/allCoupons")
    public R allCoupons(){
        return couponTestFeign.allList().put("测试获取优惠券","成功!");
    }

    /**
     * @param userRegisterVo
     * @return com.clx4399.common.utils.R
     * @author CLX
     * @describe: 用户注册
     * @date 2021/7/29 21:49
     */
    @PostMapping("/regist")
    public R regist(@RequestBody MemberUserRegisterVo userRegisterVo){
        boolean regist = memberService.regist(userRegisterVo);
        return regist?R.ok():R.error();
    }

    @PostMapping("/login")
    public R login(@RequestBody MemberUserLoginVo userLoginVo){
        MemberEntity regist = memberService.login(userLoginVo);
        return regist!=null?R.ok().setData(regist):R.error();
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("member:member:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = memberService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("member:member:info")
    public R info(@PathVariable("id") Long id){
		MemberEntity member = memberService.getById(id);

        return R.ok().put("member", member);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("member:member:save")
    public R save(@RequestBody MemberEntity member){
		memberService.save(member);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("member:member:update")
    public R update(@RequestBody MemberEntity member){
		memberService.updateById(member);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("member:member:delete")
    public R delete(@RequestBody Long[] ids){
		memberService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
