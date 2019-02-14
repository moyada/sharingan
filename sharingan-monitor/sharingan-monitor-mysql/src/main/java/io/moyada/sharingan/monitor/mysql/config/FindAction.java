package io.moyada.sharingan.monitor.mysql.config;

/**
 * @author xueyikang
 * @since 1.0
 **/
public interface FindAction {

    String getTable();

    String getColumn();

    String getKey();

    String getCondition();
}
