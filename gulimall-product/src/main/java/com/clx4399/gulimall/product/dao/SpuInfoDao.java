package com.clx4399.gulimall.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clx4399.gulimall.product.entity.SpuInfoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * spu信息
 *
 * @author clx
 * @email whtclx@gmail.com
 * @date 2020-07-27 00:49:19
 */
@Mapper
public interface SpuInfoDao extends BaseMapper<SpuInfoEntity> {

    /**
     * @param spuId
	 * @param code
     * @return void
     * @author CLX
     * @describe: 根据id更新上架下架状态
     * @date 2021/4/14 14:41
     */
    void upSpuStatus(@Param("spuId") Long spuId, @Param("code") Integer code);
}
