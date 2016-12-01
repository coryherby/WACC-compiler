package wacc.CodeGenerator.CodeGenerationTools.PredefinedFunctions;

import wacc.CodeGenerator.CodeGenerationTools.PredefinedMessagesGenerator;
import wacc.CodeGenerator.Commands.BranchingCommand.BlCommand;
import wacc.CodeGenerator.Commands.Command;
import wacc.CodeGenerator.Commands.ConditionCode;
import wacc.CodeGenerator.Commands.DataMovementCommand.MovCommand;
import wacc.CodeGenerator.Commands.DataTransferCommand.DataSize;
import wacc.CodeGenerator.Commands.DataTransferCommand.LdrCommand;
import wacc.CodeGenerator.Commands.LogicalCommand.AddCommand;
import wacc.CodeGenerator.Commands.StackManipulationCommand.PopCommand;
import wacc.CodeGenerator.Commands.StackManipulationCommand.PushCommand;
import wacc.CodeGenerator.HardwareManager.HardwareManager;
import wacc.CodeGenerator.HardwareManager.NormalRegister;
import wacc.CodeGenerator.HardwareManager.SpecialRegister;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReadFunction {

    private static final String READ_INT_MESSAGE = "\"%d\\0\"";
    private static final String READ_CHAR_MESSAGE = "\" %c\\0\"";

    private static final String SCANF_FUNCTION = "scanf";

    public static List<Command> generateReadIntCommands() {
        return generateReadCommands(READ_INT_MESSAGE);
    }

    public static List<Command> generateReadCharCommands() {
        return generateReadCommands(READ_CHAR_MESSAGE);
    }

    private static List<Command> generateReadCommands(String message) {

        // Get the message generator and add the error messages to show
        PredefinedMessagesGenerator predefinedMessagesGenerator
            = PredefinedMessagesGenerator.getInstance();

        String readMessage = predefinedMessagesGenerator
            .addPredefinedMessage(message);

        // Start command generation
        HardwareManager hardwareManager = HardwareManager.getInstance();
        NormalRegister register0 = hardwareManager.getRegister0();

        ArrayList<Command> commands = new ArrayList<>();

        // Push lr register
        PushCommand pushLr = new PushCommand(ConditionCode.AL,
            new ArrayList<>(Collections.singletonList(SpecialRegister.LR)));
        commands.add(pushLr);

        // Move the argument given in the register r0 (the variable address)
        // into the register r1
        MovCommand movR0inR1 = new MovCommand(ConditionCode.AL,
            hardwareManager.getRegister1(), register0);
        commands.add(movR0inR1);

        // Load message in r0 (different if char or int)
        LdrCommand loadMsgInR0 = new LdrCommand(ConditionCode.AL,
            DataSize.W, register0, readMessage);
        commands.add(loadMsgInR0);

        // Increment r0
        AddCommand add4ToR0 = new AddCommand(ConditionCode.AL,
            register0, register0, "4");
        commands.add(add4ToR0);

        // Call function scanf
        BlCommand callScanf
            = new BlCommand(ConditionCode.AL, SCANF_FUNCTION);
        commands.add(callScanf);

        // Pop program counter (pc)
        PopCommand popPc = new PopCommand(ConditionCode.AL,
            Collections.singletonList(SpecialRegister.PC));
        commands.add(popPc);

        return commands;
    }
}
