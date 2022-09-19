package site.dishui.calcite.test;

import cn.hutool.core.text.csv.CsvData;
import cn.hutool.core.text.csv.CsvUtil;
import cn.hutool.core.text.csv.CsvWriter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import site.dishui.calcite.utils.CalciteUtil;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

/**
 * 使用自定义的 csv 源，查询数据
 */
public class MyTest1 {
    public static void main(String[] args) {
        String filePath = "/model.json";
        Connection connection = null;
        Statement statement = null;
        try {
            connection = CalciteUtil.getConnect(filePath);
            statement = connection.createStatement();

            String sql = "with a as (select t1.org_name,t.type_code,t.num from TEST_CSV.TMP as t join db1.share_organization as t1 on t.app_id = t1.app_id ) select a.org_name,t3.type_name,a.num from a left join db1.sys_certificate t3 on a.type_code = t3.type_code ";

            String[] strArray = {
                    "select  from TEST_CSV.TMP t",
                    "select app_id ,org_name from db1.share_organization",
                    "select type_name ,type_code from db1.sys_certificate",
            };

//            for (String sql : strArray) {

                ResultSet resultSet = statement.executeQuery(sql);
                System.out.println("-------------------------  " +
                        "start sql"
                        + "  -------------------------  ");
            CsvWriter writer = CsvUtil.getWriter("result.csv", StandardCharsets.UTF_8);
            writer.write(CalciteUtil.getDataCsv(resultSet));
            writer.close();
//            System.out.println(data);
//                String pretty = JSON.toJSONString(CalciteUtil.getData(resultSet),
//                        SerializerFeature.PrettyFormat,
//                        SerializerFeature.WriteMapNullValue,
//                        SerializerFeature.WriteDateUseDateFormat);
//                System.out.println(pretty);
                System.out.println("-------------------------  " +
                        "end sql"
                        + "  -------------------------  ");
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                statement.close();
                connection.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }


}
