package com.lzg.takeout.repository;

import com.lzg.takeout.entity.Dish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DishRepository extends JpaRepository<Dish, Long> {
    List<Dish> findByMerchantId(Long merchantId);
    List<Dish> findByCategoryId(Long categoryId);
}
