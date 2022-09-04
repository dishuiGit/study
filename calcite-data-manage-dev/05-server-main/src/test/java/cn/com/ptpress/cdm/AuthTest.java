package cn.com.ptpress.cdm;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 认证测试
 *
 * @author jimo
 */
@Slf4j
@SuppressWarnings("SqlNoDataSourceInspection")
public class AuthTest {

    @Test
    void baseAuthTest() throws SQLException {
        final Properties p = new Properties();
        p.put("avatica_user", "USER1");
        p.put("avatica_password", "password1");
        p.put("serialization", "protobuf");
        try (Connection conn = DriverManager.getConnection("jdbc:avatica:remote:url=http://localhost:8765;" +
                "authentication=BASIC", p)) {
            final Statement statement = conn.createStatement();
            final ResultSet rs = statement.executeQuery("SHOW DATABASES");
            assertTrue(rs.next());
            // 查询数据
            final Statement stmt1 = conn.createStatement();
            final ResultSet rs1 = stmt1.executeQuery("SELECT * FROM sys_user");
            rs1.next();
            assertEquals("admin", rs.getString("user_name"));
        }
    }

    @Test
    void digestAuthTest() throws SQLException {
        final Properties p = new Properties();
        p.put("avatica_user", "USER1");
        p.put("avatica_password", "password1");
        p.put("serialization", "protobuf");
        try (Connection conn = DriverManager.getConnection("jdbc:avatica:remote:url=http://localhost:8765;" +
                "authentication=DIGEST", p)) {
            final Statement statement = conn.createStatement();
            final ResultSet rs = statement.executeQuery("SHOW DATABASES");
            assertTrue(rs.next());
            // 查询数据
            final Statement stmt1 = conn.createStatement();
            final ResultSet rs1 = stmt1.executeQuery("SELECT * FROM sys_user");
            rs1.next();
            assertEquals("admin", rs1.getString("user_name"));
        }
    }

}
