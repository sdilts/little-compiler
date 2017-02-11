// -*- mode: antlr; -*-

grammar Little;

options {
    //generate java code:
    language = Java;
}
@header {
    
package antlr.main;

}
//program
program : 'PROGRAM' id 'BEGIN' pgm_body 'END';
id : IDENTIFIER;
pgm_body : decl func_declarations;
decl : (string_decl decl) | (var_decl decl) | ;

//string declarations
string_decl : 'STRING' id ':=' str;
str : STRINGLITERAL;

// variable decaraltion
var_decl : var_type id_list;
var_type : 'FLOAT' | 'INT';
any_type : var_type | 'VOID';
id_list : id id_tail ;
id_tail : ',' id id_tail | ;

//functions
param_decl_list : param_decl param_decl_tail | ;
param_decl : var_type id;
param_decl_tail : ',' param_decl param_decl_tail | ;

//function declaration
func_declarations : func_decl func_declarations | ;
func_decl : 'FUNCTION' any_type id '(' param_decl_list ')' 'BEGIN' func_body 'END';
func_body : decl stmt_list;

//statement list
stmt_list : stmt stmt_list | ;
stmt : base_stmt | if_stmt | while_stmt;
base_stmt : assign_stmt | read_stmt | write_stmt | return_stmt;

//basic statements
assign_stmt : assign_expr;
assign_expr : id ':=' expr;
read_stmt : 'READ' '(' id_list ')';
write_stmt : 'WRITE' '(' id_list ')';
return_stmt : 'RETURN' expr;

//expresions
expr : expr_prefix factor;
expr_prefix : expr_prefix factor addop | ;
factor : factor_prefix postfix_expr;
factor_prefix : factor_prefix postfix_expr mulop | ;
postfix_expr : primary | call_expr;
call_expr : id '(' expr_list ')';
expr_list : expr expr_list_tail | ;
expr_list_tail : ',' expr expr_list_tail | ;
primary : '(' expr ')' | id | INTLITERAL | FLOATLITERAL;
addop : '+' | '-';
mulop : '*' | '/';

//complex statements
if_stmt : 'IF' '(' cond ')' decl stmt_list else_part 'ENDIF';
else_part : 'ELSE' decl stmt_list | ;
cond : expr compop expr;
compop : '<' | '>' | '=' | '!=' | '<=' | '>=';

//while
while_stmt : 'WHILE' '(' cond ')' decl stmt_list 'ENDWHILE';


//************************************************************************************************************


FLOATLITERAL : ('0'..'9')* '.' ('0'..'9')+;

INTLITERAL : [0-9]+;

KEYWORD : 'PROGRAM' | 'BEGIN' | 'END' | 'FUNCTION' | 'READ' | 'WRITE' | 'IF' | 'ELSE' | 'ENDIF' | 'WHILE' |
            'ENDWHILE' | 'CONTINUE' | 'BREAK' | 'RETURN' | 'INT' | 'VOID' | 'STRING' | 'FLOAT';

OPERATOR : (':=' | '+' | '-' | '*'  | '/' | '=' | '!=' | '<' | '>' | '(' | ')' | ';' | ',' | '<=' | '>=');

STRINGLITERAL : '"' ~('"')*? '"' ;

IDENTIFIER : (('a'..'z') | ('A'..'Z')) (('a'..'z') | ('A'..'Z') | ('0'..'9'))*;

Comment : '--' ~( '\r' | '\n' )* -> skip;
WS : [ \t\r\n]+ -> skip;
