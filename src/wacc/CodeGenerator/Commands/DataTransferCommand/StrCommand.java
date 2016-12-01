package wacc.CodeGenerator.Commands.DataTransferCommand;

import wacc.CodeGenerator.Commands.ConditionCode;
import wacc.CodeGenerator.HardwareManager.Register;

public class StrCommand extends DataTransferCommand {

    public StrCommand(ConditionCode conditionField,
                      DataSize dataSize,
                      Register destinationRegister,
                      String value) {

        super(conditionField, dataSize, destinationRegister, value);
    }

    public StrCommand(ConditionCode conditionField,
                      DataSize dataSize,
                      Register destinationRegister,
                      Register sourceRegister) {

        super(conditionField, dataSize, destinationRegister, sourceRegister);
    }

    public StrCommand(ConditionCode conditionField,
                      DataSize dataSize,
                      Register destinationRegister,
                      Register sourceRegister,
                      String value,
                      boolean isPreIndexAddressing,
                      boolean isDualUpdating) {

        super(conditionField, dataSize, destinationRegister, sourceRegister,
            value, isPreIndexAddressing, isDualUpdating);
    }

    public StrCommand(ConditionCode conditionField,
                      DataSize dataSize,
                      Register destinationRegister,
                      Register sourceRegister,
                      Register sourceRegister2,
                      boolean isPreIndexAddressing,
                      boolean isDualUpdating) {

        super(conditionField, dataSize, destinationRegister, sourceRegister,
            sourceRegister2, isPreIndexAddressing, isDualUpdating);
    }

    public StrCommand(ConditionCode conditionField,
                      DataSize dataSize,
                      Register destinationRegister,
                      Register sourceRegister,
                      Register sourceRegister2,
                      String value,
                      boolean isPreIndexAddressing,
                      boolean isDualUpdating) {

        super(conditionField, dataSize, destinationRegister, sourceRegister,
            sourceRegister2, value, isPreIndexAddressing, isDualUpdating);
    }

    @Override
    protected String getCommandCode() {
        return "STR";
    }
}
