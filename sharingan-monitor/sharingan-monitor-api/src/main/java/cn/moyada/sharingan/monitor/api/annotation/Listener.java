package cn.moyada.sharingan.monitor.api.annotation;

import cn.moyada.sharingan.monitor.api.entity.Protocol;

import java.lang.annotation.*;

/**
 * 监视标记
 * @author xueyikang
 * @since 0.0.1
 **/
@Inherited
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Listener {

    /**
     * 域信息，可被 {@link Catch#value()} 替换
     * @return
     */
    String domain();

    /**
     * 服务提供的协议
     * @return
     */
    Protocol protocol();
}
