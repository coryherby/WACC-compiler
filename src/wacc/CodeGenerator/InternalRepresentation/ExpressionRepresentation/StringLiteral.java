package wacc.CodeGenerator.InternalRepresentation.ExpressionRepresentation;

import wacc.CodeGenerator.CodeGenerationTools.PredefinedMessagesGenerator;
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

public class StringLiteral implements Expression, AssignRhs {

    private String stringLiteral;

    public StringLiteral(String stringLiteral) {
        this.stringLiteral = stringLiteral;
    }

    @Override
    public List<Command> generateCommandsForExpression() {
        String messageLabel = PredefinedMessagesGenerator.getInstance()
            .addPredefinedMessage(stringLiteral);

        HardwareManager hardwareManager = HardwareManager.getInstance();
        NormalRegister strRegister = hardwareManager.getFreeRegister();
        hardwareManager.addStorageRegister(strRegister);

        // Loading preset string from memory into a register
        ConditionCode always = ConditionCode.AL;
        DataSize basic = DataSize.W;
        LdrCommand loadString = new LdrCommand(always, basic,
            strRegister, messageLabel);

        List<Command> commands = new ArrayList<>();
        commands.add(loadString);

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
        return generateRepresentation(depthCount, "StringLiteral Assignment");
    }

    @Override
    public String generateExpressionRepresentation(int depthCount) {
        return generateRepresentation(depthCount, "StringLiteral Expression");
    }

    private String generateRepresentation(int depthCount, String mainName) {

        // Depth representation in spaces
        String depth
            = RepresentationFormatter.getDepthRepresentation(depthCount);
        String indentDepth = depth + RepresentationFormatter.DEPTH_UNIT;

        return RepresentationFormatter.generateRepresentation(
            depth,
            mainName,
            indentDepth + stringLiteral
        );
    }

}
