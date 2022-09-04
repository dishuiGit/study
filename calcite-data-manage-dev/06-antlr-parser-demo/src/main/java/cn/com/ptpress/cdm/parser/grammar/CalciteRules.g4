grammar CalciteRules;    //定义规则文件grammar
program : stmt SEMICOLON? EOF;
stmt
    : loadStmt //定义load 规则
    ;
loadStmt:
        LOAD loadFromStmt TO loadToStmt loadColumns (SEPARATOR STRING)?
        ;
loadFromStmt :
       IDENTIFIER COLON STRING
       ;
loadToStmt :
       IDENTIFIER COLON STRING
       ;
loadColumns :
       OPEN_P columnsItem CLOSE_P;
columnsItem:
      ((IDENTIFIER IDENTIFIER COMA) | (IDENTIFIER IDENTIFIER))+
      ;
//lexers antlr将某个句子进行分词的时候，分词单元就是如下的lexer
//keywords  定义一些关键字的lexer，忽略大小写
LOAD: [Ll][Oo][Aa][Dd];
TO: [Tt][Oo];
SEPARATOR:[Ss][Ee][Pp][Aa][Rr][Aa][Tt][Oo][Rr];
COLON : ':';
COMA : ',';
OPEN_P : '(';
CLOSE_P : ')';
SEMICOLON : ';' ;
//base  定义一些基础的lexer,
fragment LETTER:[a-zA-Z]+;  //匹配字母
STRING        //匹配带引号的文本
    : '\'' ( ~('\''|'\\') | ('\\' .) )* '\''
    | '"' ( ~('"'|'\\') | ('\\' .) )* '"'
    ;
IDENTIFIER    //匹配文本
    : LETTER
    ;

//--hiden  定义需要隐藏的文本，指向channel(HIDDEN)就会隐藏。这里的channel可以自定义，到时在后台获取不同的channel的数据进行不同的处理
SIMPLE_COMMENT: '--' ~[\r\n]* '\r'? '\n'? -> channel(HIDDEN);   //忽略行注释
BRACKETED_EMPTY_COMMENT: '/**/' -> channel(HIDDEN);  //忽略多行注释
BRACKETED_COMMENT : '/*' ~[+] .*? '*/' -> channel(HIDDEN) ;  //忽略多行注释
WS: [ \r\n\t]+ -> channel(HIDDEN);  //忽略空白符
