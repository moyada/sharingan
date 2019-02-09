package io.moyada.sharingan.monitor.database.handler;

import io.moyada.sharingan.monitor.api.handler.MultiConsumer;
import io.moyada.sharingan.monitor.database.data.Record;

import java.util.Collection;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class RecordConsumer implements MultiConsumer<Record>  {

    private DataRepository dataRepository;

    public RecordConsumer(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    @Override
    public void consumer(Collection<Record> data) {
        dataRepository.save(data);
    }
}
