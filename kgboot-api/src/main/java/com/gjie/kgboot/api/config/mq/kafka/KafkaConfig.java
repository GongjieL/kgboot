package com.gjie.kgboot.api.config.mq.kafka;

import com.gjie.kgboot.api.client.mq.kafka.KgBootKafkaConsumer;
import com.gjie.kgboot.api.config.APICommonProperties;
import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.ConsumerAwareListenerErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.messaging.Message;

@Configuration
@EnableConfigurationProperties({KafkaClientProperties.class, APICommonProperties.class})
@ConditionalOnProperty(name = "kgboot.enable.kafka", havingValue = "true")
public class KafkaConfig {
    @Autowired
    private KafkaClientProperties kafkaClientProperties;



    /**
     * 消费异常处理器
     *
     * @return
     */
    @Bean
    public ConsumerAwareListenerErrorHandler consumerAwareListenerErrorHandler() {
        return new ConsumerAwareListenerErrorHandler() {
            @Override
            public Object handleError(Message<?> message, ListenerExecutionFailedException exception, Consumer<?, ?> consumer) {
                System.out.println(1111);
                //打印消费异常的消息和异常信息
//                log.error("consumer failed! message: {}, exceptionMsg: {}, groupId: {}", message, exception.getMessage(), exception.getGroupId());
                return null;
            }
        };
    }



    @Bean
    @ConditionalOnProperty(value = "spring.kafka.listener.type",
            havingValue = "batch")
    public KgBootKafkaConsumer kgBootKafkaConsumer() {
        return new KgBootKafkaConsumer();
    }

}

