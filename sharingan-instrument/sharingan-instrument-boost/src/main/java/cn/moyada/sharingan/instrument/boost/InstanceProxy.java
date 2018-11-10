package cn.moyada.sharingan.instrument.boost;

import java.util.List;

/**
 * @author xueyikang
 * @since 1.0
 **/
public interface InstanceProxy {

    <T> T proxy(T instance, List<ProxyMethod> methods);
}
