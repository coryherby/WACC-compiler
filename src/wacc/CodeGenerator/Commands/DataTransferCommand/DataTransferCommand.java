package wacc.CodeGenerator.Commands.DataTransferCommand;

import wacc.CodeGenerator.Commands.Command;
import wacc.CodeGenerator.Commands.CommandHelper;
import wacc.CodeGenerator.Commands.ConditionCode;
import wacc.CodeGenerator.Commands.DataMovementCommand.MovCommand;
import wacc.CodeGenerator.HardwareManager.Register;

public abstract class DataTransferCommand implements Command {

    public enum AddressType {
        VALUE, REGISTER, IMMEDIATE_OFFSET, IMMEDIATE_OFFSET_DUAL_UPDATING,
        REGISTER_OFFSET, REGISTER_OFFSET_DUAL_UPDATING, REGISTER_SHIFTED_OFFSET,
        REGISTER_SHIFTED_OFFSET_DUAL_UPDATING
    }

    private ConditionCode conditionField;
    private DataSize dataSize;
    private Register destinationRegister;
    private Register sourceRegister;
    private Register sourceRegister2;
    boolean isPreIndexAddressing;
    private String value;
    private String registerOffset;
    private AddressType addressType;

    public DataTransferCommand(ConditionCode conditionField,
                               DataSize dataSize,
                               Register destinationRegister,
                               String value) {

        this.conditionField = conditionField;
        this.dataSize = dataSize;
        this.destinationRegister = destinationRegister;
        this.value = value;
        this.sourceRegister = null;
        this.sourceRegister2 = null;
        this.isPreIndexAddressing = false;
        this.registerOffset = null;

        addressType = AddressType.VALUE;

    }

    public DataTransferCommand(ConditionCode conditionField,
                               DataSize dataSize,
                               Register destinationRegister,
                               Register sourceRegister) {

        this.conditionField = conditionField;
        this.dataSize = dataSize;
        this.destinationRegister = destinationRegister;
        this.sourceRegister = sourceRegister;
        this.sourceRegister2 = null;
        this.isPreIndexAddressing = false;
        this.value = null;
        this.registerOffset = null;

        addressType = AddressType.REGISTER;

    }

    public DataTransferCommand(ConditionCode conditionField,
                               DataSize dataSize,
                               Register destinationRegister,
                               Register sourceRegister,
                               String registerOffset,
                               boolean isPreIndexAddressing,
                               boolean isDualUpdating) {

        this.conditionField = conditionField;
        this.dataSize = dataSize;
        this.destinationRegister = destinationRegister;
        this.sourceRegister = sourceRegister;
        this.sourceRegister2 = null;
        this.isPreIndexAddressing = isPreIndexAddressing;
        this.registerOffset = registerOffset;
        this.value = null;

        if (isDualUpdating) {
            addressType = AddressType.IMMEDIATE_OFFSET_DUAL_UPDATING;
        } else {
            addressType = AddressType.IMMEDIATE_OFFSET;
        }

    }

    public DataTransferCommand(ConditionCode conditionField,
                               DataSize dataSize,
                               Register destinationRegister,
                               Register sourceRegister,
                               Register sourceRegister2,
                               boolean isPreIndexAddressing,
                               boolean isDualUpdating) {

        this.conditionField = conditionField;
        this.dataSize = dataSize;
        this.destinationRegister = destinationRegister;
        this.sourceRegister = sourceRegister;
        this.sourceRegister2 = sourceRegister2;
        this.isPreIndexAddressing = isPreIndexAddressing;
        this.value = null;
        this.registerOffset = null;

        if (isDualUpdating) {
            addressType = AddressType.REGISTER_OFFSET_DUAL_UPDATING;
        } else {
            addressType = AddressType.REGISTER_OFFSET;
        }

    }

    public DataTransferCommand(ConditionCode conditionField,
                               DataSize dataSize,
                               Register destinationRegister,
                               Register sourceRegister,
                               Register sourceRegister2,
                               String registerOffset,
                               boolean isPreIndexAddressing,
                               boolean isDualUpdating) {

        this.conditionField = conditionField;
        this.dataSize = dataSize;
        this.destinationRegister = destinationRegister;
        this.sourceRegister = sourceRegister;
        this.sourceRegister2 = sourceRegister2;
        this.isPreIndexAddressing = isPreIndexAddressing;
        this.value = null;
        this.registerOffset = registerOffset;

        if (isDualUpdating) {
            addressType = AddressType.REGISTER_SHIFTED_OFFSET_DUAL_UPDATING;
        } else {
            addressType = AddressType.REGISTER_SHIFTED_OFFSET;
        }

    }

