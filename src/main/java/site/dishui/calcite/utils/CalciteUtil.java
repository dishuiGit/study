package site.dishui.calcite.utils;

import cn.hutool.core.text.csv.CsvRow;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.net.URL;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

public class CalciteUtil {
    /**
     * 根据给定的 model.json 文件获取 Connection
     *
     * @param filePath
     * @return
     */
    public static Connection getConnect(String filePath) {
        Connection connection = null;
        try {
            URL url = CalciteUtil.class.getResource(filePath);
            String str = URLDecoder.decode(url.toString(), "UTF-8");
            Properties info = new Properties();
            info.put("model", str.replace("file:", ""));
            info.put("lex", "MYSQL");
            connection = DriverManager.getConnection("jdbc:calcite:", info);
//            connection.unwrap(CalciteConnection.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static List<String[]> getDataCsv(ResultSet resultSet) throws Exception {
        List<String[]> list = Lists.newArrayList();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnSize = metaData.getColumnCount();

        while (resultSet.next()) {
            String[] row = new String[columnSize];
            for (int i = 1; i < columnSize + 1; i++) {
                Object objVal = resultSet.getObject(i);
                row[i - 1] = Objects.isNull(objVal)?"null":objVal.toString();
            }
            list.add(row);
        }
        return list;
    }


    public static List<Map<String, Object>> getData(ResultSet resultSet) throws Exception {
        List<Map<String, Object>> list = Lists.newArrayList();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnSize = metaData.getColumnCount();

        while (resultSet.next()) {

            Map<String, Object> map = Maps.newLinkedHashMap();
            for (int i = 1; i < columnSize + 1; i++) {
                map.put(metaData.getColumnLabel(i), resultSet.getObject(i));
            }
            list.add(map);
        }
        return list;
    }
}
