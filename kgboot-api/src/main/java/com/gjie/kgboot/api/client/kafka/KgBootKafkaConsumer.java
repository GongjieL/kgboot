package com.gjie.kgboot.api.client.kafka;

import com.gjie.kgboot.api.config.kafka.KafkaClientProperties;
import com.gjie.kgboot.api.strategy.kafka.AbstractKafkaConsumerProcessor;
import com.gjie.kgboot.api.strategy.kafka.KafkaTopicProcessorFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;

import javax.annotation.Resource;
import java.util.List;

public class KgBootKafkaConsumer {
    @Resource
    private KafkaClientProperties kafkaClientProperties;
    private static Logger logger = LogManager.getLogger("KAFKA_API_LOG");

    @KafkaListener(topics = "#{'${kgboot.kafka.consumer.topics}'.split(',')}", containerFactory = "kafkaListenerContainerFactory")
    public void consumerBatch(List<ConsumerRecord> records, Consumer consumer) {
        for (ConsumerRecord record : records) {
            AbstractKafkaConsumerProcessor consumerProcessor =
                    KafkaTopicProcessorFactory.getConsumerProcessor(record.topic());
            if (consumerProcessor == null) {
                logger.warn(StringUtils.join("无topic处理器:", record.topic()));
                continue;
            }
            //具体的处理
            consumerProcessor.consumerMessage(record, consumer);
        }
    }

}
