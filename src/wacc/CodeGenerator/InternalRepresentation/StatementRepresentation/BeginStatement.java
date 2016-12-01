package wacc.CodeGenerator.InternalRepresentation.StatementRepresentation;

import wacc.CodeGenerator.CodeGenerationTools.ScopeOffsetGenerator;
import wacc.CodeGenerator.Commands.Command;
import wacc.CodeGenerator.HardwareManager.HardwareManager;
import wacc.CodeGenerator.InternalRepresentation.RepresentationFormatter;

import java.util.ArrayList;
import java.util.List;

public class BeginStatement implements Statement {

    private Statement statement;

    public BeginStatement(Statement statement) {
        this.statement = statement;
    }

    @Override
    public List<Command> generateCommandsForStatement() {
        HardwareManager hardwareManager = HardwareManager.getInstance();

        List<Command> commands = new ArrayList<>();

        // Moving to next scope and retrieving totalOffSet for new scope
        hardwareManager.moveToNextStackScope();
        int totalOffset = hardwareManager.getTotalOffsetForCurrentScope();

        // Move the stack pointer depending on total offSet
        // (variable initialization) if not equal to zero
        List<Command> stackSubtraction =
            ScopeOffsetGenerator.initializeScopeOffset(totalOffset);
        commands.addAll(stackSubtraction);

        // Generate command for the statement
        List<Command> statementCommands =
            statement.generateCommandsForStatement();
        commands.addAll(statementCommands);

        // Reset stack pointer to its original value if totalOffset is not zero
        List<Command> stackAddition =
            ScopeOffsetGenerator.returnScopeOffset(totalOffset);
        commands.addAll(stackAddition);

        // Leave the begin statement scope
        hardwareManager.moveToNextStackScope();

        return commands;
    }

    /*
      Representation for debugging
    */

    @Override
    public String generateStatementRepresentation(int depthCount) {

        // Depth representation in spaces
        String depth
            = RepresentationFormatter.getDepthRepresentation(depthCount);

        return RepresentationFormatter.generateRepresentation(
            depth,
            "BeginStatement",
            statement.generateStatementRepresentation(
                depthCount + RepresentationFormatter.INDENT_ONCE)
        );
    }

}
