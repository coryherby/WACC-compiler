package wacc.SemanticErrorDetector.HelperSemantic.Analyser.AssignSemanticAnalyser;

import antlr.WaccParser.AssignRhsContext;
import wacc.SemanticErrorDetector.ErrorManager.ErrorReporter;
import wacc.WaccTable.Function;
import wacc.WaccTable.FunctionTable;
import wacc.WaccTable.Symbol;
import wacc.WaccTable.MainWaccTable;
import wacc.SemanticErrorDetector.WaccTypes.Type;

class FunctionAssignAnalyser {

    // Checks assignment for functions call
    public static void checkAssignement(Symbol variable, 
    									String functionName,
    									AssignRhsContext assignRhsContext) {

        Type expectedType = variable.getType();

        FunctionTable functionTable = MainWaccTable.getInstance().getFunctionTable();
        Function function = functionTable.get(functionName);

        // If function does not exist, return
        if (function == null) {
            return;
        }

        // Get the function return type
        Type actualType = function.getReturnType();

        // Check that the function return type has the right type
        if (!expectedType.equals(actualType)) {
            ErrorReporter.reportFunctionAssignError(
                variable, functionName, actualType, assignRhsContext);
        }
    }

    /*
      WaccTypes identification functions
    */

    public static boolean isFunction(AssignRhsContext assignRhsContext) {
        return assignRhsContext.CALL() != null;
    }
}
