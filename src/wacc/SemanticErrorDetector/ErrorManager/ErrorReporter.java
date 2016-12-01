package wacc.SemanticErrorDetector.ErrorManager;

import antlr.WaccParser.*;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import wacc.SemanticErrorDetector.HelperSemantic.GeneralHelper;
import wacc.WaccTable.Symbol;
import wacc.SemanticErrorDetector.WaccTypes.Type;
import wacc.SemanticErrorDetector.WaccTypes.TypeBuilder;
import wacc.SemanticErrorDetector.WaccTypes.WaccType;

public class ErrorReporter {

    private static int semanticErrorCounter;

    public static int getSemanticErrorCounter() {
        return semanticErrorCounter;
    }

    public static boolean hasNoError() {
        return semanticErrorCounter == 0;
    }

    /*
      If condition errors
    */

    public static void reportIfConditionError(
            StatementContext statementContext) {

    	reportError();
    	
        Type actualType = TypeBuilder
            .buildTypeFromExpression(statementContext.expression());
        String actual = actualType != null ?
            actualType.toString() : "UNDEFINED TYPE";

        int startIndex = statementContext.IF().getSymbol().getStartIndex();
        int stopIndex = statementContext.THEN().getSymbol().getStopIndex();
        ParserRuleContext errorExpression = statementContext.expression();
        String typeOfError = "If Condition";
        String errorDescription = ErrorPrintHelper
            .generateErrorMessageForTypeError("BOOL", actual);


        ErrorPrintHelper.printError(startIndex, stopIndex, errorExpression,
                typeOfError, errorDescription);
    }

    /*
       While condition errors
    */

	public static void reportWhileConditionError(
        StatementContext statementContext) {

		reportError();
		
        Type actualType = TypeBuilder
            .buildTypeFromExpression(statementContext.expression());
        String actual = actualType != null ?
            actualType.toString() : "UNDEFINED TYPE";

        int startIndex = statementContext.WHILE().getSymbol().getStartIndex();
        int stopIndex = statementContext.DO().getSymbol().getStopIndex();
        ParserRuleContext errorConrext = statementContext.expression();
        String typeOfError = "While Condition";
        String errorDescription = ErrorPrintHelper
            .generateErrorMessageForTypeError("BOOL", actual);

        ErrorPrintHelper.printError(startIndex, stopIndex, errorConrext,
            typeOfError, errorDescription);
    }

    /*
      Multiple variable errors
    */

    public static void reportMultipleVariableDeclaration(
            StatementContext statementContext) {

    	reportError();

        int startIndex = statementContext.getStart().getStartIndex();
        int stopIndex = statementContext.getStop().getStopIndex();
        Token errorTokenStart = statementContext.IDENTIFICATION().getSymbol();
        String typeOfError = "Variable Declaration";
        String errorDescription = "Double declaration of variable";

        ErrorPrintHelper.printError(startIndex, stopIndex, errorTokenStart,
            typeOfError, errorDescription);
    }

    /*
      Undeclared variables errors
    */

    public static void reportUndeclaredVariableAssignment(
            StatementContext statementContext) {

    	reportError();

        int startIndex = statementContext.assignLhs().getStart()
                .getStartIndex();
        int stopIndex = statementContext.assignRhs().getStop().getStopIndex();
        ParserRuleContext errorContext = statementContext.assignLhs();
        String typeOfError = "Variable Assignment";
        String errorDescription = "Undeclared variable assignment";

        ErrorPrintHelper.printError(startIndex, stopIndex, errorContext,
            typeOfError, errorDescription);
    }

    /*
      Multiple function declaration errors
    */

    public static void reportMultipleFunctionDeclaration(
            FunctionContext functionContext) {

    	reportError();

        int startIndex = functionContext.getStart().getStartIndex();
        int stopIndex = functionContext.CLOSE_BRACKET().getSymbol()
                .getStopIndex();
        Token errorTokenStart = functionContext.IDENTIFICATION().getSymbol();
        String typeOfError = "Function Declaration";
        String errorDescription = "Multiple declaration of function";

        ErrorPrintHelper.printError(startIndex, stopIndex, errorTokenStart,
                typeOfError, errorDescription);
    }

    /*
      Undeclared function errors
    */

    public static void reportUndeclaredFunctionCall(AssignRhsContext
                                                             assignRhsContext) {

    	reportError();

        int startIndex = assignRhsContext.getParent().getStart()
                .getStartIndex();
        int stopIndex = assignRhsContext.getStop().getStopIndex();
        Token errorTokenStart = assignRhsContext.IDENTIFICATION().getSymbol();
        String typeOfError = "Function Call";
        String errorDescription = "Undeclared function call";

        ErrorPrintHelper.printError(startIndex, stopIndex, errorTokenStart,
            typeOfError, errorDescription);
    }

