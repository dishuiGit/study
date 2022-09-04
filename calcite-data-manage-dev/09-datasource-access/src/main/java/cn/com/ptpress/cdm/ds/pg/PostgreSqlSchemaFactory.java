package cn.com.ptpress.cdm.ds.pg;

import org.apache.calcite.schema.Schema;
import org.apache.calcite.schema.SchemaFactory;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.schema.Table;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PG构造schema的工厂
 *
 * @author jimo
 */
public class PostgreSqlSchemaFactory implements SchemaFactory {

    @Override
    public Schema create(SchemaPlus parentSchema, String name, Map<String, Object> operand) {
        final PostgreSqlInfo info = new PostgreSqlInfo(String.valueOf(operand.get("url")),
                String.valueOf(operand.get("user")), String.valueOf(operand.get("password")));
        try (final Connection conn = DriverManager.getConnection(info.getUrl(), info.getUser(), info.getPassword())) {
            final Statement stmt = conn.createStatement();
            final ResultSet rs = stmt.executeQuery("select table_name\n" +
                    "from information_schema.tables\n" +
                    "where table_schema = 'public'");
            Map<String, Table> tableMap = new HashMap<>();
            while (rs.next()) {
                final String table = rs.getString(1);
                tableMap.put(table.toUpperCase(), new PostgreSqlTable(getColumns(conn, table), info));
            }
            return new PostgreSqlSchema(tableMap);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<CdmColumn> getColumns(Connection conn, String table) throws SQLException {
        final Statement stmt = conn.createStatement();
        final ResultSet rs = stmt.executeQuery(String.format("select column_name, data_type\n" +
                "from information_schema.columns\n" +
                "where table_schema = 'public'\n" +
                "  and table_name = '%s'", table));
        List<CdmColumn> columns = new ArrayList<>();
        while (rs.next()) {
            columns.add(new CdmColumn(rs.getString("column_name"),
                    typeMap(pureType(rs.getString("data_type")))));
        }
        return columns;
    }

    /**
     * mysql 有的类型和 calcite不一样，需要修改下别名
     */
    private String typeMap(String type) {
        switch (type.toLowerCase()) {
            case "name":
            case "text":
            case "char":
            case "character":
            case "character varying":
                return "varchar";
            case "point":
                return "geometry";
            default:
                return type;
        }
    }

    /**
     * 传入的type含有类型长度，如 bigint(20), varchar(258)
     * 需要去掉括号
     */
    private String pureType(String type) {
        final int i = type.indexOf('(');
        return i > 0 ? type.substring(0, i) : type;
    }
}
