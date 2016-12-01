package wacc.CodeGenerator.CodeGenerationTools.PredefinedFunctions;


import wacc.CodeGenerator.CodeGenerationTools.PredefinedFunctionGenerator;
import wacc.CodeGenerator.CodeGenerationTools.PredefinedMessagesGenerator;
import wacc.CodeGenerator.Commands.BranchingCommand.BlCommand;
import wacc.CodeGenerator.Commands.Command;
import wacc.CodeGenerator.Commands.ConditionCode;
import wacc.CodeGenerator.Commands.DataTransferCommand.DataSize;
import wacc.CodeGenerator.Commands.DataTransferCommand.LdrCommand;
import wacc.CodeGenerator.HardwareManager.HardwareManager;
import wacc.CodeGenerator.HardwareManager.NormalRegister;

import java.util.ArrayList;
import java.util.List;

public class ThrowOverflowErrorFunction {

    private static final String PRINT_OVERFLOW_MESSAGE
        = "\"OverflowError: the result is too small/large to store" +
        " in a 4-byte signed-integer.\\n\"";

    public static List<Command> generateThrowOverflowErrorCommands() {

        // Get the message generator and add the error messages to show
        PredefinedMessagesGenerator predefinedMessagesGenerator
            = PredefinedMessagesGenerator.getInstance();

        String overflowMessage = predefinedMessagesGenerator
            .addPredefinedMessage(PRINT_OVERFLOW_MESSAGE);

        // Start command generation
        HardwareManager hardwareManager = HardwareManager.getInstance();
        NormalRegister register0 = hardwareManager.getRegister0();

        List<Command> commands = new ArrayList<>();

        // Ldr message
        LdrCommand ldrMsg = new LdrCommand(ConditionCode.AL, DataSize.W,
            register0, overflowMessage);
        commands.add(ldrMsg);

        // Throw runtime error
        BlCommand runtime = new BlCommand(ConditionCode.AL,
            PredefinedFunctionLabels.THROW_RUNTIME_ERROR.toString());
        commands.add(runtime);

        // Add the function into the list of function to print
        PredefinedFunctionGenerator codeGenHelper
            = PredefinedFunctionGenerator.getInstance();
        codeGenHelper.addPredefinedFunction(
            PredefinedFunctionLabels.THROW_RUNTIME_ERROR);

        return commands;
    }
}
