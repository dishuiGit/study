package cn.com.ptpress.cdm.spatial;

import cn.com.ptpress.cdm.util.PrintUtil;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class SpatialTest {

    private void testSql(String sql) throws SQLException {
        URL url = ClassLoader.getSystemClassLoader().getResource("model.json");
        assertNotNull(url);
        try (Connection connection =
                     DriverManager.getConnection("jdbc:calcite:model=" + url.getPath() + ";fun=spatial")) {
            Statement st = connection.createStatement();
            System.out.println(sql + "===>");
            PrintUtil.printResult(st.executeQuery(sql));
        }
    }

    @Test
    void testFunction() throws SQLException {
        testSql("select * from \"user_pos\"");
        testSql("select ST_Point(\"lon\",\"lat\") from \"user_pos\"");
    }

    @Test
    void testPredicates() throws SQLException {
        testSql("select * from \"user_pos\" where ST_Within(ST_Point(\"lon\",\"lat\") " +
                ",ST_MakeEnvelope(106.331229,29.570227,106.341229,29.584227))");
    }
}
