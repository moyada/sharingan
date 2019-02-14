package io.moyada.sharingan.spring.boot.autoconfigure.test;

import io.moyada.sharingan.spring.boot.autoconfigure.annotation.Listener;
import io.moyada.sharingan.spring.boot.autoconfigure.annotation.Rename;

/**
 * @author xueyikang
 * @since 1.0
 **/
public interface Interface1 extends Interface2 {

    void say();

    void setValue(String name);

    default void close(@Rename("haha") String name, String hehe) {

    }

    @Listener(value = "test")
    static void ping(int peng) {

    }
}
