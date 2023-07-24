package com.gjie.kgboot.api.client.mq.rabbitmq;

import com.alibaba.fastjson.JSON;
import com.gjie.kgboot.common.constant.ErrorEnum;
import com.gjie.kgboot.common.exception.BaseException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutionException;

public class KgBootRabbitmqClient {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private final static String MSG_TEMPLATE = "[exchange:%s,message:%s,detail:%s]";

    /**
     * 同步发送消息，遇到无exchange和queue会抛异常
     *
     * @param exchange
     * @param message
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public void syncSend(String exchange, Object message) {
        CorrelationData correlationData = new CorrelationData();
        //可设置msgId
        rabbitTemplate.convertSendAndReceive(exchange, null,
                message, correlationData);
        try {
            CorrelationData.Confirm confirm = correlationData.getFuture().get();
            if (!confirm.isAck()) {
                //发送exchange失败
                throw new BaseException(ErrorEnum.MSG_SEND_ERROR,
                        String.format(MSG_TEMPLATE, exchange, JSON.toJSONString(message), confirm.getReason()));
            }
        } catch (Exception e) {
            throw new BaseException(
                    String.format(MSG_TEMPLATE, exchange, JSON.toJSONString(message), StringUtils.EMPTY), e);
        }
        //queue不存在情况
        ReturnedMessage returnedMessage = correlationData.getReturned();
        if (returnedMessage != null) {
            throw new BaseException(ErrorEnum.MSG_SEND_ERROR,
                    String.format(MSG_TEMPLATE, exchange, JSON.toJSONString(message), returnedMessage.getReplyText()));
        }

    }


    public void send(String exchange, String message) {
        CorrelationData correlationData = new CorrelationData();
        rabbitTemplate.convertAndSend(exchange, null, message, correlationData);
    }

    /**
     * 初始化rabbitTemplate，设置confirm
     */
    @PostConstruct
    public void initRabbitTemplate() {
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            /**
             * 交换机(Exchange)收到消息就会回调
             * CorrelationData 当前消息的唯一关联数据
             * ack 消息是否成功送达
             * cause 失败的原因
             */
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                //查询exchange
                String exchange = correlationData.getReturned().getExchange();
                //策略判定exchange执行
                System.out.println("exchange:" + exchange + "接收成功");
            }
        });
//        // ReturnCallback消息抵达队列的回调
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            /**
             * 只要消息没有投递给指定的队列就会触发该回调
             * @param message 投递失败的消息
             * @param replayCode 回复的状态码
             * @param replayText 回复的文本内容
             * @param exchange 交换机
             * @param routingKey 路由key
             */
            @Override
            public void returnedMessage(Message message, int replayCode, String replayText, String exchange, String routingKey) {
                System.out.println(1);
            }
        });
    }
}
