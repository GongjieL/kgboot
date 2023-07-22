package com.gjie.kgboot.api.client.mq.kafka;

import com.gjie.kgboot.api.strategy.kafka.AbstractKafkaConsumerProcessor;
import com.gjie.kgboot.api.strategy.kafka.KafkaTopicProcessorFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.List;

public class KgBootKafkaConsumer {
    private static Logger logger = LogManager.getLogger("KAFKA_API_LOG");

    @KafkaListener(topics = "#{'${kgboot.kafka.consumer.topics}'.split(',')}",
            containerFactory = "kafkaListenerContainerFactory", groupId = "1")
    public void consumerBatch(List<ConsumerRecord> records, Consumer consumer) {
        System.out.println(Thread.currentThread().getName());
//        int i = 1 / 0;
        //todo 1、消费者未提交位移，只有消费者重新启动才会消费
        //     2、提交后续位移会覆盖之前未消费的
        //     3、原生的可针对topic进行线程的分配，如何保证线程分配，且还能包装执行
        //     问题：
        //     1、由于某条错误，怎么处理？发送到其他补偿/数据库记录，同时支持重试次数，重试结束还是
        //     2、全部问题怎么处理？支持，可通过配置错误条数，后续不放入数据库
        //     可通过注解解析，通过拦截器代理执行前后行为
        System.out.println(System.currentTimeMillis() + "进来了");
        for (ConsumerRecord record : records) {
            System.out.println(consumer.groupMetadata().groupId());
            AbstractKafkaConsumerProcessor consumerProcessor =
                    KafkaTopicProcessorFactory.getConsumerProcessor(record.topic());
            if (consumerProcessor == null) {
                logger.warn(StringUtils.join("无topic处理器:", record.topic()));
                continue;
            }
            //具体的处理
//            consumerProcessor.consumerMessage(record, consumer);
        }
    }


}
