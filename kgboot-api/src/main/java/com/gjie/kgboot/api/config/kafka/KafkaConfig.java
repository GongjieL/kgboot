package com.gjie.kgboot.api.config.kafka;

import com.gjie.kgboot.api.client.kafka.KafkaProducerClient;
import com.gjie.kgboot.api.client.kafka.KgBootKafkaConsumer;
import com.gjie.kgboot.api.config.APICommonProperties;
import com.gjie.kgboot.api.strategy.kafka.CommonKafkaConsumerProcessor;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ConsumerAwareListenerErrorHandler;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.messaging.Message;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableConfigurationProperties({KafkaClientProperties.class, APICommonProperties.class})
@ConditionalOnProperty(name = "kgboot.enable.kafka", havingValue = "true")
public class KafkaConfig {
    @Autowired
    private KafkaClientProperties kafkaClientProperties;

    private ProducerFactory<String, String> producerFactory() {
        Map<String, Object> props = new HashMap<>();
        //kafka 集群地址
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaClientProperties.getBootstrapServers());
        //重试次数
        props.put(ProducerConfig.RETRIES_CONFIG, kafkaClientProperties.getRetires() == null ? 3 :
                kafkaClientProperties.getRetires());
        //应答级别
        props.put(ProducerConfig.ACKS_CONFIG, kafkaClientProperties.getAcks() == null ? "1" :
                kafkaClientProperties.getAcks());
        //KafkaProducer.send() 和 partitionsFor() 方法的最长阻塞时间 单位 ms
        props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, kafkaClientProperties.getMaxBlockMs() == null ? 6000 :
                kafkaClientProperties.getMaxBlockMs());
        //批量处理的最大大小 单位 byte
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, kafkaClientProperties.getBatchSize() == null ? 4096 :
                kafkaClientProperties.getBatchSize());
        //发送延时,当生产端积累的消息达到batch-size或接收到消息linger.ms后,生产者就会将消息提交给kafka
        props.put(ProducerConfig.LINGER_MS_CONFIG, kafkaClientProperties.getLingerMs() == null ? 1000 :
                kafkaClientProperties.getLingerMs());
        //生产者可用缓冲区的最大值 单位 byte
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, kafkaClientProperties.getBufferMemory() == null ? 33554432 :
                kafkaClientProperties.getBufferMemory());
        //每条消息最大的大小
        props.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, kafkaClientProperties.getMaxRequestSize() == null ? 1048576 :
                kafkaClientProperties.getMaxRequestSize());
        //客户端ID
        props.put(ProducerConfig.CLIENT_ID_CONFIG, kafkaClientProperties.getClientId() == null ? "hello-kafka" :
                kafkaClientProperties.getClientId());
        //Key 序列化方式
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, kafkaClientProperties.getKeySerializerClass() == null ?
                StringSerializer.class.getName() : kafkaClientProperties.getKeySerializerClass());
        //Value 序列化方式
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, kafkaClientProperties.getValueSerializerClass() == null ? StringSerializer.class.getName() :
                kafkaClientProperties.getValueSerializerClass());
        //消息压缩：none、lz4、gzip、snappy，默认为 none。
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, kafkaClientProperties.getCompressionType() == null ? "gzip" :
                kafkaClientProperties.getCompressionType());
        //自定义分区器
        if (kafkaClientProperties.getPartitionerClass() != null) {
            props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, kafkaClientProperties.getPartitionerClass());
        }

        return new DefaultKafkaProducerFactory<>(props);
    }

    /**
     * 包含事务 producerFactory
     *
     * @return
     */
    private ProducerFactory<String, String> producerFactoryWithTransaction() {
        DefaultKafkaProducerFactory<String, String> defaultKafkaProducerFactory = (DefaultKafkaProducerFactory<String, String>) producerFactory();
        //设置事务Id前缀
        defaultKafkaProducerFactory.setTransactionIdPrefix("tx");
        return defaultKafkaProducerFactory;
    }

    /**
     * 不包含事务 kafkaTemplate
     *
     * @return
     */
    @Bean("kafkaTemplate")
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    /**
     * 包含事务 kafkaTemplate
     *
     * @return
     */
    @Bean("kafkaTemplateWithTransaction")
    public KafkaTemplate<String, String> kafkaTemplateWithTransaction() {
        return new KafkaTemplate<>(producerFactoryWithTransaction());
    }


    @Bean("kafkaProducerClient")
    public KafkaProducerClient kafkaProducerClient() {
        return new KafkaProducerClient();
    }


    /**
     * ---消费者---
     *
     * @return
     */
    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        //设置 consumerFactory
        factory.setConsumerFactory(consumerFactory());
        //设置是否开启批量监听
        factory.setBatchListener(true);
        //设置消费者组中的线程数量(消费者组)
        factory.setConcurrency(kafkaClientProperties.getConsumerThreadNum() == null ? 1 :
                kafkaClientProperties.getConsumerThreadNum());
        if (!factory.getConsumerFactory().isAutoCommit()) {
            factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        }

        return factory;
    }

    /**
     * consumerFactory
     *
     * @return
     */
    private ConsumerFactory<String, Object> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        //kafka集群地址
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaClientProperties.getBootstrapServers());
        //自动提交 offset 默认 true
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, kafkaClientProperties.getEnableAutoCommit() == null ? true :
                kafkaClientProperties.getEnableAutoCommit());
        //自动提交的频率 单位 ms
