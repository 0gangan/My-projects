package com.lzg.takeout.service;

import com.lzg.takeout.entity.OrderDetail;
import com.lzg.takeout.repository.OrderDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;

    public OrderDetail save(OrderDetail orderDetail) {
        return orderDetailRepository.save(orderDetail);
    }

    public List<OrderDetail> saveAll(List<OrderDetail> details) {
        return orderDetailRepository.saveAll(details);
    }

    public List<OrderDetail> findByOrderId(Long orderId) {
        return orderDetailRepository.findByOrderId(orderId);
    }

    // 新增根据商家ID和用户ID查询订单详情
    public List<OrderDetail> findByMerchantIdAndUserId(Long merchantId, Long userId) {
        return orderDetailRepository.findByOrder_Merchant_IdAndOrder_User_Id(merchantId, userId);
    }
}
