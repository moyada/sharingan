package io.moyada.sharingan.spring.boot.autoconfigure.test;

import io.moyada.sharingan.monitor.api.entity.Protocol;
import io.moyada.sharingan.spring.boot.autoconfigure.annotation.Listener;
import io.moyada.sharingan.spring.boot.autoconfigure.annotation.Monitor;

import java.io.Serializable;

/**
 * @author xueyikang
 * @since 1.0
 **/
@Monitor(value = Protocol.DUBBO)
public abstract class AbstractClass implements Serializable {

    private int num;

    @Listener(value = "test")
    public abstract void jiade(String name);
}
