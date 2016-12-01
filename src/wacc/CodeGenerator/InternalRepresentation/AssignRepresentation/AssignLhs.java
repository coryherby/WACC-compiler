package wacc.CodeGenerator.InternalRepresentation.AssignRepresentation;

import wacc.CodeGenerator.Commands.Command;

import java.util.List;

public interface AssignLhs {

    // Generate Commands for the assignLhs
    List<Command> generateCommandsForAssignLhs();

    // Get the identifier of the variable assigned to.
    String getIdentifier();

    // Generate String representation of the expression
    String generateLhsRepresentation(int depthCount);
}
