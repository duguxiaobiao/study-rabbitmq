package com.lonely.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @author 独孤小标
 * @description 第一种模式，最简单的发送和接收
 * @since 2022/8/26 11:38 下午
 */
public class Producer_HelloWorld {


    public static void main(String[] args) throws Exception {


        //1. 创建工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();

        //2. 配置
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/study");

        //3. 创建连接
        Connection connection = connectionFactory.newConnection();

        //4. 创建channel
        Channel channel = connection.createChannel();

        //5. 创建队列
        /**
         * String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments
         * queue: 队列名
         * durable: 是否持久化
         * exclusive: 是否只有一个消费者
         * autoDelete: 当没有了消费者，是否自动删除该队列
         * arguments: 参数配置
         */
        channel.queueDeclare("hello_world", true, false, false, null);

        //6. 发送消息
        /**
         * String exchange, String routingKey, BasicProperties props, byte[] body
         * exchange: 交换器名，基于当前场景是最简答的场景，使用默认的即可
         * routingKey: 当使用默认的exchange时候，这里需要填写跟队列名一致
         */
        channel.basicPublish("", "hello_world", null, "hi,my name is duguxiaobiao~~".getBytes(StandardCharsets.UTF_8));

        //6. 关闭连接
        channel.close();
        connection.close();

    }


}
