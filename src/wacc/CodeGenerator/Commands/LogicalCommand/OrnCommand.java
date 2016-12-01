package wacc.CodeGenerator.Commands.LogicalCommand;

import wacc.CodeGenerator.Commands.ConditionCode;
import wacc.CodeGenerator.HardwareManager.Register;

public class OrnCommand extends LogicalCommand {

    public OrnCommand(ConditionCode conditionField,
                      Register destinationRegister,
                      Register sourceRegister,
                      String value) {

        super(conditionField, destinationRegister, sourceRegister, value);
    }

    public OrnCommand(ConditionCode conditionField,
                      Register destinationRegister,
                      Register sourceRegister,
                      Register sourceRegister2) {

        super(conditionField, destinationRegister, sourceRegister,
            sourceRegister2);
    }

    public OrnCommand(ConditionCode conditionField,
                      Register destinationRegister,
                      Register sourceRegister,
                      Register sourceRegister2,
                      String registerShift) {

        super(conditionField, destinationRegister, sourceRegister,
            sourceRegister2, registerShift);
    }

    @Override
    protected String getCommandCode() {
        return "ORN";
    }
}
