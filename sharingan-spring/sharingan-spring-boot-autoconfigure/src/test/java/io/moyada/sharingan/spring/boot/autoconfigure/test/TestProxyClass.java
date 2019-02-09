package io.moyada.sharingan.spring.boot.autoconfigure.test;

import io.moyada.sharingan.monitor.api.entity.Protocol;
import io.moyada.sharingan.spring.boot.autoconfigure.annotation.Listener;
import io.moyada.sharingan.spring.boot.autoconfigure.annotation.Monitor;

/**
 * @author xueyikang
 * @since 1.0
 **/
@Monitor(value = Protocol.DUBBO)
public class TestProxyClass extends AbstractClass implements Interface1 {

    private String name;

    public TestProxyClass() throws IllegalStateException {
    }

    @Deprecated
    public TestProxyClass(String name) {
        this();
    }



    @Listener(value = "test")
    public void boo(String value, boolean flag) {
        System.out.println(value);
    }

    @Listener(value = "test")
    @Override
    public void say() {

    }

    @Listener(value = "test")
    @Override
    public void setValue(String name) {

    }

    @Override
    public void jiade(String name) {
//        boo(name, true);
    }

    @Override
    public String go(String time) {

        System.out.println(time);

        return "666";
    }
}
