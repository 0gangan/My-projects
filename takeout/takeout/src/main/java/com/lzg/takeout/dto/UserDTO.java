package com.lzg.takeout.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String phone;
    private String email;
}
