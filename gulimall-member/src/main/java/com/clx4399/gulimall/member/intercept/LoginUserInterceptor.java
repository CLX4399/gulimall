package com.clx4399.gulimall.member.intercept;

import com.clx4399.common.constant.AuthServerConstant;
import com.clx4399.common.vo.MemberResponseVo;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author WhtCl
 * @program: gulimall
 * @description: 用户登录拦截器
 * @date 2021-08-23 15:45:59
 */
@Component
public class LoginUserInterceptor implements HandlerInterceptor {

    public static ThreadLocal<MemberResponseVo> loginUser = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        boolean match = new AntPathMatcher().match("/member/**", requestURI);
        if(match){
            return true;
        }
        MemberResponseVo memberRespVo = (MemberResponseVo)request.getSession().getAttribute(AuthServerConstant.LOGIN_USER);
        if(memberRespVo != null){
            loginUser.set(memberRespVo);
            return true;
        } else {
            //没登录就去登录
            request.getSession().setAttribute("msg","请先进行登录");
            response.sendRedirect("http://auth.gulimall.com/login.html");
            return false;
        }

    }
}
