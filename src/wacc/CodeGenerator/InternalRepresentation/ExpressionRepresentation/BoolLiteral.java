package wacc.CodeGenerator.InternalRepresentation.ExpressionRepresentation;

import wacc.CodeGenerator.Commands.Command;
import wacc.CodeGenerator.Commands.ConditionCode;
import wacc.CodeGenerator.Commands.DataMovementCommand.MovCommand;
import wacc.CodeGenerator.HardwareManager.HardwareManager;
import wacc.CodeGenerator.HardwareManager.NormalRegister;
import wacc.CodeGenerator.InternalRepresentation.AssignRepresentation.AssignRhs;
import wacc.CodeGenerator.InternalRepresentation.RepresentationFormatter;

import java.util.ArrayList;
import java.util.List;

public class BoolLiteral implements Expression, AssignRhs {

    private static final String TRUE = "1";
    private static final String FALSE = "0";
    private boolean boolValue;

    public BoolLiteral(boolean boolValue) {
        this.boolValue = boolValue;
    }

    @Override
    public List<Command> generateCommandsForExpression() {

        List<Command> commands = new ArrayList<>();

        HardwareManager hardwareManager = HardwareManager.getInstance();

        NormalRegister strRegister = hardwareManager.getFreeRegister();

        hardwareManager.addStorageRegister(strRegister);

        // Storing the boolean value into a register
        ConditionCode al = ConditionCode.AL;
        if (boolValue) {
            MovCommand c = new MovCommand(al, strRegister, TRUE);
            commands.add(c);
        } else {
            MovCommand c = new MovCommand(al, strRegister, FALSE);
            commands.add(c);
        }

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
        return generateRepresentation(depthCount, "BoolLiteral Assignement");
    }

    @Override
    public String generateExpressionRepresentation(int depthCount) {
        return generateRepresentation(depthCount, "BoolLiteral Expression");
    }

    private String generateRepresentation(int depthCount, String mainName) {

        // Depth representation in spaces
        String depth
            = RepresentationFormatter.getDepthRepresentation(depthCount);
        String indentDepth = depth + RepresentationFormatter.DEPTH_UNIT;

        return RepresentationFormatter.generateRepresentation(
            depth,
            mainName,
            indentDepth + String.valueOf(boolValue)
        );
    }

}
