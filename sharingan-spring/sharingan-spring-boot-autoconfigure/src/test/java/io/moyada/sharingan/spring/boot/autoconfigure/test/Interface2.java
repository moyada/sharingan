package io.moyada.sharingan.spring.boot.autoconfigure.test;

import io.moyada.sharingan.monitor.api.entity.Protocol;
import io.moyada.sharingan.spring.boot.autoconfigure.annotation.Listener;
import io.moyada.sharingan.spring.boot.autoconfigure.annotation.Monitor;

/**
 * @author xueyikang
 * @since 1.0
 **/
@Monitor(value = Protocol.DUBBO)
public interface Interface2 {

    @Listener(value = "test")
    String go(String time);
}
