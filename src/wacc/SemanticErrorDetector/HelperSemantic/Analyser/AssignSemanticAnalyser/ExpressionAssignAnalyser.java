package wacc.SemanticErrorDetector.HelperSemantic.Analyser.AssignSemanticAnalyser;

import antlr.WaccParser;
import antlr.WaccParser.ExpressionContext;
import wacc.SemanticErrorDetector.ErrorManager.ErrorReporter;
import wacc.WaccTable.Symbol;
import wacc.SemanticErrorDetector.WaccTypes.Type;
import wacc.SemanticErrorDetector.WaccTypes.TypeBuilder;

class ExpressionAssignAnalyser {

    // Checks assignment for Expressions
    public static void checkAssignement(Symbol variable,
                                        ExpressionContext exprContext) {

        Type expectedType = variable.getType();
        Type actualType = TypeBuilder.buildTypeFromExpression(exprContext);

        if (!expectedType.equals(actualType)) {
            ErrorReporter.reportExpressionAssignError(
                variable, actualType, exprContext);
        }
    }

    /*
      WaccTypes identification functions
    */

    public static boolean isExpressionContext(
        WaccParser.AssignRhsContext assignRhsContext) {

        return assignRhsContext.expression(0) != null;
    }
}
