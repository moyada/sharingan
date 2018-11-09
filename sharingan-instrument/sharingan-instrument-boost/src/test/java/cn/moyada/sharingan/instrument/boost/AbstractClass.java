package cn.moyada.sharingan.instrument.boost;

import cn.moyada.sharingan.monitor.api.Catch;
import cn.moyada.sharingan.monitor.api.Listener;
import cn.moyada.sharingan.monitor.api.RpcProtocol;

/**
 * @author xueyikang
 * @since 1.0
 **/
@Listener(value = "test", protocol = RpcProtocol.DUBBO)
public abstract class AbstractClass {

    @Catch
    public abstract void jiade(String name);
}
