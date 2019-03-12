package io.moyada.sharingan.monitor.mysql;

import io.moyada.sharingan.monitor.api.config.MonitorConfig;
import io.moyada.sharingan.monitor.api.monitor.AsyncBatchMonitor;
import io.moyada.sharingan.monitor.mysql.config.DataMapperConfig;
import io.moyada.sharingan.monitor.mysql.config.DataSourceConfig;
import io.moyada.sharingan.monitor.mysql.config.MetadataConfig;
import io.moyada.sharingan.monitor.mysql.data.Record;
import io.moyada.sharingan.monitor.mysql.handler.DataRepository;
import io.moyada.sharingan.monitor.mysql.handler.DataRepositoryImpl;
import io.moyada.sharingan.monitor.mysql.handler.RecordConsumer;
import io.moyada.sharingan.monitor.mysql.handler.RecordConverter;
import io.moyada.sharingan.monitor.mysql.support.SqlBuilder;
import io.moyada.sharingan.monitor.mysql.support.SqlExecutor;
import io.moyada.sharingan.serialization.gson.GsonSerializer;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class MysqlMonitor extends AsyncBatchMonitor<Record> {

    private MysqlMonitor(MonitorConfig monitorConfig, RecordConverter converter, RecordConsumer consumer) {
        super(monitorConfig, converter, consumer);
    }

    public static MysqlMonitor newInstance(MonitorConfig monitorConfig,
                                           DataSourceConfig dataSourceConfig,
                                           MetadataConfig metadataConfig,
                                           DataMapperConfig dataMapperConfig) {
        if (null == dataSourceConfig) {
            throw new NullPointerException("DataSourceConfig is null");
        }
        if (null == metadataConfig) {
            throw new NullPointerException("MetadataConfig is null");
        }
        if (null == dataMapperConfig) {
            throw new NullPointerException("DataMapperConfig is null");
        }
        dataSourceConfig.checkParam();
        metadataConfig.checkParam();
        dataMapperConfig.checkParam();

        SqlExecutor sqlExecutor = new SqlExecutor(dataSourceConfig);
        SqlBuilder sqlBuilder = new SqlBuilder(metadataConfig);
        DataRepository dataRepository = new DataRepositoryImpl(sqlExecutor, sqlBuilder,  dataMapperConfig);

        RecordConverter converter = new RecordConverter(dataRepository, new GsonSerializer());
        RecordConsumer consumer = new RecordConsumer(dataRepository);
        return new MysqlMonitor(monitorConfig, converter, consumer);
    }
}
