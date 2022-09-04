package cn.com.ptpress.cdm.ds.pg;

import org.apache.calcite.linq4j.AbstractEnumerable;
import org.apache.calcite.linq4j.Enumerable;
import org.apache.calcite.linq4j.Enumerator;
import org.apache.calcite.linq4j.QueryProvider;
import org.apache.calcite.schema.QueryableTable;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.schema.impl.AbstractTableQueryable;

import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * pg query
 *
 * @author jimo
 */
public class PostgreSqlQueryable<T> extends AbstractTableQueryable<T> {
    private PostgreSqlInfo info;
    private String tableName;

    public PostgreSqlQueryable(
            PostgreSqlInfo info,
            QueryProvider queryProvider, SchemaPlus schema, QueryableTable table, String tableName) {
        super(queryProvider, schema, table, tableName);
        this.info = info;
        this.tableName = tableName;
    }

    @Override
    public Enumerator<T> enumerator() {
        return (Enumerator<T>) query(null, null, -1, -1, null, null, null, null).enumerator();
    }

    public Enumerable<Object> query(List<Map.Entry<String, Class<?>>> fields,
                                    List<Map.Entry<String, String>> selectFields,
                                    Integer offset,
                                    Integer fetch,
                                    List<String> aggregate,
                                    List<String> group,
                                    List<String> predicates,
                                    List<String> order) {
        // 拼接SQL
        final StringBuilder sql = new StringBuilder();
        String fieldSql = fields.stream().map(Map.Entry::getKey).collect(Collectors.joining(","));
        final List<String> orderSql =
                order.stream().map(s -> s.split(" ")).map(item -> item[0] + " " + item[1]).collect(Collectors.toList());

        sql.append("SELECT ").append(fieldSql).append(" FROM ").append(tableName);
        if (predicates.size() > 0) sql.append(" WHERE ").append(predicates.get(0));
        if (group.size() > 0) sql.append(" GROUP BY ").append(String.join(",", group));
        if (order.size() > 0) sql.append(" ORDER BY ").append(String.join(",", orderSql));
        if (fetch >= 0) sql.append(" LIMIT ").append(fetch);

        return new AbstractEnumerable<Object>() {
            @Override
            public Enumerator<Object> enumerator() {
                try {
                    Class.forName("org.postgresql.Driver");

                    final Connection conn = DriverManager.getConnection(info.getUrl(), info.getUser(),
                            info.getPassword());
                    final Statement stmt = conn.createStatement();
                    final ResultSet rs = stmt.executeQuery(sql.toString());
                    return new PostgreSqlEnumerator(rs, fields);
                } catch (ClassNotFoundException | SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
}
