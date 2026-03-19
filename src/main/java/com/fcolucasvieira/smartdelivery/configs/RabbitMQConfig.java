package com.fcolucasvieira.smartdelivery.configs;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String QUEUE_ORDER_CREATED = "pedido-criado";


    // Gera fila (RabbitMQ) ao iniciar aplicação (durável)
    @Bean
    public Queue queue(){
        return new Queue(QUEUE_ORDER_CREATED, true);
    }

    // Configura conversão automática entre objetos Java e JSON nas mensagens
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
