package com.lonely.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @author 独孤小标
 * @description 基于第二种模式，工作队列模式，即 多个消费者来处理消息，但是每个消息只会被一个消费者处理
 * @since 2022/8/27 11:41 上午
 */
public class Producer_WorkQueue {


    public static void main(String[] args) throws Exception {


        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setVirtualHost("/study");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);

        //创建连接
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        //创建队列
        channel.queueDeclare("work_queue", true, false, false, null);

        for (int i = 0; i < 10; i++) {
            String message = "workqueue:" + i;
            channel.basicPublish("", "work_queue", null, message.getBytes(StandardCharsets.UTF_8));
        }

        channel.close();
        connection.close();

    }

}
