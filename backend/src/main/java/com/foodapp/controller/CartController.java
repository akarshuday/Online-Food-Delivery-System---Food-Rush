package com.foodapp.controller;

import com.foodapp.dto.CartDto;
import com.foodapp.dto.CartItemRequest;
import com.foodapp.entity.User;
import com.foodapp.repository.UserRepository;
import com.foodapp.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserRepository userRepository;

    private Long getCurrentUserId() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
        return user.getId();
    }

    @GetMapping
    public ResponseEntity<CartDto> getCart() {
        return ResponseEntity.ok(cartService.getCartDtoByUserId(getCurrentUserId()));
    }

    @PostMapping("/add")
    public ResponseEntity<CartDto> addItemToCart(@RequestBody CartItemRequest request) {
        return ResponseEntity.ok(cartService.addItemToCart(getCurrentUserId(), request));
    }

    @PutMapping("/update/{cartItemId}")
    public ResponseEntity<CartDto> updateCartItem(@PathVariable Long cartItemId, @RequestParam int quantity) {
        return ResponseEntity.ok(cartService.updateCartItem(cartItemId, quantity));
    }

    @DeleteMapping("/remove/{cartItemId}")
    public ResponseEntity<CartDto> removeItemFromCart(@PathVariable Long cartItemId) {
        return ResponseEntity.ok(cartService.removeItemFromCart(cartItemId));
    }

    @DeleteMapping("/clear")
    public ResponseEntity<CartDto> clearCart() {
        return ResponseEntity.ok(cartService.clearCart(getCurrentUserId()));
    }
}
