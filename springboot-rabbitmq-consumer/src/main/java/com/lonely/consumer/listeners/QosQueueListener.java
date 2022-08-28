package com.lonely.consumer.listeners;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author 独孤小标
 * @description  基于消费端限流 自动拉去队列数据
 * @since 2022/8/28 11:38 上午
 */
@Component
@RabbitListener(queues = "queue_qos_boot")
public class QosQueueListener {

    /**
     * 处理消息
     * @param msg
     * @param message
     * @param channel
     * @throws Exception
     */
    @RabbitHandler
    public void process(String msg, Message message, Channel channel) throws Exception {

        //接收消息
        System.out.println("接收消息：" + msg);

        //业务处理
        Thread.sleep(1000);

        //确认
        //如果不确认，则会一直处理该消息，不会在接受新消息
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),true);
    }


}
