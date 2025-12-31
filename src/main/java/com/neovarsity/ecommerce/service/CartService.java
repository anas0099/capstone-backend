package com.neovarsity.ecommerce.service;

import com.neovarsity.ecommerce.dto.CartItemRequest;
import com.neovarsity.ecommerce.model.Cart;
import com.neovarsity.ecommerce.model.CartItem;
import com.neovarsity.ecommerce.model.Product;
import com.neovarsity.ecommerce.model.User;
import com.neovarsity.ecommerce.repository.CartRepository;
import com.neovarsity.ecommerce.repository.ProductRepository;
import com.neovarsity.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class CartService {
    
    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    
    @Transactional
    public Map<String, Object> addToCart(CartItemRequest request) {
        User user = getCurrentUser();
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
        
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        if (product.getStockQuantity() < request.getQuantity()) {
            throw new RuntimeException("Insufficient stock");
        }
        
        CartItem existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(request.getProductId()))
                .findFirst()
                .orElse(null);
        
        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + request.getQuantity());
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(request.getQuantity());
            cart.getItems().add(cartItem);
        }
        
        cartRepository.save(cart);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Item added to cart successfully");
        response.put("cartItems", cart.getItems().size());
        return response;
    }
    
    public Map<String, Object> getCart() {
        User user = getCurrentUser();
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
        
        BigDecimal total = cart.getItems().stream()
                .map(item -> item.getProduct().getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        Map<String, Object> response = new HashMap<>();
        response.put("items", cart.getItems());
        response.put("totalAmount", total);
        response.put("itemCount", cart.getItems().size());
        return response;
    }
    
    @Transactional
    public Map<String, Object> removeFromCart(Long cartItemId) {
        User user = getCurrentUser();
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        
        cart.getItems().removeIf(item -> item.getId().equals(cartItemId));
        cartRepository.save(cart);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Item removed from cart successfully");
        return response;
    }
    
    @Transactional
    public Map<String, Object> clearCart() {
        User user = getCurrentUser();
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        
        cart.getItems().clear();
        cartRepository.save(cart);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Cart cleared successfully");
        return response;
    }
}

