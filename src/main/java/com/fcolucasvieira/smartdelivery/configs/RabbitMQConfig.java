package com.fcolucasvieira.smartdelivery.configs;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {
    public static final String EXCHANGE = "pedido-exchange";

    public static final String QUEUE_ORDER_CREATED = "pedido-criado";
    public static final String QUEUE_ORDER_CREATED_DLQ = "pedido-criado-dlq";

    public static final String ROUTING_KEY = "pedido.criado";
    public static final String ROUTING_KEY_DLQ = "pedido.criado.dlq";

    // Exchange
    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE);
    }

    // Fila principal
    @Bean
    public Queue orderCreatedQueue() {
        return QueueBuilder.durable(QUEUE_ORDER_CREATED)
                .withArgument("x-dead-letter-exchange", EXCHANGE)
                .withArgument("x-dead-letter-routing-key", ROUTING_KEY_DLQ)
                .build();
    }

    // Fila DLQ
    @Bean
    public Queue orderCreatedDLQ() {
        return QueueBuilder.durable(QUEUE_ORDER_CREATED_DLQ).build();
    }

    // Binding fila principal
    @Bean
    public Binding binding() {
        return BindingBuilder
                .bind(orderCreatedQueue())
                .to(exchange())
                .with(ROUTING_KEY);
    }

    // Binding DLQ
    @Bean
    public Binding bindingDLQ() {
        return BindingBuilder
                .bind(orderCreatedDLQ())
                .to(exchange())
                .with(ROUTING_KEY_DLQ);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         MessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory customRabbitListenerContainerFactory(ConnectionFactory connectionFactory){
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);

        factory.setMessageConverter(messageConverter());
        factory.setDefaultRequeueRejected(false);

        return factory;
    }

}
