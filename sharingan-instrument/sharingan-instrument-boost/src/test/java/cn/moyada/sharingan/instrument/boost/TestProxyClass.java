package cn.moyada.sharingan.instrument.boost;

import cn.moyada.sharingan.monitor.api.entity.Protocol;
import cn.moyada.sharingan.monitor.api.annotation.Catch;
import cn.moyada.sharingan.monitor.api.annotation.Exclusive;
import cn.moyada.sharingan.monitor.api.annotation.Listener;

/**
 * @author xueyikang
 * @since 1.0
 **/
@Listener(domain = "test", protocol = Protocol.DUBBO)
public class TestProxyClass extends AbstractClass implements Interface1 {

    private String name;

    public TestProxyClass() throws IllegalStateException {
    }

    @Deprecated
    public TestProxyClass(String name) {
        this();
    }



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
//        boo(name, true);
    }

    @Override
    public String go(long time) {

        System.out.println(time);

        return "666";
    }
}
