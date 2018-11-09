package cn.moyada.sharingan.monitor.local;

import cn.moyada.sharingan.monitor.api.Invocation;
import cn.moyada.sharingan.monitor.api.Monitor;

import javax.annotation.PostConstruct;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class TestMonitor implements Monitor {

    @PostConstruct
    private void init() {
        System.out.println("test monitor init");
    }

    @Override
    public void listener(Invocation invocation) {
        System.out.println(invocation.getArgs().toString());
    }
}
