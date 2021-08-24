package com.clx4399.gulimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.clx4399.common.utils.PageUtils;
import com.clx4399.gulimall.member.entity.MemberReceiveAddressEntity;
import com.clx4399.gulimall.member.vo.MemberAddressVo;

import java.util.List;
import java.util.Map;

/**
 * 会员收货地址
 *
 * @author clx
 * @email whtclx@gmail.com
 * @date 2020-08-02 21:50:00
 */
public interface MemberReceiveAddressService extends IService<MemberReceiveAddressEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * @return java.util.List<com.clx4399.gulimall.member.vo.MemberAddressVo>
     * @author CLX
     * @describe: 放回地址用户信息
     * @date 2021/8/24 11:03
     */
    List<MemberReceiveAddressEntity> getAddressByMemberId(Long memberId);
}

