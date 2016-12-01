package wacc.CodeGenerator.CodeGenerationTools.PredefinedFunctions;

import wacc.CodeGenerator.CodeGenerationTools.HardwareFunctions.HardwareFunctions;
import wacc.CodeGenerator.CodeGenerationTools.PredefinedMessagesGenerator;
import wacc.CodeGenerator.Commands.BranchingCommand.BlCommand;
import wacc.CodeGenerator.Commands.Command;
import wacc.CodeGenerator.Commands.ComparisonCommand.CmpCommand;
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

public class PrintBoolFunction {

    private static final String BOOL_TRUE_MESSAGE = "\"true\\0\"";
    private static final String BOOL_FALSE_MESSAGE = "\"false\\0\"";

    public static List<Command> generatePrintBoolCommands() {

        // Get the message generator and add the error messages to show
        PredefinedMessagesGenerator predefinedMessagesGenerator
            = PredefinedMessagesGenerator.getInstance();

        String boolTrueMessage =
            predefinedMessagesGenerator.addPredefinedMessage(
                BOOL_TRUE_MESSAGE);
        String boolFalseMessage =
            predefinedMessagesGenerator.addPredefinedMessage(
                BOOL_FALSE_MESSAGE);

        // Start command generation
        HardwareManager hardwareManager = HardwareManager.getInstance();
        NormalRegister register0 = hardwareManager.getRegister0();

        ArrayList<Command> commands = new ArrayList<>();

        ConditionCode always = ConditionCode.AL;

        // Push lr register
        PushCommand pushLr = new PushCommand(ConditionCode.AL,
            new ArrayList<>(Collections.singletonList(SpecialRegister.LR)));
        commands.add(pushLr);

        // Compare bool element with 0
        CmpCommand cmpWith0 = new CmpCommand(always, register0, "0");
        commands.add(cmpWith0);

        // If not equal, load "true" into register
        LdrCommand loadTrue = new LdrCommand(
            ConditionCode.NE, DataSize.W, register0, boolTrueMessage);
        commands.add(loadTrue);

        // If equal, load "false" into register
        LdrCommand loadFalse = new LdrCommand(
            ConditionCode.EQ, DataSize.W, register0, boolFalseMessage);
        commands.add(loadFalse);

        // Increment r0
        AddCommand incrementR0
            = new AddCommand(always, register0, register0, "4");
        commands.add(incrementR0);

        // Call printf
        BlCommand callPrintf = new BlCommand(always,
            HardwareFunctions.PRINTF_FUNCTION.toString());
        commands.add(callPrintf);

        // Restore r0
        MovCommand restoreR0 = new MovCommand(always, register0, "0");
        commands.add(restoreR0);

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
