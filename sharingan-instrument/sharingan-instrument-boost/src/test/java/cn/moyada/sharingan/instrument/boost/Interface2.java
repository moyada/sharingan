package cn.moyada.sharingan.instrument.boost;

import cn.moyada.sharingan.monitor.api.entity.Protocol;
import cn.moyada.sharingan.monitor.api.annotation.Catch;
import cn.moyada.sharingan.monitor.api.annotation.Listener;

/**
 * @author xueyikang
 * @since 1.0
 **/
@Listener(domain = "test", protocol = Protocol.DUBBO)
public interface Interface2 {

    @Catch
    String go(long time);
}
