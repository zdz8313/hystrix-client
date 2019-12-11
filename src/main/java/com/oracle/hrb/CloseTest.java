package com.oracle.hrb;

import com.netflix.config.ConfigurationManager;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandMetrics;
import com.netflix.hystrix.HystrixCommandProperties;

/**
 *Hystrix默认的熔断之后会在5秒时尝试重新连接，如果此时连接正常，恢复开通状态，否则仍然保持熔断状态
 *
 */
public class CloseTest {

    public static void main(String[] args) throws InterruptedException {
   //10秒
    ConfigurationManager.getConfigInstance().setProperty("hystrix.command.default.circuitBreaker.timeInMilliseconds",10000);
   //如果超过10个请求，熔断
   ConfigurationManager.getConfigInstance().setProperty("hystrix.command.default.circuitBreaker.requestVolumeThreshold",3);
   //50%错误
   ConfigurationManager.getConfigInstance().setProperty("hystrix.command.default.circuitBreaker.errorThresholdPercentage",50);
   //熔断恢复时间
   ConfigurationManager.getConfigInstance().setProperty("hystrix.command.default.circuitBreaker.sleepWindowInMilliseconds",3000);
boolean isTimeout =true;
        for(int i =0 ;i< 30 ;i ++){
         MyComman c=new MyComman(isTimeout);
           c.execute();
          HystrixCommandMetrics.HealthCounts  health = c.getMetrics().getHealthCounts();
            System.out.println("当前断路器的状态是：" +c.isCircuitBreakerOpen()+"请求总数："+health.getTotalRequests());
       if(c.isCircuitBreakerOpen()){
         isTimeout =false;
           System.out.println("---------断路器已经熔断，等待休眠结束-------------");
           Thread.sleep(4000);
      }
        }
   }
    static  class  MyComman extends HystrixCommand<String> {
         private boolean isTimeout;

        protected MyComman(boolean isTimeout) {
            super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ExampleCroup")).andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(500)));
            this.isTimeout =isTimeout;
        }

        @Override
        protected String run() throws Exception {
            if(isTimeout){
                Thread.sleep(800);
            }else{
                Thread.sleep(200);
            }

            return null;
        }


        @Override
        protected String getFallback() {
            return  "超时";
        }
    }
}
