package com.neovarsity.ecommerce.controller;

import com.neovarsity.ecommerce.dto.CartItemRequest;
import com.neovarsity.ecommerce.service.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartController {
    
    @Autowired
    private CartService cartService;
    
    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addToCart(@Valid @RequestBody CartItemRequest request) {
        return ResponseEntity.ok(cartService.addToCart(request));
    }
    
    @GetMapping
    public ResponseEntity<Map<String, Object>> getCart() {
        return ResponseEntity.ok(cartService.getCart());
    }
    
    @DeleteMapping("/item/{cartItemId}")
    public ResponseEntity<Map<String, Object>> removeFromCart(@PathVariable Long cartItemId) {
        return ResponseEntity.ok(cartService.removeFromCart(cartItemId));
    }
    
    @DeleteMapping("/clear")
    public ResponseEntity<Map<String, Object>> clearCart() {
        return ResponseEntity.ok(cartService.clearCart());
    }
}

