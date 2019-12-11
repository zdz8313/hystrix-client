package com.oracle.hrb;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.sun.deploy.nativesandbox.comm.Response;
import org.apache.http.HttpRequest;
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

    protected DemoCommand(String url) {
        //将来线程池可以直接使用
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
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
