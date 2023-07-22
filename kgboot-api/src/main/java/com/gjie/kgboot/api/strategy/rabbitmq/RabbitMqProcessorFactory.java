package com.gjie.kgboot.api.strategy.rabbitmq;

import java.util.HashMap;
import java.util.Map;

public class RabbitMqProcessorFactory {
    private static Map<String, AbstractRabbitMqProcessor> data = new HashMap<>();

    public static void registerRabbitMqProcessor(AbstractRabbitMqProcessor rabbitMqProcessor) {
        data.put(rabbitMqProcessor.exchange(), rabbitMqProcessor);
    }

    public static AbstractRabbitMqProcessor getRabbitMqProcessor(String code) {
        return data.get(code);
    }
}
