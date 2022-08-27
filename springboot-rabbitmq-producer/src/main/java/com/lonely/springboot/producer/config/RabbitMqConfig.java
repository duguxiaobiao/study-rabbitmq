package com.lonely.springboot.producer.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 独孤小标
 * @description
 * @since 2022/8/27 4:46 下午
 */
@Configuration
public class RabbitMqConfig {

    public static final String EXCHANGE_NAME = "exchange_boot_topic";
    public static final String QUEUE_NAME = "queue_boot";


    /**
     * 基于通配符模式的交换机
     *
     * @return
     */
    @Bean
    public Exchange bootTopicExchange() {
        return ExchangeBuilder.fanoutExchange(EXCHANGE_NAME).durable(true).build();
    }

    /**
     * 创建一个队列
     *
     * @return
     */
    @Bean
    public Queue bootQueue() {
        return QueueBuilder.durable(QUEUE_NAME).build();
    }


    /**
     * 绑定关系
     *
     * @param exchange
     * @param queue
     * @return
     */
    @Bean
    public Binding binding(@Qualifier("bootTopicExchange") Exchange exchange,
                           @Qualifier("bootQueue") Queue queue) {

        return BindingBuilder.bind(queue).to(exchange).with("boot.#").noargs();
    }


}
