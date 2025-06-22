package com.lzg.takeout.controller;

import com.lzg.takeout.entity.Cart;
import com.lzg.takeout.entity.Dish;
import com.lzg.takeout.entity.User;
import com.lzg.takeout.repository.CartRepository;
import com.lzg.takeout.repository.DishRepository;
import com.lzg.takeout.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final DishRepository dishRepository;

    // 获取当前用户购物车
    @GetMapping("/{userId}")
    public List<Cart> getCart(@PathVariable Long userId) {
        return cartRepository.findByUserId(userId);
    }

    // 添加购物车项
    @PostMapping
    public ResponseEntity<?> addToCart(@RequestParam Long userId,
                                       @RequestParam Long dishId,
                                       @RequestParam Integer quantity) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Dish> dishOpt = dishRepository.findById(dishId);

        if (userOpt.isEmpty() || dishOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("用户或菜品不存在");
        }

        Cart cart = new Cart();
        cart.setUser(userOpt.get());
        cart.setDish(dishOpt.get());
        cart.setQuantity(quantity);

        return ResponseEntity.ok(cartRepository.save(cart));
    }

    // 修改数量
    @PutMapping("/{cartId}")
    public ResponseEntity<?> updateQuantity(@PathVariable Long cartId, @RequestParam Integer quantity) {
        return cartRepository.findById(cartId).map(cart -> {
            cart.setQuantity(quantity);
            return ResponseEntity.ok(cartRepository.save(cart));
        }).orElse(ResponseEntity.notFound().build());
    }

    // 删除购物车项
    @DeleteMapping("/{cartId}")
    public ResponseEntity<?> deleteCartItem(@PathVariable Long cartId) {
        if (cartRepository.existsById(cartId)) {
            cartRepository.deleteById(cartId);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
