package com.oracle.hrb.issolation;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

/**
 * 熔断机制回退有两点：
 * 1、熔断器熔断会回退
 * 2、线程池满了或并发量超过阈值，或访问不成功都会回退
 *————————————————————————
 * 可选的隔离机制：
 * 1、默认的情况根据线程池来决定的，线程池一旦满了，就回退。（默认的线程池是10个线程，一旦超过就回退）
 * 2、可以选的信号量，并发时候的阈值，并发量超过阈值就会回退
 */
public class MyCommand  extends HystrixCommand<String> {
    private  int index;

    protected MyCommand(int index) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ExampleGroup")));
        this.index=index;
    }

    @Override
    protected String run() throws Exception {
        Thread.sleep(500);
        System.out.println("当前执行的索引为："+index);
        return null;
    }

    @Override
    protected String getFallback() {
        System.out.println("执行回退，当前索引为："+index);
        return  null;
    }
}
