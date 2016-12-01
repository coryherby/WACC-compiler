package wacc.CodeGenerator.CodeGenerationTools.PredefinedFunctions;

import wacc.CodeGenerator.CodeGenerationTools.HardwareFunctions.HardwareFunctions;
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

public class PrintLnFunction {

    private static final String PRINT_LN_MESSAGE = "\"\\0\"";

    public static List<Command> generatePrintLnCommands() {

        // Get the message generator and add the error messages to show
        PredefinedMessagesGenerator predefinedMessagesGenerator
            = PredefinedMessagesGenerator.getInstance();

        String printLnMessage =
            predefinedMessagesGenerator.addPredefinedMessage(PRINT_LN_MESSAGE);

        // Start command generation
        List<Command> commands = new ArrayList<>();

        HardwareManager hardwareManager = HardwareManager.getInstance();
        NormalRegister register0 = hardwareManager.getRegister0();

        ConditionCode always = ConditionCode.AL;

        // Push lr register
        PushCommand pushLr = new PushCommand(always,
            new ArrayList<>(Collections.singletonList(SpecialRegister.LR)));
        commands.add(pushLr);

        // Load message into register
        LdrCommand loadMsg
            = new LdrCommand(always, DataSize.W, register0, printLnMessage);
        commands.add(loadMsg);

        // Increment r0
        AddCommand incrementR0
            = new AddCommand(always, register0, register0, "4");
        commands.add(incrementR0);

        // Call puts function
        BlCommand callPuts = new BlCommand(ConditionCode.AL,
            HardwareFunctions.PUTS_FUNCTION.toString());
        commands.add(callPuts);

        // Restore r0
        MovCommand put0inR0 = new MovCommand(always, register0, "0");
        commands.add(put0inR0);

        // Call fflush
        BlCommand callFflush = new BlCommand(always,
            HardwareFunctions.FFLUSH_FUNCTION.toString());
        commands.add(callFflush);


        // Pop program counter (pc)
        PopCommand popPc = new PopCommand(ConditionCode.AL,
            Collections.singletonList(SpecialRegister.PC));
        commands.add(popPc);

        return commands;
    }
}
