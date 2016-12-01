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

public class CharLiteral implements Expression, AssignRhs {

    private static final String EMPTY_CHAR = "\'\\0\'";

    private String character;

    public CharLiteral(String character) {
        this.character = character;
    }

    @Override
    public List<Command> generateCommandsForExpression() {

        List<Command> commands = new ArrayList<>();

        HardwareManager hardwareManager = HardwareManager.getInstance();
        NormalRegister strRegister = hardwareManager.getFreeRegister();
        hardwareManager.addStorageRegister(strRegister);

        // Moving the character value into a register

        ConditionCode always = ConditionCode.AL;
        MovCommand movChar;
        String characterString = String.valueOf(character);

        if (character.equals(EMPTY_CHAR)) {
            movChar = new MovCommand(always, strRegister, "0");
        } else {
            movChar = new MovCommand(always, strRegister, characterString);
        }


        commands.add(movChar);

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
        return generateRepresentation(depthCount, "CharLiteral Assignment");
    }

    @Override
    public String generateExpressionRepresentation(int depthCount) {
        return generateRepresentation(depthCount, "CharLiteral Expression");
    }

    private String generateRepresentation(int depthCount, String mainName) {

        // Depth representation in spaces
        String depth
            = RepresentationFormatter.getDepthRepresentation(depthCount);
        String indentDepth = depth + RepresentationFormatter.DEPTH_UNIT;

        return RepresentationFormatter.generateRepresentation(
            depth,
            mainName,
            indentDepth + character
        );
    }

}
