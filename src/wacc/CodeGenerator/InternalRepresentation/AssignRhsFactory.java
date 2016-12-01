package wacc.CodeGenerator.InternalRepresentation;

import antlr.WaccParser.ArrayLiteralContext;
import antlr.WaccParser.AssignRhsContext;
import antlr.WaccParser.ExpressionContext;
import antlr.WaccParser.PairElementContext;
import wacc.CodeGenerator.InternalRepresentation.AssignRepresentation.*;
import wacc.CodeGenerator.InternalRepresentation.ExpressionRepresentation.Expression;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AssignRhsFactory {

    private static final int FIRST_EXPRESSION = 0;
    private static final int SECOND_EXPRESSION = 1;

    public static AssignRhs buildAssignRhsFromAssignRhsContext(
        AssignRhsContext assignRhsContext) {

        if (assignRhsContext.NEWPAIR() != null) {
            // Pair assignment
            return buildNewpairAssignment(assignRhsContext);

        } else if (assignRhsContext.pairElement() != null) {
            // PairElem assignment
            return buildPairElemAssignment(assignRhsContext);

        } else if (assignRhsContext.arrayLiteral() != null) {
            // ArrayLiteral assignment
            return buildArrayLiteralAssignment(assignRhsContext);

        } else if (assignRhsContext.CALL() != null) {
            // Function assignment
            return buildFunctionAssignment(assignRhsContext);

        } else if (assignRhsContext.expression() != null) {
            // Expression assignment
            return buildExpressionAssignment(assignRhsContext);
        }


        return null;
    }

    private static NewpairAssignment buildNewpairAssignment(
        AssignRhsContext assignRhsContext) {

        ExpressionContext leftExpressionContext
            = assignRhsContext.expression(FIRST_EXPRESSION);
        ExpressionContext rightExpressionContext
            = assignRhsContext.expression(SECOND_EXPRESSION);

        Expression leftExpression = ExpressionFactory
            .buildExpressionFromExpressionContext(leftExpressionContext);
        Expression rightExpression = ExpressionFactory
            .buildExpressionFromExpressionContext(rightExpressionContext);

        return new NewpairAssignment(leftExpression, rightExpression);
    }

    private static PairElemAssignment buildPairElemAssignment(
        AssignRhsContext assignRhsContext) {

        PairElementContext pairElementContext = assignRhsContext.pairElement();

        ExpressionContext expressionContext
            = pairElementContext.expression();

        PairElemAssignment.PairElemPosition pairElemPosition
            = pairElementContext.FIRST() != null ?
                PairElemAssignment.PairElemPosition.FIRST :
                PairElemAssignment.PairElemPosition.SECOND;

        Expression expression = ExpressionFactory
            .buildExpressionFromExpressionContext(expressionContext);

        return new PairElemAssignment(pairElemPosition, expression);
    }

    private static AssignRhs buildExpressionAssignment(
        AssignRhsContext assignRhsContext) {

        ExpressionContext expressionContext
            = assignRhsContext.expression(FIRST_EXPRESSION);

        Expression expression = ExpressionFactory
            .buildExpressionFromExpressionContext(expressionContext);

        return (AssignRhs) expression;
    }

    private static ArrayLiteralAssignment buildArrayLiteralAssignment(
        AssignRhsContext assignRhsContext) {

        ArrayLiteralContext arrayLiteralContext
            = assignRhsContext.arrayLiteral();

        List<ExpressionContext> arrayElementContextList
            = arrayLiteralContext.expression();

        List<Expression> arrayElementList
            = arrayElementContextList
                .stream()
                .map(ExpressionFactory::buildExpressionFromExpressionContext)
                .collect(Collectors.toList());

        return new ArrayLiteralAssignment(arrayElementList);
    }

    private static FunctionAssignment buildFunctionAssignment(
        AssignRhsContext assignRhsContext) {

        String functionIdentifier = assignRhsContext.IDENTIFICATION().getText();
        List<Expression> functionArgumentList;

        if (assignRhsContext.argumentList() != null) {

            List<ExpressionContext> functionArgumentContextList
                = assignRhsContext.argumentList().expression();

            functionArgumentList
                = functionArgumentContextList
                    .stream()
                    .map(ExpressionFactory::
                        buildExpressionFromExpressionContext)
                    .collect(Collectors.toList());

        } else {
            functionArgumentList = new ArrayList<>();
        }

        return new FunctionAssignment(functionIdentifier, functionArgumentList);
    }
}
