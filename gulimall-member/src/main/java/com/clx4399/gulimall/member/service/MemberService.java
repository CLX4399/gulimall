package com.clx4399.gulimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.clx4399.common.utils.PageUtils;
import com.clx4399.gulimall.member.entity.MemberEntity;
import com.clx4399.gulimall.member.vo.MemberUserLoginVo;
import com.clx4399.gulimall.member.vo.MemberUserRegisterVo;

import java.util.Map;

/**
 * 会员
 *
 * @author clx
 * @email whtclx@gmail.com
 * @date 2020-08-02 21:50:00
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * @param userRegisterVo
     * @return void
     * @author CLX
     * @describe: 用户注册
     * @date 2021/8/1 18:22
     */
    boolean regist(MemberUserRegisterVo userRegisterVo);

    /**
     * @param userLoginVo
     * @return boolean
     * @author CLX
     * @describe: 登录功能
     * @date 2021/8/2 16:38
     */
    MemberEntity login(MemberUserLoginVo userLoginVo);
}

