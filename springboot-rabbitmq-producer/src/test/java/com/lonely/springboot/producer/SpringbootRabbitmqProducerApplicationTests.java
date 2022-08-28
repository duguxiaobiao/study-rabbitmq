package com.lonely.springboot.producer;

import com.lonely.springboot.producer.config.RabbitMqConfig;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.security.RunAs;
import java.util.UUID;

@SpringBootTest
class SpringbootRabbitmqProducerApplicationTests {

    @Autowired
    private RabbitTemplate rabbitTemplate;


    /**
     * 简单传递消息
     */
    @Test
    void simpleSendMsg() {
        this.rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, "boot.aaa", "hello spring boot mq");
    }


    @Test
    void checkMsgTransfer() {
        //故意让 exchange名错误，造成confirm 异常
        this.rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME + "xx", "boot.aa", "check msg transfer");
    }

    @Test
    void checkReturn() {
        //故意让 routingKey错误，匹配不到对应的队列，造成return事件触发
        this.rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, "aaa", "this is error msg");
    }

    /**
     * 测试ack数据，先把消息发到对应队列
     */
    @Test
    void checkAck() {
        this.rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, "ack.test", "dugu_this is ack test message");
        this.rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, "ack.test", "this is ack test message");
    }

    /**
     * 测试限流
     */
    @Test
    void checkQos() {
        for (int i = 0; i < 10; i++) {
            this.rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, "qos.test", "this is qos message" + i);
        }
    }

    /**
     * 验证队列ttl过期时间
     */
    @Test
    void checkQueueTtl() {
        for (int i = 0; i < 10; i++) {
            this.rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, "ttl.test", "this is ttl test message");
        }
    }

    /**
     * 验证消息的ttl过期时间
     */
    @Test
    void checkMessageTtl() {

        //消息其他属性设置
        MessagePostProcessor messagePostProcessor = new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                //设置过期时间
                message.getMessageProperties().setExpiration("10000");
                return message;
            }
        };
        for (int i = 0; i < 10; i++) {
            if (i == 5) {
                //设置消息过期时间
                this.rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, "ttl.test", "this is message ttl test message" + i, messagePostProcessor);
            } else {
                //不单独设置消息的过期时间
                this.rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, "ttl.test", "this is message ttl test message" + i);
            }
        }
    }


    /**
     * 验证死信队列
     */
    @Test
    void checkDeadQueue() {
        //1. 验证超出队列长度,就会进入到死信队列
        //可以看到前10条进入普通队列，后10条进入到死信队列
        for (int i = 0; i < 20; i++) {
            this.rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, "test_dead.test", "this is message...");
        }

        //2. 应该队列设置了10秒过期，所以10秒后，之前的前10条数据也会进入到死信队列中
    }


    /**
     * 验证订单延迟队列效果---使用ttl+死信队列实现
     */
    @Test
    void checkOrderDelay() {

        //设置过期时间
        MessagePostProcessor messagePostProcessor = new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                //设置过期时间为10秒，即模拟订单10秒不消费则会进入处理
                message.getMessageProperties().setExpiration("10000");
                return message;
            }
        };

        for (int i = 0; i < 10; i++) {
            this.rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, "order.test",
                    String.format("order 下单，订单号：%s", UUID.randomUUID()), messagePostProcessor);
        }

    }

}
