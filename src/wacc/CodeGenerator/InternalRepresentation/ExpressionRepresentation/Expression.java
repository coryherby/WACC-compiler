package wacc.CodeGenerator.InternalRepresentation.ExpressionRepresentation;

import wacc.CodeGenerator.Commands.Command;

import java.util.List;

public interface Expression {

    // Generate Commands for the expression
    List<Command> generateCommandsForExpression();

    // Generate String representation of the expression
    String generateExpressionRepresentation(int depthCount);

}
