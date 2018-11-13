package cn.moyada.sharingan.monitor.api.entity;


/**
 * @author xueyikang
 * @since 1.0
 **/
public interface Record<T> {

    String getApplication();

    String getDomain();

    String getProtocol();

    T getArgs();
}
