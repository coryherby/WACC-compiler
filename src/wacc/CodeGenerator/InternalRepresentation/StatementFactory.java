package wacc.CodeGenerator.InternalRepresentation;

import antlr.WaccParser.StatementContext;
import wacc.CodeGenerator.InternalRepresentation.AssignRepresentation.AssignLhs;
import wacc.CodeGenerator.InternalRepresentation.AssignRepresentation.AssignRhs;
import wacc.CodeGenerator.InternalRepresentation.ExpressionRepresentation.Expression;
import wacc.CodeGenerator.InternalRepresentation.StatementRepresentation.*;
import wacc.SemanticErrorDetector.WaccTypes.Type;
import wacc.SemanticErrorDetector.WaccTypes.TypeBuilder;

public class StatementFactory {

    private static final int FIRST_STATEMENT = 0;
    private static final int SECOND_STATEMENT = 1;

    public static Statement buildStatementFromStatementContext(
        StatementContext statementContext) {

        if (statementContext.SKIP() != null) {
            // Skip statement
            return buildSkipStatement(statementContext);

        } else if (statementContext.type() != null) {
            // Initialisation statement
            return buildInitialisationStatement(statementContext);

        } else if (statementContext.READ() != null) {
            // Read statement
            return buildReadStatement(statementContext);

        } else if (statementContext.assignLhs() != null) {
            // Assignement statement
            return buildAssignmentStatement(statementContext);

        } else if (statementContext.FREE() != null) {
            // Free statement
            return buildFreeStatement(statementContext);

        } else if (statementContext.RETURN() != null) {
            // Return statement
            return buildReturnStatement(statementContext);

        } else if (statementContext.EXIT() != null) {
            // Exit statement
            return buildExitStatement(statementContext);

        } else if (statementContext.PRINT() != null) {
            // Print statement
            return buildPrintStatement(statementContext);

        } else if (statementContext.PRINTLN() != null) {
            // PrintLn statement
            return buildPrintLnStatement(statementContext);

        } else if (statementContext.IF() != null) {
            // If statement
            return buildIfStatement(statementContext);

        } else if (statementContext.WHILE() != null) {
            // While statement
            return buildWhileStatement(statementContext);

        } else if (statementContext.BEGIN() != null) {
            // New scope statement
            return buildBeginStatement(statementContext);

        } else if (statementContext.SEMI_COLON() != null) {
            // Two statements in a row
            return buildCommaStatement(statementContext);
        }

        return null;
    }

    private static SkipStatement buildSkipStatement(
        StatementContext statementContext) {

        return new SkipStatement();
    }

    private static InitialisationStatement buildInitialisationStatement(
        StatementContext statementContext) {

        Type variableType
            = TypeBuilder.buildTypeFromAntlr(statementContext.type());
        String identifier = statementContext.IDENTIFICATION().getText();
        AssignRhs assignment = AssignRhsFactory
            .buildAssignRhsFromAssignRhsContext(statementContext.assignRhs());

        return new InitialisationStatement(
            variableType, identifier, assignment);
    }

    private static AssignmentStatement buildAssignmentStatement(
        StatementContext statementContext) {

        AssignLhs variable = AssignLhsFactory
            .buildAssignLhsFromAssignLhsContext(statementContext.assignLhs());
        AssignRhs assignment = AssignRhsFactory
            .buildAssignRhsFromAssignRhsContext(statementContext.assignRhs());

        return new AssignmentStatement(variable, assignment);
    }

    private static ReadStatement buildReadStatement(
        StatementContext statementContext) {

        AssignLhs readVariable = AssignLhsFactory
            .buildAssignLhsFromAssignLhsContext(statementContext.assignLhs());

        return new ReadStatement(readVariable);
    }

    private static FreeStatement buildFreeStatement(
        StatementContext statementContext) {

        Expression expression
            = ExpressionFactory.buildExpressionFromExpressionContext(
                statementContext.expression());

        return new FreeStatement(expression);
    }

    private static ReturnStatement buildReturnStatement(
        StatementContext statementContext) {

        Expression expression
            = ExpressionFactory.buildExpressionFromExpressionContext(
                statementContext.expression());

        return new ReturnStatement(expression);
    }

    private static ExitStatement buildExitStatement(
        StatementContext statementContext) {

        Expression expression
            = ExpressionFactory.buildExpressionFromExpressionContext(
                statementContext.expression());

        return new ExitStatement(expression);
    }

    private static PrintStatement buildPrintStatement(
        StatementContext statementContext) {

        Expression expression
            = ExpressionFactory.buildExpressionFromExpressionContext(
                statementContext.expression());

        return new PrintStatement(expression);
    }

    private static PrintLnStatement buildPrintLnStatement(
        StatementContext statementContext) {

        Expression expression
            = ExpressionFactory.buildExpressionFromExpressionContext(
                statementContext.expression());

        return new PrintLnStatement(expression);
    }

    private static IfStatement buildIfStatement(
        StatementContext statementContext) {

        StatementContext thenStatementContext
            = statementContext.statement(FIRST_STATEMENT);
        StatementContext elseStatementContext
            = statementContext.statement(SECOND_STATEMENT);

        Expression ifCondition
            = ExpressionFactory.buildExpressionFromExpressionContext(
                statementContext.expression());

        Statement thenStatement = StatementFactory
            .buildStatementFromStatementContext(thenStatementContext);
        Statement elseStatement = StatementFactory
            .buildStatementFromStatementContext(elseStatementContext);

        return new IfStatement(ifCondition, thenStatement, elseStatement);
    }

    private static WhileStatement buildWhileStatement(
        StatementContext statementContext) {

        StatementContext whileStatementContext
            = statementContext.statement(FIRST_STATEMENT);

        Expression whileCondition
            = ExpressionFactory.buildExpressionFromExpressionContext(
                statementContext.expression());

        Statement whileStatement = StatementFactory
            .buildStatementFromStatementContext(whileStatementContext);

        return new WhileStatement(whileCondition, whileStatement);
    }

    private static BeginStatement buildBeginStatement(
        StatementContext statementContext) {

        StatementContext beginStatementContext
            = statementContext.statement(FIRST_STATEMENT);

        Statement beginStatement = StatementFactory
            .buildStatementFromStatementContext(beginStatementContext);

        return new BeginStatement(beginStatement);
    }

    private static CommaStatement buildCommaStatement(
        StatementContext statementContext) {

        StatementContext firstStatementContext
            = statementContext.statement(FIRST_STATEMENT);
        StatementContext secondStatementContext
            = statementContext.statement(SECOND_STATEMENT);

        Statement firstStatement = StatementFactory
            .buildStatementFromStatementContext(firstStatementContext);
        Statement secondStatement = StatementFactory
            .buildStatementFromStatementContext(secondStatementContext);

        return new CommaStatement(firstStatement, secondStatement);
    }
}
