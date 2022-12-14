// Generated from E:/calcite-data-manage/antlr-parser-demo/src/main/java/cn/com/ptpress/cdm/parser/grammar\CalciteRules.g4 by ANTLR 4.8
package cn.com.ptpress.cdm.parser.parser;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class CalciteRulesLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.8", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		LOAD=1, TO=2, SEPARATOR=3, COLON=4, COMA=5, OPEN_P=6, CLOSE_P=7, SEMICOLON=8, 
		STRING=9, IDENTIFIER=10, SIMPLE_COMMENT=11, BRACKETED_EMPTY_COMMENT=12, 
		BRACKETED_COMMENT=13, WS=14;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"LOAD", "TO", "SEPARATOR", "COLON", "COMA", "OPEN_P", "CLOSE_P", "SEMICOLON", 
			"LETTER", "STRING", "IDENTIFIER", "SIMPLE_COMMENT", "BRACKETED_EMPTY_COMMENT", 
			"BRACKETED_COMMENT", "WS"
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


	public CalciteRulesLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "CalciteRules.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\20\u0088\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\3\2\3\2\3\2\3\2"+
		"\3\2\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\6\3"+
		"\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n\6\n?\n\n\r\n\16\n@\3\13\3\13\3\13\3\13"+
		"\7\13G\n\13\f\13\16\13J\13\13\3\13\3\13\3\13\3\13\3\13\7\13Q\n\13\f\13"+
		"\16\13T\13\13\3\13\5\13W\n\13\3\f\3\f\3\r\3\r\3\r\3\r\7\r_\n\r\f\r\16"+
		"\rb\13\r\3\r\5\re\n\r\3\r\5\rh\n\r\3\r\3\r\3\16\3\16\3\16\3\16\3\16\3"+
		"\16\3\16\3\17\3\17\3\17\3\17\3\17\7\17x\n\17\f\17\16\17{\13\17\3\17\3"+
		"\17\3\17\3\17\3\17\3\20\6\20\u0083\n\20\r\20\16\20\u0084\3\20\3\20\3y"+
		"\2\21\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\2\25\13\27\f\31\r\33\16\35"+
		"\17\37\20\3\2\21\4\2NNnn\4\2QQqq\4\2CCcc\4\2FFff\4\2VVvv\4\2UUuu\4\2G"+
		"Ggg\4\2RRrr\4\2TTtt\4\2C\\c|\4\2))^^\4\2$$^^\4\2\f\f\17\17\3\2--\5\2\13"+
		"\f\17\17\"\"\2\u0091\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2"+
		"\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\25\3\2\2\2\2\27\3"+
		"\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\3!\3\2\2\2"+
		"\5&\3\2\2\2\7)\3\2\2\2\t\63\3\2\2\2\13\65\3\2\2\2\r\67\3\2\2\2\179\3\2"+
		"\2\2\21;\3\2\2\2\23>\3\2\2\2\25V\3\2\2\2\27X\3\2\2\2\31Z\3\2\2\2\33k\3"+
		"\2\2\2\35r\3\2\2\2\37\u0082\3\2\2\2!\"\t\2\2\2\"#\t\3\2\2#$\t\4\2\2$%"+
		"\t\5\2\2%\4\3\2\2\2&\'\t\6\2\2\'(\t\3\2\2(\6\3\2\2\2)*\t\7\2\2*+\t\b\2"+
		"\2+,\t\t\2\2,-\t\4\2\2-.\t\n\2\2./\t\4\2\2/\60\t\6\2\2\60\61\t\3\2\2\61"+
		"\62\t\n\2\2\62\b\3\2\2\2\63\64\7<\2\2\64\n\3\2\2\2\65\66\7.\2\2\66\f\3"+
		"\2\2\2\678\7*\2\28\16\3\2\2\29:\7+\2\2:\20\3\2\2\2;<\7=\2\2<\22\3\2\2"+
		"\2=?\t\13\2\2>=\3\2\2\2?@\3\2\2\2@>\3\2\2\2@A\3\2\2\2A\24\3\2\2\2BH\7"+
		")\2\2CG\n\f\2\2DE\7^\2\2EG\13\2\2\2FC\3\2\2\2FD\3\2\2\2GJ\3\2\2\2HF\3"+
		"\2\2\2HI\3\2\2\2IK\3\2\2\2JH\3\2\2\2KW\7)\2\2LR\7$\2\2MQ\n\r\2\2NO\7^"+
		"\2\2OQ\13\2\2\2PM\3\2\2\2PN\3\2\2\2QT\3\2\2\2RP\3\2\2\2RS\3\2\2\2SU\3"+
		"\2\2\2TR\3\2\2\2UW\7$\2\2VB\3\2\2\2VL\3\2\2\2W\26\3\2\2\2XY\5\23\n\2Y"+
		"\30\3\2\2\2Z[\7/\2\2[\\\7/\2\2\\`\3\2\2\2]_\n\16\2\2^]\3\2\2\2_b\3\2\2"+
		"\2`^\3\2\2\2`a\3\2\2\2ad\3\2\2\2b`\3\2\2\2ce\7\17\2\2dc\3\2\2\2de\3\2"+
		"\2\2eg\3\2\2\2fh\7\f\2\2gf\3\2\2\2gh\3\2\2\2hi\3\2\2\2ij\b\r\2\2j\32\3"+
		"\2\2\2kl\7\61\2\2lm\7,\2\2mn\7,\2\2no\7\61\2\2op\3\2\2\2pq\b\16\2\2q\34"+
		"\3\2\2\2rs\7\61\2\2st\7,\2\2tu\3\2\2\2uy\n\17\2\2vx\13\2\2\2wv\3\2\2\2"+
		"x{\3\2\2\2yz\3\2\2\2yw\3\2\2\2z|\3\2\2\2{y\3\2\2\2|}\7,\2\2}~\7\61\2\2"+
		"~\177\3\2\2\2\177\u0080\b\17\2\2\u0080\36\3\2\2\2\u0081\u0083\t\20\2\2"+
		"\u0082\u0081\3\2\2\2\u0083\u0084\3\2\2\2\u0084\u0082\3\2\2\2\u0084\u0085"+
		"\3\2\2\2\u0085\u0086\3\2\2\2\u0086\u0087\b\20\2\2\u0087 \3\2\2\2\16\2"+
		"@FHPRV`dgy\u0084\3\2\3\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}