package cn.moyada.sharingan.monitor.mysql;

import cn.moyada.sharingan.monitor.api.entity.Record;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Collection;

/**
 * SQL 构造器
 * @author xueyikang
 * @since 1.0
 **/
public class SqlBuilder {

    private final String table, column;

    private final Instant current = Instant.now(Clock.system(ZoneId.of("Asia/Shanghai")));

    private final DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public SqlBuilder(String table, String[] columns) {
        this.table = table;
        StringBuilder columnSql = new StringBuilder(" (");
        for (String column : columns) {
            columnSql.append("`").append(column).append("`,");
        }
        columnSql.deleteCharAt(columnSql.length()-1).append(") ");
        this.column = columnSql.toString();
    }

    /**
     * 批量插入语句
     * @param records
     * @param <T>
     * @return
     */
    public <T> String insert(Collection<Record<T>> records) {
        String now = simpleDateFormat.format(current);

        StringBuilder sql = new StringBuilder(records.size() * 16);
        sql.append("INSERT INTO ").append(table).append(column).append("VALUES");
        for (Record<T> record : records) {
            sql.append("\n('");
            sql.append(record.getApplication());
            sql.append("','");
            sql.append(record.getDomain());
//            sql.append("','");
//            sql.append(record.getProtocol());
            sql.append("','");
            sql.append(record.getArgs().toString());
            sql.append("','");
            sql.append(now);
            sql.append("'),");
        }
        sql.deleteCharAt(sql.length() - 1).append(";");
        return sql.toString();
    }
}
