package io.vickze;

import java.util.concurrent.locks.Lock;

import io.vickze.lock.ZookeeperLock;

/**
 * @author vick.zeng
 * @email zengyukang@hey900.com
 * @date 2018-01-03 18:01
 */
public class Test {
    int n = 500;

    @org.junit.Test
    public void test() throws InterruptedException {
        Runnable runnable = () -> {
            Lock lock = null;
            try {
                lock = new ZookeeperLock("127.0.0.1:2181", "test1");
                lock.lock();
                System.out.println(--n);
                System.out.println(Thread.currentThread().getName() + "正在运行");
            } finally {
                if (lock != null) {
                    lock.unlock();
                }
            }
        };

        for (int i = 0; i < 10; i++) {
            Thread t = new Thread(runnable);
            t.start();
        }

        Thread.sleep(20000);
    }
}
