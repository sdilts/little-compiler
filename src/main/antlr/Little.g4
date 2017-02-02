// -*- mode: antlr; -*-

grammar Little;
options {
    //generate java code:
    language = Java;
}
@header {
    
package antlr.main;

}

everything : .*?;

FLOATLITERAL : ('0'..'9')* '.' ('0'..'9')+;

INTLITERAL : [0-9]+;

KEYWORD : 'PROGRAM' | 'BEGIN' | 'END' | 'FUNCTION' | 'READ' | 'WRITE' | 'IF' | 'ELSE' | 'ENDIF' | 'WHILE' |
            'ENDWHILE' | 'CONTINUE' | 'BREAK' | 'RETURN' | 'INT' | 'VOID' | 'STRING' | 'FLOAT';

OPERATOR : (':=' | '+' | '-' | '*'  | '/' | '=' | '!=' | '<' | '>' | '(' | ')' | ';' | ',' | '<=' | '>=');

STRINGLITERAL : '"' ~('"')*? '"' ;

IDENTIFIER : (('a'..'z') | ('A'..'Z')) (('a'..'z') | ('A'..'Z') | ('0'..'9'))*;

Comment : '--' ~( '\r' | '\n' )* -> skip;
WS : [ \t\r\n]+ -> skip;