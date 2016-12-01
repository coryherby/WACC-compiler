parser grammar WaccParser;

options {
  tokenVocab=WaccLexer;
}

program : BEGIN (function)* statement END EOF;

function : type IDENTIFICATION OPEN_BRACKET (parameterList)? 
    CLOSE_BRACKET IS statement END;

parameterList : parameter (COMMA parameter)*;

parameter : type IDENTIFICATION;

statement : SKIP
    | type IDENTIFICATION EQUAL_ASSIGNEMENT assignRhs
    | assignLhs EQUAL_ASSIGNEMENT assignRhs
    | READ assignLhs
    | FREE expression
    | PRINT expression
    | PRINTLN expression
    | RETURN expression
    | EXIT expression
    | IF expression THEN statement ELSE statement FI
    | WHILE expression DO statement DONE
    | BEGIN statement END
    | statement SEMI_COLON statement
    | IF THEN statement ELSE statement FI
        {notifyErrorListeners("Missing expression in 'if' statement.");}
    | IF expression (statement)? ELSE statement FI 
        {notifyErrorListeners("Missing 'then' in 'if' statement.");}
    | IF expression THEN statement FI 
        {notifyErrorListeners("Missing 'else' in 'if' statement.");}
    | IF expression THEN statement ELSE statement 
        {notifyErrorListeners("Missing 'fi' in 'if' statement.");}
    | WHILE expression (statement)? DONE 
        {notifyErrorListeners("Missing 'do' in 'while' loop.");}
    | WHILE expression DO statement
        {notifyErrorListeners("Missing 'done' in 'while' loop.");}
    | statement (SEMI_COLON)+
        {notifyErrorListeners("Too many semi-colons.");}
    ;

assignLhs : IDENTIFICATION 
    | arrayElement
    | pairElement
    ;

assignRhs : expression 
    | arrayLiteral
    | NEWPAIR OPEN_BRACKET expression COMMA expression CLOSE_BRACKET
    | pairElement
    | CALL IDENTIFICATION OPEN_BRACKET (argumentList)? CLOSE_BRACKET
    ;

argumentList : expression (COMMA expression)*;

pairElement : FIRST expression
    | SECOND expression
    ;

type : baseType
    | type OPEN_SQUARE_BRACKET CLOSE_SQUARE_BRACKET
    | pairType
    ;

baseType : INT
    | BOOL
    | CHAR
    | STRING
    ;

arrayType : type OPEN_SQUARE_BRACKET CLOSE_SQUARE_BRACKET;

pairType : PAIR OPEN_BRACKET pairElementType COMMA 
    pairElementType CLOSE_BRACKET
    ;

pairElementType : baseType
    | arrayType
    | PAIR
    ;

expression: intLiteral 
    | boolLiteral
    | CHAR_LITERAL
    | STRING_LITERAL
    | PAIR_LITERAL
    | IDENTIFICATION
    | arrayElement
    | unaryOperator expression
    | OPEN_BRACKET expression CLOSE_BRACKET
    | expression binOpPrecedence1 expression
    | expression binOpPrecedence2 expression
    | expression binOpPrecedence3 expression
    | expression binOpPrecedence4 expression
    | expression binOpPrecedence5 expression
    | expression binOpPrecedence6 expression
    ;
    
unaryOperator : NOT_OPERATOR
    | LENGTH
    | ORD_OPERATOR
    | CHR_OPERATOR
    | MINUS
    ;

binOpPrecedence1 : MULTIPLY 
    | DIVIDE
    | MODULUS
    ; 

binOpPrecedence2 : PLUS
    | MINUS
    ;

binOpPrecedence3 : GREATER_THAN
    | GREATER_OR_EQUAL_THAN
    | SMALLER_THAN
    | SMALLER_OR_EQUAL_THAN
    ;

binOpPrecedence4 : IS_EQUAL_TO
    | IS_NOT_EQUAL_TO
    ;

binOpPrecedence5 : AND;

binOpPrecedence6 : OR;

arrayElement : IDENTIFICATION 
    (OPEN_SQUARE_BRACKET expression CLOSE_SQUARE_BRACKET)+;

intLiteral : INTEGER;

boolLiteral : TRUE
    | FALSE
    ;

arrayLiteral : OPEN_SQUARE_BRACKET (expression (COMMA expression)*)? 
    CLOSE_SQUARE_BRACKET
    | arrayLiteral CONCAT arrayLiteral 
        {notifyErrorListeners("Array concatenation is not allowed.");}
    ;
