package com.lonely.rabbitmq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;

/**
 * @author 独孤小标
 * @description 基于订阅发布模式的生产者，给订阅的每个队列都发送相同的消息，不同的消费者独自处理数据
 * @since 2022/8/27 1:07 下午
 */
public class Producer_PubSub {


    public static void main(String[] args) throws Exception {


        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setVirtualHost("/study");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();


        // 创建交换机(基于广播类型)
        String exchangeName = "exchange_fanout";
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.FANOUT, true, false, false, null);

        // 创建队列
        String queueName1 = "fanout_queue_1";
        String queueName2 = "fanout_queue_2";
        channel.queueDeclare(queueName1, true, false, false, null);
        channel.queueDeclare(queueName2, true, false, false, null);

        // 绑定关系(广播模式下，routeingkey字段为空字符串即可)
        channel.queueBind(queueName1, exchangeName, "", null);
        channel.queueBind(queueName2, exchangeName, "", null);

        // 发送消息
        channel.basicPublish(exchangeName, "", null, "hello this is pub/sub message~~".getBytes(StandardCharsets.UTF_8));


    }


}
