package cn.moyada.sharingan.instrument.boost;

import java.util.List;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class ASTProxy implements ClassProxy {

    @Override
    public <T> Class<T> wrapper(Class<T> target, List<ProxyMethod> methods) throws Exception {
        return null;
    }
}
