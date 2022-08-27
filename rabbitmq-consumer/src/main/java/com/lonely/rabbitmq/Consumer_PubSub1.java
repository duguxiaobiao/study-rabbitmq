package com.lonely.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * @author 独孤小标
 * @description 基于发布订阅模式的消费者1
 * @since 2022/8/27 1:18 下午
 */
public class Consumer_PubSub1 {


    public static void main(String[] args) throws Exception {

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setVirtualHost("/study");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        String queueName = "fanout_queue_1";
        channel.queueDeclare(queueName, true, false, false, null);

        DefaultConsumer consumer = new DefaultConsumer(channel) {
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
                System.out.println("订阅者1接收到消息：" + new String(body));
            }
        };
        channel.basicConsume(queueName, true, consumer);


    }


}
