package com.imooc.service.Impl;

import com.imooc.dto.OrderDTO;
import com.imooc.enums.ResultEnum;
import com.imooc.exception.SellException;
import com.imooc.service.OrderService;
import com.imooc.service.PayService;
import com.imooc.utils.JsonUtil;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PayServiceImpl implements PayService {
    @Autowired
    private BestPayServiceImpl bestPayService;

    @Autowired
    private OrderService orderService;

    private static final String ORDER_NAME="微信点餐订单";

    @Override
    public void create(OrderDTO orderDTO) {
        PayRequest payRequest = new PayRequest();
        payRequest.setOpenid(orderDTO.getBuyerOpenid());
        payRequest.setOrderAmount(orderDTO.getOrderAmount().doubleValue());
        payRequest.setOrderId(orderDTO.getOrderId());
        payRequest.setOrderName(ORDER_NAME);
        payRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_H5);
        log.info("[微信支付] 发起支付 request={}", JsonUtil.toJson(payRequest));
        PayResponse payResponse = bestPayService.pay(payRequest);
        log.info("[微信支付] 发起支付={}",JsonUtil.toJson(payResponse));
    }

    @Override
    public PayResponse notify(String notifyData) {
        // 1.验证签名
        // 2.支付状态
        // 3.支付金额
        // 4.支付人(下单人==支付人)
        PayResponse payResponse = bestPayService.asyncNotify(notifyData);
        log.info("[微信支付] 异步通知,payResponse={}",JsonUtil.toJson(payResponse));

       //修改订单支付状态
        OrderDTO orderDTO = orderService.findOne(payResponse.getOrderId());
        if (orderDTO==null){
            log.error("[微信支付] 异步通知,订单不存在,orderId={}",payResponse.getOrderId());
            throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
        }
        //用comparaeTo比较金额 两个金额相减
        if (!orderDTO.getOrderAmount().equals(payResponse.getOrderAmount())){
            //log
            //error
        }
//        orderService.paid()
        return payResponse;


    }

}
