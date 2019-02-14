package io.moyada.sharingan.spring.boot.autoconfigure.test;

import io.moyada.sharingan.spring.boot.autoconfigure.annotation.Listener;

/**
 * @author xueyikang
 * @since 1.0
 **/
public interface Interface2 {

    @Listener(value = "test")
    String go(String time);
}