    /*
      Wrong number of arguments function errors
    */

    public static void reportWrongNumberOfArgument(
            AssignRhsContext assignRhsContext, int numberOfArguments,
            int numberOfArgumentTaken) {

    	reportError();

        int startIndex = assignRhsContext.getParent().getStart()
                .getStartIndex();
        int stopIndex = assignRhsContext.getStop().getStopIndex();
        Token errorTokenStart = assignRhsContext.IDENTIFICATION().getSymbol();
        String typeOfError = "Function Arguments Number";
        String errorDescription = "Wrong number of argument"
                + "\nExpected : " + numberOfArgumentTaken
                + "\nActual : " + numberOfArguments;

        ErrorPrintHelper.printError(startIndex, stopIndex, errorTokenStart,
            typeOfError, errorDescription);
    }

    /*
      Wrong types of arguments function errors
    */

    public static void reportWrongTypesOfArgument(
        ExpressionContext expressionContext, Type expected, Type actual) {

        String actualType = actual != null ?
            actual.toString() : "UNDEFINED TYPE";

        reportError();

        int startIndex = GeneralHelper.contextFromInitialContext(
            expressionContext, AssignRhsContext.class).getStart()
            .getStartIndex();
        int stopIndex = GeneralHelper.contextFromInitialContext(
            expressionContext, AssignRhsContext.class).getStop()
            .getStopIndex();
        String typeOfError = "Function Arguments Type";
        String errorDescription = ErrorPrintHelper
            .generateErrorMessageForTypeError(expected.toString(), actualType);

        ErrorPrintHelper.printError(startIndex, stopIndex, expressionContext,
            typeOfError, errorDescription);
    }

    /*
      Undeclared variable errors
    */

    public static void reportUndeclaredVariableUse(
            ExpressionContext expressionContext) {

    	reportError();

        int startIndex = GeneralHelper.contextFromInitialContext
                (expressionContext, StatementContext.class).getStart()
                .getStartIndex();
        int stopIndex = GeneralHelper.contextFromInitialContext
                (expressionContext, StatementContext.class).getStop()
                .getStopIndex();
        Token errorTokenStart = expressionContext.IDENTIFICATION().getSymbol();
        String typeOfError = "Variable Use";
        String errorDescription = "Undeclared variable use";

        ErrorPrintHelper.printError(startIndex, stopIndex, errorTokenStart,
            typeOfError, errorDescription);
    }

    /*
      Return position errors
    */

    public static void reportInvalidReturnStatementPosition (
            StatementContext statementContext ) {

    	reportError();

        int startIndex = statementContext.getStart().getStartIndex();
        int stopIndex = statementContext.getStop().getStopIndex();
        ParserRuleContext errorContext = statementContext;
        String typeOfError = "Return Position";
        String errorDescription = "Cannot return from main";

        ErrorPrintHelper.printError(startIndex, stopIndex, errorContext,
            typeOfError, errorDescription);
    }

    /*
      Return type errors
    */

    public static void reportInvalidReturnStatementType(
        StatementContext statementContext,
        Type typeOfFunction, Type typeOfExpression) {

        String actualType = typeOfExpression != null ?
                typeOfExpression.toString() : "UNDEFINED TYPE";

        reportError();

        int startIndex = statementContext.getStart().getStartIndex();
        int stopIndex = statementContext.getStop().getStopIndex();
        ParserRuleContext errorContext = statementContext.expression();
        String typeOfError = "Invalid Return Type";
        String errorDescription = ErrorPrintHelper
                .generateErrorMessageForTypeError(
                        typeOfFunction.toString(), actualType);

        ErrorPrintHelper.printError(startIndex, stopIndex, errorContext,
            typeOfError, errorDescription);
    }

    /*
      Exit errors
    */

    public static void reportWrongExitType(StatementContext statementContext) {

    	reportError();

        int startIndex = statementContext.getStart().getStartIndex();
        int stopIndex = statementContext.getStop().getStopIndex();
        ParserRuleContext errorContext = statementContext.expression();
        String typeOfError = "Invalid Exit";
        String errorDescription = ErrorPrintHelper
                .generateErrorMessageForTypeError("INT", TypeBuilder
                        .buildTypeFromExpression(statementContext.expression
                                ()).toString());

        ErrorPrintHelper.printError(startIndex, stopIndex, errorContext,
            typeOfError, errorDescription);
    }

    /*
      Free errors
    */

