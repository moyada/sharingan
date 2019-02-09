package io.moyada.sharingan.monitor.database.config;

import io.moyada.sharingan.monitor.api.config.MonitorConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author xueyikang
 * @since 1.0
 **/
@ConfigurationProperties(prefix = MonitorConfig.PREFIX + ".app-info")
public class ApplicationConfig {

    private String table;

    private String paramColumn;

    private String resultColumn;

    public void checkParam() {
        if (null == table) {
            throw new NullPointerException("table is null");
        }
        if (null == paramColumn) {
            throw new NullPointerException("paramColumn is null");
        }
        if (null == resultColumn) {
            throw new NullPointerException("resultColumn is null");
        }
    }

    public String getTable() {
        return table;
    }

    public String getParamColumn() {
        return paramColumn;
    }

    public String getResultColumn() {
        return resultColumn;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public void setParamColumn(String paramColumn) {
        this.paramColumn = paramColumn;
    }

    public void setResultColumn(String resultColumn) {
        this.resultColumn = resultColumn;
    }
}
