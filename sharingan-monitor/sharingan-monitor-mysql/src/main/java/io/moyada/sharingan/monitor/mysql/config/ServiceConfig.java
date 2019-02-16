package io.moyada.sharingan.monitor.mysql.config;

import io.moyada.sharingan.monitor.api.config.MonitorConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author xueyikang
 * @since 1.0
 **/
@ConfigurationProperties(prefix = MonitorConfig.PREFIX + ".service-mapper")
public class ServiceConfig implements FindAction {

    private String table = "service_info";

    private String column = "`app_id`, `name`, `protocol`, `protocol_type`, `class_name`";

    private String keyColumn = "id";

    private String nameColumn = "name";

    public void setTable(String table) {
        if (null == table) {
            return;
        }
        this.table = table;
    }

    public void setColumn(String column) {
        if (null == column) {
            return;
        }
        this.column = column;
    }

    public void setKeyColumn(String keyColumn) {
        if (null == keyColumn) {
            return;
        }
        this.keyColumn = keyColumn;
    }

    public void setNameColumn(String nameColumn) {
        if (null == nameColumn) {
            return;
        }
        this.nameColumn = nameColumn;
    }

    @Override
    public String getTable() {
        return table;
    }

    @Override
    public String getColumn() {
        return column;
    }

    @Override
    public String getKey() {
        return keyColumn;
    }

    @Override
    public String getCondition() {
        return nameColumn;
    }
}
