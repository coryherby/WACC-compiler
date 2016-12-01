package wacc.CodeGenerator.CodeGenerationTools.PredefinedFunctions;

import wacc.CodeGenerator.CodeGenerationTools.PredefinedFunctionGenerator;
import wacc.CodeGenerator.CodeGenerationTools.PredefinedMessagesGenerator;
import wacc.CodeGenerator.Commands.BranchingCommand.BlCommand;
import wacc.CodeGenerator.Commands.Command;
import wacc.CodeGenerator.Commands.ComparisonCommand.CmpCommand;
import wacc.CodeGenerator.Commands.ConditionCode;
import wacc.CodeGenerator.Commands.DataTransferCommand.DataSize;
import wacc.CodeGenerator.Commands.DataTransferCommand.LdrCommand;
import wacc.CodeGenerator.Commands.StackManipulationCommand.PopCommand;
import wacc.CodeGenerator.Commands.StackManipulationCommand.PushCommand;
import wacc.CodeGenerator.HardwareManager.HardwareManager;
import wacc.CodeGenerator.HardwareManager.NormalRegister;
import wacc.CodeGenerator.HardwareManager.SpecialRegister;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CheckArrayBoundsFunction {

    private static final String NEGATIVE_INDEX_ERROR_MESSAGE
        = "\"ArrayIndexOutOfBoundsError: negative index\\n\\0\"";
    private static final String INDEX_TOO_LARGE_ERROR_MESSAGE
        = "\"ArrayIndexOutOfBoundsError: index too large\\n\\0\"";

    public static List<Command> generateCheckArrayBoundsCommands() {

        // Get the message generator and add the error messages to show
        PredefinedMessagesGenerator predefinedMessagesGenerator
            = PredefinedMessagesGenerator.getInstance();

        String firstErrorMessage =
            predefinedMessagesGenerator.addPredefinedMessage(
                NEGATIVE_INDEX_ERROR_MESSAGE);
        String secondErrorMessage =
            predefinedMessagesGenerator.addPredefinedMessage(
                INDEX_TOO_LARGE_ERROR_MESSAGE);

        // Start command generation
        HardwareManager hardwareManager = HardwareManager.getInstance();
        NormalRegister register0 = hardwareManager.getRegister0();
        NormalRegister register1 = hardwareManager.getRegister1();

        List<Command> commands = new ArrayList<>();

        ConditionCode always = ConditionCode.AL;

        // Push lr register
        PushCommand pushLr = new PushCommand(always,
            new ArrayList<>(Collections.singletonList(SpecialRegister.LR)));
        commands.add(pushLr);

        // Compare index with 0
        CmpCommand cmpWith0 = new CmpCommand(always, register0, "0");
        commands.add(cmpWith0);

        // Load error message
        LdrCommand loadMsg0 = new LdrCommand(ConditionCode.LT, DataSize.W,
            register0, firstErrorMessage);
        commands.add(loadMsg0);

        // Call runtime error
        BlCommand runtimeError = new BlCommand(ConditionCode.LT,
            PredefinedFunctionLabels.THROW_RUNTIME_ERROR.toString());
        commands.add(runtimeError);

        PredefinedFunctionGenerator codeGenHelper
            = PredefinedFunctionGenerator.getInstance();
        codeGenHelper.addPredefinedFunction(
            PredefinedFunctionLabels.THROW_RUNTIME_ERROR);

        // Load array size into register 1
        LdrCommand loadArraySize = new LdrCommand(always, DataSize.W, register1,
            register1);
        commands.add(loadArraySize);

        // Compare array size and index
        CmpCommand cmpIndexSize = new CmpCommand(always, register0, register1);
        commands.add(cmpIndexSize);

        // Load error message
        LdrCommand loadMsg1 = new LdrCommand(ConditionCode.CS, DataSize.W,
            register0, secondErrorMessage);
        commands.add(loadMsg1);

        // Call runtime error
        runtimeError = new BlCommand(ConditionCode.CS,
            PredefinedFunctionLabels.THROW_RUNTIME_ERROR.toString());
        commands.add(runtimeError);

        // Pop program counter (pc)
        PopCommand popPc = new PopCommand(ConditionCode.AL,
            Collections.singletonList(SpecialRegister.PC));
        commands.add(popPc);

        return commands;

    }

}
