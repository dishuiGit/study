package cn.com.ptpress.cdm.parser.driver;

import cn.com.ptpress.cdm.parser.parser.CalciteRulesLexer;
import cn.com.ptpress.cdm.parser.parser.CalciteRulesParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.calcite.sql.SqlNode;

/**
 * @author sunxiaojun
 * @date 2021/5/16
 * @since 2.0.0
 */
public class ParserDriver {
    private String sql;

    public ParserDriver(String sql) {
        this.sql = sql;
    }

    public SqlNode parser(){
        //将输入转成antlr的input流
        ANTLRInputStream input = new ANTLRInputStream(sql);
        //词法分析
        CalciteRulesLexer lexer = new CalciteRulesLexer(input);
        //转成token流
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        // 语法分析
        CalciteRulesParser parser = new CalciteRulesParser(tokens);
        //获取program节点
        CalciteRulesParser.ProgramContext program = parser.program();
        //创建访问节点
        CalciteVisit visit = new CalciteVisit();
        return visit.visitProgram(program);
    }
}