//        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, kafkaClientProperties.getAutoCommitIntervalMs() == null ? 10000 :
//                kafkaClientProperties.getAutoCommitIntervalMs());
        //批量消费最大数量
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, kafkaClientProperties.getMaxPollRecords() == null ? 100 :
                kafkaClientProperties.getMaxPollRecords());
        //消费者组
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaClientProperties.getGroupId() == null ? "testGroup" :
                kafkaClientProperties.getGroupId());
        //session超时，超过这个时间consumer没有发送心跳,就会触发rebalance操作
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, kafkaClientProperties.getSessionTimeoutMs() == null ? 120000 :
                kafkaClientProperties.getSessionTimeoutMs());
        //请求超时
        props.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, kafkaClientProperties.getRequestTimeoutMs() == null ? 120000 :
                kafkaClientProperties.getRequestTimeoutMs());
        //Key 反序列化类
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, kafkaClientProperties.getKeyDeserializerClass() == null ? StringDeserializer.class.getName() :
                kafkaClientProperties.getKeyDeserializerClass());
        //Value 反序列化类
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, kafkaClientProperties.getValueDeserializerClass() == null ? StringDeserializer.class.getName() :
                kafkaClientProperties.getValueDeserializerClass());
        //当kafka中没有初始offset或offset超出范围时将自动重置offset
        //earliest:重置为分区中最小的offset
        //latest:重置为分区中最新的offset(消费分区中新产生的数据)
        //none:只要有一个分区不存在已提交的offset,就抛出异常
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaClientProperties.getAutoOffsetReset() == null ? "latest" :
                kafkaClientProperties.getAutoOffsetReset());
        //设置Consumer拦截器
        if (kafkaClientProperties.getInterceptorClasses() != null) {
            props.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, kafkaClientProperties.getInterceptorClasses());
        }
        return new DefaultKafkaConsumerFactory<>(props);
    }

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
                //打印消费异常的消息和异常信息
//                log.error("consumer failed! message: {}, exceptionMsg: {}, groupId: {}", message, exception.getMessage(), exception.getGroupId());
                return null;
            }
        };
    }


    @Bean
    public CommonKafkaConsumerProcessor commonKafkaConsumerProcessor() {
        return new CommonKafkaConsumerProcessor();
    }

    @Bean
    public KgBootKafkaConsumer kgBootKafkaConsumer() {
        return new KgBootKafkaConsumer();
    }

}

