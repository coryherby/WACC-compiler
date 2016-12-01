package wacc.SemanticErrorDetector.HelperSemantic.HelpersExpressionSemantic;

import antlr.WaccParser.ExpressionContext;
import wacc.SemanticErrorDetector.ErrorManager.ErrorReporter;
import wacc.WaccTable.MainWaccTable;

public class VariableHelper {

    public static boolean isAVariable(ExpressionContext expressionContext) {

        return expressionContext.IDENTIFICATION() != null;
    }

    public static void checkDeclaredVariableUse(
        ExpressionContext expressionContext) {

        if(MainWaccTable.getInstance().getSymbolTable().get(
            expressionContext.IDENTIFICATION().getText()) == null) {
        	
            ErrorReporter.reportUndeclaredVariableUse(expressionContext);
        }
    }
}
