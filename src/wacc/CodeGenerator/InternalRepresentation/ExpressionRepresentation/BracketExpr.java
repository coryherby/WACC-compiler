package wacc.CodeGenerator.InternalRepresentation.ExpressionRepresentation;

import wacc.CodeGenerator.Commands.Command;
import wacc.CodeGenerator.InternalRepresentation.AssignRepresentation.AssignRhs;
import wacc.CodeGenerator.InternalRepresentation.RepresentationFormatter;

import java.util.List;

public class BracketExpr implements Expression, AssignRhs {

    private Expression insideBracketExpression;

    public BracketExpr(Expression insideBracketExpression) {
        this.insideBracketExpression = insideBracketExpression;
    }

    @Override
    public List<Command> generateCommandsForExpression() {
        return insideBracketExpression.generateCommandsForExpression();
    }

    @Override
    public List<Command> generateCommandsForAssignRhs() {
        return insideBracketExpression.generateCommandsForExpression();
    }

    public Expression getInsideBracketExpression() {
        return insideBracketExpression;
    }

    /*
      Representation for debugging
    */

    @Override
    public String generateAssignRhsRepresentation(int depthCount) {
        return generateRepresentation(depthCount, "Bracket Assignment");
    }

    @Override
    public String generateExpressionRepresentation(int depthCount) {
        return generateRepresentation(depthCount, "Bracket Expression");
    }

    private String generateRepresentation(int depthCount, String mainName) {

        // Depth representation in spaces
        String depth
            = RepresentationFormatter.getDepthRepresentation(depthCount);

        return RepresentationFormatter.generateRepresentation(
            depth,
            mainName,
            insideBracketExpression
                .generateExpressionRepresentation(depthCount + 1)
        );
    }

}
