package cn.moyada.sharingan.monitor.api.entity;


/**
 * 持久化数据
 * @author xueyikang
 * @since 1.0
 **/
public interface Record<T> {

    String getApplication();

    String getDomain();

    String getProtocol();

    T getArgs();
}
