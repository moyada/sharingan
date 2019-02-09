package io.moyada.sharingan.monitor.api.test;


import io.moyada.sharingan.monitor.api.config.MonitorConfig;
import io.moyada.sharingan.monitor.api.handler.InvocationConverter;
import io.moyada.sharingan.monitor.api.handler.MultiConsumer;
import io.moyada.sharingan.monitor.api.monitor.AsyncBatchMonitor;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class TestMonitor extends AsyncBatchMonitor<String> {

    public TestMonitor(MonitorConfig monitorConfig,
                       InvocationConverter<String> converter,
                       MultiConsumer<String> consumer) {
        super(monitorConfig, converter, consumer);
    }
}
