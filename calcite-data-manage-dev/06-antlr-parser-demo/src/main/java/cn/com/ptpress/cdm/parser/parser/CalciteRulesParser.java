// Generated from E:/calcite-data-manage/antlr-parser-demo/src/main/java/cn/com/ptpress/cdm/parser/grammar\CalciteRules.g4 by ANTLR 4.8
package cn.com.ptpress.cdm.parser.parser;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class CalciteRulesParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.8", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		LOAD=1, TO=2, SEPARATOR=3, COLON=4, COMA=5, OPEN_P=6, CLOSE_P=7, SEMICOLON=8, 
		STRING=9, IDENTIFIER=10, SIMPLE_COMMENT=11, BRACKETED_EMPTY_COMMENT=12, 
		BRACKETED_COMMENT=13, WS=14;
	public static final int
		RULE_program = 0, RULE_stmt = 1, RULE_loadStmt = 2, RULE_loadFromStmt = 3, 
		RULE_loadToStmt = 4, RULE_loadColumns = 5, RULE_columnsItem = 6;
	private static String[] makeRuleNames() {
		return new String[] {
			"program", "stmt", "loadStmt", "loadFromStmt", "loadToStmt", "loadColumns", 
			"columnsItem"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, null, "':'", "','", "'('", "')'", "';'", null, null, 
			null, "'/**/'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "LOAD", "TO", "SEPARATOR", "COLON", "COMA", "OPEN_P", "CLOSE_P", 
			"SEMICOLON", "STRING", "IDENTIFIER", "SIMPLE_COMMENT", "BRACKETED_EMPTY_COMMENT", 
			"BRACKETED_COMMENT", "WS"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "CalciteRules.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public CalciteRulesParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class ProgramContext extends ParserRuleContext {
		public StmtContext stmt() {
			return getRuleContext(StmtContext.class,0);
		}
		public TerminalNode EOF() { return getToken(CalciteRulesParser.EOF, 0); }
		public TerminalNode SEMICOLON() { return getToken(CalciteRulesParser.SEMICOLON, 0); }
		public ProgramContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_program; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CalciteRulesListener ) ((CalciteRulesListener)listener).enterProgram(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CalciteRulesListener ) ((CalciteRulesListener)listener).exitProgram(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CalciteRulesVisitor ) return ((CalciteRulesVisitor<? extends T>)visitor).visitProgram(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProgramContext program() throws RecognitionException {
		ProgramContext _localctx = new ProgramContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_program);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(14);
			stmt();
			setState(16);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SEMICOLON) {
				{
				setState(15);
				match(SEMICOLON);
				}
			}

			setState(18);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StmtContext extends ParserRuleContext {
		public LoadStmtContext loadStmt() {
			return getRuleContext(LoadStmtContext.class,0);
		}
		public StmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CalciteRulesListener ) ((CalciteRulesListener)listener).enterStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CalciteRulesListener ) ((CalciteRulesListener)listener).exitStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CalciteRulesVisitor ) return ((CalciteRulesVisitor<? extends T>)visitor).visitStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StmtContext stmt() throws RecognitionException {
		StmtContext _localctx = new StmtContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_stmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(20);
			loadStmt();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LoadStmtContext extends ParserRuleContext {
		public TerminalNode LOAD() { return getToken(CalciteRulesParser.LOAD, 0); }
		public LoadFromStmtContext loadFromStmt() {
			return getRuleContext(LoadFromStmtContext.class,0);
		}
		public TerminalNode TO() { return getToken(CalciteRulesParser.TO, 0); }
		public LoadToStmtContext loadToStmt() {
			return getRuleContext(LoadToStmtContext.class,0);
		}
		public LoadColumnsContext loadColumns() {
			return getRuleContext(LoadColumnsContext.class,0);
		}
		public TerminalNode SEPARATOR() { return getToken(CalciteRulesParser.SEPARATOR, 0); }
		public TerminalNode STRING() { return getToken(CalciteRulesParser.STRING, 0); }
		public LoadStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_loadStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CalciteRulesListener ) ((CalciteRulesListener)listener).enterLoadStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CalciteRulesListener ) ((CalciteRulesListener)listener).exitLoadStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CalciteRulesVisitor ) return ((CalciteRulesVisitor<? extends T>)visitor).visitLoadStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LoadStmtContext loadStmt() throws RecognitionException {
		LoadStmtContext _localctx = new LoadStmtContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_loadStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(22);
			match(LOAD);
			setState(23);
			loadFromStmt();
			setState(24);
			match(TO);
			setState(25);
			loadToStmt();
			setState(26);
			loadColumns();
			setState(29);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SEPARATOR) {
				{
				setState(27);
				match(SEPARATOR);
				setState(28);
				match(STRING);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LoadFromStmtContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(CalciteRulesParser.IDENTIFIER, 0); }
		public TerminalNode COLON() { return getToken(CalciteRulesParser.COLON, 0); }
		public TerminalNode STRING() { return getToken(CalciteRulesParser.STRING, 0); }
		public LoadFromStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_loadFromStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CalciteRulesListener ) ((CalciteRulesListener)listener).enterLoadFromStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CalciteRulesListener ) ((CalciteRulesListener)listener).exitLoadFromStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CalciteRulesVisitor ) return ((CalciteRulesVisitor<? extends T>)visitor).visitLoadFromStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LoadFromStmtContext loadFromStmt() throws RecognitionException {
		LoadFromStmtContext _localctx = new LoadFromStmtContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_loadFromStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(31);
			match(IDENTIFIER);
			setState(32);
			match(COLON);
			setState(33);
			match(STRING);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LoadToStmtContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(CalciteRulesParser.IDENTIFIER, 0); }
		public TerminalNode COLON() { return getToken(CalciteRulesParser.COLON, 0); }
		public TerminalNode STRING() { return getToken(CalciteRulesParser.STRING, 0); }
		public LoadToStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_loadToStmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CalciteRulesListener ) ((CalciteRulesListener)listener).enterLoadToStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CalciteRulesListener ) ((CalciteRulesListener)listener).exitLoadToStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CalciteRulesVisitor ) return ((CalciteRulesVisitor<? extends T>)visitor).visitLoadToStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LoadToStmtContext loadToStmt() throws RecognitionException {
		LoadToStmtContext _localctx = new LoadToStmtContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_loadToStmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(35);
			match(IDENTIFIER);
			setState(36);
			match(COLON);
			setState(37);
			match(STRING);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LoadColumnsContext extends ParserRuleContext {
		public TerminalNode OPEN_P() { return getToken(CalciteRulesParser.OPEN_P, 0); }
		public ColumnsItemContext columnsItem() {
			return getRuleContext(ColumnsItemContext.class,0);
		}
		public TerminalNode CLOSE_P() { return getToken(CalciteRulesParser.CLOSE_P, 0); }
		public LoadColumnsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_loadColumns; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CalciteRulesListener ) ((CalciteRulesListener)listener).enterLoadColumns(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CalciteRulesListener ) ((CalciteRulesListener)listener).exitLoadColumns(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CalciteRulesVisitor ) return ((CalciteRulesVisitor<? extends T>)visitor).visitLoadColumns(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LoadColumnsContext loadColumns() throws RecognitionException {
		LoadColumnsContext _localctx = new LoadColumnsContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_loadColumns);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(39);
			match(OPEN_P);
			setState(40);
			columnsItem();
			setState(41);
			match(CLOSE_P);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ColumnsItemContext extends ParserRuleContext {
		public List<TerminalNode> IDENTIFIER() { return getTokens(CalciteRulesParser.IDENTIFIER); }
		public TerminalNode IDENTIFIER(int i) {
			return getToken(CalciteRulesParser.IDENTIFIER, i);
		}
		public List<TerminalNode> COMA() { return getTokens(CalciteRulesParser.COMA); }
		public TerminalNode COMA(int i) {
			return getToken(CalciteRulesParser.COMA, i);
		}
		public ColumnsItemContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_columnsItem; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CalciteRulesListener ) ((CalciteRulesListener)listener).enterColumnsItem(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CalciteRulesListener ) ((CalciteRulesListener)listener).exitColumnsItem(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CalciteRulesVisitor ) return ((CalciteRulesVisitor<? extends T>)visitor).visitColumnsItem(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ColumnsItemContext columnsItem() throws RecognitionException {
		ColumnsItemContext _localctx = new ColumnsItemContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_columnsItem);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(48); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				setState(48);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
				case 1:
					{
					{
					setState(43);
					match(IDENTIFIER);
					setState(44);
					match(IDENTIFIER);
					setState(45);
					match(COMA);
					}
					}
					break;
				case 2:
					{
					{
					setState(46);
					match(IDENTIFIER);
					setState(47);
					match(IDENTIFIER);
					}
					}
					break;
				}
				}
				setState(50); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==IDENTIFIER );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\20\67\4\2\t\2\4\3"+
		"\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\3\2\3\2\5\2\23\n\2\3\2\3"+
		"\2\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\5\4 \n\4\3\5\3\5\3\5\3\5\3\6\3"+
		"\6\3\6\3\6\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\6\b\63\n\b\r\b\16\b\64"+
		"\3\b\2\2\t\2\4\6\b\n\f\16\2\2\2\63\2\20\3\2\2\2\4\26\3\2\2\2\6\30\3\2"+
		"\2\2\b!\3\2\2\2\n%\3\2\2\2\f)\3\2\2\2\16\62\3\2\2\2\20\22\5\4\3\2\21\23"+
		"\7\n\2\2\22\21\3\2\2\2\22\23\3\2\2\2\23\24\3\2\2\2\24\25\7\2\2\3\25\3"+
		"\3\2\2\2\26\27\5\6\4\2\27\5\3\2\2\2\30\31\7\3\2\2\31\32\5\b\5\2\32\33"+
		"\7\4\2\2\33\34\5\n\6\2\34\37\5\f\7\2\35\36\7\5\2\2\36 \7\13\2\2\37\35"+
		"\3\2\2\2\37 \3\2\2\2 \7\3\2\2\2!\"\7\f\2\2\"#\7\6\2\2#$\7\13\2\2$\t\3"+
		"\2\2\2%&\7\f\2\2&\'\7\6\2\2\'(\7\13\2\2(\13\3\2\2\2)*\7\b\2\2*+\5\16\b"+
		"\2+,\7\t\2\2,\r\3\2\2\2-.\7\f\2\2./\7\f\2\2/\63\7\7\2\2\60\61\7\f\2\2"+
		"\61\63\7\f\2\2\62-\3\2\2\2\62\60\3\2\2\2\63\64\3\2\2\2\64\62\3\2\2\2\64"+
		"\65\3\2\2\2\65\17\3\2\2\2\6\22\37\62\64";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}