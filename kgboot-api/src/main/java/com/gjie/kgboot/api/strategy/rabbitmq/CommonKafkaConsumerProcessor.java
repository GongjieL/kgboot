package com.gjie.kgboot.api.strategy.rabbitmq;

import com.gjie.kgboot.common.constant.CommonConstants;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CommonKafkaConsumerProcessor extends AbstractRabbitMqProcessor{

    @Override
    public String exchange() {
        return CommonConstants.RABBIT_EXCHANGE_DEFAULT;
    }

}
