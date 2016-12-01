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

public class FreePairFunction {

    private static final String NULL_REFERENCE_ERROR_MESSAGE
        = "\"NullReferenceError: dereference a null reference\\n\\0\"";

    private static final String FREE_FUNCTION = "free";

    public static List<Command> generateFreePairCommands() {

        // Get the message generator and add the error messages to show
        PredefinedMessagesGenerator predefinedMessagesGenerator
            = PredefinedMessagesGenerator.getInstance();

        String errorMessage = predefinedMessagesGenerator
            .addPredefinedMessage(NULL_REFERENCE_ERROR_MESSAGE);

        // Start command generation
        HardwareManager hardwareManager = HardwareManager.getInstance();
        NormalRegister R0 = hardwareManager.getRegister0();

        ArrayList<Command> commands = new ArrayList<>();

        // push lr
        ConditionCode al = ConditionCode.AL;
        PushCommand pushLr = new PushCommand(al,
            new ArrayList<>(Collections.singletonList(SpecialRegister.LR)));
        commands.add(pushLr);

        // compare parameter to 0
        CmpCommand cmpR0to0 = new CmpCommand(al,R0,"0");
        commands.add(cmpR0to0);

        // load message if param == 0
        DataSize w = DataSize.W;
        LdrCommand ldrIf0
            = new LdrCommand(ConditionCode.EQ, w, R0, errorMessage);
        commands.add(ldrIf0);

        // throw runtime error if true
        BlCommand runtime
            = new BlCommand(ConditionCode.EQ,
            PredefinedFunctionLabels.THROW_RUNTIME_ERROR.toString());
        commands.add(runtime);

        PredefinedFunctionGenerator codeGenHelper
            = PredefinedFunctionGenerator.getInstance();
        codeGenHelper.addPredefinedFunction(
            PredefinedFunctionLabels.THROW_RUNTIME_ERROR);

        // push r0
        PushCommand pushR0 = new PushCommand(
            al, new ArrayList<>(Collections.singletonList(R0)));
        commands.add(pushR0);

        //load R0 at memory address R0
        LdrCommand ldrR0toMem
            = new LdrCommand(al, w, R0, R0);
        commands.add(ldrR0toMem);

        // free the memory
        BlCommand free = new BlCommand(al, FREE_FUNCTION);
        commands.add(free);

        //load R0 in stack
        ldrR0toMem
            = new LdrCommand(al, w, R0, SpecialRegister.SP);
        commands.add(ldrR0toMem);

        //load R0 at memory address R0 and offset it by 4
        ldrR0toMem
            = new LdrCommand(al, w, R0, R0, "4", true, false);
        commands.add(ldrR0toMem);

        // free the memory
        commands.add(free);

        PopCommand popR0 = new PopCommand(
            ConditionCode.AL, Collections.singletonList(R0));
        commands.add(popR0);

        // free the memory
        commands.add(free);

        // Pop program counter (pc)
        PopCommand popPc = new PopCommand(ConditionCode.AL,
            Collections.singletonList(SpecialRegister.PC));
        commands.add(popPc);

        return commands;
    }
}
