package com.gjie.kgboot.api.strategy.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import java.util.Map;

public class CommonKafkaConsumerProcessor extends AbstractKafkaConsumerProcessor<String, String, String> {
    @Override
    public String topic() {
        return "test-topic";
    }

    @Override
    protected String preCommit(ConsumerRecord<String, String> consumerRecord) {
        return null;
    }

    @Override
    protected void postCommit(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {
        return;
    }


}
