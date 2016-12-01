package wacc.CodeGenerator.Commands.StackManipulationCommand;

import wacc.CodeGenerator.Commands.ConditionCode;
import wacc.CodeGenerator.HardwareManager.Register;

import java.util.List;

public class PushCommand extends StackManipulationCommand {


    public PushCommand(ConditionCode conditionField, List<Register> registers) {
        super(conditionField, registers);
    }

    @Override
    protected String getCommandCode() {
        return "PUSH";
    }
}
