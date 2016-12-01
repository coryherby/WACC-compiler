lexer grammar WaccLexer;

UNNECESSARY_CHAR : [ \t\r\n]+ -> skip;

// comment
COMMENT : '#' ~('\n')* -> skip; 


// unary operators
NOT_OPERATOR : '!';
LENGTH : 'len';
ORD_OPERATOR : 'ord';
CHR_OPERATOR : 'chr';


// binary operators
PLUS : '+' ;
MINUS : '-' ;
MULTIPLY : '*';
DIVIDE : '/';
MODULUS : '%';
GREATER_THAN : '>';
GREATER_OR_EQUAL_THAN : '>=';
SMALLER_THAN : '<';
SMALLER_OR_EQUAL_THAN : '<=';
IS_EQUAL_TO : '==';
IS_NOT_EQUAL_TO : '!=';
AND : '&&';
OR : '||';


// brackets
OPEN_BRACKET : '(' ;
CLOSE_BRACKET : ')' ;
OPEN_SQUARE_BRACKET : '[';
CLOSE_SQUARE_BRACKET : ']';


// statement
SKIP : 'skip';
READ : 'read';
FREE : 'free';
RETURN : 'return';
EXIT : 'exit';
PRINT : 'print';
PRINTLN : 'println';
IF : 'if';
THEN : 'then';
ELSE : 'else';
FI : 'fi';
WHILE : 'while';
DO : 'do';
DONE : 'done';
BEGIN : 'begin';
END : 'end';
IS : 'is';
PAIR : 'pair';
NEWPAIR : 'newpair';
CALL : 'call';
FIRST : 'fst';
SECOND : 'snd';


// type
INT : 'int';
BOOL : 'bool';
CHAR : 'char';
STRING : 'string';


// layout
COMMA : ',';
SEMI_COLON : ';';
EQUAL_ASSIGNEMENT : '=';
PAIR_LITERAL : 'null';


// boolean
TRUE : 'true';
FALSE : 'false';


//numbers
fragment DIGIT : '0'..'9' ; 
INTEGER : ('-'|'+')? DIGIT+ ;


// string
STRING_LITERAL : '"' ( '\\"' | . )*? '"';


// character (except special char such as " ' \)
fragment APOSTROPHE : '\'';
fragment ESCAPED_CHAR : '\\0' 
    | '\\b'
    | '\\t'
    | '\\f'
    | '\\r'
    | '\\\"'
    | '\\\''
    | '\\\\'
    | '\\n'
    ;

CHAR_LITERAL : APOSTROPHE (~(['"\\]) | ESCAPED_CHAR) APOSTROPHE;


// identification
fragment UPPERCASE_ALPHABET : 'A'..'Z';
fragment LOWERCASE_ALPHABET : 'a'..'z';
fragment UNDERSCORE : '_';

IDENTIFICATION : (UNDERSCORE | LOWERCASE_ALPHABET | UPPERCASE_ALPHABET) 
    (UNDERSCORE | LOWERCASE_ALPHABET | UPPERCASE_ALPHABET | DIGIT)*;
    
CONCAT : '++';
    
ErrorCharacter : . ;