package io.moyada.sharingan.monitor.database.test;

import io.moyada.sharingan.monitor.api.Monitor;
import io.moyada.sharingan.monitor.api.config.MonitorConfig;
import io.moyada.sharingan.monitor.api.entity.DefaultInvocation;
import io.moyada.sharingan.monitor.api.entity.Invocation;
import io.moyada.sharingan.monitor.api.entity.SerializationType;
import io.moyada.sharingan.monitor.database.DataBaseMonitor;
import io.moyada.sharingan.monitor.database.config.ApplicationConfig;
import io.moyada.sharingan.monitor.database.config.DataConfig;
import io.moyada.sharingan.monitor.database.config.DataSourceConfig;

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

        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setTable("app_info");
        applicationConfig.setParamColumn("name");
        applicationConfig.setResultColumn("id");

        DataConfig dataConfig = new DataConfig();
        dataConfig.setTable("invoke_data");
        dataConfig.setColumn("app_id, domain, param_value, date_create");

        Monitor monitor = DataBaseMonitor.newInstance(monitorConfig, dataSourceConfig, applicationConfig, dataConfig);

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
