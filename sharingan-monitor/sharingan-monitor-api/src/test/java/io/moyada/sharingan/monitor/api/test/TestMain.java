package io.moyada.sharingan.monitor.api.test;

import io.moyada.sharingan.monitor.api.Monitor;
import io.moyada.sharingan.monitor.api.config.MonitorConfig;
import io.moyada.sharingan.monitor.api.entity.DefaultInvocation;
import io.moyada.sharingan.monitor.api.entity.Invocation;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class TestMain {

//    public static void main(String[] args) throws InterruptedException {
//        MonitorConfig monitorConfig = new MonitorConfig();
//        monitorConfig.setIntervalTime(300);
//        monitorConfig.setThresholdSize(100);
//
//
//        Monitor testMonitor = new AbstractAsyncMonitor(monitorConfig) {
//            @Override
//            protected void consumer(Invocation invocation) {
//                System.out.println(invocation);
//            }
//        };
//
//        Invocation invocation;
//        for (int i = 0; i < 200; i++) {
//            System.out.println(i);
//            invocation = new DefaultInvocation();
//            invocation.setApplication("test");
//            invocation.setDomain("test");
//            invocation.addArgs("id", i);
//            testMonitor.listener(invocation);
//            Thread.sleep(100L);
//        }
//
//        Thread.currentThread().join();
//    }

    public static void main(String[] args) throws InterruptedException {
        MonitorConfig monitorConfig = new MonitorConfig();
        monitorConfig.setIntervalTime(3000);
        monitorConfig.setThresholdSize(100);


        Monitor testMonitor = new TestMonitor(monitorConfig, new TestInvocationConverter(), new TestMultiConsumer());

        Invocation invocation;
        for (int i = 0; i < 500; i++) {
            System.out.println(i);
            invocation = new DefaultInvocation();
            invocation.setApplication("test");
            invocation.setDomain("test");
            invocation.addArgs("id", i);
            testMonitor.listener(invocation);
            Thread.sleep(100L);
        }

        Thread.currentThread().join();
    }
}