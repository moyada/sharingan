package cn.moyada.sharingan.monitor.api;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class TestMonitor implements Monitor {

    @Override
    public void listener(Invocation invocation) {
        System.out.println(invocation.toString());
    }
}