    public static void reportFreeWrongType(StatementContext statementContext) {

        Type actual = TypeBuilder.buildTypeFromExpression(
                statementContext.expression());

        String actualType = actual != null ?
                actual.toString() : "UNDEFINED TYPE";

    	reportError();

        int startIndex = statementContext.getStart().getStartIndex();
        int stopIndex = statementContext.getStop().getStopIndex();
        ParserRuleContext errorContext = statementContext.expression();
        String typeOfError = "Invalid Free";
        String errorDescription = ErrorPrintHelper
                .generateErrorMessageForTypeError(
                        "T[] or PAIR(T1, T2)", actualType);

        ErrorPrintHelper.printError(startIndex, stopIndex, errorContext,
            typeOfError, errorDescription);
    }

    /*
      Read errors
    */

    public static void reportReadWrongType(
        StatementContext statementContext, Type actual) {

    	reportError();
    	
        String actualType = actual != null ?
            actual.toString() : "UNDEFINED TYPE";

        int startIndex = statementContext.getStart().getStartIndex();
        int stopIndex = statementContext.getStop().getStopIndex();
        ParserRuleContext errorContext = statementContext.assignLhs();
        String typeOfError = "Invalid Read";
        String errorDescription = ErrorPrintHelper
            .generateErrorMessageForTypeError("INT or CHAR"
                , actualType);

        ErrorPrintHelper.printError(startIndex, stopIndex, errorContext,
            typeOfError, errorDescription);
    }

    /*
      Operator errors
    */

    public static void reportInvalidOperatorUse(
        ExpressionContext expressionContext) {

        reportError();

        int startIndex = GeneralHelper.contextFromInitialContext
                (expressionContext, StatementContext.class).getStart()
                .getStartIndex();
        int stopIndex = GeneralHelper.contextFromInitialContext
                (expressionContext, StatementContext.class).getStop()
                .getStopIndex();
        String typeOfError = "Invalid Operator Use";
        String errorDescription = "Wrong use of operator in the following " +
                "expression";
        
        ErrorPrintHelper.printError(startIndex, stopIndex, expressionContext,
                typeOfError, errorDescription);
    }

    /*
      Types errors
    */

    public static void reportArrayLiteralAssignError(
        Symbol variable, ArrayLiteralContext arrayLiteralContext) {

        reportError();

        StatementContext statementContext
                = GeneralHelper.contextFromInitialContext(
                        arrayLiteralContext, StatementContext.class);
        int startIndex = statementContext.getStart().getStartIndex();
        int stopIndex = statementContext.getStop().getStopIndex();
        ParserRuleContext errorContext = statementContext.assignRhs();
        String typeOfError = "Assignment Error";
        String errorDescription = ErrorPrintHelper
            .generateErrorMessageForTypeError(arrayLiteralContext.getText(),
                variable.getType().toString());

        ErrorPrintHelper.printError(startIndex, stopIndex, errorContext,
            typeOfError, errorDescription);
    }

    public static void reportArrayLiteralAssignError(
        Symbol variable, Type actual,
        ExpressionContext expressionContext) {

        String actualType = actual != null ?
            actual.toString() : "UNDEFINED TYPE";

        reportError();

        StatementContext statementContext
                = GeneralHelper.contextFromInitialContext(
                expressionContext, StatementContext.class);
        int startIndex = statementContext.getStart().getStartIndex();
        int stopIndex = statementContext.getStop().getStopIndex();
        ParserRuleContext errorContext = statementContext.assignRhs();
        String typeOfError = "Assignment Error";
        String errorDescription = ErrorPrintHelper
            .generateErrorMessageForTypeError(variable.getType().toString(),
                actualType);

        ErrorPrintHelper.printError(startIndex, stopIndex, errorContext,
            typeOfError, errorDescription);
    }

    public static void reportArrayLiteralAssignError(
        Symbol variable, Type expected, Type actual,
        ExpressionContext expressionContext) {

        String actualType = actual != null ?
                actual.toString() : "UNDEFINED TYPE";

        reportError();

        StatementContext statementContext
                = GeneralHelper.contextFromInitialContext(
                expressionContext, StatementContext.class);
        int startIndex = statementContext.getStart().getStartIndex();
        int stopIndex = statementContext.getStop().getStopIndex();
        ParserRuleContext errorContext = statementContext.assignRhs();
        String typeOfError = "Assignment Error";
        String errorDescription = ErrorPrintHelper
            .generateErrorMessageForTypeError(expected.toString(),
                actualType);

        ErrorPrintHelper.printError(startIndex, stopIndex, errorContext,
            typeOfError, errorDescription);
    }

