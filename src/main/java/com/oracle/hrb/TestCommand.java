package com.oracle.hrb;

public class TestCommand {
    public static void main(String[] args) {
        String url ="http://localhost:8080/error";
        DemoCommand command =new DemoCommand(url);
        String result = (String) command.execute();
        System.out.println("请求服务，返回数据："+result);

    }
}
//显示异常 timed-out  超时