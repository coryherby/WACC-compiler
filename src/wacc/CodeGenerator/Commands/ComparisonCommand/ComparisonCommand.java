package wacc.CodeGenerator.Commands.ComparisonCommand;

import wacc.CodeGenerator.Commands.Command;
import wacc.CodeGenerator.Commands.CommandHelper;
import wacc.CodeGenerator.Commands.ConditionCode;
import wacc.CodeGenerator.Commands.DataMovementCommand.MovCommand;
import wacc.CodeGenerator.Commands.DataTransferCommand.LdrCommand;
import wacc.CodeGenerator.HardwareManager.Register;

public abstract class ComparisonCommand implements Command {

    private ConditionCode conditionField;
    private Register sourceRegister;
    private String value;
    private Register sourceRegister2;
    private String registerShift;

    public ComparisonCommand(ConditionCode conditionField,
                             Register sourceRegister,
                             String value) {

        this.conditionField = conditionField;
        this.sourceRegister = sourceRegister;
        this.value = value;
        this.sourceRegister2 = null;
        this.registerShift = null;
    }

    public ComparisonCommand(ConditionCode conditionField,
                             Register sourceRegister,
                             Register sourceRegister2) {

        this.conditionField = conditionField;
        this.sourceRegister = sourceRegister;
        this.sourceRegister2 = sourceRegister2;
        this.value = null;
        this.registerShift = null;
    }

    public ComparisonCommand(ConditionCode conditionField,
                             Register sourceRegister,
                             Register sourceRegister2,
                             String registerShift) {

        this.conditionField = conditionField;
        this.sourceRegister = sourceRegister;
        this.sourceRegister2 = sourceRegister2;
        this.registerShift = registerShift;
        this.value = null;
    }

    @Override
    public String generateCommandRepresentation() {

        String conditionCode = CommandHelper.getConditionCode(conditionField);


        if (sourceRegister2 == null) {

            return getCommandCode() + conditionCode + " " + sourceRegister
                + ", #" + value;

        } else if (registerShift == null) {

            return getCommandCode() + conditionCode + " " + sourceRegister
                + ", " + sourceRegister2;

        } else {

            return getCommandCode() + conditionCode + " " + sourceRegister
                + ", " + sourceRegister2 + ", " + registerShift;
        }
    }

    protected abstract String getCommandCode();

    public boolean canBeOptimised() {
        return (value == null) && (registerShift == null);
    }

    public ComparisonCommand optimiseLdrCommand(LdrCommand ldrCommand) {
        String value = ldrCommand.getValue();
        return createOptimisedCommand(value);
    }

    public ComparisonCommand optimiseMovCommand(MovCommand movCommand) {
        String value = movCommand.getValue();
        return createOptimisedCommand(value);
    }

    private ComparisonCommand createOptimisedCommand(String value) {

        ComparisonCommand optimisedCommand = null;
        Register register = sourceRegister;

        if (this instanceof CmnCommand) {
            optimisedCommand
                = new CmnCommand(conditionField, register, value);

        } else if (this instanceof CmpCommand) {
            optimisedCommand
                = new CmpCommand(conditionField, register, value);

        } else if (this instanceof TeqCommand) {
            optimisedCommand
                = new TeqCommand(conditionField, register, value);

        } else if (this instanceof TstCommand) {
            optimisedCommand
                = new TstCommand(conditionField, register, value);

        }

        return optimisedCommand;
    }

    public Register getSourceRegister2() {
        return sourceRegister2;
    }
}
