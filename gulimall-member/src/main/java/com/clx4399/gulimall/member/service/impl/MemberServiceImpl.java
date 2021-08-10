package com.clx4399.gulimall.member.service.impl;

import com.clx4399.gulimall.member.entity.MemberLevelEntity;
import com.clx4399.gulimall.member.service.MemberLevelService;
import com.clx4399.gulimall.member.service.MemberService;
import com.clx4399.gulimall.member.vo.MemberUserLoginVo;
import com.clx4399.gulimall.member.vo.MemberUserRegisterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clx4399.common.utils.PageUtils;
import com.clx4399.common.utils.Query;

import com.clx4399.gulimall.member.dao.MemberDao;
import com.clx4399.gulimall.member.entity.MemberEntity;


@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {

    @Autowired
    private MemberLevelService memberLevelService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberEntity> page = this.page(
                new Query<MemberEntity>().getPage(params),
                new QueryWrapper<MemberEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public boolean regist(MemberUserRegisterVo userRegisterVo) {

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode(userRegisterVo.getPassword());

        MemberEntity memberEntity = new MemberEntity();

        MemberLevelEntity default_status = memberLevelService.getOne(new QueryWrapper<MemberLevelEntity>().eq("default_status", 1));

        memberEntity.setLevelId(default_status.getId());
        memberEntity.setUsername(userRegisterVo.getUserName());
        memberEntity.setPassword(encode);
        memberEntity.setMobile(userRegisterVo.getPhone());
        memberEntity.setStatus(1);
        memberEntity.setCreateTime(new Date());
        memberEntity.setIntegration(0);
        memberEntity.setGrowth(0);
        memberEntity.setNickname(userRegisterVo.getUserName());

        return this.save(memberEntity);
    }

    @Override
    public MemberEntity login(MemberUserLoginVo userLoginVo) {
        MemberEntity memberEntity = baseMapper.selectOne(new QueryWrapper<MemberEntity>().eq("username", userLoginVo.getLoginacct()));
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if (memberEntity!=null) {
            boolean matches = bCryptPasswordEncoder.matches(userLoginVo.getPassword(), memberEntity.getPassword());
            return memberEntity;
        }else {
            return null;
        }
    }

}
