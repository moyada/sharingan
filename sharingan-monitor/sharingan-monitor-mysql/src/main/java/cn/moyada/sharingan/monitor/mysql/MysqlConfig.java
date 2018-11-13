package cn.moyada.sharingan.monitor.mysql;

import cn.moyada.sharingan.monitor.api.MonitorConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author xueyikang
 * @since 1.0
 **/
@ConfigurationProperties(MysqlConfig.PREFIX_NAME)
public class MysqlConfig extends MonitorConfig {

    static final String PREFIX_NAME = "sharingan.datasource";

    private String url;

    private String username;

    private String password;

    private String table;

    private String[] column;

    public boolean isInvalid() {
        if (null == url) {
            return false;
        }
        if (null == username) {
            return false;
        }
        if (null == password) {
            return false;
        }
        if (null == table) {
            return false;
        }
        if (null == column) {
            return false;
        }
        return true;
    }

    public void setColumn(String column) {
        column = column.replaceAll(" ", "");
        String[] columns = column.split(",");
        this.column = columns;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String[] getColumn() {
        return column;
    }

}
