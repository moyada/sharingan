package io.moyada.sharingan.monitor.api.test;

import io.moyada.sharingan.monitor.api.handler.MultiConsumer;

import java.util.Collection;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class TestMultiConsumer implements MultiConsumer<String> {

    @Override
    public void consumer(Collection<String> data) {
        System.out.println("save data: " + data.size());
    }
}
