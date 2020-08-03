package com.clx4399.gulimall.member.dao;

import com.clx4399.gulimall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author clx
 * @email whtclx@gmail.com
 * @date 2020-08-02 21:50:00
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
