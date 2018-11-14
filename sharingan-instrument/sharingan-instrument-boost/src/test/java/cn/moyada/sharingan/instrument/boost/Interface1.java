package cn.moyada.sharingan.instrument.boost;

import cn.moyada.sharingan.monitor.api.entity.Protocol;
import cn.moyada.sharingan.monitor.api.annotation.Catch;
import cn.moyada.sharingan.monitor.api.annotation.Listener;
import cn.moyada.sharingan.monitor.api.annotation.Rename;

/**
 * @author xueyikang
 * @since 1.0
 **/
@Listener(domain = "test", protocol = Protocol.DUBBO)
public interface Interface1 extends Interface2 {

    void say();

    void setValue(String name);

    default void close(@Rename("haha") String name, String hehe) {

    }

    @Catch
    static void ping(int peng) {

    }
}