    public static void reportExpressionAssignError(
        Symbol variable, Type actual, ExpressionContext expressionContext) {

        reportError();

        String actualType = actual != null ?
            actual.toString() : "UNDEFINED TYPE";

        StatementContext statementContext
                = GeneralHelper.contextFromInitialContext(
                expressionContext, StatementContext.class);
        int startIndex = statementContext.getStart().getStartIndex();
        int stopIndex = statementContext.getStop().getStopIndex();
        ParserRuleContext errorContext = statementContext.assignRhs();
        String typeOfError = "Assignment Error";
        String errorDescription = ErrorPrintHelper
            .generateErrorMessageForTypeError(variable.getType().toString(),
                actualType);

        ErrorPrintHelper.printError(startIndex, stopIndex, errorContext,
            typeOfError, errorDescription);
    }

    public static void reportFunctionAssignError(
        Symbol variable, String functionName, Type actual,
        AssignRhsContext assignRhsContext) {

        reportError();

        String actualType = actual != null ?
            actual.toString() : "UNDEFINED TYPE";

        StatementContext statementContext
                = GeneralHelper.contextFromInitialContext(
                assignRhsContext, StatementContext.class);
        int startIndex = statementContext.getStart().getStartIndex();
        int stopIndex = statementContext.getStop().getStopIndex();
        Token errorTokenStart = assignRhsContext.IDENTIFICATION().getSymbol();
        String typeOfError = "Assignment Error";
        String errorDescription = ErrorPrintHelper
            .generateErrorMessageForTypeError(variable.getType().toString(),
                actualType);

        ErrorPrintHelper.printError(startIndex, stopIndex, errorTokenStart,
            typeOfError, errorDescription);
    }

    public static void reportNewpairAssignError(
        Symbol variable, ExpressionContext leftExpressionContext,
        ExpressionContext rightExpressionContext) {

        reportError();

        Type actualLeft
            = TypeBuilder.buildTypeFromExpression(leftExpressionContext);
        Type actualRight
            = TypeBuilder.buildTypeFromExpression(rightExpressionContext);

        String actualLeftType = actualLeft != null ?
            actualLeft.toString() : "UNDEFINED TYPE";
        String actualRightType = actualRight != null ?
            actualRight.toString() : "UNDEFINED TYPE";

        StatementContext statementContext
                = GeneralHelper.contextFromInitialContext(
                leftExpressionContext, StatementContext.class);
        int startIndex = statementContext.getStart().getStartIndex();
        int stopIndex = statementContext.getStop().getStopIndex();
        Token errorTokenStart = leftExpressionContext.getStart();
        int startErrorIndex = leftExpressionContext.getStart().getStartIndex();
        int stopErrorIndex = rightExpressionContext.getStop().getStopIndex();
        String typeOfError = "Assignment Error";
        String errorDescription = ErrorPrintHelper
                .generateErrorMessageForTypeError(
                        variable.getType().toString(),
                        "(" + actualLeftType + ", " + actualRightType + ")");

        ErrorPrintHelper.printError(startIndex, stopIndex, errorTokenStart,
                startErrorIndex, stopErrorIndex, typeOfError, errorDescription);
    }

    public static void reportPairElementAssignError(
        Symbol variable, PairElementContext pairElementContext,
        WaccType expected) {

        reportError();

        Type actual = TypeBuilder.buildTypeFromExpression(
            pairElementContext.expression());

        String actualType = actual != null ?
            actual.toString() : "UNDEFINED TYPE";

        int startIndex = pairElementContext.getStart().getStartIndex();
        int stopIndex = pairElementContext.getStop().getStopIndex();
        ParserRuleContext errorContext = pairElementContext.expression();
        String typeOfError = "Assignment Error";
        String errorDescription = ErrorPrintHelper
            .generateErrorMessageForTypeError(actualType,
                expected.toString());

        ErrorPrintHelper.printError(startIndex, stopIndex, errorContext,
            typeOfError, errorDescription);
    }

    public static void reportPairElementAssignError(
        Symbol variable, PairElementContext pairElementContext,
        Type actual) {

        reportError();

        String actualType = actual != null ?
            actual.toString() : "UNDEFINED TYPE";

        int startIndex = pairElementContext.getStart().getStartIndex();
        int stopIndex = pairElementContext.getStop().getStopIndex();
        ParserRuleContext errorContext = pairElementContext.expression();
        String typeOfError = "Assignment Error";
        String errorDescription = ErrorPrintHelper
            .generateErrorMessageForTypeError(actualType,
                variable.getType().toString());

        ErrorPrintHelper.printError(startIndex, stopIndex, errorContext,
            typeOfError, errorDescription);
    }

    /*
      Helper for reporting Errors
    */

    private static void reportError() {
        if (semanticErrorCounter == 0) {
            System.err.println();
            System.err.println("Semantic errors detected during compilation!" +
                " Error code 200 returned.");
        }
        semanticErrorCounter++;
    }
    
}











