package wacc.SemanticErrorDetector.HelperSemantic.Analyser.ExpressionSemanticAnalyser;

import antlr.WaccParser.ExpressionContext;
import wacc.SemanticErrorDetector.HelperSemantic.Analyser.ExpressionSemanticAnalyser.ExpressionOpAnalyser.BracketExpressionAnalyser;
import wacc.SemanticErrorDetector.HelperSemantic.Analyser.ExpressionSemanticAnalyser.ExpressionOpAnalyser.UnaryOpAnalyser;
import wacc.SemanticErrorDetector.WaccTypes.WaccType;

public class CharExpressionAnalyser {

    public static final WaccType CLASS_TYPE = WaccType.CHAR;

    // Checks if a given expression is a valid CHAR expression
    public static boolean isCharExpression(ExpressionContext expContext) {

        if (IdentificationExpressionAnalyser.hasIdentification(expContext)) {

            return IdentificationExpressionAnalyser
                .isIdentificationSameAsType(expContext, CLASS_TYPE);

        } else if (ArrayElemExpressionAnalyser.isArrayElem(expContext)) {

            return ArrayElemExpressionAnalyser.isArrayElemSameAsExpectedType(
                expContext.arrayElement(), CLASS_TYPE);

        } else if (BracketExpressionAnalyser.hasBracket(expContext)) {

            return isCharExpression(BracketExpressionAnalyser
                .getExpressionInsideBrackets(expContext));

        } else if (UnaryOpAnalyser.isUnaryOp(expContext)) {

            return UnaryOpAnalyser.isUnaryExpressionChar(expContext);
        }

        return isCharLiteral(expContext);
    }

    /*
      WaccTypes identification functions
    */

    private static boolean isCharLiteral(
        ExpressionContext expContext) {

        return expContext.CHAR_LITERAL() != null;
    }
}
