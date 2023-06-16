package com.gjie.kgboot.api.strategy.kafka;

import com.gjie.kgboot.api.config.KafkaClientProperties;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.consumer.OffsetCommitCallback;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;
import java.util.Map;

public abstract class AbstractKafkaConsumerProcessor<Resp, K, V> implements InitializingBean {


    @Resource
    private KafkaClientProperties kafkaClientProperties;

    public abstract String topic();


    @Override
    public void afterPropertiesSet() throws Exception {
        KafkaTopicProcessorFactory.registerKafkaConsumerProcessor(this);
    }

    /**
     * 具体消费逻辑
     * @param consumerRecord
     * @return
     */
    protected abstract Resp preCommit(ConsumerRecord<K, V> consumerRecord);

    protected abstract void postCommit(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception);

    public void consumerMessage(ConsumerRecord record, Consumer consumer) {
        preCommit(record);
        //提交位移
        if (kafkaClientProperties.getEnableAutoCommit() == null ?
                false : !kafkaClientProperties.getEnableAutoCommit()) {
            consumer.commitAsync(new OffsetCommitCallback() {
                @Override
                public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {
                    postCommit(offsets, exception);
                }
            });
        }
    }
}
