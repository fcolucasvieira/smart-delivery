package com.fcolucasvieira.smartdelivery.modules.orders.consumers;

import com.fcolucasvieira.smartdelivery.infra.configs.RabbitMQConfig;
import com.fcolucasvieira.smartdelivery.infra.exceptions.NoDeliveryManAvailableException;
import com.fcolucasvieira.smartdelivery.modules.orders.usecases.AssignDeliveryManToOrderUseCase;
import com.fcolucasvieira.smartdelivery.modules.orders.dto.OrderEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
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

        log.info("Message received from the queue. OrderId: {}", event.id());

        try {

            this.assignDeliveryManToOrderUseCase.execute(UUID.fromString(event.id()));
            log.info("Order {} processed successfully", event.id());

        } catch (NoDeliveryManAvailableException ex) {

            int currentRetry = (retryCount == null) ? 0 : retryCount;

            if(currentRetry < 3){
                log.warn("No delivery man available for order {}. Attempt {}", event.id(), currentRetry + 1);

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
                log.error("Order {} failed after 3 attempts. Sending to DLQ", event.id(), ex);
                throw ex;
            }

        } catch (IllegalArgumentException ex){
            log.error("Invalid data error in the order {}. Sending directly to DLQ", event.id(), ex);
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected error in the order {}. Sending to DLQ", event.id(), ex);
            throw ex;
        }
    }
}
