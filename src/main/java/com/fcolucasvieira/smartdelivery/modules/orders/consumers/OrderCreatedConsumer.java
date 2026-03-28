package com.fcolucasvieira.smartdelivery.modules.orders.consumers;

import com.fcolucasvieira.smartdelivery.configs.RabbitMQConfig;
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

        log.info("Mensagem recebida da fila. OrderId: {}", event.id());

        try {

            this.assignDeliveryManToOrderUseCase.execute(UUID.fromString(event.id()));
            log.info("Pedido {} processado com sucesso", event.id());

        } catch (IllegalStateException ex) {

            int currentRetry = (retryCount == null) ? 0 : retryCount;

            if(currentRetry < 3){
                log.warn("Sem entregador disponível para pedido {}. Tentativa {}", event.id(), currentRetry + 1);

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
                log.error("Pedido {} falhou após 3 tentativas. Enviando para DLQ", event.id(), ex);
                throw ex;
            }

        } catch (IllegalArgumentException ex){
            log.error("Erro de dados inválidos no pedido {}. Enviando direto para DLQ", event.id(), ex);
            throw ex;
        } catch (Exception ex) {
            log.error("Erro inesperado no pedido {}. Enviando para DLQ", event.id(), ex);
            throw ex;
        }
    }
}
