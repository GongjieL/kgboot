package com.gjie.kgboot.api.kafka;

import com.gjie.kgboot.api.config.APICommonProperties;
import com.gjie.kgboot.api.config.KafkaClientProperties;
import com.gjie.kgboot.common.constant.CommonConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.MDC;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.Resource;

/**
 * kafka发送消息客户端
 */
public class KafkaProducerClient {

    private static Logger logger = LogManager.getLogger("KAFKA_API_LOG");
    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    @Resource
    private KafkaTemplate<String, String> kafkaTemplateWithTransaction;

    @Resource
    private APICommonProperties apiCommonProperties;


    /**
     * 发送消息（异步）
     *
     * @param topic   主题
     * @param message 消息内容
     */
    public void sendMessageAsync(String topic, Integer partition, String partitionKey, String message) {
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, partition, partitionKey, message);
        //获取traceId
        String traceIdKey = apiCommonProperties.getTraceIdKey() == null ?
                CommonConstants.LOG_TRACE_ID : apiCommonProperties.getTraceIdKey();
        String traceId = MDC.get(traceIdKey);
        //添加回调
        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onFailure(Throwable throwable) {
                //traceId
                MDC.put(traceIdKey, traceId);
                //发送消息失败
                logger.error("sendMessageAsync failure! topic : {}, message: {}", topic, message);
            }

            @Override
            public void onSuccess(SendResult<String, String> stringStringSendResult) {
                //traceId
                MDC.put(traceIdKey, traceId);
                logger.info("sendMessageAsync success! topic: {}, message: {}", topic, message);
            }
        });
    }


    /**
     * 以事务方式发送消息
     *
     * @param topic
     * @param key
     * @param message
     */
    public void sendMessageInTransaction(String topic, String key, String message) {
        kafkaTemplateWithTransaction.executeInTransaction(new KafkaOperations.OperationsCallback<String, String, Object>() {
            @Override
            public Object doInOperations(KafkaOperations<String, String> kafkaOperations) {
                kafkaOperations.send(topic, key, message);
                //出现异常将会中断事务，消息不会发送出去
                throw new RuntimeException("exception");
            }
        });
    }

}

