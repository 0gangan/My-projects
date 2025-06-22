package com.lzg.takeout.dto;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderDetailDTO {
    private Long id;
    private Integer quantity;
    private BigDecimal price;

    private DishDTO dish;
}
