package com.lzg.takeout.controller;

import com.lzg.takeout.entity.Dish;
import com.lzg.takeout.entity.Merchant;
import com.lzg.takeout.repository.DishRepository;
import com.lzg.takeout.repository.MerchantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/dishes")
@RequiredArgsConstructor
public class DishController {

    private final DishRepository dishRepository;
    private final MerchantRepository merchantRepository;
    @GetMapping
    public List<Dish> getAll() {
        return dishRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dish> getById(@PathVariable Long id) {
        return dishRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Dish> create(@RequestBody Dish dish) {
        if (dish.getMerchant() == null || dish.getMerchant().getId() == null) {
            return ResponseEntity.badRequest().build();
        }

        Optional<Merchant> merchantOpt = merchantRepository.findById(dish.getMerchant().getId());
        if (merchantOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        dish.setMerchant(merchantOpt.get());

        // category 也类似处理

        Dish savedDish = dishRepository.save(dish);
        return ResponseEntity.ok(savedDish);
    }


//    @PutMapping("/{id}")
    ////    public ResponseEntity<Dish> update(@PathVariable Long id, @RequestBody Dish updatedDish) {
    ////        return dishRepository.findById(id).map(dish -> {
    ////            dish.setName(updatedDish.getName());
    ////            dish.setPrice(updatedDish.getPrice());
    ////            dish.setDescription(updatedDish.getDescription());
    ////            dish.setImageUrl(updatedDish.getImageUrl());
    ////            dish.setAvailable(updatedDish.getAvailable());
    ////            dish.setCategory(updatedDish.getCategory());
    ////            dish.setMerchant(updatedDish.getMerchant());
    ////            return ResponseEntity.ok(dishRepository.save(dish));
    ////        }).orElse(ResponseEntity.notFound().build());
    ////    }
    @PutMapping("/{id}")
    public ResponseEntity<Dish> update(@PathVariable Long id, @RequestBody Dish updatedDish) {
        return dishRepository.findById(id).map(dish -> {
            if (updatedDish.getName() != null) dish.setName(updatedDish.getName());
            if (updatedDish.getPrice() != null) dish.setPrice(updatedDish.getPrice());
            if (updatedDish.getDescription() != null) dish.setDescription(updatedDish.getDescription());
            // 其他字段不更新，避免被 null 覆盖
            return ResponseEntity.ok(dishRepository.save(dish));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (dishRepository.existsById(id)) {
            dishRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
