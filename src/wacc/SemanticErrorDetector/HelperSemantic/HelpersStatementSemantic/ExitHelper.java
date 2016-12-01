package wacc.SemanticErrorDetector.HelperSemantic.HelpersStatementSemantic;

import antlr.WaccParser.StatementContext;
import wacc.SemanticErrorDetector.HelperSemantic.Analyser.ExpressionSemanticAnalyser.IntExpressionAnalyser;
import wacc.SemanticErrorDetector.ErrorManager.ErrorReporter;

public class ExitHelper {

    public static void checkExit(StatementContext statementContext) {

        if(!IntExpressionAnalyser
            .isIntExpression(statementContext.expression())) {
            ErrorReporter.reportWrongExitType(statementContext);
        }
    }

    /*
      WaccTypes identification functions
    */

    public static boolean isExitStatement(StatementContext statementContext) {

        return statementContext.EXIT() != null;
    }
}
