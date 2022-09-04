package cn.com.ptpress.cdm.parser;

import cn.com.ptpress.cdm.parser.extend.CdmSqlParserImpl;
import org.apache.calcite.config.Lex;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.dialect.OracleSqlDialect;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.parser.SqlParser;
import org.junit.jupiter.api.Test;

class CalciteParserDemoTest {

    @Test
    void testParseLoad() throws SqlParseException {
        // Sql语句
        String sql = "LOAD hdfs:'/data/user.txt' TO mysql:'db.t_user' (c1 c2,c3 c4) SEPARATOR ','";
//        String sql = "select * from t_user ";
        // 解析配置
        SqlParser.Config mysqlConfig = SqlParser.config()
                .withParserFactory(CdmSqlParserImpl.FACTORY)
                .withLex(Lex.MYSQL);
        // 创建解析器
        SqlParser parser = SqlParser.create(sql, mysqlConfig);
        // 解析sql
        SqlNode sqlNode = parser.parseQuery();
        System.out.println(sqlNode.toString());
        // 还原某个方言的SQL
        System.out.println(sqlNode.toSqlString(OracleSqlDialect.DEFAULT));
    }
}