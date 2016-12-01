package wacc.SemanticErrorDetector.HelperSemantic.HelpersStatementSemantic;

import antlr.WaccParser;
import antlr.WaccParser.ExpressionContext;
import antlr.WaccParser.StatementContext;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import wacc.SemanticErrorDetector.ErrorManager.ErrorReporter;
import wacc.SemanticErrorDetector.HelperSemantic
    .Analyser.ExpressionSemanticAnalyser.BoolExpressionAnalyser;
import wacc.SemanticErrorDetector.HelperSemantic.Analyser.ExpressionAnalysers.ExpressionAnalyser;

import java.util.List;

public class ConditionStatementHelper {

    public static boolean isInWhileLoop = false;

    public static void checkConditionIfIsBool(
        StatementContext statementContext) {

        if(!BoolExpressionAnalyser
            .isBoolExpression(statementContext.expression())) {

            ErrorReporter.reportIfConditionError(statementContext);
        }
    }

    public static void checkConditionWhileIsBool(
        StatementContext statementContext) {

        if(!BoolExpressionAnalyser
            .isBoolExpression(statementContext.expression())) {

            ErrorReporter.reportWhileConditionError(statementContext);
        }
    }

    /*
      WaccTypes identification functions
    */

    public static boolean isIfStatement(StatementContext statementContext) {

        return statementContext.IF() != null;
    }

    public static boolean isWhileStatement(StatementContext statementContext) {

        return statementContext.WHILE() != null;
    }

    public static StatementContext controlFlowAnalysisForIf(
        StatementContext statementContext) {

        ExpressionContext expression = statementContext.expression();
        StatementContext resultStatement =
            new StatementContext(statementContext.getParent(), 0);

        if(ExpressionAnalyser.alwaysEvaluateToFalse(expression)) {

            resultStatement.addChild(new CommonToken(WaccParser.BEGIN));
            resultStatement.addChild(statementContext.statement(1));
            statementContext.statement(1).parent = resultStatement;
            resultStatement.addChild(new CommonToken(WaccParser.END));

            return replaceStatementContext(statementContext, resultStatement);
        } else if(ExpressionAnalyser.alwaysEvaluateToTrue(expression)) {

            resultStatement.addChild(new CommonToken(WaccParser.BEGIN));
            resultStatement.addChild(statementContext.statement(0));
            statementContext.statement(0).parent = resultStatement;
            resultStatement.addChild(new CommonToken(WaccParser.END));

            return replaceStatementContext(statementContext, resultStatement);
        }
        return statementContext;
    }

    public static StatementContext controlFlowAnalysisForWhile(
        StatementContext statementContext) {

        ExpressionContext expression =
            statementContext.expression();

        if(ExpressionAnalyser.alwaysEvaluateToFalse(expression)) {
            StatementContext resultStatement =
                new StatementContext(statementContext.getParent(), 0);
            resultStatement.addChild(new CommonToken(WaccParser.SKIP));
            resultStatement.addChild(statementContext.statement(0));

            return replaceStatementContext(statementContext, resultStatement);
        }
        return statementContext;
    }

    private static StatementContext replaceStatementContext(
        StatementContext statementContext, StatementContext newStatement) {

        List<ParseTree> parentChildren = statementContext.getParent().children;

        int index = parentChildren.indexOf(statementContext);
        parentChildren.remove(index);
        newStatement.parent = statementContext.getParent();
        parentChildren.add(index, newStatement);

        return newStatement;
    }
}