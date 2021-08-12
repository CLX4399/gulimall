package com.clx4399.gulimall.auth.controller;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.TypeReference;
import com.clx4399.common.constant.AuthServerConstant;
import com.clx4399.common.exception.BizCodeEnum;
import com.clx4399.common.utils.R;
import com.clx4399.gulimall.auth.feign.MemberFeignService;
import com.clx4399.gulimall.auth.feign.ThridPartyFeignService;
import com.clx4399.common.vo.MemberResponseVo;
import com.clx4399.gulimall.auth.vo.UserLoginVo;
import com.clx4399.gulimall.auth.vo.UserRegisterVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author WhtCl
 * @program: gulimall
 * @description: 首页控制器
 * @date 2021-07-21 18:59:57
 */
@Controller
public class IndexController {

    @Autowired
    private ThridPartyFeignService thridPartyFeignService;

    @Autowired
    private MemberFeignService memberFeignService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * @param phone
     * @return com.clx4399.common.utils.R
     * @author CLX
     * @describe: 获取验证码，限制同个手机号获取验证码频率需间隔60s
     * @date 2021/7/28 17:08
     */
    @GetMapping("/sms/sendCode")
    @ResponseBody
    public R sendCode(@RequestParam("phone")String phone){

        //接口防刷问题
        String codeValue = redisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + phone);
        if (StringUtils.isNotBlank(codeValue)) {
            String generateCodeTime = codeValue.split("_")[1];
            if (System.currentTimeMillis() - Long.valueOf(generateCodeTime) < 60000) {
                return R.error(BizCodeEnum.SMS_CODE_EXCEPTION.getCode(), BizCodeEnum.SMS_CODE_EXCEPTION.getMessage());
            }
        }

        String code = RandomUtil.randomNumbers(6);
        redisTemplate.opsForValue().set(AuthServerConstant.SMS_CODE_CACHE_PREFIX+phone
                ,code+"_"+System.currentTimeMillis()
                ,10
                , TimeUnit.MINUTES);

        thridPartyFeignService.sendCode(phone,code);
        return R.ok();
    }

    /**
     * @param userRegisterVo
	 * @param result
	 * @param redirectAttributes
     * @return java.lang.String
     * @author CLX
     * @describe: 用户注册
     * @date 2021/8/2 16:30
     */
    @PostMapping("/register")
    public String registerOperation(@Valid UserRegisterVo userRegisterVo, BindingResult result, RedirectAttributes redirectAttributes){
        //注册参数校验
        if (result.hasErrors()){
            Map<String, String> collect = result.getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            redirectAttributes.addFlashAttribute("errors",collect);
            return "redirect:http://auth.gulimall.com/reg.html";
        }

        //开始注册前，检验验证码
        String code = redisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + userRegisterVo.getPhone());
        if (StringUtils.isNotBlank(code) && userRegisterVo.getCode().equals(code.split("_")[0])){
            redisTemplate.delete(AuthServerConstant.SMS_CODE_CACHE_PREFIX + userRegisterVo.getPhone());
        } else {
            HashMap<String, String> errors = new HashMap<>();
            errors.put("errors","验证码异常!");
            redirectAttributes.addAllAttributes(errors);
            return "redirect:http://auth.gulimall.com/reg.html";
        }

        memberFeignService.regist(userRegisterVo);
        return "redirect:http://auth.gulimall.com/login.html";
    }

    /**
     * @param userLoginVo
     * @return java.lang.String
     * @author CLX
     * @describe:
     * @date 2021/8/2 16:30
     */
    @PostMapping("/login")
    String registerOperation(UserLoginVo userLoginVo, HttpSession session) {
        R login = memberFeignService.login(userLoginVo);
        if (login.getCode()==0){
            MemberResponseVo data = login.getData("data", new TypeReference<MemberResponseVo>(){});
            session.setAttribute(AuthServerConstant.LOGIN_USER,data);
            return "redirect:http://gulimall.com";
        }else {
            return "redirect:http://gulimall.com";
        }
    }

    @RequestMapping("/login.html")
    public String loginPage(HttpSession session){
        Object attribute = session.getAttribute(AuthServerConstant.LOGIN_USER);
        return "login";
    }

    @RequestMapping("/reg.html")
    public String regPage(){
        return "reg";
    }
}
