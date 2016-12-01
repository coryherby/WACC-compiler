package wacc.CodeGenerator.Commands.DataMovementCommand;

import wacc.CodeGenerator.Commands.ConditionCode;
import wacc.CodeGenerator.HardwareManager.Register;

public class MovCommand extends DataMovementCommand {

    public MovCommand(ConditionCode conditionField,
                      Register destinationRegister,
                      String value) {

        super(conditionField, destinationRegister, value);
    }

    public MovCommand(ConditionCode conditionField,
                      Register destinationRegister,
                      Register sourceRegister) {

        super(conditionField, destinationRegister, sourceRegister);
    }

    public MovCommand(ConditionCode conditionField,
                      Register destinationRegister,
                      Register sourceRegister,
                      String registerShift) {

        super(conditionField, destinationRegister, sourceRegister,
            registerShift);
    }

    @Override
    protected String getCommandCode() {
        return "MOV";
    }

    public boolean canBeOptimisedForLogicalOrCmpCommands() {
        return super.canBeOptimisedForLogicalOrCmpCommands();
    }

    public boolean canBeOptimisedForLdrCommands() {
        return super.canBeOptimisedForLdrCommands();
    }

    public boolean isValueSmallEnough() {
        return super.isValueSmallEnough();
    }
}
