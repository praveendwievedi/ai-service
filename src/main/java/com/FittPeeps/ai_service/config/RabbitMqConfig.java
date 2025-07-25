package com.FittPeeps.ai_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Value("${rabbitmq.exchange.name}")
    public String exchangeName;
    @Value("${rabbitmq.queue.name}")
    public String queueName;
    @Value("${rabbitmq.routing.key}")
    public String routingKey;

    @Bean
    public Queue activityQueue() {
        return new Queue(queueName, true);
    }

    @Bean
    public DirectExchange activityExchange() {
        return new DirectExchange(exchangeName);
    }

    @Bean
    public Binding binding(Queue activityQueue, DirectExchange activityExchange) {
        return BindingBuilder.bind(activityQueue).to(activityExchange).with(routingKey);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
