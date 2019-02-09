package io.moyada.sharingan.monitor.database.support;


import io.moyada.sharingan.monitor.database.config.ApplicationConfig;
import io.moyada.sharingan.monitor.database.config.DataConfig;
import io.moyada.sharingan.monitor.database.data.Record;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

/**
 * SQL 构造器
 * @author xueyikang
 * @since 1.0
 **/
public class SqlBuilder {

    private final ApplicationConfig applicationConfig;
    private final DataConfig dataConfig;
    private final String column;

    private final DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public SqlBuilder(ApplicationConfig applicationConfig, DataConfig dataConfig) {
        this.applicationConfig = applicationConfig;
        this.dataConfig = dataConfig;
        this.column = fixColumn(dataConfig.getColumn());
    }

    private String fixColumn(String[] columns) {
        StringBuilder columnSql = new StringBuilder("(");
        for (String column : columns) {
            columnSql.append("`").append(column).append("`,");
        }
        return columnSql.deleteCharAt(columnSql.length()-1).append(") ").toString();
    }

    public String findAppId(String application) {
        return "SELECT `" + applicationConfig.getResultColumn() +
                "` FROM " + applicationConfig.getTable() +
                " WHERE `" + applicationConfig.getParamColumn() + "` = '" + application + "';";
    }

    /**
     * 批量插入语句
     * @param records
     * @return
     */
    public String insert(Collection<Record> records) {
        String now = simpleDateFormat.format(new Date(System.currentTimeMillis()));

        StringBuilder sql = new StringBuilder(records.size() * 16);
        sql.append("INSERT IGNORE INTO ").append(dataConfig.getTable()).append(column).append("VALUES");
        for (Record record : records) {
            sql.append("\n(");
            sql.append(record.getAppId());
            sql.append(",'");
            sql.append(record.getDomain());
            sql.append("','");
            sql.append(record.getParamValue());
            sql.append("','");
            sql.append(now);
            sql.append("'),");
        }
        sql.deleteCharAt(sql.length() - 1).append(";");
        return sql.toString();
    }
}
