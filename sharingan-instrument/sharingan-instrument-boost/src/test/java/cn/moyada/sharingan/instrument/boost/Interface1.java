package cn.moyada.sharingan.instrument.boost;

import cn.moyada.sharingan.monitor.api.Catch;
import cn.moyada.sharingan.monitor.api.Listener;
import cn.moyada.sharingan.monitor.api.Rename;
import cn.moyada.sharingan.monitor.api.RpcProtocol;

/**
 * @author xueyikang
 * @since 1.0
 **/
@Listener(domain = "test", protocol = RpcProtocol.DUBBO)
public interface Interface1 extends Interface2 {

    void say();

    void setValue(String name);

    default void close(@Rename("haha") String name, String hehe) {

    }

    @Catch
    static void ping(int peng) {

    }
}
