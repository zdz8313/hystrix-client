package com.oracle.hrb;

import com.netflix.config.ConfigurationManager;
import com.netflix.hystrix.HystrixCollapser;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class TestCollapse {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
     //设置合并时间为1秒，以为太短了
        ConfigurationManager.getConfigInstance().setProperty("hystrix.collapser.default.timerDelayInMilliseconds",1000);
        //获取请求上下文
        HystrixRequestContext context  =HystrixRequestContext.initializeContext();
        //创建请求合并器
        myHystrixCollaper hc1=new myHystrixCollaper("tom");
        myHystrixCollaper hc2=new myHystrixCollaper("jerry");
        myHystrixCollaper hc3=new myHystrixCollaper("mac");
        myHystrixCollaper hc4=new myHystrixCollaper("lucy");
        myHystrixCollaper hc5=new myHystrixCollaper("rose");
        //执行（异步）
        Future<Person> f1=hc1.queue();
        Future<Person> f2=hc2.queue();
        Future<Person> f3=hc3.queue();
        Future<Person> f4=hc4.queue();
        Future<Person> f5=hc5.queue();
        System.out.println(f1.get());
        System.out.println(f2.get());
        System.out.println(f3.get());
        System.out.println(f4.get());
        System.out.println(f5.get());

        //关闭上下文
        context.shutdown();
    }

    /**
     * 合并命令
      */
    static  class  CollapseCommand extends HystrixCommand<Map<String,Person>>{
      //请求集合
        private Collection<HystrixCollapser.CollapsedRequest<Person,String>> requests;

        protected CollapseCommand(Collection<HystrixCollapser.CollapsedRequest<Person,String>> requests) {
            super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ExampleGroup")));
            this.requests =requests;
        }

        @Override
        protected Map<String, Person> run() throws Exception {
            System.out.println("执行命令的参数数量："+requests.size());
            //处理参数
            List<String> names =new ArrayList<>();
            for (HystrixCollapser.CollapsedRequest<Person,String>  request : requests){
             //getArgument()  获取参数
             names.add(request.getArgument());
            }
            //模拟访问数据库
            return personMap(names);
        }

        private Map<String, Person> personMap(List<String> names){
            Random random =new Random();
          Map<String ,Person> map =new HashMap<>();
          for(String name :names ){
              Person p =new Person();
              p.name =name;
              p.id =UUID.randomUUID().toString();
              p.age=1+random.nextInt(50);
              map.put(name,p);
          }
          return  map;
        }
    }
    static  class  Person{
        String id;
        String name;
        int age;

        @Override
        public String toString() {
            return "Person{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }

    /**
     * 合并处理器
     */
    static  class  myHystrixCollaper extends  HystrixCollapser<Map<String, Person>,Person,String>{
        private  String name;

        public myHystrixCollaper(String name) {
            this.name = name;
        }
        //获取参数
        @Override
        public String getRequestArgument() {
            return this.name;
        }
        //创建命令
        @Override
        protected HystrixCommand<Map<String, Person>> createCommand(Collection<CollapsedRequest<Person, String>> requests) {
            return new CollapseCommand(requests);
        }
        //结果集和请求的关系映射
        @Override
        protected void mapResponseToRequests(Map<String, Person> responses, Collection<CollapsedRequest<Person, String>> requests) {
        for (CollapsedRequest<Person,String>  request : requests){
        //取返回结果
            Person person =responses.get(request.getArgument());
         //将结果映射到请求中
            request.setResponse(person);
        }
        }
    }
}
