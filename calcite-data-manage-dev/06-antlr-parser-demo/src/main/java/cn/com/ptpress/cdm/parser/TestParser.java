package cn.com.ptpress.cdm.parser;

import cn.com.ptpress.cdm.parser.driver.ParserDriver;
import org.apache.calcite.sql.SqlNode;

/**
 * @author sunxiaojun
 * @date 2021/5/15
 * @since 2.0.0
 */
public class TestParser {
    public static void main(String[] args) {
        String sql= "LOAD hdfs:'/data/user.txt' TO mysql:'db.t_user' (name name,age age) SEPARATOR ',' ";
        antlrWithCalcite(sql);
    }

    public static void antlrWithCalcite(String sql){
        //生成解析driver
        ParserDriver parserDriver = new ParserDriver(sql);
        //生成sqlNode
        SqlNode sqlNode = parserDriver.parser();
        System.out.println(sqlNode);
    }

}
