package com.gjie.kgboot.api.strategy.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CommonKafkaConsumerProcessor extends AbstractKafkaConsumerProcessor<String, String, String> {
    @Override
    public String topic() {
        return "testTopic";
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
