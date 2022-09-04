// Generated from E:/calcite-data-manage/antlr-parser-demo/src/main/java/cn/com/ptpress/cdm/parser/grammar\CalciteRules.g4 by ANTLR 4.8
package cn.com.ptpress.cdm.parser.parser;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link CalciteRulesParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface CalciteRulesVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link CalciteRulesParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(CalciteRulesParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link CalciteRulesParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStmt(CalciteRulesParser.StmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link CalciteRulesParser#loadStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLoadStmt(CalciteRulesParser.LoadStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link CalciteRulesParser#loadFromStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLoadFromStmt(CalciteRulesParser.LoadFromStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link CalciteRulesParser#loadToStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLoadToStmt(CalciteRulesParser.LoadToStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link CalciteRulesParser#loadColumns}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLoadColumns(CalciteRulesParser.LoadColumnsContext ctx);
	/**
	 * Visit a parse tree produced by {@link CalciteRulesParser#columnsItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnsItem(CalciteRulesParser.ColumnsItemContext ctx);
}