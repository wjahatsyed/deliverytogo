package com.deliverytogo.auth_service.messaging;

import com.deliverytogo.auth_service.config.RabbitMQConfig;
import com.deliverytogo.auth_service.event.UserRegisteredEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public UserEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishUserRegisteredEvent(UserRegisteredEvent event) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.USER_REGISTERED_QUEUE, event);
    }
}
