package cn.com.ptpress.cdm.schema.mysql;

import cn.com.ptpress.cdm.schema.common.CdmColumn;
import cn.com.ptpress.cdm.schema.common.CdmTable;
import org.apache.calcite.schema.Schema;
import org.apache.calcite.schema.SchemaFactory;
import org.apache.calcite.schema.SchemaPlus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * create custom schema
 *
 * @author jimo
 */
public class MysqlSchemaFactory implements SchemaFactory {

    @Override
    public Schema create(SchemaPlus parentSchema, String name, Map<String, Object> operand) {
        try (final Connection conn = DriverManager.getConnection(String.valueOf(operand.get("url")),
                String.valueOf(operand.get("user")), String.valueOf(operand.get("pass")))) {
            final Statement stmt = conn.createStatement();
            final ResultSet rs = stmt.executeQuery("SHOW TABLES");
            List<CdmTable> tables = new ArrayList<>(8);
            while (rs.next()) {
                final String table = rs.getString(1);
                tables.add(new CdmTable(table, getColumns(conn, table)));
            }
            return new MysqlSchema(name, tables);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<CdmColumn> getColumns(Connection conn, String table) throws SQLException {
        final Statement stmt = conn.createStatement();
        final ResultSet rs = stmt.executeQuery("DESC " + table);
        List<CdmColumn> columns = new ArrayList<>();
        while (rs.next()) {
            columns.add(new CdmColumn(rs.getString("Field"),
                    typeMap(pureType(rs.getString("Type")))));
        }
        return columns;
    }

    /**
     * mysql 有的类型和 calcite不一样，需要修改下别名
     */
    private String typeMap(String type) {
        switch (type.toLowerCase()) {
            case "int":
                return "integer";
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
