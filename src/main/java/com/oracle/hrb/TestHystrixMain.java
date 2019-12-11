package com.oracle.hrb;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class TestHystrixMain {
    public static void main(String[] args) throws IOException {
        //发一个get请求                 //地址可以改为：http://localhost:8080/error进行错误测试
        HttpGet get =new HttpGet("http://localhost:8080/ok");
        CloseableHttpClient client =HttpClients.createDefault();
       HttpResponse response =client.execute(get);
        System.out.println(EntityUtils.toString(response.getEntity()));

    }
}
