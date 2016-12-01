package wacc.SemanticErrorDetector.HelperSemantic.Analyser.AssignSemanticAnalyser;

import antlr.WaccParser.ArrayLiteralContext;
import antlr.WaccParser.AssignRhsContext;
import antlr.WaccParser.ExpressionContext;
import antlr.WaccParser.PairElementContext;
import wacc.WaccTable.Symbol;

public class AssignementAnalyser {

    public static void checkAssignType(
        Symbol variable, AssignRhsContext assignRhsContext) {

        // Nothing to check
        if (variable == null) {
            return;
        }

        if (NewpairAssignAnalyser.isNewpair(assignRhsContext)) {

            ExpressionContext leftExpression = assignRhsContext.expression(0);
            ExpressionContext rightExpression = assignRhsContext.expression(1);

            NewpairAssignAnalyser.checkAssignement(
                variable, leftExpression, rightExpression);

        } else if (
            ExpressionAssignAnalyser.isExpressionContext(assignRhsContext)) {

            ExpressionContext expression = assignRhsContext.expression(0);
            ExpressionAssignAnalyser.checkAssignement(variable, expression);

        } else if (
            ArrayLiteralAssignAnalyser.isArrayLiteral(assignRhsContext)) {

            ArrayLiteralContext arrayLiteral = assignRhsContext.arrayLiteral();
            ArrayLiteralAssignAnalyser.checkAssignement(variable, arrayLiteral);

        } else if (PairElementAssignAnalyser.isPairElement(assignRhsContext)) {

            PairElementContext pairElement = assignRhsContext.pairElement();
            PairElementAssignAnalyser.checkAssignement(variable, pairElement);

        } else if (FunctionAssignAnalyser.isFunction(assignRhsContext)) {

            String functionName = assignRhsContext.IDENTIFICATION().toString();
            FunctionAssignAnalyser.checkAssignement(
                variable, functionName, assignRhsContext);
        }
    }

}
