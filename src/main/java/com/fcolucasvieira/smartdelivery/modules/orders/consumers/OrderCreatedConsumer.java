package com.fcolucasvieira.smartdelivery.modules.orders.consumers;

import com.fcolucasvieira.smartdelivery.configs.RabbitMQConfig;
import com.fcolucasvieira.smartdelivery.modules.orders.CreateDeliveryOrderUseCase;
import com.fcolucasvieira.smartdelivery.modules.orders.dto.OrderEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OrderCreatedConsumer {

    private CreateDeliveryOrderUseCase createDeliveryOrderUseCase;

    public OrderCreatedConsumer(CreateDeliveryOrderUseCase createDeliveryOrderUseCase) {
        this.createDeliveryOrderUseCase = createDeliveryOrderUseCase;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_ORDER_CREATED)
    public void listener(OrderEvent event){
        // Debug para testes
        System.out.println("Chegou mensagem");
        System.out.println(event.id());

        this.createDeliveryOrderUseCase.execute(UUID.fromString(event.id()));
    }
}
