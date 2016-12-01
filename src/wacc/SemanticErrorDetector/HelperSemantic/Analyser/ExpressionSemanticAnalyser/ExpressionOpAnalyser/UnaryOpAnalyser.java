package wacc.SemanticErrorDetector.HelperSemantic.Analyser.ExpressionSemanticAnalyser.ExpressionOpAnalyser;

import antlr.WaccParser.UnaryOperatorContext;
import antlr.WaccParser.ExpressionContext;
import wacc.SemanticErrorDetector.HelperSemantic.Analyser.ExpressionSemanticAnalyser.ArrayExpressionAnalyser;
import wacc.SemanticErrorDetector.HelperSemantic.Analyser.ExpressionSemanticAnalyser.BoolExpressionAnalyser;
import wacc.SemanticErrorDetector.HelperSemantic.Analyser.ExpressionSemanticAnalyser.CharExpressionAnalyser;
import wacc.SemanticErrorDetector.HelperSemantic.Analyser.ExpressionSemanticAnalyser.IntExpressionAnalyser;

public class UnaryOpAnalyser {

    public static boolean isUnaryExpressionBool(ExpressionContext expContext) {

        return isNotUnaryOp(getOperator(expContext))
            && BoolExpressionAnalyser
                    .isBoolExpression(getExpression(expContext));
    }

    public static boolean isUnaryExpressionInt(ExpressionContext expContext) {

        return (isNegationOperator(getOperator(expContext))
                && IntExpressionAnalyser
                        .isIntExpression(getExpression(expContext)))
            || ((isLengthOperator(getOperator(expContext)))
                && ArrayExpressionAnalyser
                        .isArrayExpression(getExpression(expContext)))
            || (isOrdOperator(getOperator(expContext))
                && CharExpressionAnalyser
                        .isCharExpression(getExpression(expContext)));
    }

    public static boolean isUnaryExpressionChar(ExpressionContext expContext) {

        return isChrOperator(getOperator(expContext))
            && IntExpressionAnalyser.isIntExpression(getExpression(expContext));
    }

    /*
      Expression Context helper functions
    */

    private static UnaryOperatorContext getOperator(
        ExpressionContext expressionContext) {

        return expressionContext.unaryOperator();
    }

    private static ExpressionContext getExpression(
        ExpressionContext expressionContext) {

        return expressionContext.expression(0);
    }

    /*
      WaccTypes identification functions
    */

    public static boolean isUnaryOp(ExpressionContext expContext) {

        return expContext.unaryOperator() != null;
    }

    private static boolean isNotUnaryOp(
        UnaryOperatorContext unaryOperatorContext) {

        return unaryOperatorContext.NOT_OPERATOR() != null;
    }

    private static boolean isNegationOperator(
        UnaryOperatorContext unaryOperatorContext) {

        return unaryOperatorContext.MINUS() != null;
    }

    private static boolean isLengthOperator(
        UnaryOperatorContext unaryOperatorContext) {

        return unaryOperatorContext.LENGTH() != null;
    }

    private static boolean isOrdOperator(
        UnaryOperatorContext unaryOperatorContext) {

        return unaryOperatorContext.ORD_OPERATOR() != null;
    }

    private static boolean isChrOperator(
        UnaryOperatorContext unaryOperatorContext) {

        return unaryOperatorContext.CHR_OPERATOR() != null;
    }
}
