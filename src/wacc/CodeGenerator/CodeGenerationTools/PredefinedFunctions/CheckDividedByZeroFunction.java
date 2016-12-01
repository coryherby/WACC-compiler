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

public class CheckDividedByZeroFunction {

    private static final String DIVIDE_OR_MODULO_BY_ZERO_ERROR_MESSAGE
        = "\"DivideByZeroError: divide or modulo by zero\\n\\0\"";

    public static List<Command> generateDivideByZeroCommands() {

        // Get the message generator and add the error messages to show
        PredefinedMessagesGenerator predefinedMessagesGenerator
            = PredefinedMessagesGenerator.getInstance();

        String errorMessage =
            predefinedMessagesGenerator.addPredefinedMessage(
                DIVIDE_OR_MODULO_BY_ZERO_ERROR_MESSAGE);

        // Start command generation
        HardwareManager hardwareManager = HardwareManager.getInstance();
        NormalRegister register0 = hardwareManager.getRegister0();
        NormalRegister register1 = hardwareManager.getRegister1();

        List<Command> commands = new ArrayList<>();

        ConditionCode always = ConditionCode.AL;

        // push the lr register
        PushCommand pushLr = new PushCommand(always,
            new ArrayList<>(Collections.singletonList(SpecialRegister.LR)));
        commands.add(pushLr);

        // compare the parameter to 0
        CmpCommand cmpTo0
            = new CmpCommand(always,register1,"0");
        commands.add(cmpTo0);

        // load message error in R0 if parameter is 0
        LdrCommand ldrIf0 = new LdrCommand(
            ConditionCode.EQ, DataSize.W, register0, errorMessage);
        commands.add(ldrIf0);

        // checks runtime errors
        BlCommand runtimeError = new BlCommand(ConditionCode.EQ,
            PredefinedFunctionLabels.THROW_RUNTIME_ERROR.toString());
        commands.add(runtimeError);

        PredefinedFunctionGenerator codeGenHelper
            = PredefinedFunctionGenerator.getInstance();
        codeGenHelper.addPredefinedFunction(
            PredefinedFunctionLabels.THROW_RUNTIME_ERROR);

        // Pop program counter (pc)
        PopCommand popPc = new PopCommand(ConditionCode.AL,
            Collections.singletonList(SpecialRegister.PC));
        commands.add(popPc);

        return commands;
    }
}
