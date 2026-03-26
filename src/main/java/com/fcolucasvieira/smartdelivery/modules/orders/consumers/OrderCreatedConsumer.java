package com.fcolucasvieira.smartdelivery.modules.orders.consumers;

import com.fcolucasvieira.smartdelivery.configs.RabbitMQConfig;
import com.fcolucasvieira.smartdelivery.modules.orders.AssignDeliveryManToOrderUseCase;
import com.fcolucasvieira.smartdelivery.modules.orders.dto.OrderEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OrderCreatedConsumer {

    private AssignDeliveryManToOrderUseCase assignDeliveryManToOrderUseCase;
    private RabbitTemplate rabbitTemplate;

    public OrderCreatedConsumer(AssignDeliveryManToOrderUseCase assignDeliveryManToOrderUseCase, RabbitTemplate rabbitTemplate) {
        this.assignDeliveryManToOrderUseCase = assignDeliveryManToOrderUseCase;
        this.rabbitTemplate = rabbitTemplate;
    }

    // Listener que processo o pedido
    // Se falhar, tenta novamente até 3 vezes antes de mandar para DLQ
    @RabbitListener(
            queues = RabbitMQConfig.QUEUE_ORDER_CREATED,
            containerFactory = "customRabbitListenerContainerFactory")
    public void listener(
            OrderEvent event,
            @Header(name = "x-retry-count", required = false) Integer retryCount
    ) {
        System.out.println("Chegou a mensagem");
        System.out.println("ID: " + event.id());

        try {
            this.assignDeliveryManToOrderUseCase.execute(UUID.fromString(event.id()));
        } catch (Exception ex) {
            int currentRetry = (retryCount == null) ? 0 : retryCount;

            if(currentRetry < 3){
                System.out.println("Retry tentativa: " + (currentRetry + 1));

                rabbitTemplate.convertAndSend(
                        RabbitMQConfig.EXCHANGE,
                        RabbitMQConfig.ROUTING_KEY_RETRY,
                        event,
                        message -> {
                            message.getMessageProperties()
                                    .setHeader("x-retry-count", currentRetry + 1);
                            return message;
                        }
                );
            } else {
                System.out.println("Enviando para DLQ após 3 tentativas");

                throw ex;
            }
        }
    }
}
