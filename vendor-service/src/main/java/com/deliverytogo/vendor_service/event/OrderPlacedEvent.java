package com.deliverytogo.vendor_service.event;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderPlacedEvent implements Serializable {
    private Long orderId;
    private Long userId;
    private Long vendorId;
    private BigDecimal totalAmount;
    private List<Item> items;

    @Data
    public static class Item {
        private String productName;
        private int quantity;
        private BigDecimal price;
    }
}