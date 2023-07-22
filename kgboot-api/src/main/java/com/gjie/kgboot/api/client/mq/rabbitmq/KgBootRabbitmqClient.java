package com.gjie.kgboot.api.client.mq.rabbitmq;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class KgBootRabbitmqClient {
    @Autowired
    private RabbitTemplate rabbitTemplate;


    public void send(String exchange, String message) {
        rabbitTemplate.convertAndSend(exchange, null, message);
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
        rabbitTemplate.setMandatory(true);
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

            }
        });
    }
}
