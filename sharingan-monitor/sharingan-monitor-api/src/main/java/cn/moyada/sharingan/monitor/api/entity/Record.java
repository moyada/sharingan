package cn.moyada.sharingan.monitor.api.entity;


/**
 * @author xueyikang
 * @since 1.0
 **/
public interface Record {

    String getApplication();

    String getDomain();

    String getProtocol();

    String getArgs();
}
