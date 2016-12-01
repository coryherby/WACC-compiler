package wacc.CodeGenerator.InternalRepresentation.AssignRepresentation;

import wacc.CodeGenerator.CodeGenerationTools.ScopeOffsetGenerator;
import wacc.CodeGenerator.Commands.BranchingCommand.BlCommand;
import wacc.CodeGenerator.Commands.Command;
import wacc.CodeGenerator.Commands.ConditionCode;
import wacc.CodeGenerator.Commands.DataMovementCommand.MovCommand;
import wacc.CodeGenerator.Commands.DataTransferCommand.DataSize;
import wacc.CodeGenerator.Commands.DataTransferCommand.StrCommand;
import wacc.CodeGenerator.HardwareManager.HardwareManager;
import wacc.CodeGenerator.HardwareManager.NormalRegister;
import wacc.CodeGenerator.HardwareManager.SpecialRegister;
import wacc.CodeGenerator.InternalRepresentation.ExpressionRepresentation.Expression;
import wacc.CodeGenerator.InternalRepresentation.RepresentationFormatter;
import wacc.WaccTable.FunctionTable;
import wacc.WaccTable.MainWaccTable;

import java.util.ArrayList;
import java.util.List;

public class FunctionAssignment implements AssignRhs {

    private String functionIdentifier;
    private List<Expression> arguments;

    public FunctionAssignment(String functionIdentifier,
                              List<Expression> arguments) {

        this.functionIdentifier = functionIdentifier;
        this.arguments = arguments;
    }

    @Override
    public List<Command> generateCommandsForAssignRhs() {

        HardwareManager hardwareManager = HardwareManager.getInstance();
        FunctionTable functionTable = MainWaccTable.getInstance()
            .getFunctionTable();
        SpecialRegister spRegister = SpecialRegister.SP;

        List<Command> commands = new ArrayList<>();

        int totalOffsetSize = 0;
        List<Integer> sizeParameter = functionTable
            .getParameterOffset(functionIdentifier);

        for (int i = arguments.size() - 1; i >= 0; i--) {

            int currentParameterSize = sizeParameter.get(i);
            String currentParameter = "-"
                + String.valueOf(currentParameterSize);
            totalOffsetSize += currentParameterSize;

            // Generate commands for argument
            List<Command> argumentCommands =
                arguments.get(i).generateCommandsForExpression();
            commands.addAll(argumentCommands);

            // Get register to use
            NormalRegister usedRegister = hardwareManager.getStorageRegister();

            // Add argument in the stack
            if (currentParameterSize == MainWaccTable.STACK_SIZE_1) {
                StrCommand strArgument =
                    new StrCommand(ConditionCode.AL, DataSize.B, usedRegister,
                        spRegister, currentParameter, true, true);
                commands.add(strArgument);
            } else if (currentParameterSize == MainWaccTable.STACK_SIZE_4) {
                StrCommand strArgument =
                    new StrCommand(ConditionCode.AL, DataSize.W, usedRegister,
                        spRegister, currentParameter, true, true);
                commands.add(strArgument);
            }

            // Free used register
            hardwareManager.freeRegister(usedRegister);

            // Add temporary offset
            hardwareManager.addToTemporaryOffset(currentParameterSize);
        }

        // Jump command to function Identifier
        Command jumpFunction = new BlCommand(
            ConditionCode.AL, "f_" + functionIdentifier);
        commands.add(jumpFunction);

        // Reset stack pointer to its original value if totalOffset is not zero
        List<Command> stackAddition =
            ScopeOffsetGenerator.returnScopeOffset(totalOffsetSize);
        commands.addAll(stackAddition);

        // Reset temporary offset
        hardwareManager.resetTemporaryOffset();

        // Mov command to retrieve function return statement
        NormalRegister usedRegister = hardwareManager.getFreeRegister();
        MovCommand movCommand = new MovCommand(
            ConditionCode.AL, usedRegister, hardwareManager.getRegister0());
        commands.add(movCommand);

        hardwareManager.addStorageRegister(usedRegister);

        return commands;
    }

    /*
      Representation for debugging
    */

    @Override
    public String generateAssignRhsRepresentation(int depthCount) {

        // Depth representation in spaces
        String depth
            = RepresentationFormatter.getDepthRepresentation(depthCount);
        String indentDepth = depth + RepresentationFormatter.DEPTH_UNIT;

        StringBuilder argumentsRepresentation = new StringBuilder();

        for (Expression argument : arguments) {
            argumentsRepresentation.append(
                argument.generateExpressionRepresentation(
                    depthCount + RepresentationFormatter.INDENT_TWICE));
            argumentsRepresentation.append("\n");
        }

        return RepresentationFormatter.generateRepresentation(
            depth,
            "FunctionAssignment",
            new String[]{
                "Function Identifier",
                "Function Arguments"},
            new String[]{
                indentDepth + functionIdentifier,
                argumentsRepresentation.toString()}
        );
    }

}
