package wacc.CodeGenerator.Commands.LogicalCommand;

import wacc.CodeGenerator.Commands.Command;
import wacc.CodeGenerator.Commands.CommandHelper;
import wacc.CodeGenerator.Commands.ConditionCode;
import wacc.CodeGenerator.Commands.DataMovementCommand.MovCommand;
import wacc.CodeGenerator.Commands.DataTransferCommand.LdrCommand;
import wacc.CodeGenerator.HardwareManager.Register;

public abstract class LogicalCommand implements Command {

    private ConditionCode conditionField;
    private Register destinationRegister;
    private Register sourceRegister;
    private String value;
    private Register sourceRegister2;
    private String registerShift;

    public LogicalCommand(ConditionCode conditionField,
                          Register destinationRegister,
                          Register sourceRegister,
                          String value) {

        this.conditionField = conditionField;
        this.destinationRegister = destinationRegister;
        this.sourceRegister = sourceRegister;
        this.value = value;
        this.sourceRegister2 = null;
        this.registerShift = null;
    }

    public LogicalCommand(ConditionCode conditionField,
                          Register destinationRegister,
                          Register sourceRegister,
                          Register sourceRegister2) {

        this.conditionField = conditionField;
        this.destinationRegister = destinationRegister;
        this.sourceRegister = sourceRegister;
        this.sourceRegister2 = sourceRegister2;
        this.value = null;
        this.registerShift = null;
    }

    public LogicalCommand(ConditionCode conditionField,
                          Register destinationRegister,
                          Register sourceRegister,
                          Register sourceRegister2,
                          String registerShift) {

        this.conditionField = conditionField;
        this.destinationRegister = destinationRegister;
        this.sourceRegister = sourceRegister;
        this.sourceRegister2 = sourceRegister2;
        this.registerShift = registerShift;
        this.value = null;
    }

    @Override
    public String generateCommandRepresentation() {

        String conditionCode = CommandHelper.getConditionCode(conditionField);

        if (sourceRegister2 == null) {

            return getCommandCode() + conditionCode + " "
                + destinationRegister + ", " + sourceRegister + ", #" + value;

        } else if (registerShift == null) {

            return getCommandCode() + conditionCode + " "
                + destinationRegister + ", " + sourceRegister + ", "
                + sourceRegister2;

        } else {

            return getCommandCode() + conditionCode + " "
                + destinationRegister + ", " + sourceRegister + ", "
                + sourceRegister2 + ", " + registerShift;
        }
    }

    public boolean canBeOptimised() {
        return (conditionField.equals(ConditionCode.AL)
            || conditionField.equals(ConditionCode.SAL))
            && (value == null) && (registerShift == null);
    }

    public LogicalCommand optimiseMovCommand(MovCommand movCommand) {

        String value = movCommand.getValue();
        return createOptimisedCommand(value);
    }

    public LogicalCommand optimiseLdrCommand(LdrCommand ldrCommand) {

        String value = ldrCommand.getValue();
        return createOptimisedCommand(value);
    }

    private LogicalCommand createOptimisedCommand(String value) {
        LogicalCommand optimisedCommand = null;
        Register register1 = destinationRegister;
        Register register2 = sourceRegister;

        if (this instanceof AddCommand) {
            optimisedCommand
                = new AddCommand(conditionField, register1, register2, value);

        } else if (this instanceof AndCommand) {
            optimisedCommand
                = new AndCommand(conditionField, register1, register2, value);

        } else if (this instanceof BicCommand) {
            optimisedCommand
                = new AndCommand(conditionField, register1, register2, value);

        } else if (this instanceof EorCommand) {
            optimisedCommand
                = new EorCommand(conditionField, register1, register2, value);

        } else if (this instanceof OrnCommand) {
            optimisedCommand
                = new OrnCommand(conditionField, register1, register2, value);

        } else if (this instanceof OrrCommand) {
            optimisedCommand
                = new OrrCommand(conditionField, register1, register2, value);

        } else if (this instanceof RsbCommand) {
            optimisedCommand
                = new RsbCommand(conditionField, register1, register2, value);

        } else if (this instanceof SubCommand) {
            optimisedCommand
                = new SubCommand(conditionField, register1, register2, value);
        }

        return optimisedCommand;
    }

    public Register getSourceRegister2() {
        return sourceRegister2;
    }

    protected abstract String getCommandCode();
}