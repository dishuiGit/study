package cn.com.ptpress.cdm.schema.custom;

import org.apache.calcite.config.CalciteConnectionProperty;
import org.apache.calcite.server.ServerDdlExecutor;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CdmCustomSchemaTest {

    @Test
    void testDdl() throws SQLException {
        final Properties p = new Properties();
        p.put(CalciteConnectionProperty.PARSER_FACTORY.camelName(), ServerDdlExecutor.class.getName() +
                "#PARSER_FACTORY");
        try (final Connection conn = DriverManager.getConnection("jdbc:calcite:", p)) {
            final Statement s = conn.createStatement();
            // 创建schema
            s.execute("CREATE SCHEMA s");
            // 创建表
            s.executeUpdate("CREATE TABLE s.t(age int, name varchar(10))");
            s.executeUpdate("INSERT INTO s.t values(18,'jimo'),(20,'hehe')");
            ResultSet rs = s.executeQuery("SELECT count(*) FROM s.t");
            rs.next();
            assertEquals(2, rs.getInt(1));
            // 创建视图
            s.executeUpdate("CREATE VIEW v1 AS select name from s.t");
            rs = s.executeQuery("SELECT * FROM v1");
            rs.next();
            assertEquals("jimo", rs.getString(1));
            // 创建数据类型
            s.executeUpdate("CREATE TYPE vc10 as varchar(10)");
            s.executeUpdate("CREATE TABLE t1(age int, name vc10)");

            // 删除
            s.executeUpdate("DROP VIEW v1");
            s.executeUpdate("DROP TYPE vc10");
        }
    }
}