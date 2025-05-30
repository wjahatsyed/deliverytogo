package com.deliverytogo.order_service.controller;

import com.deliverytogo.order_service.dto.OrderRequest;
import com.deliverytogo.order_service.model.Order;
import com.deliverytogo.order_service.model.OrderStatus;
import com.deliverytogo.order_service.repository.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "testuser", roles = {"USER"})
public class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
    }

    @Test
    void shouldPlaceAndFetchOrderSuccessfully() throws Exception {
        OrderRequest.ItemRequest item = new OrderRequest.ItemRequest();
        item.setProductName("Chicken Biryani");
        item.setQuantity(2);
        item.setPrice(new BigDecimal("250"));

        OrderRequest request = new OrderRequest();
        request.setUserId(100L);
        request.setVendorId(200L);
        request.setItems(List.of(item));

        String response = mockMvc.perform(post("/api/orders")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.userId").value(100))
                .andExpect(jsonPath("$.vendorId").value(200))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Order placedOrder = objectMapper.readValue(response, Order.class);


        mockMvc.perform(get("/api/orders/" + placedOrder.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(placedOrder.getId()))
                .andExpect(jsonPath("$.status").value("PLACED"));
    }

    @Test
    void shouldUpdateOrderStatus() throws Exception {
        Order order = new Order();
        order.setUserId(101L);
        order.setVendorId(201L);
        order.setTotalAmount(new BigDecimal("1000"));
        order.setStatus(OrderStatus.PLACED);
        order = orderRepository.save(order);


        mockMvc.perform(put("/api/orders/" + order.getId() + "/status")
                        .with(csrf())
                        .param("status", "DELIVERED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DELIVERED"));
    }

    @Test
    void shouldFetchOrdersByVendor() throws Exception {
        Long vendorId = 300L;

        Order o1 = new Order();
        o1.setUserId(1L);
        o1.setVendorId(vendorId);
        o1.setTotalAmount(new BigDecimal("300"));
        o1.setStatus(OrderStatus.PLACED);

        Order o2 = new Order();
        o2.setUserId(2L);
        o2.setVendorId(vendorId);
        o2.setTotalAmount(new BigDecimal("500"));
        o2.setStatus(OrderStatus.PLACED);

        orderRepository.saveAll(List.of(o1, o2));

        mockMvc.perform(get("/api/orders/vendor/" + vendorId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }
}

