package wacc.CodeGenerator.Commands.StackManipulationCommand;

import wacc.CodeGenerator.Commands.Command;
import wacc.CodeGenerator.Commands.CommandHelper;
import wacc.CodeGenerator.Commands.ConditionCode;
import wacc.CodeGenerator.HardwareManager.Register;

import java.util.List;

public abstract class StackManipulationCommand implements Command {

    private ConditionCode conditionField;
    private List<Register> registers;

    public StackManipulationCommand(ConditionCode conditionField,
                                    List<Register> registers) {

        this.conditionField = conditionField;
        this.registers = registers;

    }

    @Override
    public String generateCommandRepresentation() {

        String conditionCode = CommandHelper.getConditionCode(conditionField);

        String representation = getCommandCode() + conditionCode + " {";
        representation += registers.get(0).toString();
        for (int i = 1; i < registers.size(); i++) {
            representation += ", " + registers.get(i);
        }
        representation += "}";

        return representation;
    }

    protected abstract String getCommandCode();

}
