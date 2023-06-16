package com.gjie.kgboot.api.strategy.http;

import java.util.HashMap;
import java.util.Map;

public class RespProcessorFactory {
    private static Map<String, AbstractRespProcessor> data = new HashMap<>();

    public static void registerRespProcessor(AbstractRespProcessor respProcessor) {
        data.put(respProcessor.processorCode(), respProcessor);
    }

    public static AbstractRespProcessor getRespProcessor(String code) {
        return data.get(code);
    }
}
