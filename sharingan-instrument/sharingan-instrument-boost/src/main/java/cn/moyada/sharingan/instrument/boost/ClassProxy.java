package cn.moyada.sharingan.instrument.boost;

import java.util.List;

/**
 * @author xueyikang
 * @since 1.0
 **/
public interface ClassProxy {

    <T> Class<T> wrapper(Class<T> target, List<ProxyMethod> methods) throws Exception;
}
