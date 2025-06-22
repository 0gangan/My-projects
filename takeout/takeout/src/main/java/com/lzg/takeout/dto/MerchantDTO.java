package com.lzg.takeout.dto;

import com.lzg.takeout.entity.User;
import lombok.Data;

import lombok.Data;

@Data
public class MerchantDTO {
    private Long id;
    private String name;
    private User user;
    private String address;
    private String phone;
    private String description;
}
