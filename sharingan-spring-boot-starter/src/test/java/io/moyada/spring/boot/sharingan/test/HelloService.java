package io.moyada.spring.boot.sharingan.test;

/**
 * @author xueyikang
 * @since 1.0
 **/
public interface HelloService {

    String sayHello(String target);

    boolean isExist(String name);

    void save(int id, Bean bean);
}
