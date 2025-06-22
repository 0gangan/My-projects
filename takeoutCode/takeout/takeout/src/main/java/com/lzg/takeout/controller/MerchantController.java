package com.lzg.takeout.controller;

import com.lzg.takeout.dto.DishDTO;
import com.lzg.takeout.dto.MerchantDTO;
import com.lzg.takeout.entity.Dish;
import com.lzg.takeout.entity.Merchant;
import com.lzg.takeout.entity.User;
import com.lzg.takeout.repository.DishRepository;
import com.lzg.takeout.repository.MerchantRepository;
import com.lzg.takeout.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/merchants")
@RequiredArgsConstructor
public class MerchantController {

    private final MerchantRepository merchantRepository;
    private final DishRepository dishRepository;
    private final OrderRepository orderRepository;

    // 添加商家
    @PostMapping
    public ResponseEntity<MerchantDTO> createMerchant(@RequestBody MerchantDTO merchantDTO) {
        Merchant merchant = toEntity(merchantDTO);
        Merchant savedMerchant = merchantRepository.save(merchant);
        return ResponseEntity.ok(toDTO(savedMerchant));
    }

    // 获取所有商家
    @GetMapping
    public ResponseEntity<List<MerchantDTO>> getAllMerchants() {
        List<Merchant> merchants = merchantRepository.findAll();
        List<MerchantDTO> dtos = merchants.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // 根据 ID 获取商家
    @GetMapping("/{id}")
    public ResponseEntity<MerchantDTO> getMerchantById(@PathVariable Long id) {
        Optional<Merchant> merchant = merchantRepository.findById(id);
        return merchant.map(m -> ResponseEntity.ok(toDTO(m)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 获取指定商家的菜品列表（保留 Dish 实体）
    @GetMapping("/{merchantId}/dishes")
    public ResponseEntity<List<Dish>> getDishesByMerchant(@PathVariable Long merchantId) {
        List<Dish> dishes = dishRepository.findByMerchantId(merchantId);
        return ResponseEntity.ok(dishes);
    }

    // 获取当天订单数量
    @GetMapping("/{merchantId}/orders/today-count")
    public ResponseEntity<Map<String, Long>> getTodayOrderCount(@PathVariable Long merchantId) {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(23, 59, 59);
        long count = orderRepository.countByMerchantIdAndOrderTimeBetween(merchantId, startOfDay, endOfDay);
        return ResponseEntity.ok(Collections.singletonMap("count", count));
    }
    @PutMapping("/{id}")
    public ResponseEntity<MerchantDTO> updateMerchant(@PathVariable Long id, @RequestBody MerchantDTO merchantDTO) {
        Optional<Merchant> optionalMerchant = merchantRepository.findById(id);
        if (optionalMerchant.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Merchant merchant = optionalMerchant.get();

        // 更新商家信息
        merchant.setName(merchantDTO.getName());
        merchant.setDescription(merchantDTO.getDescription());

        // 更新关联用户信息（假设 Merchant 实体里有 getUser/setUser）
        if (merchant.getUser() != null && merchantDTO.getUser() != null) {
            User user = merchant.getUser();
            user.setUsername(merchantDTO.getUser().getUsername());
            user.setEmail(merchantDTO.getUser().getEmail());
            user.setPhone(merchantDTO.getUser().getPhone());
            user.setAddress(merchantDTO.getUser().getAddress());
            // 如果需要单独保存User，调用userRepository.save(user);
        }

        Merchant savedMerchant = merchantRepository.save(merchant);

        return ResponseEntity.ok(toDTO(savedMerchant));
    }
//    @PostMapping("/merchants/{merchantId}/dishes")
//    public ResponseEntity<DishDTO> addDish(@PathVariable Long merchantId, @RequestBody DishDTO dishDTO) {
//        Optional<Merchant> merchantOpt = merchantRepository.findById(merchantId);
//        if (merchantOpt.isEmpty()) {
//            return ResponseEntity.notFound().build();
//        }
//        Merchant merchant = merchantOpt.get();
//
//        Dish dish = new Dish();
//        dish.setName(dishDTO.getName());
//        dish.setPrice(dishDTO.getPrice());
//        dish.setDescription(dishDTO.getDescription());
//        dish.setImageUrl(dishDTO.getImageUrl());
//        dish.setAvailable(true); // 默认上架
//        dish.setCategory(dishDTO.getCategory());
//        dish.setMerchant(merchant);
//
//        Dish savedDish = dishRepository.save(dish);
//
//        return ResponseEntity.ok(toDTO(savedDish));
//    }


    // DTO 转实体
    private Merchant toEntity(MerchantDTO dto) {
        Merchant merchant = new Merchant();
        merchant.setId(dto.getId());
        merchant.setName(dto.getName());
        merchant.setAddress(dto.getAddress());
        merchant.setPhone(dto.getPhone());
        return merchant;
    }

    // 实体转 DTO
    private MerchantDTO toDTO(Merchant merchant) {
        MerchantDTO dto = new MerchantDTO();
        dto.setId(merchant.getId());
        dto.setName(merchant.getName());
        dto.setAddress(merchant.getAddress());
        dto.setPhone(merchant.getPhone());
        return dto;
    }
}
