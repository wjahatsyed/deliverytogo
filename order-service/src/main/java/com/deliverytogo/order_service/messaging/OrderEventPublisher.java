package com.deliverytogo.order_service.messaging;

import com.deliverytogo.order_service.config.RabbitMQConfig;
import com.deliverytogo.order_service.event.OrderPlacedEvent;
import com.deliverytogo.order_service.model.Order;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderEventPublisher {
    private final RabbitTemplate rabbitTemplate;

    public OrderEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishOrderPlaced(Order order) {
        OrderPlacedEvent event = new OrderPlacedEvent();
        event.setOrderId(order.getId());
        event.setUserId(order.getUserId());
        event.setVendorId(order.getVendorId());
        event.setTotalAmount(order.getTotalAmount());

        List<OrderPlacedEvent.Item> items = order.getItems().stream().map(i -> {
            OrderPlacedEvent.Item item = new OrderPlacedEvent.Item();
            item.setProductName(i.getProductName());
            item.setQuantity(i.getQuantity());
            item.setPrice(i.getPrice());
            return item;
        }).collect(Collectors.toList());

        event.setItems(items);

        rabbitTemplate.convertAndSend(RabbitMQConfig.ORDER_PLACED_QUEUE, event);
    }


}
