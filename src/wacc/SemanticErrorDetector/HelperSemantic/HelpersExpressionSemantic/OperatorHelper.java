package wacc.SemanticErrorDetector.HelperSemantic.HelpersExpressionSemantic;

import antlr.WaccParser.ExpressionContext;
import wacc.SemanticErrorDetector.HelperSemantic.Analyser.ExpressionSemanticAnalyser.ExpressionOpAnalyser.OperatorAnalyser;
import wacc.SemanticErrorDetector.ErrorManager.ErrorReporter;

public class OperatorHelper {

    public static boolean checkOperatorExpressionValid(
        ExpressionContext expressionContext) {

        boolean isValid
            = OperatorAnalyser.areOperatorExpressionValid(expressionContext);

        if (!isValid) {
            ErrorReporter.reportInvalidOperatorUse(expressionContext);
        }

        return isValid;
    }
}
