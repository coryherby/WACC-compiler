package wacc.CodeGenerator.InternalRepresentation.StatementRepresentation;

import wacc.CodeGenerator.CodeGenerationTools.PredefinedFunctionGenerator;
import wacc.CodeGenerator.CodeGenerationTools.PredefinedFunctions.PredefinedFunctionLabels;
import wacc.CodeGenerator.Commands.BranchingCommand.BlCommand;
import wacc.CodeGenerator.Commands.Command;
import wacc.CodeGenerator.Commands.ConditionCode;
import wacc.CodeGenerator.Commands.DataMovementCommand.MovCommand;
import wacc.CodeGenerator.HardwareManager.HardwareManager;
import wacc.CodeGenerator.HardwareManager.NormalRegister;
import wacc.CodeGenerator.InternalRepresentation.ExpressionRepresentation.Expression;
import wacc.CodeGenerator.InternalRepresentation.RepresentationFormatter;

import java.util.List;

public class FreeStatement implements Statement {

    private Expression expression;

    public FreeStatement(Expression expression) {
        this.expression = expression;
    }

    @Override
    public List<Command> generateCommandsForStatement() {

        HardwareManager hardwareManager = HardwareManager.getInstance();

        //List<Command> commands = new ArrayList<>();
        hardwareManager.setMemoryAddressBeingFreed(true);
        List<Command> commands = expression.generateCommandsForExpression();
        
        NormalRegister register = hardwareManager.getStorageRegister();
        hardwareManager.freeRegister(register);
        NormalRegister R0 = hardwareManager.getRegister0();

        // mov the expression register into R0;
        MovCommand movRegR0;
        movRegR0 = new MovCommand(ConditionCode.AL, R0, register);

        commands.add(movRegR0);

        // calls p_free_pair
        BlCommand freePair = new BlCommand(ConditionCode.AL,
            PredefinedFunctionLabels.FREE_PAIR.toString());
        commands.add(freePair);

        PredefinedFunctionGenerator codeGenHelper
                = PredefinedFunctionGenerator.getInstance();

        codeGenHelper.addPredefinedFunction(PredefinedFunctionLabels.FREE_PAIR);

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
            "FreeStatement",
            expression.generateExpressionRepresentation(
                depthCount + RepresentationFormatter.INDENT_ONCE)
        );
    }

}
