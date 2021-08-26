package com.clx4399.gulimall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clx4399.common.exception.NoStockException;
import com.clx4399.common.utils.PageUtils;
import com.clx4399.common.utils.Query;
import com.clx4399.common.utils.R;
import com.clx4399.gulimall.ware.dao.WareSkuDao;
import com.clx4399.gulimall.ware.entity.WareOrderTaskDetailEntity;
import com.clx4399.gulimall.ware.entity.WareOrderTaskEntity;
import com.clx4399.gulimall.ware.entity.WareSkuEntity;
import com.clx4399.gulimall.ware.feign.ProductFeign;
import com.clx4399.gulimall.ware.service.WareOrderTaskService;
import com.clx4399.gulimall.ware.service.WareSkuService;
import com.clx4399.gulimall.ware.vo.OrderItemVo;
import com.clx4399.gulimall.ware.vo.SkuHasStockVo;
import com.clx4399.gulimall.ware.vo.WareSkuLockVo;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

    @Autowired
    ProductFeign productFeign;

    @Autowired
    WareOrderTaskService wareOrderTaskService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<WareSkuEntity> queryWrapper = new QueryWrapper<>();
        String skuId = (String) params.get("skuId");
        String wareId = (String) params.get("wareId");

        if (StringUtils.isNotBlank(skuId)){
            queryWrapper.eq("sku_id",skuId);
        }

        if (StringUtils.isNotBlank(wareId)){
            queryWrapper.eq("ware_id",wareId);
        }

        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public void addStock(Long skuId, Long wareId, Integer skuNum) {
        List<WareSkuEntity> skuEntityList = this.baseMapper.selectList(new QueryWrapper<WareSkuEntity>().eq("sku_id", skuId)
                .eq("ware_id", wareId));
        if (skuEntityList.size()>0){
            //更新库存信息
            this.baseMapper.updateStock(skuId,wareId,skuNum);
        }else {
            //新增库存信息
            WareSkuEntity wareSkuEntity = new WareSkuEntity();
            wareSkuEntity.setSkuId(skuId);
            wareSkuEntity.setWareId(wareId);
            wareSkuEntity.setStock(skuNum);
            wareSkuEntity.setStockLocked(0);
            //TODO 处理调用异常时对方法处理
            try {
                R info = productFeign.getSkuInfo(skuId);
                Map<String, Object> skuInfo = (Map<String, Object>) info.get("skuInfo");
                wareSkuEntity.setSkuName((String) skuInfo.get("skuName"));
            }catch (Exception e){
                e.printStackTrace();
            }

            this.save(wareSkuEntity);
        }
    }

    @Override
    public List<SkuHasStockVo> getSkuHasStock(List<Long> skuIds) {
        List<SkuHasStockVo> collect = skuIds.stream().map(item -> {
            SkuHasStockVo skuHasStockVo = new SkuHasStockVo();
            Long stock = baseMapper.getStock(item);
            skuHasStockVo.setSkuId(item);
            skuHasStockVo.setStock(stock==null?false:(stock>0?true:false));
            return skuHasStockVo;
        }).collect(Collectors.toList());
        return collect;
    }

    /**
     * 为某个订单锁定库存
     *默认只要是运行时异常都会回滚
     *
     *库存解锁的场景
     * 1、下订单成功，订单过期没有支付被系统自动取消，被用户手动取消，都要解锁库存
     * 2、下订单成功，库存锁定成功，接下来的业务调用失败，导致订单回滚
     *    之前解锁的库存就要自动解锁
     * @param lockVo
     * @return
     */
    @Transactional
    @Override
    public boolean orderLockStock(WareSkuLockVo lockVo) {

        WareOrderTaskEntity taskEntity = new WareOrderTaskEntity();
        taskEntity.setOrderSn(lockVo.getOrderSn());
        wareOrderTaskService.save(taskEntity);

        //按照下单的收货地址，找到一个就近仓库，锁定库存
        //找到每个商品在哪个仓库都有库存
        List<OrderItemVo> locks = lockVo.getLocks();
        List<SkuWareHasStock> collect = locks.stream().map(item -> {
            SkuWareHasStock skuWareHasStock = new SkuWareHasStock();
            skuWareHasStock.setSkuId(item.getSkuId());
            skuWareHasStock.setNum(item.getCount());
            List<Long> wareIds = this.baseMapper.listWareIdHasSkuStock(item.getSkuId());
            skuWareHasStock.setWareId(wareIds);
            return skuWareHasStock;
        }).collect(Collectors.toList());

        //2、锁定库存
        for (SkuWareHasStock hasStock : collect) {
            boolean skuStocked = false;
            Long skuId = hasStock.getSkuId();
            List<Long> wareIds = hasStock.getWareId();
            if (wareIds == null || wareIds.size() == 0) {
                //没有任何仓库有这个商品的库存
                throw new NoStockException(skuId);
            }
            //1、如果每个商品都锁定成功，将当前商品锁定了几件的工作单记录发送给MQ
            //2、如果锁定失败，前面保存的工作的回滚，发送出去的消息，即使要解锁记录，由于去数据库查不到id,所以就不用解锁
            for (Long wareId : wareIds) {
                //成功就返回1，否则为0
                Long count = baseMapper.lockSkuStock(skuId,wareId,hasStock.getNum());
                if(count == 1){
                    skuStocked = true;
                    // TODO 告诉MQ库存锁定成功
                   /* WareOrderTaskDetailEntity entity =
                            new WareOrderTaskDetailEntity(null, skuId, null, hasStock.getNum(), taskEntity.getId(), wareId, 1);
                    wareOrderTaskDetailService.save(entity);
                    StockLockedTo lockedTo = new StockLockedTo();
                    lockedTo.setId(taskEntity.getId());
                    StockDetailTo stockDetailTo = new StockDetailTo();
                    BeanUtils.copyProperties(entity,stockDetailTo);
                    //只发id不行，防止回滚以后找不到数据
                    lockedTo.setDetail(stockDetailTo);

                    rabbitTemplate.convertAndSend("stock-event-exchange","stock.locked",lockedTo);*/
                    break;
                } else {
                    //当前仓库锁失败，重试下一个仓库
                }
            }
            if(skuStocked == false){
                //当前商品所有仓库都没有锁住
                throw new NoStockException(skuId);
            }
        }
        return false;
    }

    @Data
    class SkuWareHasStock {
        private Long skuId;
        private Integer num;
        private List<Long> wareId;
    }

}
