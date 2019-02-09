package io.moyada.sharingan.monitor.database.config;

import io.moyada.sharingan.monitor.api.config.MonitorConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author xueyikang
 * @since 1.0
 **/
@ConfigurationProperties(prefix = MonitorConfig.PREFIX + ".store-info")
public class DataConfig {

    /**
     * 表名
     */
    private String table;

    /**
     * 列名，以 , 分割
     */
    private String[] column;

    public void checkParam() {
        if (null == table) {
            throw new NullPointerException("table is null");
        }
        if (null == column) {
            throw new NullPointerException("column is null");
        }
        if (column.length == 0) {
            throw new NullPointerException("column is empty");
        }
    }

    public String getTable() {
        return table;
    }

    public String[] getColumn() {
        return column;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public void setColumn(String column) {
        column = column.replaceAll(" ", "");
        this.column = column.split(",");
    }
}
