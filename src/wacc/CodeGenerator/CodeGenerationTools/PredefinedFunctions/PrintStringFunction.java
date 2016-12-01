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

public class PrintStringFunction {

    private static final String PRINT_STRING_MESSAGE = "\"%.*s\\0\"";

    public static List<Command> generatePrintStringCommands() {

        // Get the message generator and add the error messages to show
        PredefinedMessagesGenerator predefinedMessagesGenerator
            = PredefinedMessagesGenerator.getInstance();

        String printStringMessage = predefinedMessagesGenerator
            .addPredefinedMessage(PRINT_STRING_MESSAGE);

        // Start command generation
        HardwareManager hardwareManager = HardwareManager.getInstance();
        NormalRegister register0 = hardwareManager.getRegister0();
        NormalRegister register1 = hardwareManager.getRegister1();
        NormalRegister register2 = hardwareManager.getRegister2();

        ArrayList<Command> commands = new ArrayList<>();

        ConditionCode always = ConditionCode.AL;

        // Push lr register
        PushCommand pushLr = new PushCommand(ConditionCode.AL,
            new ArrayList<>(Collections.singletonList(SpecialRegister.LR)));
        commands.add(pushLr);

        // Load r1 into r2
        LdrCommand ldrCommand
                = new LdrCommand(always, DataSize.W, register1, register0);
        commands.add(ldrCommand);

        // Increment r2
        AddCommand addCommand
                = new AddCommand(always, register2, register0, "4");
        commands.add(addCommand);

        // Load error message
        LdrCommand loadMsg = new LdrCommand(
            always, DataSize.W, register0, printStringMessage);
        commands.add(loadMsg);

        // Increment r0
        addCommand = new AddCommand(always, register0, register0, "4");
        commands.add(addCommand);

        // Call print
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
