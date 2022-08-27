package com.lonely.rabbitmq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;

/**
 * @author 独孤小标
 * @description
 * @since 2022/8/27 1:46 下午
 */
public class Producer_Routing {


    public static void main(String[] args) throws Exception {

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setVirtualHost("/study");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        //创建交换机
        String exchangeName = "exchange_routing";
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT, true, false, false, null);

        //创建队列
        String queueName1 = "queue_routing_1";
        String queueName2 = "queue_routing_2";
        channel.queueDeclare(queueName1, true, false, false, null);
        channel.queueDeclare(queueName2, true, false, false, null);

        //绑定routing
        channel.queueBind(queueName1, exchangeName, "info");

        channel.queueBind(queueName2, exchangeName, "error");
        channel.queueBind(queueName2, exchangeName, "info");
        channel.queueBind(queueName2, exchangeName, "warn");


        //发送消息
        channel.basicPublish(exchangeName, "info", null, "this is info log".getBytes(StandardCharsets.UTF_8));
        channel.basicPublish(exchangeName, "error", null, "this is error log".getBytes(StandardCharsets.UTF_8));

    }


}
