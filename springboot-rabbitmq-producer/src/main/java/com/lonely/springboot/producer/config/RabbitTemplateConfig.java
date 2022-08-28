package com.lonely.springboot.producer.config;

import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 独孤小标
 * @description rabbitmq template类的设置
 * @since 2022/8/27 9:06 下午
 */
@Configuration
public class RabbitTemplateConfig {


    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);

        //设置消息确认传递给交换机回调事件
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                System.out.println("正确接收到客户段的消息");
            } else {
                System.out.println("传递给交换机失败，异常原因：" + cause);
            }
        });

        //设置消息确认 交换机传递给队列失败回调事件

        //这里不设置为true的话，默认是丢去消息处理
        rabbitTemplate.setMandatory(true);

        rabbitTemplate.setReturnsCallback(returned -> {
            System.out.println("消息未传递到队列中");
            System.out.println("Exchange"+returned.getExchange());
            System.out.println("getReplyText"+returned.getReplyText());
            System.out.println("getRoutingKey"+returned.getRoutingKey());
            System.out.println("Body"+new String(returned.getMessage().getBody()));
            System.out.println("getReplyCode"+returned.getReplyCode());
        });

        return rabbitTemplate;
    }


}
