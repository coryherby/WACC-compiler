package wacc.CodeGenerator.InternalRepresentation;

import antlr.WaccParser.*;
import wacc.CodeGenerator.InternalRepresentation.ExpressionRepresentation.*;

import java.util.List;
import java.util.stream.Collectors;

public class ExpressionFactory {

    private static final int FIRST_EXPRESSION = 0;
    private static final int SECOND_EXPRESSION = 1;
    private static final int NUM_EXPRESSIONS_IN_A_BINARY_EXPRESSION = 2;

    public static Expression buildExpressionFromExpressionContext(
        ExpressionContext expressionContext) {

        if (expressionContext.intLiteral() != null) {
            // Int expression
            return buildIntLiteral(expressionContext);

        } else if (expressionContext.boolLiteral() != null) {
            // Bool expression
            return buildBoolLiteral(expressionContext);

        } else if (expressionContext.CHAR_LITERAL() != null) {
            // Char expression
            return buildCharLiteral(expressionContext);

        } else if (expressionContext.STRING_LITERAL() != null) {
            // String expression
            return buildStringLiteral(expressionContext);

        } else if (expressionContext.PAIR_LITERAL() != null) {
            // Pair expression
            return buildPairLiteral(expressionContext);

        } else if (expressionContext.IDENTIFICATION() != null) {
            // Identification expression
            return buildIdentification(expressionContext);

        } else if (expressionContext.arrayElement() != null) {
            // ArrayElement expression
            return buildArrayElem(expressionContext);

        } else if (expressionContext.unaryOperator() != null) {
            // UnaryOperator expression
            return buildUnaryOperatorExpr(expressionContext);

        }  else if (expressionContext.OPEN_BRACKET() != null) {
            // Bracket expression
            return buildBracketExpr(expressionContext);

        } else if (expressionContext.expression() != null
            && expressionContext.expression().size()
                == NUM_EXPRESSIONS_IN_A_BINARY_EXPRESSION) {
            // BinaryOperator expression
            return buildBinaryOperatorExpr(expressionContext);
        }

        return null;
    }

    private static IntLiteral buildIntLiteral(
        ExpressionContext expressionContext) {

        IntLiteralContext intLiteralContext = expressionContext.intLiteral();

        int intLiteral
            = Integer.parseInt(intLiteralContext.INTEGER().getText());

        return new IntLiteral(intLiteral);
    }

    private static BoolLiteral buildBoolLiteral(
        ExpressionContext expressionContext) {

        BoolLiteralContext boolLiteralContext = expressionContext.boolLiteral();

        boolean isTrue = boolLiteralContext.TRUE() != null;

        return new BoolLiteral(isTrue);
    }

    private static CharLiteral buildCharLiteral(
        ExpressionContext expressionContext) {

        String character = expressionContext.CHAR_LITERAL().getText();

        return new CharLiteral(character);
    }

    private static StringLiteral buildStringLiteral(
        ExpressionContext expressionContext) {

        String stringLiteral = expressionContext.STRING_LITERAL().getText();

        return new StringLiteral(stringLiteral);
    }

    private static PairLiteral buildPairLiteral(
        ExpressionContext expressionContext) {

        return new PairLiteral();
    }

    private static Identification buildIdentification(
        ExpressionContext expressionContext) {

        String identifier = expressionContext.IDENTIFICATION().getText();

        return new Identification(identifier);
    }

    private static ArrayElem buildArrayElem(
        ExpressionContext expressionContext) {

        ArrayElementContext arrayElementContext
            = expressionContext.arrayElement();

        String identifier = arrayElementContext.IDENTIFICATION().getText();

        List<Expression> arrayAccesses
            = arrayElementContext
            .expression()
            .stream()
            .map(ExpressionFactory::buildExpressionFromExpressionContext)
            .collect(Collectors.toList());

        return new ArrayElem(identifier, arrayAccesses);
    }

    private static UnaryOperatorExpr buildUnaryOperatorExpr(
        ExpressionContext expressionContext) {

        UnaryOperatorContext unaryOperatorContext
            = expressionContext.unaryOperator();
        ExpressionContext UnaryOperatorExpressionContext
            = expressionContext.expression(FIRST_EXPRESSION);

        UnaryOperatorExpr.UnaryOp unaryOp = null;

        if (unaryOperatorContext.NOT_OPERATOR() != null) {
            unaryOp = UnaryOperatorExpr.UnaryOp.LOGICAL_NOT;
        } else if (unaryOperatorContext.MINUS() != null) {
            unaryOp = UnaryOperatorExpr.UnaryOp.NEGATION;
        } else if (unaryOperatorContext.LENGTH() != null) {
            unaryOp = UnaryOperatorExpr.UnaryOp.ARRAY_LENGTH;
        } else if (unaryOperatorContext.ORD_OPERATOR() != null) {
            unaryOp = UnaryOperatorExpr.UnaryOp.ASCII_VALUE;
        } else if (unaryOperatorContext.CHR_OPERATOR() != null) {
            unaryOp = UnaryOperatorExpr.UnaryOp.CHAR_REPRESENTATION;
        }

        Expression expression = buildExpressionFromExpressionContext(
            UnaryOperatorExpressionContext);

        return new UnaryOperatorExpr(unaryOp, expression);
    }

