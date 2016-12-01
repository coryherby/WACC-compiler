package wacc.CodeGenerator.InternalRepresentation.StatementRepresentation;

import wacc.CodeGenerator.Commands.Command;

import java.util.List;

public interface Statement {

    // Generate Commands for the statement
    List<Command> generateCommandsForStatement();

    // Generate String representation of the statement
    String generateStatementRepresentation(int depth);

}
