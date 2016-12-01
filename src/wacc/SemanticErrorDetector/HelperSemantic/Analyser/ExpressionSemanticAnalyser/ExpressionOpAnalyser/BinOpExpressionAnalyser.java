package wacc.SemanticErrorDetector.HelperSemantic.Analyser.ExpressionSemanticAnalyser.ExpressionOpAnalyser;

import antlr.WaccParser.ExpressionContext;
import wacc.SemanticErrorDetector.HelperSemantic.Analyser.ExpressionSemanticAnalyser.*;

public class BinOpExpressionAnalyser {

    public static boolean isBinOpPrecedence1or2ExpressionInt(
        ExpressionContext expContext) {

        return isLeftAndRightSideIntExpression(expContext);
    }

    public static boolean isBinOpPrecedence3ExpressionBool(
        ExpressionContext expContext) {

        return isLeftAndRightSideIntExpression(expContext)
            || isLeftAndRightSideCharExpression(expContext)
            || isLeftAndRightSideStringExpression(expContext);
    }

    public static boolean isBinOpPrecedence4ExpressionBool(
        ExpressionContext expContext) {

        return isLeftAndRightSideIntExpression(expContext)
            || isLeftAndRightSideCharExpression(expContext)
            || isLeftAndRightSideBoolExpression(expContext)
            || isLeftAndRightSideStringExpression(expContext)
            || isLeftAndRightSideArrayExpression(expContext)
            || isLeftAndRightPairExpression(expContext);
    }

    public static boolean isBinOpPrecedence5or6ExpressionBool(
        ExpressionContext expContext) {

        return isLeftAndRightSideBoolExpression(expContext);
    }

    /*
      BinOp helper functions
    */

    private static boolean isLeftAndRightSideIntExpression(
        ExpressionContext expContext) {

        return IntExpressionAnalyser
                    .isIntExpression(getLeftSideExpression(expContext))
            && IntExpressionAnalyser
                    .isIntExpression(getRightSideExpression(expContext));
    }

    private static boolean isLeftAndRightSideCharExpression(
        ExpressionContext expContext) {

        return CharExpressionAnalyser
                    .isCharExpression(getLeftSideExpression(expContext))
            && CharExpressionAnalyser
                    .isCharExpression(getRightSideExpression(expContext));
    }

    private static boolean isLeftAndRightSideBoolExpression(
        ExpressionContext expContext) {

        return BoolExpressionAnalyser
                    .isBoolExpression(getLeftSideExpression(expContext))
            && BoolExpressionAnalyser
                    .isBoolExpression(getRightSideExpression(expContext));
    }

    private static boolean isLeftAndRightSideStringExpression(
        ExpressionContext expContext) {

        return StringExpressionAnalyser
                    .isStringExpression(getLeftSideExpression(expContext))
            && StringExpressionAnalyser
                    .isStringExpression(getRightSideExpression(expContext));
    }

    private static boolean isLeftAndRightSideArrayExpression(
        ExpressionContext expContext) {

        return ArrayExpressionAnalyser
                    .isArrayExpression(getLeftSideExpression(expContext))
            && ArrayExpressionAnalyser
                    .isArrayExpression(getRightSideExpression(expContext));
    }

    private static boolean isLeftAndRightPairExpression(
        ExpressionContext expContext) {

        return PairExpressionAnalyser
                    .isPairExpression(getLeftSideExpression(expContext))
            && PairExpressionAnalyser
                    .isPairExpression(getRightSideExpression(expContext));
    }

    /*
      Expression Context helper functions
    */

    private static ExpressionContext getLeftSideExpression(
        ExpressionContext expressionContext) {

        return expressionContext.expression(0);
    }

    private static ExpressionContext getRightSideExpression(
        ExpressionContext expressionContext) {

        return expressionContext.expression(1);
    }


    /*
      WaccTypes identification functions
    */

    public static boolean hasBinOp1or2(ExpressionContext expContext) {

        return (expContext.binOpPrecedence1() != null
            || expContext.binOpPrecedence2() != null);
    }

    public static boolean hasBinOp3(ExpressionContext expContext) {

        return expContext.binOpPrecedence3() != null;
    }

    public static boolean hasBinOp4(ExpressionContext expContext) {

        return expContext.binOpPrecedence4() != null;
    }

    public static boolean hasBinOp5or6(ExpressionContext expContext) {

        return (expContext.binOpPrecedence5() != null
            || expContext.binOpPrecedence6() != null);
    }
}