    private static BracketExpr buildBracketExpr(
        ExpressionContext expressionContext) {

        ExpressionContext expression
            = expressionContext.expression(FIRST_EXPRESSION);

        Expression expressionBetweenBrackets
            = buildExpressionFromExpressionContext(expression);

        return new BracketExpr(expressionBetweenBrackets);
    }

    private static BinaryOperatorExpr buildBinaryOperatorExpr(
        ExpressionContext expressionContext) {

        BinaryOperatorExpr.BinOp binOp = null;

        if (expressionContext.binOpPrecedence1() != null) {
            binOp = getBinOp(expressionContext.binOpPrecedence1());
        } else if (expressionContext.binOpPrecedence2() != null) {
            binOp = getBinOp(expressionContext.binOpPrecedence2());
        } else if (expressionContext.binOpPrecedence3() != null) {
            binOp = getBinOp(expressionContext.binOpPrecedence3());
        } else if (expressionContext.binOpPrecedence4() != null) {
            binOp = getBinOp(expressionContext.binOpPrecedence4());
        } else if (expressionContext.binOpPrecedence5() != null) {
            binOp = getBinOp(expressionContext.binOpPrecedence5());
        } else if (expressionContext.binOpPrecedence6() != null) {
            binOp = getBinOp(expressionContext.binOpPrecedence6());
        }

        Expression leftExpression = buildExpressionFromExpressionContext(
            expressionContext.expression(FIRST_EXPRESSION));
        Expression rightExpression = buildExpressionFromExpressionContext(
            expressionContext.expression(SECOND_EXPRESSION));

        return new BinaryOperatorExpr(binOp, leftExpression, rightExpression);
    }

    /*
      Helpers for binaryOperatorExpr build
     */

    private static BinaryOperatorExpr.BinOp getBinOp(
        BinOpPrecedence1Context binOpContext) {

        BinaryOperatorExpr.BinOp binOp = null;

        if (binOpContext.MULTIPLY() != null) {
            binOp = BinaryOperatorExpr.BinOp.MULTIPLY;
        } else if (binOpContext.DIVIDE() != null) {
            binOp = BinaryOperatorExpr.BinOp.DIVIDE;
        } else if (binOpContext.MODULUS() != null) {
            binOp = BinaryOperatorExpr.BinOp.MODULUS;
        }

        return binOp;
    }

    private static BinaryOperatorExpr.BinOp getBinOp(
        BinOpPrecedence2Context binOpContext) {

        BinaryOperatorExpr.BinOp binOp = null;

        if (binOpContext.PLUS() != null) {
            binOp = BinaryOperatorExpr.BinOp.PLUS;
        } else if (binOpContext.MINUS() != null) {
            binOp = BinaryOperatorExpr.BinOp.MINUS;
        }

        return binOp;
    }

    private static BinaryOperatorExpr.BinOp getBinOp(
        BinOpPrecedence3Context binOpContext) {

        BinaryOperatorExpr.BinOp binOp = null;

        if (binOpContext.GREATER_THAN() != null) {
            binOp = BinaryOperatorExpr.BinOp.GREATER;
        } else if (binOpContext.GREATER_OR_EQUAL_THAN() != null) {
            binOp = BinaryOperatorExpr.BinOp.GREATER_OR_EQUAL;
        } else if (binOpContext.SMALLER_THAN() != null) {
            binOp = BinaryOperatorExpr.BinOp.SMALLER;
        } else if (binOpContext.SMALLER_OR_EQUAL_THAN() != null) {
            binOp = BinaryOperatorExpr.BinOp.SMALLER_OR_EQUAL;
        }

        return binOp;
    }

    private static BinaryOperatorExpr.BinOp getBinOp(
        BinOpPrecedence4Context binOpContext) {

        BinaryOperatorExpr.BinOp binOp = null;

        if (binOpContext.IS_EQUAL_TO() != null) {
            binOp = BinaryOperatorExpr.BinOp.EQUAL;
        } else if (binOpContext.IS_NOT_EQUAL_TO() != null) {
            binOp = BinaryOperatorExpr.BinOp.NOT_EQUAL;
        }

        return binOp;
    }


    private static BinaryOperatorExpr.BinOp getBinOp(
        BinOpPrecedence5Context binOpContext) {

        return BinaryOperatorExpr.BinOp.AND;
    }

    private static BinaryOperatorExpr.BinOp getBinOp(
        BinOpPrecedence6Context binOpContext) {

        return BinaryOperatorExpr.BinOp.OR;
    }
}
