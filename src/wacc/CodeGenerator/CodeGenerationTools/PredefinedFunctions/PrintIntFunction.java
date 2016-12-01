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

public class PrintIntFunction {

    private static final String PRINT_INT_MESSAGE = "\"%d\\0\"";

    public static List<Command> generatePrintIntCommands() {

        // Get the message generator and add the error messages to show
        PredefinedMessagesGenerator predefinedMessagesGenerator
            = PredefinedMessagesGenerator.getInstance();

        String printIntMessage =
            predefinedMessagesGenerator.addPredefinedMessage(PRINT_INT_MESSAGE);

        // Start command generation
        HardwareManager hardwareManager = HardwareManager.getInstance();
        NormalRegister register0 = hardwareManager.getRegister0();
        NormalRegister register1 = hardwareManager.getRegister1();

        ArrayList<Command> commands = new ArrayList<>();

        ConditionCode always = ConditionCode.AL;

        // Push lr register
        PushCommand pushLr = new PushCommand(ConditionCode.AL,
            new ArrayList<>(Collections.singletonList(SpecialRegister.LR)));
        commands.add(pushLr);

        // Move r0 into r1
        MovCommand movR0IntoR1 = new MovCommand(always, register1, register0);
        commands.add(movR0IntoR1);

        // Load message into register
        LdrCommand loadMsg
            = new LdrCommand(always, DataSize.W, register0, printIntMessage);
        commands.add(loadMsg);

        // Increment r0
        AddCommand addCommand
            = new AddCommand(always, register0, register0, "4");
        commands.add(addCommand);

        // Call printf
        BlCommand printf = new BlCommand(always,
            HardwareFunctions.PRINTF_FUNCTION.toString());
        commands.add(printf);

        // Restore r0
        MovCommand movCommand = new MovCommand(always, register0, "0");
        commands.add(movCommand);

        // Call fflush
        BlCommand fflush = new BlCommand(always,
            HardwareFunctions.FFLUSH_FUNCTION.toString());
        commands.add(fflush);

        // Pop program counter (pc)
        PopCommand popPc = new PopCommand(ConditionCode.AL,
            Collections.singletonList(SpecialRegister.PC));
        commands.add(popPc);

        return commands;
    }
}
