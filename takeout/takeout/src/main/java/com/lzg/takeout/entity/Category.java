package com.lzg.takeout.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // 分类名称

    @ManyToOne
    @JoinColumn(name = "merchant_id")
    private Merchant merchant;
}
