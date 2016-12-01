package wacc.SemanticErrorDetector.HelperSemantic.Analyser.ExpressionSemanticAnalyser;

import antlr.WaccParser.ExpressionContext;
import wacc.SemanticErrorDetector.HelperSemantic.Analyser.ExpressionSemanticAnalyser.ExpressionOpAnalyser.BinOpExpressionAnalyser;
import wacc.SemanticErrorDetector.HelperSemantic.Analyser.ExpressionSemanticAnalyser.ExpressionOpAnalyser.BracketExpressionAnalyser;
import wacc.SemanticErrorDetector.HelperSemantic.Analyser.ExpressionSemanticAnalyser.ExpressionOpAnalyser.UnaryOpAnalyser;
import wacc.SemanticErrorDetector.WaccTypes.WaccType;

public class IntExpressionAnalyser {

    public static final WaccType CLASS_TYPE = WaccType.INT;

    // Checks if a given expression is a valid INT expression
    public static boolean isIntExpression(ExpressionContext expContext) {

        if (IdentificationExpressionAnalyser.hasIdentification(expContext)) {

            return IdentificationExpressionAnalyser
                .isIdentificationSameAsType(expContext, CLASS_TYPE);

        } else if (ArrayElemExpressionAnalyser.isArrayElem(expContext)) {

            return ArrayElemExpressionAnalyser.isArrayElemSameAsExpectedType(
                expContext.arrayElement(), CLASS_TYPE);

        } else if (BinOpExpressionAnalyser.hasBinOp1or2(expContext)) {

            return BinOpExpressionAnalyser
                        .isBinOpPrecedence1or2ExpressionInt(expContext);

        } else if (BracketExpressionAnalyser.hasBracket(expContext)) {

            return isIntExpression(BracketExpressionAnalyser
                .getExpressionInsideBrackets(expContext));

        } else if (UnaryOpAnalyser.isUnaryOp(expContext)) {

            return UnaryOpAnalyser.isUnaryExpressionInt(expContext);
        }

        return isIntLiteral(expContext);
    }

    /*
      WaccTypes identification functions
    */

    private static boolean isIntLiteral(
        ExpressionContext expContext) {

        return expContext.intLiteral() != null;
    }

}
