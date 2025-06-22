package com.lzg.takeout.repository;

import com.lzg.takeout.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

    List<OrderDetail> findByOrderId(Long orderId);

    // 根据商家ID和用户ID查询订单详情
    List<OrderDetail> findByOrder_Merchant_IdAndOrder_User_Id(Long merchantId, Long userId);
}
