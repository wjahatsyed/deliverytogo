package com.deliverytogo.order_service.service;

import com.deliverytogo.order_service.dto.OrderRequest;
import com.deliverytogo.order_service.messaging.OrderEventPublisher;
import com.deliverytogo.order_service.model.Order;
import com.deliverytogo.order_service.model.OrderStatus;
import com.deliverytogo.order_service.repository.OrderRepository;
import com.deliverytogo.order_service.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class OrderServiceImplTest {

    private OrderRepository orderRepository;
    private OrderEventPublisher orderEventPublisher;
    private OrderServiceImpl orderService;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        orderEventPublisher = mock(OrderEventPublisher.class);
        orderService = new OrderServiceImpl(orderRepository, orderEventPublisher);
    }

    @Test
    void shouldPlaceOrderSuccessfully() {
        // Given
        OrderRequest request = new OrderRequest();
        request.setUserId(1L);
        request.setVendorId(2L);

        OrderRequest.ItemRequest item1 = new OrderRequest.ItemRequest();
        item1.setProductName("Burger");
        item1.setQuantity(2);
        item1.setPrice(new BigDecimal("300"));

        request.setItems(List.of(item1));

        // When
        Order savedOrder = new Order();
        savedOrder.setId(1L);
        savedOrder.setUserId(1L);
        savedOrder.setVendorId(2L);
        savedOrder.setTotalAmount(new BigDecimal("600"));

        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        Order result = orderService.placeOrder(request);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getUserId());
        assertEquals(2L, result.getVendorId());
        assertEquals(new BigDecimal("600"), result.getTotalAmount());
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(orderEventPublisher, times(1)).publishOrderPlaced(savedOrder);
    }

    @Test
    void shouldReturnOrderById() {
        // Given
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);
        order.setUserId(1L);
        order.setVendorId(2L);
        order.setTotalAmount(new BigDecimal("800"));

        when(orderRepository.findById(orderId)).thenReturn(java.util.Optional.of(order));

        // When
        Order result = orderService.getOrderById(orderId);

        // Then
        assertNotNull(result);
        assertEquals(orderId, result.getId());
        assertEquals(1L, result.getUserId());
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    void shouldUpdateOrderStatus() {
        // Given
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatus.PLACED);

        when(orderRepository.findById(orderId)).thenReturn(java.util.Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Order updatedOrder = orderService.updateOrderStatus(orderId, OrderStatus.DELIVERED);

        // Then
        assertNotNull(updatedOrder);
        assertEquals(OrderStatus.DELIVERED, updatedOrder.getStatus());
        verify(orderRepository, times(1)).save(order);
    }


}

