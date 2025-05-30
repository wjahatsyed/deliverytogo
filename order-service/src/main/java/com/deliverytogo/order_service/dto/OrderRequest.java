package com.deliverytogo.order_service.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderRequest {

    private Long userId;
    private Long vendorId;
    private List<ItemRequest> items;

    @Data
    public static class ItemRequest {
        private String productName;
        private int quantity;
        private BigDecimal price;
    }
}
