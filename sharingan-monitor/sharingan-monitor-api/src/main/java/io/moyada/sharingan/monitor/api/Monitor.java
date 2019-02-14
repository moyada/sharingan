package io.moyada.sharingan.monitor.api;

import io.moyada.sharingan.monitor.api.entity.Invocation;

/**
 * 监视器
 * @author xueyikang
 * @since 0.0.1
 **/
public interface Monitor {

    String TYPE = "null";

    /**
     * 调用数据监视
     * @param invocation
     */
    void listener(Invocation invocation);
}
