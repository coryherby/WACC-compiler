package wacc.CodeGenerator.CodeGenerationTools;

import wacc.CodeGenerator.Commands.Command;
import wacc.CodeGenerator.Commands.ConditionCode;
import wacc.CodeGenerator.Commands.LogicalCommand.AddCommand;
import wacc.CodeGenerator.Commands.LogicalCommand.SubCommand;
import wacc.CodeGenerator.HardwareManager.SpecialRegister;

import java.util.ArrayList;
import java.util.List;

public class ScopeOffsetGenerator {

    private static final int MAX_INT_BYTE = 1024;

    public static List<Command> initializeScopeOffset(int totalOffset) {
        List<Command> commands = new ArrayList<>();

        while(totalOffset > 0) {
            String totalOffsetRepresentation = getStringValue(totalOffset);

            SubCommand stackSubtraction = new SubCommand(ConditionCode.AL,
                SpecialRegister.SP, SpecialRegister.SP,
                totalOffsetRepresentation);
            commands.add(stackSubtraction);

            totalOffset = totalOffset - MAX_INT_BYTE;
        }

        return commands;
    }

    public static List<Command> returnScopeOffset(int totalOffset) {
        List<Command> commands = new ArrayList<>();

        while(totalOffset > 0) {
            String totalOffsetRepresentation = getStringValue(totalOffset);

            AddCommand stackAddition = new AddCommand(
                ConditionCode.AL, SpecialRegister.SP,
                SpecialRegister.SP, totalOffsetRepresentation);
            commands.add(stackAddition);

            totalOffset = totalOffset - MAX_INT_BYTE;
        }

        return commands;
    }

    private static String getStringValue(int totalOffset) {
        if (totalOffset >= MAX_INT_BYTE) {
            return String.valueOf(MAX_INT_BYTE);
        } else {
            return String.valueOf(totalOffset);
        }
    }
}
