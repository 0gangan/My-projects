package com.lzg.takeout.service;

import com.lzg.takeout.dto.OrderRequest;
import com.lzg.takeout.entity.Merchant;
import com.lzg.takeout.entity.Order;
import com.lzg.takeout.repository.DishRepository;
import com.lzg.takeout.repository.MerchantRepository;
import com.lzg.takeout.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.lzg.takeout.entity.*;
import com.lzg.takeout.repository.*;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final MerchantRepository merchantRepository;

    /**
     * 根据用户ID获取订单列表
     */
    public List<Order> findByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    /**
     * 根据商家ID获取订单列表
     */
    public List<Order> findByMerchantId(Long merchantId) {
        return orderRepository.findByMerchantId(merchantId);
    }

    /**
     * 根据订单ID查询订单详情
     */
    public Optional<Order> findById(Long orderId) {
        return orderRepository.findById(orderId);
    }

    /**
     * 保存订单（包含订单详情）
     */
    public Order saveOrder(Order order) {
        if (order.getUser() != null) {
            userRepository.findById(order.getUser().getId())
                    .orElseThrow(() -> new RuntimeException("用户不存在"));
        }
        if (order.getMerchant() != null) {
            merchantRepository.findById(order.getMerchant().getId())
                    .orElseThrow(() -> new RuntimeException("商家不存在"));
        }
        return orderRepository.save(order);
    }
    public long countOrdersTodayByMerchantId(Long merchantId) {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay(); // 今天 00:00:00
        LocalDateTime endOfDay = startOfDay.plusDays(1);           // 明天 00:00:00，不包含

        return orderRepository.countByMerchantIdAndOrderTimeBetween(merchantId, startOfDay, endOfDay);
    }

}
