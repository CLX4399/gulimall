package com.clx4399.seckill.controller;

import com.clx4399.common.utils.R;
import com.clx4399.seckill.services.SeckillService;
import com.clx4399.seckill.to.SeckillSkuRedisTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author WhtCl
 * @program: gulimall
 * @description:
 * @date 2021-09-06 21:27:11
 */
@Controller
public class SeckillController {

    @Autowired
    SeckillService seckillService;

    /**
     * 返回当前时间可以参与的秒杀商品信息
     * @return
     */
    @ResponseBody
    @GetMapping("/currentSeckillSkus")
    public R getCurrentSeckillSkus(){
        List<SeckillSkuRedisTo> vos = seckillService.getCurrentSeckillSkus();
        return R.ok().setData(vos);
    }

    /**
     * @param skuId
     * @return com.clx4399.common.utils.R
     * @author CLX
     * @describe: 获取秒杀商品信息
     * @date 2021/9/7 10:13
     */
    @ResponseBody
    @GetMapping("/sku/seckill/{skuId}")
    public R getSkuSeckillInfo(@PathVariable Long skuId){
        SeckillSkuRedisTo to = seckillService.getSkuSeckillInfo(skuId);
        return R.ok().setData(to);
    }

    /**
     * @param killId
	 * @param key
	 * @param num
	 * @param model
     * @return java.lang.String
     * @author CLX
     * @describe:  商品秒杀操作
     * @date 2021/9/7 16:02
     */
    @GetMapping("/kill")
    public String secKill(@RequestParam("killId") String killId,
                          @RequestParam("key") String key,
                          @RequestParam("num") Integer num,
                          Model model){
        String orderSn = seckillService.kill(killId,key,num);
        model.addAttribute("orderSn",orderSn);

        //1、判断是否登录
        return "success";
    }

}
