package wacc.SemanticErrorDetector.HelperSemantic.Analyser.ExpressionSemanticAnalyser.ExpressionOpAnalyser;

import antlr.WaccParser.ExpressionContext;

public class BracketExpressionAnalyser {

    /*
      Expression Context function
    */

    public static ExpressionContext getExpressionInsideBrackets(
        ExpressionContext expressionContext) {

        return expressionContext.expression(0);
    }

    /*
      WaccTypes identification functions
    */

    public static boolean hasBracket(ExpressionContext expContext) {

        return expContext.OPEN_BRACKET() != null;
    }
}
