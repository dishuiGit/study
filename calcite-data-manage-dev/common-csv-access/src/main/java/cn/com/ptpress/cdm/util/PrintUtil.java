package cn.com.ptpress.cdm.util;

import java.net.URL;
import java.sql.*;

public class PrintUtil {

    /**
     * 打印结果
     */
    public static void printResult(ResultSet rs) throws SQLException {
        printResult(rs, 1, 0);
    }

    /**
     * 只打印某些列
     */
    public static void printResult(ResultSet rs, int start, int end) throws SQLException {
        final ResultSetMetaData md = rs.getMetaData();
        if (end <= 0) {
            end = md.getColumnCount();
        }
        if (start <= 0) {
            start = 1;
        }
        printMetaHeader(md, start, end);
        while (rs.next()) {
            for (int i = start; i <= end; i++) {
                System.out.print(rs.getString(i) + "\t");
            }
            System.out.println();
        }
    }

    public static void executeAndPrint(String... sql) {
        URL url = ClassLoader.getSystemClassLoader().getResource("model.json");
        assert url != null;
        try (Connection connection = DriverManager.getConnection("jdbc:calcite:model=" + url.getPath())) {
            for (String s : sql) {
                Statement st = connection.createStatement();
                System.out.println("SQL>> " + s);
                printResult(st.executeQuery(s));
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void showTables() {
        showTables(null);
    }

    public static void showTables(String schema) {
        URL url = ClassLoader.getSystemClassLoader().getResource("model.json");
        assert url != null;
        try (Connection connection = DriverManager.getConnection("jdbc:calcite:model=" + url.getPath())) {
            final DatabaseMetaData md = connection.getMetaData();
            final ResultSet tables = md.getTables(null, schema, "%", null);
            printResult(tables, 2, 4);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void descTable(String schema, String table) {
        executeAndPrint(String.format("select \"tableSchem\",\"tableName\",\"columnName\",\"typeName\" " +
                "from \"metadata\".columns where \"tableName\"='%s' %s", table, schema == null ? "" :
                " and \"tableSchem\"='" + schema + "'"));
    }

    public static void descTable(String table) {
        descTable(null, table);
    }

    private static void printMetaHeader(ResultSetMetaData rmd, int start, int end) throws SQLException {
        for (int i = start; i <= end; i++) {
            System.out.print(rmd.getColumnLabel(i) + "\t");
        }
        System.out.println("\n--------------------------------------");
    }
}
