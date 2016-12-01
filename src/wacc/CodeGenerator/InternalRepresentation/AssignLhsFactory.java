package wacc.CodeGenerator.InternalRepresentation;

import antlr.WaccParser.ArrayElementContext;
import antlr.WaccParser.AssignLhsContext;
import antlr.WaccParser.ExpressionContext;
import antlr.WaccParser.PairElementContext;
import wacc.CodeGenerator.InternalRepresentation.AssignRepresentation.AssignLhs;
import wacc.CodeGenerator.InternalRepresentation.AssignRepresentation.PairElemAssignment;
import wacc.CodeGenerator.InternalRepresentation.ExpressionRepresentation.ArrayElem;
import wacc.CodeGenerator.InternalRepresentation.ExpressionRepresentation.Expression;
import wacc.CodeGenerator.InternalRepresentation.ExpressionRepresentation.Identification;

import java.util.List;
import java.util.stream.Collectors;

public class AssignLhsFactory {

    public static AssignLhs buildAssignLhsFromAssignLhsContext(
        AssignLhsContext assignLhsContext) {

        if (assignLhsContext.IDENTIFICATION() != null) {
            // Identification assignment
            return buildIdentification(assignLhsContext);

        } else if (assignLhsContext.arrayElement() != null) {
            // ArrayElement assignment
            return buildArrayElem(assignLhsContext);

        } else if (assignLhsContext.pairElement() != null) {
            // PairElem assignment
            return buildPairElemAssignment(assignLhsContext);
        }

        return null;
    }

    private static AssignLhs buildIdentification(
        AssignLhsContext assignLhsContext) {

        String identifier = assignLhsContext.IDENTIFICATION().getText();

        return new Identification(identifier);
    }

    private static AssignLhs buildArrayElem(
        AssignLhsContext assignLhsContext) {

        ArrayElementContext arrayElementContext
            = assignLhsContext.arrayElement();

        String identifier = arrayElementContext.IDENTIFICATION().getText();

        List<Expression> arrayAccesses
            = arrayElementContext
                .expression()
                .stream()
                .map(ExpressionFactory::buildExpressionFromExpressionContext)
                .collect(Collectors.toList());

        return new ArrayElem(identifier, arrayAccesses);
    }

    private static AssignLhs buildPairElemAssignment(
        AssignLhsContext assignLhsContext) {

        PairElementContext pairElementContext = assignLhsContext.pairElement();
        ExpressionContext expressionContext = pairElementContext.expression();

        PairElemAssignment.PairElemPosition pairElemPosition
            = pairElementContext.FIRST() != null ?
                PairElemAssignment.PairElemPosition.FIRST :
                PairElemAssignment.PairElemPosition.SECOND;

        Expression expression = ExpressionFactory
            .buildExpressionFromExpressionContext(expressionContext);

        return new PairElemAssignment(pairElemPosition, expression);
    }
}
