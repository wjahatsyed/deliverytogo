package com.deliverytogo.order_service.controller;

import com.deliverytogo.order_service.dto.OrderRequest;
import com.deliverytogo.order_service.model.Order;
import com.deliverytogo.order_service.model.OrderStatus;
import com.deliverytogo.order_service.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Order> placeOrder(@RequestBody OrderRequest request) {
        Order order = orderService.placeOrder(request);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/vendor/{vendorId}")
    public ResponseEntity<List<Order>> getOrdersByVendor(@PathVariable Long vendorId) {
        return ResponseEntity.ok(orderService.getOrdersByVendor(vendorId));
    }


    @PutMapping("/{id}/status")
    public ResponseEntity<Order> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status) {
        Order updated = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(updated);
    }
}

