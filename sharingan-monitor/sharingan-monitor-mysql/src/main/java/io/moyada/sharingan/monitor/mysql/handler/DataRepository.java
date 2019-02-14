package io.moyada.sharingan.monitor.mysql.handler;

import io.moyada.sharingan.monitor.mysql.data.Record;

import java.util.Collection;

/**
 * @author xueyikang
 * @since 1.0
 **/
public interface DataRepository {

    Integer getAppId(String appName);

    void save(Collection<Record> records);
}
