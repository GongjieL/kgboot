package com.gjie.kgboot.api.strategy.kafka;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.consumer.OffsetCommitCallback;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;

public abstract class AbstractKafkaConsumerProcessor<Resp, K, V> implements InitializingBean {


    public abstract String topic();


    @Value("${spring.kafka.consumer.enable-auto-commit:true}")
    private Boolean autoCommit;

    @Override
    public void afterPropertiesSet() throws Exception {
        KafkaTopicProcessorFactory.registerKafkaConsumerProcessor(this);
    }

    /**
     * 具体消费逻辑
     *
     * @param consumerRecord
     * @return
     */
    protected abstract Resp preCommit(ConsumerRecord<K, V> consumerRecord);

    protected abstract void postCommit(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception);

    public void consumerMessage(ConsumerRecord record, Consumer consumer) {

        preCommit(record);
        if (!autoCommit) {
            if (record.offset() % 2 == 0) {
                return;
            }
            //提交位移
            consumer.commitAsync(new OffsetCommitCallback() {
                @Override
                public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {
                    postCommit(offsets, exception);
                }
            });
        }
    }
}
