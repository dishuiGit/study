package cn.com.ptpress.cdm.parser;

import org.apache.calcite.config.Lex;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.dialect.OracleSqlDialect;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.parser.SqlParser;

public class CalciteParserDemo {

    public static void main(String[] args) throws SqlParseException {
        // Sql语句
        String sql = "select * from t_user where id = 1";
        // 解析配置
        SqlParser.Config mysqlConfig = SqlParser.config().withLex(Lex.MYSQL);
        // 创建解析器
        SqlParser parser = SqlParser.create(sql, mysqlConfig);
        // 解析sql
        SqlNode sqlNode = parser.parseQuery();
        System.out.println(sqlNode.toString());
        // 还原某个方言的SQL
        System.out.println(sqlNode.toSqlString(OracleSqlDialect.DEFAULT));
    }
}
