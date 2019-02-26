package io.moyada.spring.boot.sharingan.test;

import io.moyada.sharingan.monitor.api.entity.Protocol;
import io.moyada.spring.boot.sharingan.annotation.Exclusive;
import io.moyada.spring.boot.sharingan.annotation.Listener;
import io.moyada.spring.boot.sharingan.annotation.Monitor;
import io.moyada.spring.boot.sharingan.annotation.Register;
import org.springframework.stereotype.Service;

/**
 * @author xueyikang
 * @since 1.0
 **/
@Service
@Monitor(protocol = Protocol.Dubbo)
public class HelloServiceImpl implements HelloService {

    @Listener("name")
    @Register
    @Override
    public String sayHello(String target) {
        return null;
    }

    @Listener("name")
    @Override
    public boolean isExist(String name) {
        return false;
    }

    @Listener("id")
    @Register
    @Override
    public void save(int id, @Exclusive Bean bean) {

    }
}
