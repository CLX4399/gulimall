package com.clx4399.gulimall.auth.feign;

import com.clx4399.common.utils.R;
import com.clx4399.gulimall.auth.vo.UserLoginVo;
import com.clx4399.gulimall.auth.vo.UserRegisterVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

/**
 * @author WhtCl
 * @program: gulimall
 * @description: 用户服务
 * @date 2021-07-23 16:30:28
 */
@Component
@FeignClient("gulimall-member")
public interface MemberFeignService {

    /**
     * @param userRegisterVo
     * @return com.clx4399.common.utils.R
     * @author CLX
     * @describe: 用户服务注册接口
     * @date 2021/8/2 16:46
     */
    @PostMapping("/member/member/regist")
    @ResponseBody
    R regist(UserRegisterVo userRegisterVo);

    /**
     * @param userLoginVo
     * @return com.clx4399.common.utils.R
     * @author CLX
     * @describe: 用户服务登录接口
     * @date 2021/8/2 16:45
     */
    @PostMapping("/member/member/login")
    R login(UserLoginVo userLoginVo);

}
