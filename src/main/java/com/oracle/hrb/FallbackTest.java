package com.oracle.hrb;

import com.netflix.config.ConfigurationManager;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

/**
 *回退三种情况
 * 1、断路器已经打开
 * 2、线程池队列已满
 * 3、执行方法异常
 *
 *
 *
 *
 * 断路器关闭条件
 * 默认：10秒内20个以上的请求有50%的出错，断路器自动关闭
 */

public class FallbackTest {
    public static void main(String[] args) {
        //关闭断路器
        /**
         * getConfigInstance() 获取配置实例
         * setProperty()  设置全局参数
         */
        ConfigurationManager.getConfigInstance().setProperty("hystrix.command.default.circuitBreaker.forceOpen","true");
    FallbackCommand command =new FallbackCommand();
    command.execute();
    }
  static  class  FallbackCommand extends  HystrixCommand<String>{
     //实现构造器
      protected FallbackCommand() {
          super(HystrixCommandGroupKey.Factory.asKey("ExampleCroup"));
      }

      @Override
      protected String run() throws Exception {
          System.out.println("执行了run方法。。。。。。。。。。。");
          return "执行";
      }

      @Override
      protected String getFallback() {
          System.out.println("执行了getFallback方法");
          return  "回退";
      }
  }

}