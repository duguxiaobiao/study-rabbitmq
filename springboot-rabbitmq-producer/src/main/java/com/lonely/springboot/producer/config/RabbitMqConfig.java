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
    public static final String EXCHANGE_DEAD_NAME = "exchange_boot_dead_topic";
    public static final String EXCHANGE_ORDER_DEAD_NAME = "exchange_boot_order_dead_topic";

    public static final String QUEUE_NAME = "queue_boot";
    public static final String QUEUE_ACK_NAME = "queue_ack_boot";
    public static final String QUEUE_QOS_NAME = "queue_qos_boot";
    public static final String QUEUE_TTL_NAME = "queue_ttl_boot";
    public static final String QUEUE_TEST_DEAD_NAME = "queue_test_dead_boot";
    public static final String QUEUE_DEAD_NAME = "queue_dead_boot";

    public static final String QUEUE_ORDER_NAME = "queue_order_boot";
    public static final String QUEUE_ORDER_DEAD_NAME = "queue_order_dead_boot";


    /**
     * 基于通配符模式的交换机
     *
     * @return
     */
    @Bean
    public Exchange bootTopicExchange() {
        return ExchangeBuilder.topicExchange(EXCHANGE_NAME).durable(true).build();
    }


    /**
     * 死信队列交换机
     *
     * @return
     */
    @Bean
    public Exchange deadTopicExchange() {
        return ExchangeBuilder.topicExchange(EXCHANGE_DEAD_NAME).durable(true).build();
    }

    /**
     * 模拟订单系统的死信交换机
     * @return
     */
    @Bean
    public Exchange orderTopicExchange(){
        return ExchangeBuilder.topicExchange(EXCHANGE_ORDER_DEAD_NAME).durable(true).build();
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
     * ack队列
     *
     * @return
     */
    @Bean
    public Queue ackQueue() {
        return QueueBuilder.durable(QUEUE_ACK_NAME).build();
    }

    /**
     * qos队列
     *
     * @return
     */
    @Bean
    public Queue qosQueue() {
        return QueueBuilder.durable(QUEUE_QOS_NAME).build();
    }


    /**
     * 设置ttl --- 队列过期时间
     *
     * @return
     */
    @Bean
    public Queue ttlQueue() {
        //ttl 设置过期时间，单位为毫秒
        return QueueBuilder.durable(QUEUE_TTL_NAME).ttl(60 * 1000).build();
    }


    /**
     * 验证死信队列中的正常队列
     *
     * @return
     */
    @Bean
    public Queue testDeadQueue() {
        //设置队列最大消息长度为 10
        //设置队列过期时间为 10秒
        //绑定死信交换机信息
        return QueueBuilder.durable(QUEUE_TEST_DEAD_NAME)
                .maxLength(10)
                .ttl(10 * 1000)
                .deadLetterExchange(EXCHANGE_DEAD_NAME).deadLetterRoutingKey("dead.test")
                .build();
    }


    /**
     * 验证死信队列
     * @return
     */
    @Bean
    public Queue deadQueue(){
        return QueueBuilder.durable(QUEUE_DEAD_NAME).build();
    }

    /**
     * 模拟订单队列，但是不配置消费者，直接等过期时间到了让其进入死信队列中
     * @return
     */
    @Bean
    public Queue orderQueue(){

        //关联死信队列
        return QueueBuilder.durable(QUEUE_ORDER_NAME)
                .deadLetterExchange(EXCHANGE_ORDER_DEAD_NAME)
                .deadLetterRoutingKey("order_dead.test")
                .build();
    }

    /**
     * 模拟订单死信队列
     * @return
     */
    @Bean
    public Queue orderDeadQueue(){
        return QueueBuilder.durable(QUEUE_ORDER_DEAD_NAME).build();
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

        return BindingBuilder.bind(queue).to(exchange).with("boot.*").noargs();
    }

    /**
     * 绑定交换机和ack队列的关系
     *
     * @param exchange
     * @param queue
     * @return
     */
    @Bean
    public Binding ackBinding(@Qualifier("bootTopicExchange") Exchange exchange,
                              @Qualifier("ackQueue") Queue queue) {
        return BindingBuilder.bind(queue).to(exchange).with("ack.*").noargs();
    }


    /**
     * 绑定交换机和qos队列的关系
     *
     * @param exchange
     * @param queue
     * @return
     */
    @Bean
    public Binding qosBinding(@Qualifier("bootTopicExchange") Exchange exchange,
                              @Qualifier("qosQueue") Queue queue) {
        return BindingBuilder.bind(queue).to(exchange).with("qos.*").noargs();
    }

    /**
     * 绑定交换机和ttl队列的关系
     *
     * @param exchange
     * @param queue
     * @return
     */
    @Bean
    public Binding ttlBinding(@Qualifier("bootTopicExchange") Exchange exchange,
                              @Qualifier("ttlQueue") Queue queue) {
        return BindingBuilder.bind(queue).to(exchange).with("ttl.*").noargs();
    }

    /**
     * 绑定交换机和测试死信队列队列的关系
     *
     * @param exchange
     * @param queue
     * @return
     */
    @Bean
    public Binding testDeadBinding(@Qualifier("bootTopicExchange") Exchange exchange,
                              @Qualifier("testDeadQueue") Queue queue) {
        return BindingBuilder.bind(queue).to(exchange).with("test_dead.*").noargs();
    }


    /**
     * 绑定死信交换机和死信队列队列的关系
     *
     * @param exchange
     * @param queue
     * @return
     */
    @Bean
    public Binding deadBinding(@Qualifier("deadTopicExchange") Exchange exchange,
                              @Qualifier("deadQueue") Queue queue) {
        return BindingBuilder.bind(queue).to(exchange).with("dead.*").noargs();
    }




    /**
     * 绑定订单交换机和订单队列的关系
     *
     * @param exchange
     * @param queue
     * @return
     */
    @Bean
    public Binding orderBinding(@Qualifier("bootTopicExchange") Exchange exchange,
                              @Qualifier("orderQueue") Queue queue) {
        return BindingBuilder.bind(queue).to(exchange).with("order.*").noargs();
    }




    /**
     * 绑定订单死信交换机和订单死信队列的关系
     *
     * @param exchange
     * @param queue
     * @return
     */
    @Bean
    public Binding orderDeadBinding(@Qualifier("orderTopicExchange") Exchange exchange,
                              @Qualifier("orderDeadQueue") Queue queue) {
        return BindingBuilder.bind(queue).to(exchange).with("order_dead.*").noargs();
    }







}
