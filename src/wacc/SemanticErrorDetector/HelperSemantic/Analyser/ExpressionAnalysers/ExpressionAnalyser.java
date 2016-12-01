package wacc.SemanticErrorDetector.HelperSemantic.Analyser.ExpressionAnalysers;

import antlr.WaccParser.*;

public class ExpressionAnalyser {
    public static boolean alwaysEvaluateToTrue(ExpressionContext expression) {

        try {

            return BooleanExpressionAnalyser
                .generateBooleanValueOf(expression);

        } catch(IncorrectExpressionFormat ignored) {
            return false;
        }
    }

    public static boolean alwaysEvaluateToFalse(ExpressionContext expression) {
        try {

            return !BooleanExpressionAnalyser
                .generateBooleanValueOf(expression);

        } catch(IncorrectExpressionFormat e) {
            return false;
        }
    }

    public static String evaluateExpression(ExpressionContext expression) {

        try {
            return String.valueOf(
                BooleanExpressionAnalyser.generateBooleanValueOf(expression));

        } catch(IncorrectExpressionFormat ignored) {}

        try {
            return String.valueOf(
                CharExpressionAnalyser.generateCharValueOf(expression));

        } catch(IncorrectExpressionFormat ignored) {}

        try {
            return String.valueOf(
                IntegerExpressionAnalyser.generateIntegerValueOf(expression));

        } catch(IncorrectExpressionFormat ignored) {}

        try {
            return StringExpressionAnalyser.generateStringValueOf(expression);

        } catch(IncorrectExpressionFormat ignored) {}

        return null;
    }

}
