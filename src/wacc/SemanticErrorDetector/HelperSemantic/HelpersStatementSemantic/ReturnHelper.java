package wacc.SemanticErrorDetector.HelperSemantic.HelpersStatementSemantic;

import antlr.WaccParser.FunctionContext;
import antlr.WaccParser.StatementContext;
import wacc.SemanticErrorDetector.ErrorManager.ErrorReporter;
import wacc.SemanticErrorDetector.HelperSemantic.GeneralHelper;
import wacc.WaccTable.MainWaccTable;
import wacc.SemanticErrorDetector.WaccTypes.Type;
import wacc.SemanticErrorDetector.WaccTypes.TypeBuilder;

public class ReturnHelper {

    public static boolean isReturnStatement(
        StatementContext statementContext) {

        return statementContext.RETURN() != null;
    }

    public static boolean checkIsNotCalledInMain(
        StatementContext statementContext) {

        FunctionContext functionContext = GeneralHelper
            .contextFromInitialContext(statementContext, FunctionContext.class);

        if (functionContext == null) {
            ErrorReporter.reportInvalidReturnStatementPosition(
                statementContext);
            return false;
        }
        return true;
    }

    public static void checkHasCorrectType(StatementContext statementContext) {

        Type typeOfExpression = TypeBuilder.buildTypeFromExpression
            (statementContext.expression());

        Type typeOfFunction = MainWaccTable.getInstance().getFunctionTable().get(
            GeneralHelper.contextFromInitialContext(
                statementContext, FunctionContext.class).IDENTIFICATION()
                    .getText()).getReturnType();

        if(!typeOfFunction.equals(typeOfExpression)) {
            ErrorReporter.reportInvalidReturnStatementType (
                statementContext, typeOfFunction, typeOfExpression);
        }
    }
}
