package com.fcolucasvieira.smartdelivery.modules.orders.consumers;

import com.fcolucasvieira.smartdelivery.configs.RabbitMQConfig;
import com.fcolucasvieira.smartdelivery.modules.orders.AssignDeliveryManToOrderUseCase;
import com.fcolucasvieira.smartdelivery.modules.orders.dto.OrderEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OrderCreatedConsumer {

    private AssignDeliveryManToOrderUseCase assignDeliveryManToOrderUseCase;

    public OrderCreatedConsumer(AssignDeliveryManToOrderUseCase assignDeliveryManToOrderUseCase) {
        this.assignDeliveryManToOrderUseCase = assignDeliveryManToOrderUseCase;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_ORDER_CREATED)
    public void listener(OrderEvent event){
        System.out.println("Chegou a mensagem");
        System.out.println("ID: " + event.id());

        try {
            this.assignDeliveryManToOrderUseCase.execute(UUID.fromString(event.id()));
        } catch (Exception ex){
            System.err.println("Error processing order: " + ex.getMessage());
        }
    }
}
