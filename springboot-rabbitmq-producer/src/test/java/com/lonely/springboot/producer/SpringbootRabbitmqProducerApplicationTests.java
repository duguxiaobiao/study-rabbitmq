package com.lonely.springboot.producer;

import com.lonely.springboot.producer.config.RabbitMqConfig;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.security.RunAs;

@SpringBootTest
class SpringbootRabbitmqProducerApplicationTests {

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @Test
    void contextLoads() {
        this.rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME,"boot.aaa","hello spring boot mq");
    }

}
