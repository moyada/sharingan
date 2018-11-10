package cn.moyada.sharingan.monitor.api;

import java.lang.annotation.*;

/**
 * @author xueyikang
 * @since 0.0.1
 **/
@Inherited
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Listener {

    String domain();

    RpcProtocol protocol();
}
