package com.oracle.hrb.issolation;

import com.netflix.config.ConfigurationManager;
import com.netflix.hystrix.HystrixCommandProperties;

/**
 * 测试
 * 根据并发量进行回退
 */
public class SemaphoreIso {
    public static void main(String[] args) throws InterruptedException {
        //设置以信号量为隔离策略
        ConfigurationManager.getConfigInstance().setProperty("hystrix.command.default.execution.isolation.strategy",
                HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE);
        //设置最大并发数2
        ConfigurationManager.getConfigInstance().setProperty("hystrix.command.default.execution.isolation.semaphore.maxConcurrentRequests", 2);
        for (int i = 0; i < 10; i++) {
            final int index = i;
            Thread t = new Thread() {
                @Override
                public void run() {
                    //这里index只能用final的，所以把i赋值给final index
                    MyCommand c = new MyCommand(index);
                    c.execute();
                }
            };
            t.start();
        }
        Thread.sleep(5000);
    }
}