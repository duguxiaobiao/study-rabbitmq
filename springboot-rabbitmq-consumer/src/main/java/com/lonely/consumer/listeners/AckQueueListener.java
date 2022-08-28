package com.lonely.consumer.listeners;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author 独孤小标
 * @description ack 队列监听器
 * @since 2022/8/27 11:31 下午
 */
@Component
@RabbitListener(queues = "queue_ack_boot")
public class AckQueueListener {


    /**
     * 监听该队列数据
     *
     * @param message
     * @param channel
     */
    @RabbitHandler
    public void process(String msg, Message message, Channel channel) throws Exception {

        String receiveMsg = new String(message.getBody());
        System.out.println("接收到数据：" + receiveMsg);

        if (receiveMsg.startsWith("dugu")) {
            //消息正常,则手动确认
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);

        } else {
            //消息不正常
            System.out.println("消息数据不正常");
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), true, true);
            Thread.sleep(1000);
        }

    }

}
