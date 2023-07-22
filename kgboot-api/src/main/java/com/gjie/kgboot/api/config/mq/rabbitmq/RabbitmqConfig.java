package com.gjie.kgboot.api.config.mq.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {

    @Bean
    @ConditionalOnProperty(value = {"kgboot.rabbitmq.producer.confirm"},
            havingValue = "1")
    public RabbitTemplate kgBootRabbitTemplate(RabbitTemplate rabbitTemplate) {
        RabbitTemplate rabbitTemplate1 = new RabbitTemplate();
        return rabbitTemplate1;

//        return rabbitTemplate;
    }

}

