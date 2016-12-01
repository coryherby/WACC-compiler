package wacc.CodeGenerator.Commands.DataMovementCommand;

import wacc.CodeGenerator.Commands.Command;
import wacc.CodeGenerator.Commands.CommandHelper;
import wacc.CodeGenerator.Commands.ConditionCode;
import wacc.CodeGenerator.HardwareManager.Register;

public abstract class DataMovementCommand implements Command {
    
    private ConditionCode conditionField;
    private Register destinationRegister;
    private String value;
    public Register sourceRegister;
    private String registerShift;

    public DataMovementCommand(ConditionCode conditionField,
                               Register destinationRegister,
                               String value) {

        this.conditionField = conditionField;
        this.destinationRegister = destinationRegister;
        this.value = value;
        this.sourceRegister = null;
        this.registerShift = null;
    }

    public DataMovementCommand(ConditionCode conditionField,
                               Register destinationRegister,
                               Register sourceRegister) {

        this.conditionField = conditionField;
        this.destinationRegister = destinationRegister;
        this.sourceRegister = sourceRegister;
        this.value = null;
        this.registerShift = null;
    }

    public DataMovementCommand(ConditionCode conditionField,
                               Register destinationRegister,
                               Register sourceRegister,
                               String registerShift) {

        this.conditionField = conditionField;
        this.destinationRegister = destinationRegister;
        this.sourceRegister = sourceRegister;
        this.registerShift = registerShift;
        this.value = null;
    }

    @Override
    public String generateCommandRepresentation() {

        String conditionCode = CommandHelper.getConditionCode(conditionField);


        if (sourceRegister == null) {

            return getCommandCode() + conditionCode + " "
                + destinationRegister + ", #" + value;

        } else if (registerShift == null) {

            return getCommandCode() + conditionCode + " "
                + destinationRegister + ", " + sourceRegister;

        } else {

            return getCommandCode() + conditionCode + " "
                + destinationRegister + ", " + sourceRegister
                + ", " + registerShift;
        }
    }

    protected abstract String getCommandCode();

    public String getValue() {
        return value;
    }

    public boolean canBeOptimisedForLogicalOrCmpCommands() {
        return (value != null)
            && (sourceRegister == null)
            && (conditionField.equals(ConditionCode.AL)
                || conditionField.equals(ConditionCode.SAL))
            && isValueSmallEnough();
    }

    public boolean canBeOptimisedForLdrCommands() {
        return (value == null) && (registerShift == null);
    }

    public Register getDestinationRegister() {
        return destinationRegister;
    }

    public Register getSourceRegister() {
        return sourceRegister;
    }

    public boolean isValueSmallEnough() {
        try{
            return (Integer.parseInt(value) <= 255)
                && (Integer.parseInt(value) >= -256);
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
