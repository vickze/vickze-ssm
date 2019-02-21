package io.vickze.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import io.vickze.constant.MQConstant;

/**
 * @author vick.zeng
 * @email zengyukang@hey900.com
 * @date 2018-02-12 15:20
 */
@Component
public class MQRecevier {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @RabbitListener(queues = MQConstant.USER_LOGIN_QUEUE)
    public void onUserLogin(String msg) {
        logger.info("用户登录，ID:{}", msg);
    }
}
