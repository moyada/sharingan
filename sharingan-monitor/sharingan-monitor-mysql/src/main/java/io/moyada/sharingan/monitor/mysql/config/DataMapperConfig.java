package io.moyada.sharingan.monitor.mysql.config;

import io.moyada.sharingan.monitor.api.config.MonitorConfig;
import io.moyada.sharingan.monitor.mysql.data.Record;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

/**
 * @author xueyikang
 * @since 1.0
 **/
@ConfigurationProperties(prefix = MonitorConfig.PREFIX + ".data-mapper")
public class DataMapperConfig {

    private final DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 表名
     */
    private String table = "invoke_data";

    private String column;

    private String appColumn = "app_id";

    private String domainColumn = "domain";

    private String valueColumn = "param_value";

    private String dateColumn = "date_create";

    public void checkParam() {
        if (null == table) {
            throw new NullPointerException("table is null");
        }
        if (null == appColumn) {
            throw new NullPointerException("table is null");
        }
        if (null == domainColumn) {
            throw new NullPointerException("table is null");
        }
        if (null == valueColumn) {
            throw new NullPointerException("table is null");
        }
    }

    public String getColumn() {
        if (null == column) {
            StringBuilder columnSql = new StringBuilder();
            if (null != appColumn) {
                columnSql.append("`").append(appColumn).append("`,");
            }
            if (null == domainColumn) {
                columnSql.append("`").append(domainColumn).append("`,");
            }
            if (null == valueColumn) {
                columnSql.append("`").append(domainColumn).append("`,");
            }
            if (null == dateColumn) {
                columnSql.append("`").append(dateColumn).append("`,");
            }
            column = columnSql.deleteCharAt(columnSql.length()-1).toString();
        }
        return column;
    }

    /**
     * 批量插入语句
     * @param records
     * @return
     */
    public String getInsertSql(Collection<Record> records) {
        boolean hasDataColumn = hasDataColumn();
        String now;
        if (hasDataColumn) {
            now = simpleDateFormat.format(new Date(System.currentTimeMillis()));
        } else {
            now = null;
        }

        StringBuilder sql = new StringBuilder(records.size() * 16);
        sql.append("INSERT IGNORE INTO ").append(table)
                .append(" (").append(getColumn()).append(") VALUES");
        for (Record record : records) {
            sql.append("\n(");
            sql.append(record.getAppId());
            sql.append(",'");
            sql.append(record.getDomain());
            sql.append("','");
            sql.append(record.getParamValue());

            if (hasDataColumn) {
                sql.append("','");
                sql.append(now);
            }
            sql.append("'),");
        }
        sql.deleteCharAt(sql.length() - 1).append(";");
        return sql.toString();
    }

    public boolean hasDataColumn() {
        return dateColumn != null;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table.trim();
    }

    public void setAppColumn(String appColumn) {
        if (null == appColumn) {
            return;
        }
        this.appColumn = appColumn.trim();
    }

    public void setDomainColumn(String domainColumn) {
        if (null == domainColumn) {
            return;
        }
        this.domainColumn = domainColumn.trim();
    }

    public void setValueColumn(String valueColumn) {
        if (null == valueColumn) {
            return;
        }
        this.valueColumn = valueColumn.trim();
    }

    public void setDateColumn(String dateColumn) {
        this.dateColumn = dateColumn;
    }
}
