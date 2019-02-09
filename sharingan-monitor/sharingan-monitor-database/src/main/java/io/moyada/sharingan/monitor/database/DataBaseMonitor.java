package io.moyada.sharingan.monitor.database;

import cn.moyada.sharingan.serialization.jackson.JacksonSerializer;
import io.moyada.sharingan.monitor.api.config.MonitorConfig;
import io.moyada.sharingan.monitor.api.monitor.AsyncBatchMonitor;
import io.moyada.sharingan.monitor.database.config.ApplicationConfig;
import io.moyada.sharingan.monitor.database.config.DataConfig;
import io.moyada.sharingan.monitor.database.config.DataSourceConfig;
import io.moyada.sharingan.monitor.database.data.Record;
import io.moyada.sharingan.monitor.database.handler.DataRepository;
import io.moyada.sharingan.monitor.database.handler.DataRepositoryImpl;
import io.moyada.sharingan.monitor.database.handler.RecordConsumer;
import io.moyada.sharingan.monitor.database.handler.RecordConverter;
import io.moyada.sharingan.monitor.database.support.DataSourceHolder;
import io.moyada.sharingan.monitor.database.support.SqlBuilder;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class DataBaseMonitor extends AsyncBatchMonitor<Record> {

    private DataBaseMonitor(MonitorConfig monitorConfig, RecordConverter converter, RecordConsumer consumer) {
        super(monitorConfig, converter, consumer);
    }

    public static DataBaseMonitor newInstance(MonitorConfig monitorConfig,
                                              DataSourceConfig dataSourceConfig,
                                              ApplicationConfig applicationConfig,
                                              DataConfig dataConfig) {
        if (null == dataSourceConfig) {
            throw new NullPointerException("DataSourceConfig is null");
        }
        if (null == applicationConfig) {
            throw new NullPointerException("ApplicationConfig is null");
        }
        if (null == dataConfig) {
            throw new NullPointerException("DataConfig is null");
        }
        dataSourceConfig.checkParam();
        applicationConfig.checkParam();
        dataConfig.checkParam();

        DataRepository dataRepository = new DataRepositoryImpl(new DataSourceHolder(dataSourceConfig), new SqlBuilder(applicationConfig, dataConfig));

        RecordConverter converter = new RecordConverter(dataRepository, new JacksonSerializer());
        RecordConsumer consumer = new RecordConsumer(dataRepository);
        return new DataBaseMonitor(monitorConfig, converter, consumer);
    }
}
