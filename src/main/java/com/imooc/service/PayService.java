package com.imooc.service;

import com.imooc.dto.OrderDTO;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;

public interface PayService {
    void create(OrderDTO orderDTO);
    PayResponse notify(String notifyData);
}
