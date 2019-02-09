package io.moyada.sharingan.monitor.database.handler;

import io.moyada.sharingan.monitor.database.data.Record;

import java.util.Collection;

/**
 * @author xueyikang
 * @since 1.0
 **/
public interface DataRepository {

    Integer getAppId(String appName);

    void save(Collection<Record> records);
}
