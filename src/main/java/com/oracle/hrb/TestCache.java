package com.oracle.hrb;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixRequestCache;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategyDefault;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;

public class TestCache {
    public static void main(String[] args) {
    //请求上下文
     HystrixRequestContext context  =HystrixRequestContext.initializeContext();
     //请求服务
        String key = "MyKey";
        MyCommand c1 =new MyCommand(key);
        MyCommand c2 =new MyCommand(key);
        MyCommand c3 =new MyCommand(key);
        MyCommand c4 =new MyCommand(key);
        System.out.println(c1.execute()+"c1缓存："+c1.isResponseFromCache());
        System.out.println(c2.execute()+"c2缓存："+c2.isResponseFromCache());
        System.out.println(c3.execute()+"c3缓存："+c3.isResponseFromCache());

        //获取缓存实例
       HystrixRequestCache cache= HystrixRequestCache.getInstance(HystrixCommandKey.Factory.asKey("MyCommandKey"),
                HystrixConcurrencyStrategyDefault.getInstance());
       //清空缓存
        cache.clear(key);
        //与前三个做对比
        System.out.println(c4.execute()+"c4缓存："+c4.isResponseFromCache());
     //关闭上下文
    context.shutdown();
    }
    static class  MyCommand extends HystrixCommand<String> {
        private  String key;

        protected MyCommand(String key) {
            super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ExampleGroup")).andCommandKey(HystrixCommandKey.Factory.asKey("MyCommandKey")));
             this.key =key;
        }


        @Override
        protected String run() throws Exception {
            System.out.println("执行了命令");
            return null;
        }

        @Override
        protected String getCacheKey() {
          return this.key;
        }
    }
}
