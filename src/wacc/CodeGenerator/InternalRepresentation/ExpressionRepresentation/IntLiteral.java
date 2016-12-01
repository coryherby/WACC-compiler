package wacc.CodeGenerator.InternalRepresentation.ExpressionRepresentation;

import wacc.CodeGenerator.Commands.Command;
import wacc.CodeGenerator.Commands.ConditionCode;
import wacc.CodeGenerator.Commands.DataTransferCommand.DataSize;
import wacc.CodeGenerator.Commands.DataTransferCommand.LdrCommand;
import wacc.CodeGenerator.HardwareManager.HardwareManager;
import wacc.CodeGenerator.HardwareManager.NormalRegister;
import wacc.CodeGenerator.InternalRepresentation.AssignRepresentation.AssignRhs;
import wacc.CodeGenerator.InternalRepresentation.RepresentationFormatter;

import java.util.ArrayList;
import java.util.List;

public class IntLiteral implements Expression, AssignRhs {

    private int intLiteral;

    public IntLiteral(int intLiteral) {
        this.intLiteral = intLiteral;
    }

    @Override
    public List<Command> generateCommandsForExpression() {

        List<Command> commands = new ArrayList<>();

        HardwareManager hardwareManager = HardwareManager.getInstance();

        NormalRegister register = hardwareManager.getFreeRegister();
        hardwareManager.addStorageRegister(register);

        ConditionCode al = ConditionCode.AL;
        DataSize w = DataSize.W;
        String valueOfInt = String.valueOf(intLiteral);
        Command loadInt = new LdrCommand(al, w, register, valueOfInt);

        commands.add(loadInt);

        return commands;
    }

    @Override
    public List<Command> generateCommandsForAssignRhs() {
        return generateCommandsForExpression();
    }

    /*
      Representation for debugging
    */

    @Override
    public String generateAssignRhsRepresentation(int depthCount) {
        return generateRepresentation(depthCount, "IntLiteral Assignment");
    }

    @Override
    public String generateExpressionRepresentation(int depthCount) {
        return generateRepresentation(depthCount, "IntLiteral Expression");
    }

    private String generateRepresentation(int depthCount, String mainName) {

        // Depth representation in spaces
        String depth
            = RepresentationFormatter.getDepthRepresentation(depthCount);
        String indentDepth = depth + RepresentationFormatter.DEPTH_UNIT;

        return RepresentationFormatter.generateRepresentation(
            depth,
            mainName,
            indentDepth + String.valueOf(intLiteral)
        );
    }
}
