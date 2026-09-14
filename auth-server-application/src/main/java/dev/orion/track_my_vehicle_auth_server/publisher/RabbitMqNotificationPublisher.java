package dev.orion.track_my_vehicle_auth_server.publisher;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitMqNotificationPublisher {

    private final RabbitTemplate rabbitTemplate;

    /**
     *  Publish a message to the notification-service main exchange
     *
     */
}
