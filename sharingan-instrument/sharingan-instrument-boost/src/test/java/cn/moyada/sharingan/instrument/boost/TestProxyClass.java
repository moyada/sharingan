package cn.moyada.sharingan.instrument.boost;

import cn.moyada.sharingan.monitor.api.Catch;
import cn.moyada.sharingan.monitor.api.Exclusive;
import cn.moyada.sharingan.monitor.api.Listener;
import cn.moyada.sharingan.monitor.api.RpcProtocol;

/**
 * @author xueyikang
 * @since 1.0
 **/
@Listener(domain = "test", protocol = RpcProtocol.DUBBO)
public class TestProxyClass extends AbstractClass implements Interface1 {

    @Catch("faker")
    private void boo(String value, boolean flag) {
        System.out.println(value);
    }

    @Catch
    @Override
    public void say() {

    }

    @Catch
    @Override
    public void setValue(@Exclusive String name) {

    }

    @Override
    public void jiade(String name) {

        jiade(name);
    }

    @Override
    public void go(long time) {

        System.out.println(time);
    }
}
