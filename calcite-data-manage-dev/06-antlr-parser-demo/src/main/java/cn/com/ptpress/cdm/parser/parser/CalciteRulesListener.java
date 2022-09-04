// Generated from E:/calcite-data-manage/antlr-parser-demo/src/main/java/cn/com/ptpress/cdm/parser/grammar\CalciteRules.g4 by ANTLR 4.8
package cn.com.ptpress.cdm.parser.parser;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link CalciteRulesParser}.
 */
public interface CalciteRulesListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link CalciteRulesParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(CalciteRulesParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link CalciteRulesParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(CalciteRulesParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link CalciteRulesParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterStmt(CalciteRulesParser.StmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link CalciteRulesParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitStmt(CalciteRulesParser.StmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link CalciteRulesParser#loadStmt}.
	 * @param ctx the parse tree
	 */
	void enterLoadStmt(CalciteRulesParser.LoadStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link CalciteRulesParser#loadStmt}.
	 * @param ctx the parse tree
	 */
	void exitLoadStmt(CalciteRulesParser.LoadStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link CalciteRulesParser#loadFromStmt}.
	 * @param ctx the parse tree
	 */
	void enterLoadFromStmt(CalciteRulesParser.LoadFromStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link CalciteRulesParser#loadFromStmt}.
	 * @param ctx the parse tree
	 */
	void exitLoadFromStmt(CalciteRulesParser.LoadFromStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link CalciteRulesParser#loadToStmt}.
	 * @param ctx the parse tree
	 */
	void enterLoadToStmt(CalciteRulesParser.LoadToStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link CalciteRulesParser#loadToStmt}.
	 * @param ctx the parse tree
	 */
	void exitLoadToStmt(CalciteRulesParser.LoadToStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link CalciteRulesParser#loadColumns}.
	 * @param ctx the parse tree
	 */
	void enterLoadColumns(CalciteRulesParser.LoadColumnsContext ctx);
	/**
	 * Exit a parse tree produced by {@link CalciteRulesParser#loadColumns}.
	 * @param ctx the parse tree
	 */
	void exitLoadColumns(CalciteRulesParser.LoadColumnsContext ctx);
	/**
	 * Enter a parse tree produced by {@link CalciteRulesParser#columnsItem}.
	 * @param ctx the parse tree
	 */
	void enterColumnsItem(CalciteRulesParser.ColumnsItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link CalciteRulesParser#columnsItem}.
	 * @param ctx the parse tree
	 */
	void exitColumnsItem(CalciteRulesParser.ColumnsItemContext ctx);
}