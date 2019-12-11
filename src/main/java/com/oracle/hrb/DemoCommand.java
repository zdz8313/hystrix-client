package com.oracle.hrb;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class DemoCommand  extends HystrixCommand {
  //定义url
   private String url;
  private CloseableHttpClient httpClient;
    //构造器
    protected DemoCommand(String url) {
        //将来线程池可以直接使用
       // super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
        //设置的超时时间单位毫秒
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ExampleGroup")).andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(11000)));//设置超时的时间
        this.url=url;
        this.httpClient= HttpClients.createDefault();
    }

    @Override
    protected Object run() throws IOException {
        HttpGet get = new HttpGet(url);
         HttpResponse response =httpClient.execute(get);
        return EntityUtils.toString(response.getEntity());
    }

    @Override
    protected Object getFallback() {
        System.out.println("回退");
        return  "系统异常";
    }
}
