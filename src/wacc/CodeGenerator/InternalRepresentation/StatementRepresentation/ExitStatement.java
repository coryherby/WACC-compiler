package wacc.CodeGenerator.InternalRepresentation.StatementRepresentation;

import wacc.CodeGenerator.Commands.BranchingCommand.BlCommand;
import wacc.CodeGenerator.Commands.Command;
import wacc.CodeGenerator.Commands.ConditionCode;
import wacc.CodeGenerator.Commands.DataMovementCommand.MovCommand;
import wacc.CodeGenerator.HardwareManager.HardwareManager;
import wacc.CodeGenerator.HardwareManager.NormalRegister;
import wacc.CodeGenerator.InternalRepresentation.ExpressionRepresentation.Expression;
import wacc.CodeGenerator.InternalRepresentation.RepresentationFormatter;

import java.util.List;

public class ExitStatement implements Statement {

    private final String EXIT_FUNCTION = "exit";
    private Expression expression;

    public ExitStatement(Expression expression) {
        this.expression = expression;
    }

    @Override
    public List<Command> generateCommandsForStatement() {
        List<Command> commands = expression.generateCommandsForExpression();

        HardwareManager hardwareManager = HardwareManager.getInstance();
        NormalRegister register = hardwareManager.getStorageRegister();
        hardwareManager.freeRegister(register);

        NormalRegister R0 = hardwareManager.getRegister0();

        // load the exit code in R0
        MovCommand movRegisterToR0
                = new MovCommand(ConditionCode.AL,R0,register);

        // call exit label
        BlCommand callsExit = new BlCommand(ConditionCode.AL, EXIT_FUNCTION);

        // add all command into the list of commands
        commands.add(movRegisterToR0);
        commands.add(callsExit);

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
            "ExitStatement",
            expression.generateExpressionRepresentation(
                depthCount + RepresentationFormatter.INDENT_ONCE)
        );
    }

}
