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

public class ThrowRuntimeErrorFreeFunction {

    private static final String EXIT_FUNCTION = "exit";
    private static final String EXIT_CODE = "134";

    public static List<Command> generateThrowRuntimeErrorFreeCommands() {

        HardwareManager hardwareManager = HardwareManager.getInstance();
        NormalRegister register0 = hardwareManager.getRegister0();

        ArrayList<Command> commands = new ArrayList<>();

        ConditionCode always = ConditionCode.AL;

        // Load exit code
        MovCommand exitCode = new MovCommand(always, register0, EXIT_CODE);
        commands.add(exitCode);

        // Exit program
        BlCommand exit = new BlCommand(always, EXIT_FUNCTION);
        commands.add(exit);

        return commands;

    }
}
