package com.lzg.takeout.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class LoginResponse {
    private Long id;      // 用户ID
    private String token;
    private String username;
    private String role;
    private String email;
    private String phone;
    private String address;
}
