package com.clx4399.gulimall.order.intercept;

import com.clx4399.common.constant.AuthServerConstant;
import com.clx4399.common.vo.MemberResponseVo;
import com.clx4399.gulimall.order.vo.MemberRespVo;
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
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        boolean match = antPathMatcher.match("/order/order/status/**", requestURI);
        boolean match1 = antPathMatcher.match("/payed/notify", requestURI);
        if(match || match1){
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
