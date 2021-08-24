package com.clx4399.gulimall.order.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.clx4399.common.utils.R;
import com.clx4399.common.vo.MemberResponseVo;
import com.clx4399.gulimall.order.feign.CartFeignService;
import com.clx4399.gulimall.order.feign.MemberFeignService;
import com.clx4399.gulimall.order.feign.WareFeignService;
import com.clx4399.gulimall.order.intercept.LoginUserInterceptor;
import com.clx4399.gulimall.order.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clx4399.common.utils.PageUtils;
import com.clx4399.common.utils.Query;

import com.clx4399.gulimall.order.dao.OmsOrderDao;
import com.clx4399.gulimall.order.entity.OmsOrderEntity;
import com.clx4399.gulimall.order.service.OmsOrderService;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;


@Service("omsOrderService")
public class OmsOrderServiceImpl extends ServiceImpl<OmsOrderDao, OmsOrderEntity> implements OmsOrderService {

    @Autowired
    MemberFeignService memberFeignService;

    @Autowired
    CartFeignService cartFeignService;

    @Autowired
    WareFeignService wareFeignService;

    @Autowired
    ThreadPoolExecutor executor;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OmsOrderEntity> page = this.page(
                new Query<OmsOrderEntity>().getPage(params),
                new QueryWrapper<OmsOrderEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public OrderConfirmVo confirmOrder() throws ExecutionException, InterruptedException {
        OrderConfirmVo confirmVo = new OrderConfirmVo();
        MemberResponseVo memberRespVo = LoginUserInterceptor.loginUser.get();
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        CompletableFuture<Void> addresFuture = CompletableFuture.runAsync(() -> {
            //获取收货地址信息
            RequestContextHolder.setRequestAttributes(requestAttributes);
            List<MemberAddressVo> address =
                    memberFeignService.getAddress(memberRespVo.getId());
            confirmVo.setAddress(address);
        }, executor);

        CompletableFuture<Void> orderItemFuture = CompletableFuture.runAsync(() -> {
            //获取需要结算的商品信息
            RequestContextHolder.setRequestAttributes(requestAttributes);
            List<OrderItemVo> items =
                    cartFeignService.getCurrentUserCartItems();
            confirmVo.setItems(items);
        }, executor).thenRunAsync(()->{
            List<OrderItemVo> items = confirmVo.getItems();
            List<Long> collect = items.stream().map(item -> item.getSkuId()).collect(Collectors.toList());
            R skuHasStock = wareFeignService.getSkuHasStock(collect);
            List<SkuStockVo> data = skuHasStock.getData(new TypeReference<List<SkuStockVo>>() {
            });
            if (data!=null && data.size()>0){
                Map<Long, Boolean> collectMap = data.stream().collect(Collectors.toMap(SkuStockVo::getSkuId, SkuStockVo::getStock));
                confirmVo.setStocks(collectMap);
            }
        });

        //获取用户积分信息
        Integer integration = memberRespVo.getIntegration();
        confirmVo.setIntegration(integration);

        //4、其他数据自动计算


        CompletableFuture.allOf(orderItemFuture,addresFuture).get();
        return confirmVo;
    }

}
