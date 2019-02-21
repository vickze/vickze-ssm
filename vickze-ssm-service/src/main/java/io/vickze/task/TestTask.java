package io.vickze.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import io.vickze.aspect.Sync;
import io.vickze.lock.ZookeeperLock;

/**
 * 定时任务测试
 *
 * @author vick.zeng
 * @email zengyukang@hey900.com
 * @date 2018-02-11 15:27
 */
@EnableScheduling
@Component
public class TestTask {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    //每1分钟执行一次  测试用
    @Scheduled(cron = "0 0/1 * * * *")
    @Sync(lockNameSpace = "TestTask", lockKey = "test", lockClass = ZookeeperLock.class)
    public void test() {
        logger.debug("Task test: 1分钟执行一次");
    }
}
