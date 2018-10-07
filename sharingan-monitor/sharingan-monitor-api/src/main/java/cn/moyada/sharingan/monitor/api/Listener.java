package cn.moyada.sharingan.monitor.api;

import java.lang.annotation.*;

/**
 * @author xueyikang
 * @since 0.0.1
 **/
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Listener {

    String value();

    RpcProtocol protocol();
}
