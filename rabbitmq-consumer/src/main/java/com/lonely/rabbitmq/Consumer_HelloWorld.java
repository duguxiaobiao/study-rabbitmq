package com.lonely.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author 独孤小标
 * @description
 * @since 2022/8/27 9:44 上午
 */
public class Consumer_HelloWorld {

    public static void main(String[] args) throws Exception {


        //1. 创建工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();

        //2. 设置配置
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/study");

        //3. 创建连接
        Connection connection = connectionFactory.newConnection();

        //4. 创建channel
        Channel channel = connection.createChannel();

        //5. 创建队列(这一步可有可没有，如果是先启动消费者，那么就需要改行代码，不然会提示找不到对应的队列)
        channel.queueDeclare("hello_world",true,false,false,null);

        //5. 消费消息
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            /**
             * 接收消息时候的回调
             *
             * @param consumerTag
             * @param envelope
             * @param properties
             * @param body
             */
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("consumerTag:" + consumerTag);
                System.out.println("Exchange:" + envelope.getExchange());
                System.out.println("RoutingKey:" + envelope.getRoutingKey());
                System.out.println("properties:" + properties);
                System.out.println("body:" + new String(body));
            }
        };
        channel.basicConsume("hello_world", true, consumer);

        //6. 关闭连接


    }


}
