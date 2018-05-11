grammar RuleFilter;

parse
 : filter
 ;

filter
: expr
;

expr
 : '(' expr ')'                                   # subExpr
 | <assoc=right> expr '^' expr                    # exponentExpr
 | '(' expr ')'                                   # subExpr
 | expr HIGH_PRIORITY_OPERATION expr              # highPriorityOperationExpr
 | expr LOW_PRIORITY_OPERATION expr               # lowPriorityOperationExpr
 | expr COMPARISON_OPERATION expr                 # comparisonExpr
 | expr AND_OPERATION expr                        # andCondition
 | expr OR_OPERATION expr                         # orCondition
 | NOT_OPERATION '(' expr ')'                     # notCondition
 | 'IF'           '(' expr ',' expr ',' expr ')'  # ifCondition
 | functionname '(' fieldname ')'                 # oneArgCondition
 | fieldname (NOT_OPERATION)? functionname '(' expr (',' expr)* ')' # varArgCondition
 | fieldname COMPARISON_OPERATION INT timeunit    # timeCondition
 | atom                                           # atomExpr
 | fieldname                                      # fieldNameExpr
 ;

functionname
 : FIELD_NAME
 ;

timeunit
 : ('SECONDS'|'S'|'s')
 | ('MINUTES'|'M'|'m')
 | ('HOURS'|'H'|'h')
 | ('DAYS'|'D'|'d')
 ;

HIGH_PRIORITY_OPERATION
 : '*'
 | '/'
 ;

LOW_PRIORITY_OPERATION
 : '+'
 | '-'
 ;

COMPARISON_OPERATION
 : '<>'
 | '!='
 | '='
 | '=='
 | '>'
 | '<'
 | '>='
 | '<='
 ;

AND_OPERATION
 : 'AND'
 | '&&'
 ;

OR_OPERATION
 : 'OR'
 | '||'
 ;

NOT_OPERATION
 : 'NOT'
 | '!'
 ;

atom
 : INT         # intAtom
 | BOOLEAN     # booleanAtom
 | FLOAT       # floatAtom
 | STRING      # stringAtom
 | 'null'      # nullAtom
 ;

fieldname
 : FIELD_NAME
 ;

target
 : FIELD_NAME
 ;

BOOLEAN
 : 'true'
 | 'false'
 ;

STRING
 : '"' (ESC|.)*? '"'
 ;

fragment ESC
 : '\\"'
 | '\\\\'
 ;

FIELD_NAME
 : [a-zA-Z][A-Za-z0-9_]*
 ;

INT
 : [-]?[0-9]+
 ;

FLOAT
 : [-]?[0-9]+ '.' [0-9]*  // 1. or 1.1
 |        '.' [0-9]+  // .1
 ;

SPACE
 : [ \t\r\n] -> skip
 ;