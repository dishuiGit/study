package cn.com.ptpress.cdm.stream;

import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.sql.*;
import java.util.concurrent.TimeUnit;

@Slf4j
class StreamLogTableTest {

    private void printResult(ResultSet rs) throws SQLException {
        final ResultSetMetaData md = rs.getMetaData();
        for (int i = 0; i < md.getColumnCount(); i++) {
            System.out.print(md.getColumnLabel(i + 1) + "\t");
        }
        System.out.println("\n------------------------------------------");
        while (rs.next()) {
            for (int i = 0; i < md.getColumnCount(); i++) {
                System.out.print(rs.getObject(i + 1) + "\t");
            }
            System.out.println();
        }
        System.out.println();
    }

    @Test
    void testStreamQuery() throws SQLException {
        URL url = ClassLoader.getSystemClassLoader().getResource("model.json");
        assert url != null;
        try (Connection connection = DriverManager.getConnection("jdbc:calcite:model=" + url.getPath())) {
            final Statement stmt = connection.createStatement();
            final ResultSet rs = stmt.executeQuery("select STREAM * from LOG");
            // 永无止境的输出
            printResult(rs);
        }
    }

    @Test
    void testStreamWithCancel() throws SQLException {
        URL url = ClassLoader.getSystemClassLoader().getResource("model.json");
        assert url != null;
        try (Connection connection = DriverManager.getConnection("jdbc:calcite:model=" + url.getPath())) {
            final Statement stmt = connection.createStatement();
            final ResultSet rs = stmt.executeQuery("select STREAM * from LOG");
            // 开启一个定时停止线程
            new Thread(() -> {
                try {
                    // 5秒后停止
                    TimeUnit.SECONDS.sleep(5);
                    stmt.cancel();
                } catch (InterruptedException | SQLException e) {
                    e.printStackTrace();
                }
            }).start();
            // 永无止境的输出
            try {
                printResult(rs);
            } catch (SQLException e) {
                // ignore end
            }
        }
    }

    @Test
    void testStreamGroupBy() throws SQLException {
        URL url = ClassLoader.getSystemClassLoader().getResource("model.json");
        assert url != null;
        try (Connection connection = DriverManager.getConnection("jdbc:calcite:model=" + url.getPath())) {
//            final Statement stmt = connection.createStatement();
//            final ResultSet rs = stmt.executeQuery("select STREAM level,count(*) from LOG group by level");

            final Statement stmt = connection.createStatement();
            final ResultSet rs = stmt.executeQuery("select STREAM FLOOR(log_time TO SECOND) as " +
                    "log_time,level,count(*) as c from LOG group by FLOOR(log_time TO SECOND)," +
                    "level");
            printResult(rs);
        }
    }

    @Test
    void testStreamGroupByCache() throws SQLException, InterruptedException {
        URL url = ClassLoader.getSystemClassLoader().getResource("model.json");
        assert url != null;
        try (Connection connection = DriverManager.getConnection("jdbc:calcite:model=" + url.getPath())) {
            // 模拟查询10次
            for (int i = 0; i < 10; i++) {
                TimeUnit.SECONDS.sleep(1);
                final Statement stmt = connection.createStatement();
                final ResultSet rs = stmt.executeQuery("select STREAM FLOOR(log_time TO MINUTE) as " +
                        "log_time,level,count(*) as c from LOG_CACHE group by FLOOR(log_time TO MINUTE)," +
                        "level");
                printResult(rs);
            }
        }
    }

    /**
     * TUMBLE和HOP还没有实现
     */
    @Ignore
    void testHop() throws InterruptedException, SQLException {
        URL url = ClassLoader.getSystemClassLoader().getResource("model.json");
        assert url != null;
        try (Connection connection = DriverManager.getConnection("jdbc:calcite:model=" + url.getPath())) {
            // 模拟查询10次
            for (int i = 0; i < 10; i++) {
                TimeUnit.SECONDS.sleep(1);
                final Statement stmt = connection.createStatement();
                final ResultSet rs = stmt.executeQuery("select STREAM HOP_END(log_time, INTERVAL '1' HOUR, " +
                        "INTERVAL '3' HOUR) as log_time,count(*) as c from LOG_CACHE group by HOP(log_time, " +
                        "INTERVAL '1' HOUR, INTERVAL '3' HOUR)");
                printResult(rs);
            }
        }
    }

}