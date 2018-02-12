package io.vickze.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.vickze.constant.MQConstant;

/**
 * @author vick.zeng
 * @email zengyukang@hey900.com
 * @date 2018-02-12 15:11
 */
@Configuration
public class RabbitConfig {

    @Bean
    public Queue userLoginQueue() {
        return new Queue(MQConstant.USER_LOGIN_QUEUE);
    }
}
