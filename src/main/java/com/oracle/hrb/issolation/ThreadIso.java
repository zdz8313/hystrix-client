package com.oracle.hrb.issolation;

import com.netflix.config.ConfigurationManager;

/**测试回退
 * 根据线程池的
 */
public class ThreadIso {
    public static void main(String[] args) throws InterruptedException {
        //配置线程池的大小为3
        ConfigurationManager.getConfigInstance().setProperty("hystrix.threadpool.default.coreSize",3);
        for(int i =0;i<10; i++){
            MyCommand c =new MyCommand(i);
            //异步
            c.queue();
        }
        //让主线程等待5秒，要不然上面信息没打完就停止运行了
        Thread.sleep(5000);
    }
}
