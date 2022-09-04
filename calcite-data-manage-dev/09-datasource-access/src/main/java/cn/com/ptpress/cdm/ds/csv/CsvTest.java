package cn.com.ptpress.cdm.ds.csv;

import java.net.URL;
import java.sql.*;

public class CsvTest {
    public static void main(String[] args) {
        try {
            URL url = ClassLoader.getSystemClassLoader().getResource("model.json");
            Connection connection =
                    DriverManager.getConnection("jdbc:calcite:model=" + url.getPath());
            Statement st = connection.createStatement();
            testSql(st, "select * from CSV.\"data_01\"");
//            testSql(st, "select ABS(\"Score\") from \"data_01\" ");
//            testSql(st, "select EXAMPLE(\"Name\") from \"data_01\" ");
//            testSql(st, "select EXAMPLE(\"Name\",2) from \"data_01\"");
            //testSql(st, "select COLLECT_LIST(\"Name\") from \"CSV.data_01\"");
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * 测试 sql 并打印结果
     *
     * @param st  连接
     * @param sql 输入的sql
     * @throws SQLException
     */
    private static void testSql(Statement st, String sql) throws SQLException {
        ResultSet result = st.executeQuery(sql);
        int columnCount = result.getMetaData().getColumnCount();
        //打印sql
        System.out.println(sql);
        while (result.next()) {
            for (int i = 1; i <= columnCount; i++) {
                System.out.print(result.getString(i)+"     ");
            }
            System.out.println();
        }
        result.close();
    }
}
