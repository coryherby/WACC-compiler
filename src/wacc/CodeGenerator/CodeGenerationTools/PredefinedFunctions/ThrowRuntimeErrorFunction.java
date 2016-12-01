package wacc.CodeGenerator.CodeGenerationTools.PredefinedFunctions;

import wacc.CodeGenerator.CodeGenerationTools.PredefinedFunctionGenerator;
import wacc.CodeGenerator.Commands.BranchingCommand.BlCommand;
import wacc.CodeGenerator.Commands.Command;
import wacc.CodeGenerator.Commands.ConditionCode;
import wacc.CodeGenerator.Commands.DataMovementCommand.MovCommand;
import wacc.CodeGenerator.HardwareManager.HardwareManager;
import wacc.CodeGenerator.HardwareManager.NormalRegister;

import java.util.ArrayList;
import java.util.List;

public class ThrowRuntimeErrorFunction {

    private static final String EXIT_FUNCTION = "exit";

    public static List<Command> generateThrowRuntimeErrorCommands() {

        HardwareManager hardwareManager = HardwareManager.getInstance();
        NormalRegister register0 = hardwareManager.getRegister0();

        ArrayList<Command> commands = new ArrayList<>();

        ConditionCode always = ConditionCode.AL;

        // Call printString
        BlCommand printStr = new BlCommand(always,
            PredefinedFunctionLabels.PRINT_STRING.toString());
        commands.add(printStr);

        PredefinedFunctionGenerator codeGenHelper
            = PredefinedFunctionGenerator.getInstance();
        codeGenHelper.addPredefinedFunction(
            PredefinedFunctionLabels.PRINT_STRING);

        // Load exit code
        MovCommand exitCode = new MovCommand(always, register0, "-1");
        commands.add(exitCode);

        // Exit program
        BlCommand exit = new BlCommand(always, EXIT_FUNCTION);
        commands.add(exit);

        return commands;

    }
}
