package com.deliverytogo.order_service.service;

import com.deliverytogo.order_service.dto.OrderRequest;
import com.deliverytogo.order_service.model.Order;
import com.deliverytogo.order_service.model.OrderStatus;

import java.util.List;

public interface OrderService {
    Order placeOrder(OrderRequest request);

    Order getOrderById(Long id);

    Order updateOrderStatus(Long id, OrderStatus newStatus);

    List<Order> getOrdersByVendor(Long id);
}