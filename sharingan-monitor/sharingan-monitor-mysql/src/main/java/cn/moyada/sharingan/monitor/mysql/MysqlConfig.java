package cn.moyada.sharingan.monitor.mysql;

import cn.moyada.sharingan.monitor.api.MonitorConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 数据库连接信息
 * @author xueyikang
 * @since 1.0
 **/
@ConfigurationProperties(MysqlConfig.PREFIX_NAME)
public class MysqlConfig extends MonitorConfig {

    static final String PREFIX_NAME = "sharingan.datasource";

    /**
     * 驱动
     */
    private String driverClassName = "com.mysql.cj.jdbc.Driver";

    /**
     * 地址
     */
    private String url;

    /**
     * 账户
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 表名
     */
    private String table;

    /**
     * 列名，以 , 分割
     */
    private String[] column;

    public boolean isInvalid() {
        if (null == driverClassName) {
            return true;
        }
        if (null == url) {
            return true;
        }
        if (null == username) {
            return true;
        }
        if (null == password) {
            return true;
        }
        if (null == table) {
            return true;
        }
        if (null == column) {
            return true;
        }
        return false;
    }

    public void setColumn(String column) {
        column = column.replaceAll(" ", "");
        String[] columns = column.split(",");
        this.column = columns;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
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
