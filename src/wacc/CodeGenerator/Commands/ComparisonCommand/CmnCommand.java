package wacc.CodeGenerator.Commands.ComparisonCommand;

import wacc.CodeGenerator.Commands.ConditionCode;
import wacc.CodeGenerator.HardwareManager.Register;

public class CmnCommand extends ComparisonCommand {

    public CmnCommand(ConditionCode conditionField,
                      Register sourceRegister,
                      String value) {

        super(conditionField, sourceRegister, value);
    }

    public CmnCommand(ConditionCode conditionField,
                      Register sourceRegister,
                      Register sourceRegister2) {

        super(conditionField, sourceRegister, sourceRegister2);
    }

    public CmnCommand(ConditionCode conditionField,
                      Register sourceRegister,
                      Register sourceRegister2,
                      String registerShift) {

        super(conditionField, sourceRegister, sourceRegister2, registerShift);
    }

    @Override
    protected String getCommandCode() {
        return "CMN";
    }
}
