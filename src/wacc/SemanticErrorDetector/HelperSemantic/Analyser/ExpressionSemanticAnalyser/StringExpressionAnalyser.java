package wacc.SemanticErrorDetector.HelperSemantic.Analyser.ExpressionSemanticAnalyser;

import antlr.WaccParser.ExpressionContext;
import wacc.SemanticErrorDetector.HelperSemantic.Analyser.ExpressionSemanticAnalyser.ExpressionOpAnalyser.BracketExpressionAnalyser;
import wacc.SemanticErrorDetector.WaccTypes.WaccType;

public class StringExpressionAnalyser {

    public static final WaccType CLASS_TYPE = WaccType.STRING;

    // Checks if a given expression is a valid STRING expression
    public static boolean isStringExpression(ExpressionContext expContext) {

        if (IdentificationExpressionAnalyser.hasIdentification(expContext)) {

            return IdentificationExpressionAnalyser.isIdentificationSameAsType(
                expContext, CLASS_TYPE);

        } else if (ArrayElemExpressionAnalyser.isArrayElem(expContext)) {

            return ArrayElemExpressionAnalyser.isArrayElemSameAsExpectedType(
                expContext.arrayElement(), CLASS_TYPE);

        } else if (BracketExpressionAnalyser.hasBracket(expContext)) {

            return isStringExpression(BracketExpressionAnalyser
                .getExpressionInsideBrackets(expContext));
        }

        return isStringLiteral(expContext);
    }

    /*
      WaccTypes identification functions
    */

    private static boolean isStringLiteral(ExpressionContext expContext) {

        return expContext.STRING_LITERAL() != null;
    }
}
