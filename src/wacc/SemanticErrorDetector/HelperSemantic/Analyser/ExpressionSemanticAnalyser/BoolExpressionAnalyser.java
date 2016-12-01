package wacc.SemanticErrorDetector.HelperSemantic.Analyser.ExpressionSemanticAnalyser;

import antlr.WaccParser.ExpressionContext;
import wacc.SemanticErrorDetector.HelperSemantic.Analyser.ExpressionSemanticAnalyser.ExpressionOpAnalyser.BinOpExpressionAnalyser;
import wacc.SemanticErrorDetector.HelperSemantic.Analyser.ExpressionSemanticAnalyser.ExpressionOpAnalyser.BracketExpressionAnalyser;
import wacc.SemanticErrorDetector.HelperSemantic.Analyser.ExpressionSemanticAnalyser.ExpressionOpAnalyser.UnaryOpAnalyser;
import wacc.SemanticErrorDetector.WaccTypes.WaccType;

public class BoolExpressionAnalyser {

    public static final WaccType CLASS_TYPE = WaccType.BOOL;

    // Checks if a given expression is a valid BOOLEAN expression
    public static boolean isBoolExpression(ExpressionContext expContext) {

        if (IdentificationExpressionAnalyser.hasIdentification(expContext)) {

            return IdentificationExpressionAnalyser.isIdentificationSameAsType(
                expContext, CLASS_TYPE);

        } else if (ArrayElemExpressionAnalyser.isArrayElem(expContext)) {

            return ArrayElemExpressionAnalyser.isArrayElemSameAsExpectedType(
                expContext.arrayElement(), CLASS_TYPE);

        } else if (BinOpExpressionAnalyser.hasBinOp3(expContext)) {

            return BinOpExpressionAnalyser
                        .isBinOpPrecedence3ExpressionBool(expContext);

        } else if (BinOpExpressionAnalyser.hasBinOp4(expContext)) {

            return BinOpExpressionAnalyser
                        .isBinOpPrecedence4ExpressionBool(expContext);

        } else if (BinOpExpressionAnalyser.hasBinOp5or6(expContext)) {

            return BinOpExpressionAnalyser
                        .isBinOpPrecedence5or6ExpressionBool(expContext);

        } else if (UnaryOpAnalyser.isUnaryOp(expContext)) {

            return UnaryOpAnalyser
                        .isUnaryExpressionBool(expContext);

        } else if (BracketExpressionAnalyser.hasBracket(expContext)) {

            return isBoolExpression(BracketExpressionAnalyser
                .getExpressionInsideBrackets(expContext));
        }

        return isBoolLiteral(expContext);
    }

    /*
      WaccTypes identification functions
    */

    private static boolean isBoolLiteral(ExpressionContext expContext) {

        return expContext.boolLiteral() != null;
    }
}