    @Override
    public String generateCommandRepresentation() {

        String conditionCode = CommandHelper.getConditionCode(conditionField);
        String dataType = CommandHelper.getDataSize(dataSize);

        switch (addressType) {

            case VALUE:
                return getCommandCode() + conditionCode + dataType + " "
                    + destinationRegister + ", =" + value;

            case REGISTER:
                return getCommandCode() + conditionCode + dataType + " "
                    + destinationRegister + ", " + "[" + sourceRegister + "]";

            case IMMEDIATE_OFFSET:

                if (isPreIndexAddressing) {

                    return getCommandCode() + conditionCode + dataType
                        + " " + destinationRegister + ", " + "["
                        + sourceRegister + ", #" + registerOffset + "]";

                } else {

                    return getCommandCode() + conditionCode + dataType
                        + " " + destinationRegister + ", " + "["
                        + sourceRegister + "], " + registerOffset;

                }

            case IMMEDIATE_OFFSET_DUAL_UPDATING:

                return getCommandCode() + conditionCode + dataType
                    + " " + destinationRegister + ", " + "["
                    + sourceRegister + ", #" + registerOffset + "]!";

            case REGISTER_OFFSET:

                if (isPreIndexAddressing) {

                    return getCommandCode() + conditionCode + dataType
                        + " " + destinationRegister + ", " + "["
                        + sourceRegister + ", " + sourceRegister2 + "]";

                } else {

                    return getCommandCode() + conditionCode + dataType
                        + " " + destinationRegister + ", " + "["
                        + sourceRegister + "], [" + sourceRegister2 + "]";

                }

            case REGISTER_OFFSET_DUAL_UPDATING:

                return getCommandCode() + conditionCode + dataType
                    + " " + destinationRegister + ", " + "["
                    + sourceRegister + ", " + sourceRegister2 + "]!";

            case REGISTER_SHIFTED_OFFSET:

                if (isPreIndexAddressing) {

                    return getCommandCode() + conditionCode + dataType
                        + " " + destinationRegister + ", " + "["
                        + sourceRegister + ", " + sourceRegister2 + ", "
                        + registerOffset + "]";

                } else {

                    return getCommandCode() + conditionCode + dataType
                        + " " + destinationRegister + ", " + "["
                        + sourceRegister + "], " + sourceRegister2 + ", "
                        + registerOffset;

                }

            case REGISTER_SHIFTED_OFFSET_DUAL_UPDATING:

                return getCommandCode() + conditionCode + dataType
                    + " " + destinationRegister + ", " + "["
                    + sourceRegister + ", " + sourceRegister2 + ", "
                    + registerOffset + "]!";

            default:
                return null;
        }
    }

    public ConditionCode getConditionField() {
        return conditionField;
    }

    public DataSize getDataSize() {
        return dataSize;
    }

    public Register getDestinationRegister() {
        return destinationRegister;
    }

    public Register getSourceRegister() {
        return sourceRegister;
    }

    public Register getSourceRegister2() {
        return sourceRegister2;
    }

    public boolean isPreIndexAddressing() {
        return isPreIndexAddressing;
    }

    public String getValue() {
        return value;
    }

    public String getRegisterOffset() {
        return registerOffset;
    }

    public AddressType getAddressType() {
        return addressType;
    }

    public boolean canBeOptimisedForLogicalOrCmpCommands() {
        return addressType.equals(AddressType.VALUE)
            && (conditionField.equals(ConditionCode.AL)
                || conditionField.equals(ConditionCode.SAL))
            && isValueSmallEnough();
    }

    public boolean canBeOptimisedForMovCommands() {
        return addressType.equals(AddressType.VALUE)
            || addressType.equals(AddressType.REGISTER)
            || addressType.equals(AddressType.IMMEDIATE_OFFSET)
            || addressType.equals(AddressType.REGISTER_OFFSET)
            || addressType.equals(AddressType.REGISTER_SHIFTED_OFFSET);
    }

    public void setDestinationRegister(Register destinationRegister) {
        this.destinationRegister = destinationRegister;
    }

    // TODO COCO: Insert this method in previous check (canBeOptimised)
    public boolean isValueSmallEnough() {
        try {
            return (Integer.parseInt(value) <= 255)
                && (Integer.parseInt(value) >= -256);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    protected abstract String getCommandCode();

}
