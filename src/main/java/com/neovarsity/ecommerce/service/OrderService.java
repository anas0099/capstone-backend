package com.neovarsity.ecommerce.service;

import com.neovarsity.ecommerce.dto.OrderRequest;
import com.neovarsity.ecommerce.model.*;
import com.neovarsity.ecommerce.repository.CartRepository;
import com.neovarsity.ecommerce.repository.OrderRepository;
import com.neovarsity.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CartRepository cartRepository;
    
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    
    @Transactional
    public Map<String, Object> createOrder(OrderRequest request) {
        User user = getCurrentUser();
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Cart is empty"));
        
        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cannot create order with empty cart");
        }
        
        Order order = new Order();
        order.setUser(user);
        order.setShippingAddress(request.getShippingAddress());
        order.setPaymentMethod(request.getPaymentMethod());
        order.setStatus(Order.OrderStatus.PENDING);
        
        BigDecimal totalAmount = BigDecimal.ZERO;
        
        for (CartItem cartItem : cart.getItems()) {
            Product product = cartItem.getProduct();
            
            if (product.getStockQuantity() < cartItem.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }
            
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(product.getPrice());
            
            order.getItems().add(orderItem);
            
            totalAmount = totalAmount.add(product.getPrice()
                    .multiply(BigDecimal.valueOf(cartItem.getQuantity())));
            
            product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
        }
        
        order.setTotalAmount(totalAmount);
        order = orderRepository.save(order);
        
        cart.getItems().clear();
        cartRepository.save(cart);
        
        Map<String, Object> response = new HashMap<>();
        response.put("orderId", order.getId());
        response.put("totalAmount", order.getTotalAmount());
        response.put("status", order.getStatus());
        response.put("message", "Order created successfully");
        return response;
    }
    
    public List<Map<String, Object>> getUserOrders() {
        User user = getCurrentUser();
        List<Order> orders = orderRepository.findByUserId(user.getId());
        
        return orders.stream().map(order -> {
            Map<String, Object> orderMap = new HashMap<>();
            orderMap.put("id", order.getId());
            orderMap.put("totalAmount", order.getTotalAmount());
            orderMap.put("status", order.getStatus());
            orderMap.put("shippingAddress", order.getShippingAddress());
            orderMap.put("paymentMethod", order.getPaymentMethod());
            orderMap.put("createdAt", order.getCreatedAt());
            orderMap.put("items", order.getItems());
            return orderMap;
        }).collect(Collectors.toList());
    }
    
    public Map<String, Object> getOrderById(Long orderId) {
        User user = getCurrentUser();
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized access to order");
        }
        
        Map<String, Object> orderMap = new HashMap<>();
        orderMap.put("id", order.getId());
        orderMap.put("totalAmount", order.getTotalAmount());
        orderMap.put("status", order.getStatus());
        orderMap.put("shippingAddress", order.getShippingAddress());
        orderMap.put("paymentMethod", order.getPaymentMethod());
        orderMap.put("createdAt", order.getCreatedAt());
        orderMap.put("items", order.getItems());
        return orderMap;
    }
    
    @Transactional
    public Map<String, Object> updateOrderStatus(Long orderId, Order.OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        order.setStatus(status);
        orderRepository.save(order);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Order status updated successfully");
        response.put("orderId", order.getId());
        response.put("newStatus", order.getStatus());
        return response;
    }
}

