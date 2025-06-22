package com.lzg.takeout.dto;

import com.lzg.takeout.entity.Category;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DishDTO {
    private Long id;
    private String name;
    private BigDecimal price;
    private String description;
    private String imageUrl;
    private Category category;
}
