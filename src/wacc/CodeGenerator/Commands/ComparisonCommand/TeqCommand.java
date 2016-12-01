package wacc.CodeGenerator.Commands.ComparisonCommand;

import wacc.CodeGenerator.Commands.ConditionCode;
import wacc.CodeGenerator.HardwareManager.Register;

public class TeqCommand extends ComparisonCommand {

    public TeqCommand(ConditionCode conditionField,
                      Register sourceRegister,
                      String value) {

        super(conditionField, sourceRegister, value);
    }

    public TeqCommand(ConditionCode conditionField,
                      Register sourceRegister,
                      Register sourceRegister2) {

        super(conditionField, sourceRegister, sourceRegister2);
    }

    public TeqCommand(ConditionCode conditionField,
                      Register sourceRegister,
                      Register sourceRegister2,
                      String registerShift) {

        super(conditionField, sourceRegister, sourceRegister2, registerShift);
    }

    @Override
    protected String getCommandCode() {
        return "TEQ";
    }
}
