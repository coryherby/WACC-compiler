package wacc.CodeGenerator.InternalRepresentation.ExpressionRepresentation;

import wacc.CodeGenerator.Commands.Command;
import wacc.CodeGenerator.Commands.ConditionCode;
import wacc.CodeGenerator.Commands.DataTransferCommand.DataSize;
import wacc.CodeGenerator.Commands.DataTransferCommand.LdrCommand;
import wacc.CodeGenerator.HardwareManager.HardwareManager;
import wacc.CodeGenerator.HardwareManager.NormalRegister;
import wacc.CodeGenerator.HardwareManager.SpecialRegister;
import wacc.CodeGenerator.InternalRepresentation.AssignRepresentation.AssignRhs;
import wacc.CodeGenerator.InternalRepresentation.RepresentationFormatter;

import java.util.ArrayList;
import java.util.List;

public class PairLiteral implements Expression, AssignRhs {

    private static final String ZERO = "0";

    @Override
    public List<Command> generateCommandsForExpression() {

        List<Command> commands = new ArrayList<>();

        HardwareManager hardwareManager = HardwareManager.getInstance();
        NormalRegister register = hardwareManager.getFreeRegister();
        hardwareManager.addStorageRegister(register);

        ConditionCode al = ConditionCode.AL;
        DataSize w = DataSize.W;

        Command c = new LdrCommand(al, w, register, ZERO);
        commands.add(c);


        return commands;
    }

    @Override
    public List<Command> generateCommandsForAssignRhs() {

        List<Command> commands = generateCommandsForExpression();

        HardwareManager hardwareManager = HardwareManager.getInstance();
        NormalRegister register = hardwareManager.getStorageRegister();
        hardwareManager.addStorageRegister(register);

        ConditionCode al = ConditionCode.AL;
        DataSize w = DataSize.W;
        SpecialRegister sp = SpecialRegister.SP;
        Command c = new LdrCommand(al, w, register, sp);
        commands.add(c);

        return commands;
    }

    /*
      Representation for debugging
    */

    @Override
    public String generateAssignRhsRepresentation(int depthCount) {
        return generateRepresentation(depthCount, "PairLiteral Assignment");
    }

    @Override
    public String generateExpressionRepresentation(int depthCount) {
        return generateRepresentation(depthCount, "PairLiteral Expression");
    }

    private String generateRepresentation(int depthCount, String mainName) {

        // Depth representation in spaces
        String depth
            = RepresentationFormatter.getDepthRepresentation(depthCount);
        String indentDepth = depth + RepresentationFormatter.DEPTH_UNIT;

        return RepresentationFormatter.generateRepresentation(
            depth,
            mainName,
            indentDepth + "null"
        );
    }
}
