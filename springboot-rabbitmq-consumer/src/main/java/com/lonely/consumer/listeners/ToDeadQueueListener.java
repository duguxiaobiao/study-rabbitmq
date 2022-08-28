package com.lonely.consumer.listeners;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author 独孤小标
 * @description 测试让消息进入到死信队列监听
 * @since 2022/8/28 2:26 下午
 */
@Component
@RabbitListener(queues = "queue_test_dead_boot")
public class ToDeadQueueListener {


    @RabbitHandler
    public void process(String msg, Message message, Channel channel) throws Exception {
        System.out.println("接收到消息：" + msg);

        //让其进入到死信队列中
        channel.basicNack(message.getMessageProperties().getDeliveryTag(),true,false);
    }




}
