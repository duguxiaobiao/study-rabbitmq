package com.lonely.consumer.listeners;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author 独孤小标
 * @description
 * @since 2022/8/27 5:36 下午
 */
@Component
public class BootQueueListener{


    /**
     * 代表一个监听处理消息
     * @param message
     */
    @RabbitListener(queues = "queue_boot")
    public void bootQueueMessage(Message message){
        System.out.println("接收处理消息：" + new String(message.getBody()));
    }




}
