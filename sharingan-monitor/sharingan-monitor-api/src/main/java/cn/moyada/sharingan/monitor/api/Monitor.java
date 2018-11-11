package cn.moyada.sharingan.monitor.api;

import cn.moyada.sharingan.monitor.api.entity.Invocation;

/**
 * @author xueyikang
 * @since 0.0.1
 **/
public interface Monitor {

    void listener(Invocation invocation);
}
