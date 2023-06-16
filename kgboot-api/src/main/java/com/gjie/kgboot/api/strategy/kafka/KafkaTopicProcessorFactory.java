package com.gjie.kgboot.api.strategy.kafka;

import java.util.HashMap;
import java.util.Map;

public class KafkaTopicProcessorFactory {
    private static Map<String, AbstractKafkaConsumerProcessor> data = new HashMap<>();

    public static void registerKafkaConsumerProcessor(AbstractKafkaConsumerProcessor consumerProcessor) {
        data.put(consumerProcessor.topic(), consumerProcessor);
    }

    public static AbstractKafkaConsumerProcessor getConsumerProcessor(String code) {
        return data.get(code);
    }
}
