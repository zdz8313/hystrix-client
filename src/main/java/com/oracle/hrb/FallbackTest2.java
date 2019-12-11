package com.oracle.hrb;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

/**
 * 回退是属于链式回退
 * 以下是演示
 */
public class FallbackTest2 {
    public static void main(String[] args) {
        CommandMaster command=new CommandMaster();
        //执行
        command.execute();
    }
    static  class  CommandMaster extends HystrixCommand<String>{

        protected CommandMaster() {
            super(HystrixCommandGroupKey.Factory.asKey("ExampleCroup"));
        }

        @Override
        protected String run() throws Exception {
            System.out.println("CommandMaster run .....");
            new CommandA().execute();
            new CommandB().execute();
            return null;
        }


        @Override
        protected String getFallback() {
            System.out.println("CommandMaster getFallback");
            new CommandB().execute();
            return  null;
        }
    }
    static  class  CommandA extends HystrixCommand<String>{

        protected CommandA() {
            super(HystrixCommandGroupKey.Factory.asKey("ExampleCroup"));
        }

        @Override
        protected String run() throws Exception {
            System.out.println("CommandA run .....");
            throw  new RuntimeException();//整事让他出异常
            //return null;
        }


        @Override
        protected String getFallback() {
            System.out.println("CommandA getFallback");
            return  null;
        }
    }


    static  class  CommandB extends HystrixCommand<String>{

        protected CommandB() {
            super(HystrixCommandGroupKey.Factory.asKey("ExampleCroup"));
        }

        @Override
        protected String run() throws Exception {
            System.out.println("CommandB run .....");
            return null;
        }


        @Override
        protected String getFallback() {
            System.out.println("CommandB getFallback");
            return  null;
        }
    }
}
