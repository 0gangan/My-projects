package com.lzg.takeout.repository;

import com.lzg.takeout.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
    List<Order> findByMerchantId(Long merchantId);
    long countByMerchantIdAndOrderTimeBetween(Long merchantId, LocalDateTime start, LocalDateTime end);
}
