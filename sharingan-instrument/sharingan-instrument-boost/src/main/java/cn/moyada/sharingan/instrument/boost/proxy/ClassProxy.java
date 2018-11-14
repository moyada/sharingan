package cn.moyada.sharingan.instrument.boost.proxy;

import cn.moyada.sharingan.instrument.boost.common.ProxyMethod;

import java.util.List;

/**
 * 类代理
 * @author xueyikang
 * @since 0.0.1
 **/
public interface ClassProxy {

    /**
     * 包装类信息，对方法增加监控逻辑，返回代理类
     * @param target
     * @param methods
     * @param <T>
     * @return
     * @throws Exception
     */
    <T> Class<T> wrapper(Class<T> target, List<ProxyMethod> methods) throws Exception;
}
