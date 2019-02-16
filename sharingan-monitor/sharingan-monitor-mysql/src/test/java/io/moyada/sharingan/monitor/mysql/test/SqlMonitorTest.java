package io.moyada.sharingan.monitor.mysql.test;

import io.moyada.sharingan.monitor.api.Monitor;
import io.moyada.sharingan.monitor.api.config.MonitorConfig;
import io.moyada.sharingan.monitor.api.entity.DefaultInvocation;
import io.moyada.sharingan.monitor.api.entity.Invocation;
import io.moyada.sharingan.monitor.api.entity.SerializationType;
import io.moyada.sharingan.monitor.mysql.MysqlMonitor;
import io.moyada.sharingan.monitor.mysql.config.*;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class SqlMonitorTest {

    public static void main(String[] args) throws InterruptedException {
        MonitorConfig monitorConfig = new MonitorConfig();
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSourceConfig.setUrl("jdbc:mysql://127.168.0.1:3306/sharingan?useSSL=false&useUnicode=true&useAffectedRows=true&serverTimezone=Asia/Shanghai");
        dataSourceConfig.setUsername("root");
        dataSourceConfig.setPassword("root");

        MetadataConfig metadataConfig = new MetadataConfig(new AppConfig(), new ServiceConfig(), new FunctionConfig(), new HttpConfig());

        DataMapperConfig dataMapperConfig = new DataMapperConfig();

        Monitor monitor = MysqlMonitor.newInstance(monitorConfig, dataSourceConfig, metadataConfig, dataMapperConfig);

        Invocation invocation;
        for (int i = 0; i < 500; i++) {
            System.out.println(i);
            invocation = new DefaultInvocation();
            invocation.setApplication("dubbo-test");
            invocation.setDomain("test");
            invocation.addArgs("model", i);
            invocation.setSerializationType(SerializationType.VALUE);
            monitor.listener(invocation);
            Thread.sleep(100L);
        }

        Thread.currentThread().join();

    }
}
