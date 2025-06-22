package com.lzg.takeout.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@Data
public class OrderDTO {
    private Long id;
    private BigDecimal totalAmount;
    private String address;
    private String status;
    private LocalDateTime orderTime;

    private UserDTO user;
    private MerchantDTO merchant;

    private List<OrderDetailDTO> details;
}

