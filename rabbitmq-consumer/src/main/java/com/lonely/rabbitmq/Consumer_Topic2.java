package com.lonely.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * @author 独孤小标
 * @description
 * @since 2022/8/27 2:36 下午
 */
public class Consumer_Topic2 {

    public static void main(String[] args) throws Exception{

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setVirtualHost("/study");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        //创建队列
        String queueName = "queue_topic_2";
        channel.queueDeclare(queueName,true,false,false,null);

        //消费消息
        Consumer consumer = new DefaultConsumer(channel){
            /**
             * No-op implementation of {@link Consumer#handleDelivery}.
             *
             * @param consumerTag
             * @param envelope
             * @param properties
             * @param body
             */
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("消费日志：" + new String(body));
            }
        };

        channel.basicConsume(queueName,true,consumer);

    }



}
