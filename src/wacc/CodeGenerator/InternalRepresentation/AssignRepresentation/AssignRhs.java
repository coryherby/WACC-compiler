package wacc.CodeGenerator.InternalRepresentation.AssignRepresentation;

import wacc.CodeGenerator.Commands.Command;

import java.util.List;

public interface AssignRhs {

    // Generate Commands for the assignRhs
    List<Command> generateCommandsForAssignRhs();

    // Generate String representation of the expression
    String generateAssignRhsRepresentation(int depthCount);
}
