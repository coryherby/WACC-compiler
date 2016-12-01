package wacc.CodeGenerator.Commands.ComparisonCommand;

import wacc.CodeGenerator.Commands.ConditionCode;
import wacc.CodeGenerator.HardwareManager.Register;

public class TstCommand extends ComparisonCommand {

    public TstCommand(ConditionCode conditionField,
                      Register sourceRegister,
                      String value) {

        super(conditionField, sourceRegister, value);
    }

    public TstCommand(ConditionCode conditionField,
                      Register sourceRegister,
                      Register sourceRegister2) {

        super(conditionField, sourceRegister, sourceRegister2);
    }

    public TstCommand(ConditionCode conditionField,
                      Register sourceRegister,
                      Register sourceRegister2,
                      String registerShift) {

        super(conditionField, sourceRegister, sourceRegister2, registerShift);
    }

    @Override
    protected String getCommandCode() {
        return "TST";
    }
}
