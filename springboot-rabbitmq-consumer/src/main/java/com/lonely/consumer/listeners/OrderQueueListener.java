package com.lonely.consumer.listeners;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author 独孤小标
 * @description 订单队列监听器  -- 模拟 库存处理
 * @since 2022/8/28 3:57 下午
 */
@Component
@RabbitListener(queues = "queue_order_dead_boot")
public class OrderQueueListener {

    /**
     * 消息处理
     * @param msg
     * @param message
     * @param channel
     */
    @RabbitHandler
    public void process(String msg, Message message, Channel channel) throws IOException {
        System.out.println("接收到消息：" + msg);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),true);
    }


}
