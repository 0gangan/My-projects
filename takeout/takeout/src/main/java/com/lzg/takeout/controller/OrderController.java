package com.lzg.takeout.controller;

import com.lzg.takeout.dto.*;
import com.lzg.takeout.entity.*;
import com.lzg.takeout.repository.*;
import com.lzg.takeout.service.OrderDetailService;
import com.lzg.takeout.service.OrderService;
import com.lzg.takeout.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // 买家根据用户ID获取订单列表
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDTO>> getOrdersByUserId(@PathVariable Long userId) {
        List<Order> orders = orderService.findByUserId(userId);
        List<OrderDTO> dtoList = orders.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    // 商家根据商家ID获取订单列表
    @GetMapping("/merchant/{merchantId}")
    public ResponseEntity<List<OrderDTO>> getOrdersByMerchantId(@PathVariable Long merchantId) {
        List<Order> orders = orderService.findByMerchantId(merchantId);
        List<OrderDTO> dtoList = orders.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrderDetailById(@PathVariable Long orderId) {
        Optional<Order> optionalOrder = orderService.findById(orderId);
        if (!optionalOrder.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        OrderDTO dto = convertToDTO(optionalOrder.get());
        return ResponseEntity.ok(dto);
    }
    // 获取今日订单数
    @GetMapping("/merchant/{merchantId}/today-count")
    public ResponseEntity<Long> countTodayOrders(@PathVariable Long merchantId) {
        long count = orderService.countOrdersTodayByMerchantId(merchantId);
        return ResponseEntity.ok(count);
    }

//    // 获取商家在售菜品数
//    @GetMapping("/{merchantId}/dishCount")
//    public ResponseEntity<Long> getDishCount(@PathVariable Long merchantId) {
//        long count = dishService.countDishesByMerchantId(merchantId);
//        return ResponseEntity.ok(count);
//    }

    // 提交订单
    @PostMapping
    public ResponseEntity<String> submitOrder(@RequestBody OrderRequest orderRequest) {
        Order order = new Order();

        order.setTotalAmount(orderRequest.getOrder().getTotalAmount());
        order.setAddress(orderRequest.getOrder().getAddress());
        order.setStatus(orderRequest.getOrder().getStatus());
        order.setOrderTime(LocalDateTime.now());

        // 设置用户
        User user = new User();
        user.setId(orderRequest.getOrder().getUser().getId());
        order.setUser(user);

        // 设置商家
        Merchant merchant = new Merchant();
        merchant.setId(orderRequest.getOrder().getMerchant().getId());
        order.setMerchant(merchant);

        // 设置订单详情
        List<OrderDetail> detailList = orderRequest.getDetails().stream().map(detailDTO -> {
            OrderDetail detail = new OrderDetail();
            detail.setQuantity(detailDTO.getQuantity());
            detail.setPrice(detailDTO.getPrice());

            Dish dish = new Dish();
            dish.setId(detailDTO.getDish().getId());
            detail.setDish(dish);

            detail.setOrder(order); // 设置所属订单
            return detail;
        }).collect(Collectors.toList());

        order.setDetails(detailList);

        // 保存订单
        orderService.saveOrder(order);
        return ResponseEntity.ok("订单提交成功！");
    }

    // 私有方法：实体 Order 转 DTO
    private OrderDTO convertToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setAddress(order.getAddress());
        dto.setStatus(order.getStatus());
        dto.setOrderTime(order.getOrderTime());

        // 用户DTO
        if (order.getUser() != null) {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(order.getUser().getId());
            userDTO.setUsername(order.getUser().getUsername());
            // 根据需要添加 phone/email 字段
            dto.setUser(userDTO);
        }

        // 商家DTO
        if (order.getMerchant() != null) {
            MerchantDTO merchantDTO = new MerchantDTO();
            merchantDTO.setId(order.getMerchant().getId());
            merchantDTO.setName(order.getMerchant().getName());
            merchantDTO.setAddress(order.getMerchant().getAddress());
            // 根据需要添加 phone 字段
            dto.setMerchant(merchantDTO);
        }

        // 明细列表
        List<OrderDetailDTO> details = order.getDetails().stream().map(detail -> {
            OrderDetailDTO detailDTO = new OrderDetailDTO();
            detailDTO.setId(detail.getId());
            detailDTO.setQuantity(detail.getQuantity());
            detailDTO.setPrice(detail.getPrice());

            if (detail.getDish() != null) {
                DishDTO dishDTO = new DishDTO();
                dishDTO.setId(detail.getDish().getId());
                dishDTO.setName(detail.getDish().getName());
                dishDTO.setPrice(detail.getDish().getPrice());
                dishDTO.setDescription(detail.getDish().getDescription());
                dishDTO.setImageUrl(detail.getDish().getImageUrl());
                detailDTO.setDish(dishDTO);
            }

            return detailDTO;
        }).collect(Collectors.toList());

        dto.setDetails(details);
        return dto;
    }
}
