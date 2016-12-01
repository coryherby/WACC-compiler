package wacc.SemanticErrorDetector.HelperSemantic.HelpersAssignRhsSemantic;

import antlr.WaccParser.ExpressionContext;
import antlr.WaccParser.ArgumentListContext;
import antlr.WaccParser.AssignRhsContext;
import wacc.SemanticErrorDetector.ErrorManager.ErrorReporter;
import wacc.WaccTable.Function;
import wacc.WaccTable.FunctionTable;
import wacc.WaccTable.MainWaccTable;
import wacc.SemanticErrorDetector.WaccTypes.Type;
import wacc.SemanticErrorDetector.WaccTypes.TypeBuilder;

import java.util.List;

public class FunctionCallHelper {

    public static boolean checkIsAlreadyInitializedFunction(
        AssignRhsContext assignRhsContext) {

        FunctionTable functionTable = MainWaccTable.getInstance().getFunctionTable();
        String identification = assignRhsContext.IDENTIFICATION().getText();

        if (functionTable.get(identification) == null) {

            ErrorReporter.reportUndeclaredFunctionCall(assignRhsContext);
            return false;
        }

        return true;
    }

    public static void checkCorrectNumberOfArgument(
        AssignRhsContext assignRhsContext) {

        int numberOfArguments = 0;

        if (assignRhsContext.argumentList() != null) {

            for(int i = 0;
                assignRhsContext.argumentList().expression(i) != null; i++) {

                numberOfArguments++;
            }
        }

        int numberOfArgumentTaken = MainWaccTable.getInstance().getFunctionTable()
                .get(assignRhsContext.IDENTIFICATION().getText())
                .getParameterList().size();

        if (numberOfArguments != numberOfArgumentTaken) {
            ErrorReporter.reportWrongNumberOfArgument(assignRhsContext,
                numberOfArguments, numberOfArgumentTaken);
        }
    }

    public static void checkCorrectArgumentTypes(
        AssignRhsContext assignRhsContext) {

        ArgumentListContext argumentListContext
            = assignRhsContext.argumentList();

        if (argumentListContext == null) {
            return;
        }

        List<ExpressionContext> argumentList
            = argumentListContext.expression();

        FunctionTable functionTable = MainWaccTable.getInstance().getFunctionTable();
        String identification = assignRhsContext.IDENTIFICATION().getText();
        Function function = functionTable.get(identification);

        if (function != null) {

            List<Type> functionParameterTypes = function.getParameterList();

            if (functionParameterTypes.size() != argumentList.size()) {
                // Not the same number of arguments so return
                return;
            }

            for (int i = 0; i < functionParameterTypes.size(); i++) {

                ExpressionContext expressionContext
                    = argumentList.get(i);

                Type actual
                    = TypeBuilder.buildTypeFromExpression(expressionContext);
                Type expected
                    = functionParameterTypes.get(i);

                if (!expected.equals(actual)) {
                    ErrorReporter.reportWrongTypesOfArgument(expressionContext,
                        expected, actual);
                    return;
                }
            }
        }
    }

    public static boolean isFunctionAssignmentStatement(
        AssignRhsContext assignRhsContext) {

        return assignRhsContext.CALL() != null;
    }
}
