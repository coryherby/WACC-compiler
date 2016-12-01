package wacc.SemanticErrorDetector.HelperSemantic.HelpersStatementSemantic;

import antlr.WaccParser.ExpressionContext;
import antlr.WaccParser.StatementContext;
import wacc.SemanticErrorDetector.HelperSemantic.Analyser.ExpressionSemanticAnalyser.ArrayExpressionAnalyser;
import wacc.SemanticErrorDetector.HelperSemantic.Analyser.ExpressionSemanticAnalyser.PairExpressionAnalyser;
import wacc.SemanticErrorDetector.ErrorManager.ErrorReporter;

public class FreeHelper {

    public static void checkFreeStatement(StatementContext statementContext) {

        ExpressionContext expressionContext = statementContext.expression();

        boolean isValidFree
            = PairExpressionAnalyser.isPairExpression(expressionContext)
                || ArrayExpressionAnalyser.isArrayExpression(expressionContext);

        if (!isValidFree) {
            ErrorReporter.reportFreeWrongType(statementContext);
        }
    }

    /*
      WaccTypes identification functions
    */

    public static boolean isFreeStatement(StatementContext statementContext) {

        return statementContext.FREE() != null;
    }
}
