package com.lonely.rabbitmq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;

/**
 * @author 独孤小标
 * @description 基于通配符模式的生产者案例
 * @since 2022/8/27 2:24 下午
 */
public class Producer_Topic {

    public static void main(String[] args) throws Exception{

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setVirtualHost("/study");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        //创建交换机
        String exchangeName = "exchange_topic";
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC,true,false,false,null);

        //创建队列
        String queueName1 = "queue_topic_1";
        String queueName2 = "queue_topic_2";
        channel.queueDeclare(queueName1,true,false,false,null);
        channel.queueDeclare(queueName2,true,false,false,null);

        //绑定关系

        //队列1 记录： 以error结尾，或者以 order开头的数据
        channel.queueBind(queueName1,exchangeName,"#.error");
        channel.queueBind(queueName1,exchangeName,"order.*");

        //队列2 记录： 所有日志记录
        channel.queueBind(queueName2,exchangeName,"*.*");


        //发送消息
        channel.basicPublish(exchangeName,"product.error",null,"product error msg".getBytes(StandardCharsets.UTF_8));
        channel.basicPublish(exchangeName,"product.info",null,"product info msg".getBytes(StandardCharsets.UTF_8));
        channel.basicPublish(exchangeName,"order.info",null,"order info msg".getBytes(StandardCharsets.UTF_8));





    }


}
